package com.scu.intelligentdoorplateback.web.controller;

import com.scu.intelligentdoorplateback.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/file")
@Slf4j
public class FileController {
    protected FileService fileService;
    protected ResourceLoader resourceLoader;

    public FileController(FileService fileService, ResourceLoader resourceLoader){
        this.fileService = fileService;
        this.resourceLoader = resourceLoader;
    }

    @ApiOperation(value = "文件上传", notes = "文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file")MultipartFile[] file, HttpServletRequest request) throws IOException {
        Map<String, String> map = fileService.upload(file[0]);
        return ResponseEntity.ok().body(map);
    }

    private static String suffix(String fileName) {
        int i = fileName.lastIndexOf('.');
        return i == -1 ? "" : fileName.substring(i + 1);
    }

    //文件下载以axios下载时，必须时post请求，然后不需要返回json，但是需要一个响应
    @PostMapping("downloadFile")
    @ResponseBody
    public void downloadFile(HttpServletResponse response, @RequestBody String url){
        fileService.downloadFile(response,url);
    }
}
