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

import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectInfoReturn;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileHandler {
    @Autowired
    private ProjectInfoRepository projectInfoRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private GuestRepository guestRepository;


    /*
    * 接收项目文件并在数据库建立映射
    * 参数说明：
    * projectNo:项目编号
    * file:上传的文件
    * path:项目相对路径
            建模/原始建模
            资料
    */

    @RequestMapping("/upload")
    @ResponseBody
    public JSONObject fileUpload(@RequestParam("projectNo") int projectNo,@RequestParam("file") MultipartFile file,@RequestParam("path") String path){//上传单个文件
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);
        //检测参数是否正确
        if(projectInfo==null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到对应项目信息");
            return jObject;
        }
        else if(file.isEmpty()){
            jObject.put("Result", "error");
            jObject.put("Message", "文件为空");
            return jObject;
        }
        //初始化项目信息
        ProjectInfoReturn projectInfoReturn = new ProjectInfoReturn();
        projectInfoReturn.setProjectDetail(projectInfo, staffRepository, guestRepository);

        String fullPath = "E:/房地产项目"+path;

        //E:/房地产项目/项目名/模块/三级目录/文件名
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        JSONObject fileInfo = new JSONObject();
        File dest = new File(fullPath + "/" + fileName);
        fileInfo.put("Path", dest.getAbsolutePath());
        fileInfo.put("Size", file.getSize());
        dest.mkdirs();

        try {
            file.transferTo(dest); //保存文件

            //上传成功 建立数据库映射

            jObject.put("Result", "success");
            jObject.put("FileInfo", fileInfo);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            jObject.put("Result", "error");
            jObject.put("Message", "if the file has already been moved in the filesystem and is not available anymore for another transfer");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(!dest.getParentFile().exists()+dest.getParentFile().getAbsolutePath());
            jObject.put("Result", "error");
            jObject.put("Message", "in case of reading or writing errors");
        }
        return jObject;
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
        // String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
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
