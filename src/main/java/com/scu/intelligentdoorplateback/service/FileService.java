package com.scu.intelligentdoorplateback.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface FileService {
    Map upload(MultipartFile file) throws IOException;

    void downloadFile(HttpServletResponse response, String url);
}
