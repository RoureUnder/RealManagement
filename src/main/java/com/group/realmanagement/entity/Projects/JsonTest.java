package com.group.realmanagement.entity.Projects;

import java.io.*;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonTest {
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getIndexOfPath(String str) {
        JSONObject jObject = new JSONObject();
        String path = "D:/VsCodeProjects/VS-Code-Springboot/RealManagement/realmanagement/src/main/resources/depends/Directory.json";
        String tempStr = JsonTest.readJsonFile(path);
        jObject = JSON.parseObject(tempStr);
        for(int i=0;i<5;i++){
            String tempPath=jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
            if(tempPath.equals(str)){
                return 1;
            }
            if(jObject.getJSONArray("dirs").getJSONObject(i).size()!=1)
            {
                for(int j=0;j<2;j++){
                    tempPath= jObject.getJSONArray("dirs").getJSONObject(i).getJSONArray("dirs").getJSONObject(j).getString("name");
                    // System.out.println(tempPath+"-->"+2);
                    if(tempPath.equals(str)){
                        return 2;
                    }
                    tempPath=jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
                }
            }
            else{
                // System.out.println(tempPath+"-->"+1);
                if(tempPath.equals(str)){
                    return 1;
                }
            }
        }
        return 0;
    }
}
