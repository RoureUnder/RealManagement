package com.group.realmanagement.controller.FileServices;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileHandler {
    // @Autowired
    // private FileRepository fileRepository;

    @RequestMapping("/upload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file,@RequestParam("fileUrl") String path){
        if(file.isEmpty()){
            return null;
        }

        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" +size);
        // String path = "E:/test/test222";
        // File dest = new File(10,fileName,path);
        // fileRepository.save(dest);
        File dest = new File(path + "/" + fileName);
        // return dest.getParentFile().getAbsolutePath();
        // if(!dest.getParentFile().exists()){ //判断文件父目录是否存在()
            // System.out.println(!dest.getParentFile().exists());
            dest.mkdirs();
        // }
        try {
            file.transferTo(dest); //保存文件
            return dest.getAbsolutePath();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(!dest.getParentFile().exists()+dest.getParentFile().getAbsolutePath());
            return "false";
        }
    }

    @RequestMapping("/download")
    public void download(String path, HttpServletResponse response) {
        try {
        // path是指想要下载的文件的路径
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        // log.info(file.getPath());
        // 获取文件名
        String filename = file.getName();
        // 获取文件后缀名
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        // log.info("文件后缀名：" + ext);
        
        // 将文件写入输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream fis = new BufferedInputStream(fileInputStream);
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        
        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", "" + file.length());
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        outputStream.write(buffer);
        outputStream.flush();
        } catch (IOException ex) {
        ex.printStackTrace();
        }
        } 
}
