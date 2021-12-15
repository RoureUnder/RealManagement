package com.group.realmanagement.controller.FileServices;


import java.io.File;
import java.io.IOException;


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


    
}
