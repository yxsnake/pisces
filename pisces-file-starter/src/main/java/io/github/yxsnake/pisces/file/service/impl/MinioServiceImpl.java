package io.github.yxsnake.pisces.file.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.file.service.FileService;
import io.github.yxsnake.pisces.file.model.FileInfo;
import io.github.yxsnake.pisces.web.core.exception.BizException;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import com.google.common.base.Throwables;
import io.minio.*;
import io.minio.http.Method;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioServiceImpl implements FileService, InitializingBean {

  /**
   * MinIO的API地址
   */
  @Setter
  private String endpoint;

  /**
   * 用户名
   */
  @Setter
  private String accessKey;

  /**
   * 密钥
   */
  @Setter
  private String secretKey;

  /**
   * 存储桶名称
   */
  @Setter
  private String bucketName;

  /**
   * 自定义域名(非必须)
   */
  @Setter
  private String customDomain;


  private MinioClient minioClient;

  @Override
  public void afterPropertiesSet() {
    log.info("MinIO Client init...");
    Assert.notBlank(endpoint, "MinIO endpoint can not be null");
    Assert.notBlank(accessKey, "MinIO accessKey can not be null");
    Assert.notBlank(secretKey, "MinIO secretKey can not be null");
    Assert.notBlank(bucketName, "MinIO bucketName can not be null");
    this.minioClient = MinioClient.builder()
      .endpoint(endpoint)
      .credentials(accessKey,secretKey)
      .build();
  }


  /**
   * 上传文件
   *
   * @param file 表单文件对象
   * @return
   */
  @Override
  public FileInfo uploadFile(MultipartFile file) {
    // 存储桶不存在则创建
    createBucketIfAbsent(bucketName);

    // 生成文件名(日期文件夹)
    String suffix = FileUtil.getSuffix(file.getOriginalFilename());
    String uuid = IdUtil.simpleUUID();
    String fileName = DateUtil.format(LocalDateTime.now(), "yyyy/MM/dd") + "/" + uuid + "." + suffix;
    //  try-with-resource 语法糖自动释放流
    try (InputStream inputStream = file.getInputStream()) {
      // 文件上传
      PutObjectArgs putObjectArgs = PutObjectArgs.builder()
        .bucket(bucketName)
        .object(fileName)
        .contentType(file.getContentType())
        .stream(inputStream, inputStream.available(), -1)
        .build();
      minioClient.putObject(putObjectArgs);

      // 返回文件路径
      String fileUrl;
      if (StrUtil.isBlank(customDomain)) {
        //// 未配置自定义域名
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
          .bucket(bucketName).object(fileName)
          .method(Method.GET)
          .build();

        fileUrl = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        fileUrl = fileUrl.substring(0, fileUrl.indexOf("?"));
      } else {
        // 配置自定义文件路径域名
        fileUrl = customDomain + '/' + bucketName + "/" + fileName;
      }

      FileInfo fileInfo = new FileInfo();
      fileInfo.setFileId(uuid);
      fileInfo.setName(fileName);
      fileInfo.setUrl(fileUrl);
      return fileInfo;
    } catch (Exception e) {
      throw new BizException("文件上传失败");
    }
  }

  /**
   * 删除文件
   *
   * @param filePath 文件路径
   *                 https://oss.youlai.tech/default/2022/11/20/test.jpg
   * @return
   */
  @Override
  @SneakyThrows
  public boolean deleteFile(String filePath) {
    Assert.notBlank(filePath, "删除文件路径不能为空");
    String tempStr = "/" + bucketName + "/";
    // 格式： 2022/11/20/test.jpg
    String fileName = filePath.substring(filePath.indexOf(tempStr) + tempStr.length());

    RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
      .bucket(bucketName)
      .object(fileName)
      .build();
    minioClient.removeObject(removeObjectArgs);
    return true;
  }

  @Override
  public String getPreSignedObjectUrl(String filePath) {
    Assert.notBlank(filePath, "下载文件路径不能为空");
    Map<String, String> reqParams = new HashMap<String, String>();
    reqParams.put("filename","filePath");
//    reqParams.put("response-content-type", "application/json");
    GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
      .bucket(bucketName)
      .method(Method.GET)
      .expiry(1, TimeUnit.HOURS)
      .object(filePath)
      .extraQueryParams(reqParams)
      .build();
    try {
      String preSignedObjectUrl =  minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
      return preSignedObjectUrl;
    } catch (Exception e) {
      log.error("getPreSignedObjectUrl filePath:{},error:{}",filePath, Throwables.getStackTraceAsString(e));
      BizAssert.isTrue("获取文件连接失败",true);
    }
    return null;
  }

  @Override
  public void downloadFile(String downloadFileName,String filePath, HttpServletResponse response) {
    Assert.notBlank(filePath, "下载文件路径(filePath)不能为空");
    Assert.notBlank(downloadFileName, "下载文件名称(downloadFileName)不能为空");
    String tempStr = "/" + bucketName + "/";
    String fileName = filePath.substring(filePath.indexOf(tempStr) + tempStr.length());
    try {
      InputStream inputStream = getFileInputStream(fileName, bucketName);
      response.setHeader("Content-Disposition", "attachment;filename=" + new String(downloadFileName.getBytes("ISO8859-1"), StandardCharsets.UTF_8));
      ServletOutputStream servletOutputStream = response.getOutputStream();
      int len;
      byte[] buffer = new byte[1024];
      while ((len = inputStream.read(buffer)) > 0) {
        servletOutputStream.write(buffer, 0, len);
      }
      servletOutputStream.flush();
      inputStream.close();
      servletOutputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取文件流
   *
   * @param fileName   文件名
   * @param bucketName 桶名（文件夹）
   * @return
   */
  public InputStream getFileInputStream(String fileName, String bucketName) {
    try {
      return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return null;
  }


  /**
   * PUBLIC桶策略
   * 如果不配置，则新建的存储桶默认是PRIVATE，则存储桶文件会拒绝访问 Access Denied
   *
   * @param bucketName
   * @return
   */
  private static String publicBucketPolicy(String bucketName) {
    /**
     * AWS的S3存储桶策略
     * Principal: 生效用户对象
     * Resource:  指定存储桶
     * Action: 操作行为
     */
    StringBuilder builder = new StringBuilder();
    builder.append("{\"Version\":\"2012-10-17\","
      + "\"Statement\":[{\"Effect\":\"Allow\","
      + "\"Principal\":{\"AWS\":[\"*\"]},"
      + "\"Action\":[\"s3:ListBucketMultipartUploads\",\"s3:GetBucketLocation\",\"s3:ListBucket\"],"
      + "\"Resource\":[\"arn:aws:s3:::" + bucketName + "\"]},"
      + "{\"Effect\":\"Allow\"," + "\"Principal\":{\"AWS\":[\"*\"]},"
      + "\"Action\":[\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\"],"
      + "\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}");

    return builder.toString();
  }

  /**
   * 创建存储桶(存储桶不存在)
   *
   * @param bucketName
   */
  @SneakyThrows
  private void createBucketIfAbsent(String bucketName) {
    BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
    if (!minioClient.bucketExists(bucketExistsArgs)) {
      MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();

      minioClient.makeBucket(makeBucketArgs);

      // 设置存储桶访问权限为PUBLIC， 如果不配置，则新建的存储桶默认是PRIVATE，则存储桶文件会拒绝访问 Access Denied
      SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs
        .builder()
        .bucket(bucketName)
        .config(publicBucketPolicy(bucketName))
        .build();
      minioClient.setBucketPolicy(setBucketPolicyArgs);
    }
  }

}
