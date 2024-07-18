package io.github.yxsnake.pisces.file.service;

import jakarta.servlet.http.HttpServletResponse;
import io.github.yxsnake.pisces.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  /**
   * 上传文件
   *
   * @param file 表单文件对象
   * @return
   */
  FileInfo uploadFile(MultipartFile file);

  /**
   * 删除文件
   *
   * @param filePath 文件路径
   * @return
   */
  boolean deleteFile(String filePath);

  /**
   * 获取文件连接
   * @param filePath
   * @return
   */
  String getPreSignedObjectUrl(String filePath);

  /**
   * 下载文件
   * @param filePath
   * @param response
   */
  void downloadFile(String downloadFileName,String filePath, HttpServletResponse response) ;
}
