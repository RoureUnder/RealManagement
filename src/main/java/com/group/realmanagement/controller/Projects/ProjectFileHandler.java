package com.group.realmanagement.controller.Projects;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.JsonTest;
import com.group.realmanagement.entity.Projects.ProjectFile;
import com.group.realmanagement.entity.Projects.ProjectFileReview;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectInfoReturn;
import com.group.realmanagement.entity.User.Staff;
import com.group.realmanagement.repository.Projects.ProjectFileRepository;
import com.group.realmanagement.repository.Projects.ProjectFileReviewRepository;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private ProjectFileReviewRepository projectFileReviewRepository;
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
    public JSONObject fileUpload(@RequestParam("projectNo") int projectNo,@RequestParam("staffNo") int uploader,@RequestParam("file") MultipartFile file,@RequestParam("path") String path,@RequestParam("principalNo") int principalNo){//上传单个文件
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);


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
        else if(staffRepository.findByStaffNo(principalNo)==null&&staffRepository.findByStaffNo(principalNo).getAccess()!=1){//还未验证是否为主管Access = ?
            jObject.put("Result", "error");
            jObject.put("Message", "未找到对应主管信息");
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
                                        +"  上传者"+staffRepository.findByStaffNo(projectFile.getUploader()).getName());
                return jObject;
            }
        }
        List<ProjectFileReview> projectFileReviews = projectFileReviewRepository.findByProjectNo(projectNo);
        for (ProjectFileReview projectFileReview : projectFileReviews) {
            if(projectFileReview.getFullPath().equals(fullPath + "/" + fileName)){
                jObject.put("Result", "error");
                jObject.put("Message", "数据库中已生成映射，请勿重复上传。"
                                        +"  上次上传时间："+projectFileReview.getUploadTime()
                                        +"  上传者："+staffRepository.findByStaffNo(projectFileReview.getUploader()).getName()
                                        +"  审核者："+staffRepository.findByStaffNo(projectFileReview.getPrincipalNo()).getName()
                                        +"  当前状态："+projectFileReview.getStatus());
                return jObject;
            }
        }



        int size = (int) file.getSize();

        File dest = new File(fullPath + "/" + fileName);

        dest.mkdirs();

        try {
            // List<ProjectFile> projectFiles =

            file.transferTo(dest); //保存文件

            //记录文件信息
            JSONObject fileInfo = new JSONObject();
            fileInfo.put("Path", fullPath);
            fileInfo.put("Name", fileName);
            fileInfo.put("Size", getFileSize(dest));

            //上传成功 建立数据库映射
            ProjectFile projectFile = new ProjectFile();
            projectFile.setDetailPath(projectInfoReturn,fullPath+"/"+fileName,uploader);
            projectFile.setFileSize(getFileSize(dest));
            projectFile = projectFileRepository.save(projectFile);

            ProjectFileReview projectFileReview = new ProjectFileReview();
            projectFileReview.setReviewByFile(projectFile, principalNo);
            projectFile = projectFileRepository.save(projectFile);
            //
            projectFileReview = projectFileReviewRepository.save(projectFileReview);
            jObject.put("ProjectFile", projectFile);
            jObject.put("ProjectFileReview", projectFileReview);
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
        // System.out.println(file.getAbsolutePath());
        // 获取文件名
        String filename = file.getName();
        // 获取文件后缀名


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
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        outputStream.write(buffer);
        outputStream.flush();
        } catch (IOException ex) {
        ex.printStackTrace();
        }
    }



    @GetMapping("/findAll")
    JSONObject findAll(){
        JSONObject jObject = new JSONObject();
        List<ProjectFile> projectFiles = projectFileRepository.findAll();
        if(projectFiles.size()!=0){
            jObject.put("ProjectFiles", projectFiles);
            jObject.put("Result", "success");
            jObject.put("Message", "共找到"+projectFiles.size()+"个文件映射");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "数据库中未建立映射，请联系管理员");
        }
        return jObject;
    }

    @GetMapping("/findByProjectNo")
    JSONObject findByProjectNo(int projectNo){
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);
        if(projectInfo == null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该项目，请检查后重试");
            return jObject;
        }
        List<ProjectFile> projectFiles = projectFileRepository.findByProjectNo(projectNo);
        if(projectFiles.size()!=0){
            jObject.put("ProjectFiles", projectFiles);
            jObject.put("Result", "success");
            jObject.put("Message", "项目 "+projectInfo.getProjectName()+" 共找到"+projectFiles.size()+"个文件映射");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "数据库中未建立映射，请联系管理员");
        }
        return jObject;
    }

    @GetMapping("/findByProjectNoAndStaffNo")
    JSONObject findByProjectNoAndStaffNo(int projectNo,int staffNo){
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);
        Staff staff = staffRepository.findByStaffNo(staffNo);
        if(projectInfo == null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该项目，请检查后重试");
            return jObject;
        }
        else if(staff == null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该员工信息，请检查后重试");
            return jObject;
        }
        List<ProjectFile> projectFiles = projectFileRepository.findByProjectNo(projectNo);
        if(projectFiles.size()!=0){
            jObject.put("ProjectFiles", projectFiles);
            jObject.put("Result", "success");
            jObject.put("Message", "项目 "+projectInfo.getProjectName()+" 共找到"+projectFiles.size()+"个"+staff.getName()+"上传的文件映射");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "数据库中未建立映射，请联系管理员");
        }
        return jObject;
    }

    @DeleteMapping("/deleteByProjectFileNo")
    JSONObject deleteByProjectFileNo(int projectFileNo){
        JSONObject jObject = new JSONObject();
        Optional<ProjectFile> projectFile = projectFileRepository.findById(projectFileNo);
        if(!projectFile.isEmpty()){
            jObject.put("ProjectFile", projectFile);
            File file = new File(projectFile.get().getFullPath());
            if(!file.exists()){
                jObject.put("Result", "error");
                jObject.put("Message", "文件不存在");
            }
            else{
                if(file.delete()){
                    projectFileRepository.delete(projectFile.get());
                    jObject.put("Result", "success");
                    jObject.put("Message", "本地删除成功，服务器映射删除");
                }
                else{
                    jObject.put("Result", "success");
                    jObject.put("Message", "本地删除失败");
                }
            }
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "服务器未找到该文件，请联系管理员");
        }
        return jObject;
    }


    //计算文件大小
    public static String getFileSize(File file){
        String size = "";
        if(file.exists() && file.isFile()){
        long fileS = file.length();
         DecimalFormat df = new DecimalFormat("#.00");
              if (fileS < 1024) {
                 size = df.format((double) fileS) + "BT";
              } else if (fileS < 1048576) {
                 size = df.format((double) fileS / 1024) + "KB";
              } else if (fileS < 1073741824) {
                 size = df.format((double) fileS / 1048576) + "MB";
              } else {
                 size = df.format((double) fileS / 1073741824) +"GB";
              }
        }else if(file.exists() && file.isDirectory()){
        size = "";
        }else{
        size = "0BT";
        }
        return size;
       }
}
