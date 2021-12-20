package com.group.realmanagement.controller.Projects;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.JsonTest;
import com.group.realmanagement.entity.Projects.ProjectFile;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectInfoReturn;
import com.group.realmanagement.repository.Projects.ProjectFileRepository;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/project/file")
public class ProjectFileHandler {
    @Autowired
    private ProjectInfoRepository projectInfoRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private GuestRepository guestRepository;




    @PostMapping("/initializeByProjectNo")
    JSONObject initializeByProjectNo(int projectNo){
        /*
        * func:初始化项目结构
        * 步骤：
        * 1.从project_info表中查询对应的项目信息    实现
        * 2.本地建立目录结构                        实现
        * 3.数据库project_file建立映射              上传文件接口
        */
        JSONObject rObject = new JSONObject();

        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);

        if(projectInfo == null){
            rObject.put("Result", "error");
            rObject.put("Message", "项目编号错误");
            return rObject;
        }
        ProjectInfoReturn projectInfoReturn = new ProjectInfoReturn();
        projectInfoReturn.setProjectDetail(projectInfo,staffRepository,guestRepository);
        String fixedPath="E:/房地产项目/";
        fixedPath+=projectInfoReturn.getProjectName()+"-"
                +projectInfoReturn.getGuestName()+"-"
                +guestRepository.findByGuestNo(projectInfo.getGuestNo()).getPrimaryUnit()+"-"
                +projectInfoReturn.getStartTime();
        // System.out.println(fixedPath);

        JSONObject jObject = new JSONObject();
        String path = "D:/VsCodeProjects/VS-Code-Springboot/RealManagement/realmanagement/src/main/resources/depends/Directory.json";
        String str = JsonTest.readJsonFile(path);
        jObject = JSON.parseObject(str);
        for(int i=0;i<5;i++){
            String tempPath="/"+jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
            if(jObject.getJSONArray("dirs").getJSONObject(i).size()!=1)
            {
                for(int j=0;j<2;j++){
                    tempPath+="/"+jObject.getJSONArray("dirs").getJSONObject(i).getJSONArray("dirs").getJSONObject(j).getString("name");
                    File dest = new File(fixedPath+ tempPath);
                    dest.mkdirs();
                    // System.out.println(fixedPath+ tempPath);
                    tempPath="/"+jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
                }
            }
            else{
                File dest = new File(fixedPath+ tempPath);
                dest.mkdirs();
                // System.out.println(fixedPath+tempPath);
            }
        }

        rObject.put("Result", "success");
        rObject.put("ProjectPath", fixedPath);
        return rObject;
    }

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
    public JSONObject fileUpload(@RequestParam("projectNo") int projectNo,@RequestParam("staffNo") int uploader,@RequestParam("file") MultipartFile file,@RequestParam("path") String path){//上传单个文件
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);
        
        System.out.println(staffRepository.findByStaffNo(uploader));
        
        //检测参数是否正确
        //1.项目编号(project表中是否存在)
        //2.员工编号(staff表中是否存在)
        //3.路径(格式错误)
        //4.文件(为空或者重复)
        if(projectInfo==null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到对应项目信息");
            return jObject;
        }
        else if(staffRepository.findByStaffNo(uploader)==null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到对应员工信息");
            return jObject;
        }
        else if(file.isEmpty()){
            jObject.put("Result", "error");
            jObject.put("Message", "文件为空");
            return jObject;
        }
        else if(path.split("/").length>3){
            jObject.put("Result", "error");
            jObject.put("Message", "路径出错，大于二级");
            return jObject;
        }
        else {
            String tempPath[] = path.split("/");
            for(int i=1;i<tempPath.length;i++){
                if(JsonTest.getIndexOfPath(tempPath[i])!=1&&JsonTest.getIndexOfPath(tempPath[i])!=2)
                {
                    jObject.put("Result", "error");
                    jObject.put("Message", "路径出错，路径名有误");
                    jObject.put("ErrorInfo", JsonTest.getIndexOfPath(tempPath[i]));
                    return jObject;
                }
            }
            if(tempPath.length==3){
                if(tempPath[2].indexOf(tempPath[1])==-1){
                    jObject.put("Result", "error");
                    jObject.put("Message", "路径出错，不属于同一目录");
                    return jObject;
                }
                else if(tempPath[2].equals(tempPath[1])){
                    jObject.put("Result", "error");
                    jObject.put("Message", "路径出错，属于同一目录");
                    return jObject;
                }
                else if(path.endsWith("/")){
                    jObject.put("Result", "error");
                    jObject.put("Message", "路径出错，以/结尾");
                    return jObject;
                }
            }
        }

        //初始化项目信息
        ProjectInfoReturn projectInfoReturn = new ProjectInfoReturn();
        projectInfoReturn.setProjectDetail(projectInfo, staffRepository, guestRepository);


        //建立目录
        String fullPath = "E:/房地产项目/"+projectInfoReturn.getProjectName()+"-"
                +projectInfoReturn.getGuestName()+"-"
                +guestRepository.findByGuestNo(projectInfo.getGuestNo()).getPrimaryUnit()+"-"
                +projectInfoReturn.getStartTime()+path;
        //path example:  /模型/最终模型
        //E:/房地产项目/项目名/模块/三级目录/文件名
        String fileName = file.getOriginalFilename();


        //检测数据库中映射是否已录入
        List<ProjectFile> projectFiles = projectFileRepository.findByProjectNo(projectNo);
        for (ProjectFile projectFile : projectFiles) {
            if(projectFile.getFullPath().equals(fullPath + "/" + fileName)){
                jObject.put("Result", "error");
                jObject.put("Message", "数据库中已生成映射，请勿重复上传。"
                                        +"  上次上传时间："+projectFile.getUploadTime()
                                        +"  上传者"+staffRepository.findByStaffNo(uploader).getName());
                return jObject;
            }
        }




        int size = (int) file.getSize();
        JSONObject fileInfo = new JSONObject();
        File dest = new File(fullPath + "/" + fileName);
        fileInfo.put("Path", fullPath);
        fileInfo.put("Name", fileName);
        fileInfo.put("Size", file.getSize());
        dest.mkdirs();

        try {
            // List<ProjectFile> projectFiles = 

            file.transferTo(dest); //保存文件

            //上传成功 建立数据库映射
            ProjectFile projectFile = new ProjectFile();
            projectFile.setDetailPath(projectInfoReturn,fullPath+"/"+fileName,uploader);
            projectFile = projectFileRepository.save(projectFile);
            //
            jObject.put("ProjectFile", projectFile);
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



    @GetMapping("/findAll")
    List<ProjectFile> findAll(){
        return projectFileRepository.findAll();
    }

    @GetMapping("/findByProjectNo")
    List<ProjectFile> findByProjectNo(int projectNo){
        return projectFileRepository.findByProjectNo(projectNo);
    }
}
