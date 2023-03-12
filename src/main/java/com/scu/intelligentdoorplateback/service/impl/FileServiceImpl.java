package com.scu.intelligentdoorplateback.service.impl;

import com.scu.intelligentdoorplateback.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {


    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMM");

    @Value("${file-upload-path}")
    private String fileUploadPath;

    @Override
    public Map upload(MultipartFile file) throws IOException {
//        Map<String, String> map = storeFile(file, Paths.get(fileUploadPath, "image").toString());
        Map<String, String> map = storeFile(file, Paths.get(fileUploadPath, "files").toString());
        return map;
    }

    @Override
    public void downloadFile(HttpServletResponse response, String url) {
        //这部分不太明白不重要
        File file = Paths.get(fileUploadPath,url).toFile();

        try {
            ServletOutputStream os = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes= new byte[1024];
            int len;
            while ((len=bis.read(bytes))!=-1){
                os.write(bytes,0,len);
            }
            os.flush();
            bis.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> storeFile(MultipartFile file, String fileUploadPath) throws IOException {

        String yearMonth = SDF.format(new Date());//当前年月
        //String random = "" + (int) (Math.random() * 1000);//随机4位数,没补0
        String fileName = file.getOriginalFilename();//文件全名
        String suffix = suffix(fileName);//文件后缀
        String relPath = "/" + yearMonth + "/" + "-" + UUID.randomUUID().toString().replaceAll("-","") + suffix;
        //   /file/image     /202302/-adj1lk2jlkasdklajdlka.txt
        String toPath = fileUploadPath + relPath;
        FileOutputStream out = null;

        File toFile = new File(toPath).getParentFile();
        if (!toFile.exists()) {
            toFile.mkdirs(); //自动创建目录
        }
        try {
            out = new FileOutputStream(toPath);
            out.write(file.getBytes());
            out.flush();
            Map<String, String> map = new HashMap();
//            map.put("url", "./image" + relPath);
            map.put("url", "./files" + relPath);
            log.info(relPath);
            return map;
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 后缀名或empty："a.png" => ".png"
     */
    private static String suffix(String fileName) {
        int i = fileName.lastIndexOf('.');
        return i == -1 ? "" : fileName.substring(i);
    }
}
