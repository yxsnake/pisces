//package io.github.yxsnake.pisces.file.controller;
//
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.toolkit.IdWorker;
//import io.github.yxsnake.pisces.file.service.FileService;
//import io.github.yxsnake.pisces.file.model.FileInfo;
//import io.github.yxsnake.pisces.web.core.base.Result;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.Parameters;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/file")
//@RequiredArgsConstructor
//public class FileController {
//
//  private final FileService fileService;
//
//  @Operation(summary = "文件上传")
//  @Parameters({
//    @Parameter(name = "文件file",description = "file",required = true)
//  })
//  @PostMapping(value = "uploadFile" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//  public Result<FileInfo> uploadFile(@RequestParam("file") MultipartFile file) {
//    FileInfo fileInfo = fileService.uploadFile(file);
//    return Result.success(fileInfo);
//  }
//
//  @Operation(summary = "文件删除")
//  @Parameters({
//    @Parameter(name = "filePath",description = "文件路口",required = true)
//  })
//  @SneakyThrows
//  @GetMapping(value = "/deleteFile")
//  public Result deleteFile(@RequestParam("filePath") String filePath) {
//    boolean result = fileService.deleteFile(filePath);
//    return Result.judge(result);
//  }
//
//  /**
//   * 下载文件
//   * @param filePath
//   * @param response
//   * @return
//   */
//  @Parameters({
//    @Parameter(name = "filePath",description = "文件路口",required = true)
//  })
//  @SneakyThrows
//  @Operation(summary = "文件下载")
//  @GetMapping("/downloadFile")
//  public void downloadFile(@RequestParam(value = "downloadFileName",required = false)  String downloadFileName,
//                           @RequestParam("filePath")  String filePath,
//                           HttpServletResponse response) {
//    if(StrUtil.isBlank(downloadFileName)){
//      downloadFileName = IdWorker.get32UUID();
//    }
//    fileService.downloadFile(downloadFileName,filePath,response);
//  }
//
//
//  @Parameters({
//    @Parameter(name = "filePath",description = "文件路口",required = true)
//  })
//  @SneakyThrows
//  @Operation(summary = "获取文件访问连接")
//  @GetMapping("/getPreSingedUrl")
//  public Result<String> getPreSingedUrl(@RequestParam("filePath")  String filePath){
//    String preSignedObjectUrl = fileService.getPreSignedObjectUrl(filePath);
//    return Result.success(preSignedObjectUrl);
//  }
//
//}
