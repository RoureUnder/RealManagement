package com.group.realmanagement.repository.Projects;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONToken;
import com.group.realmanagement.entity.Projects.JsonTest;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.repository.Projects.ProjectFileRepository;

import org.apache.tomcat.jni.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProjectFileRepositoryTest {
    @Autowired
    private ProjectFileRepository projectFileRepository;

    @Test
    void setDetailPath() {
        String path = "/渲染/最终渲染/";
        
        System.out.println(path.endsWith("/"));
        JSONObject jObject = new JSONObject();
        if(path.split("/").length>3){
            jObject.put("Result", "error");
            jObject.put("Message", "路径出错，大于二级");
        }
        else {
            String tempPath[] = path.split("/");
            for(int i=1;i<tempPath.length;i++){
                if(JsonTest.getIndexOfPath(tempPath[i])!=1&&JsonTest.getIndexOfPath(tempPath[i])!=2)
                {
                    jObject.put("Result", "error");
                    jObject.put("Message", "路径出错，路径名有误");
                    jObject.put("ErrorInfo", JsonTest.getIndexOfPath(tempPath[i]));
                }
            }
            if(tempPath.length==3){
                if(tempPath[2].indexOf(tempPath[1])==-1){
                    jObject.put("Result", "error");
                    jObject.put("Message", "路径出错，不属于同一目录");
                }
                else if(tempPath[2].equals(tempPath[1])){
                    jObject.put("Result", "error");
                    jObject.put("Message", "路径出错，属于同一目录");
                }
            }
        }
        System.out.println(jObject);
    }


    @Test
    void findProjectFileByProjectNoAndStaffNo(){
        String path= "E:/一级/二级///四级/temp.jpg";
        String paths[]=path.split("/");
        System.out.println(paths.length);
        for (String string : paths) {
            System.out.println(string);
        }
    }

    @Test
    void getFileSize(){
        long fileS = 158360031;
        String size = ""; 
        // long fileS = file.length();
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
        System.out.println(size);
  }
  @Test
  void getDate(){
      Date date = new Date();
      System.out.println(date);
      long times = date.getTime();//时间戳
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = formatter.format(date);
    System.out.println(dateString);
  }
}
