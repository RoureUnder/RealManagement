package com.group.realmanagement.controller.Report;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.group.realmanagement.entity.Projects.ProjectFile;
import com.group.realmanagement.entity.Projects.ProjectFileReview;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectInfoReturn;
import com.group.realmanagement.entity.Projects.ProjectTask;
import com.group.realmanagement.entity.User.Guest;
import com.group.realmanagement.entity.User.Staff;
import com.group.realmanagement.repository.Projects.ProjectFileRepository;
import com.group.realmanagement.repository.Projects.ProjectFileReviewRepository;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.Projects.ProjectTaskRepository;
import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.io.OutputStream;

import java.text.DecimalFormat;


import jxl.Workbook;

import jxl.write.Label;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

@CrossOrigin
@RestController
@RequestMapping("/report")
public class ReportHandler {
    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ProjectInfoRepository projectInfoRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ProjectFileReviewRepository projectFileReviewRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    private static String[] stageStatus = { "未开始", "进行中", "已完成" };

    @RequestMapping("/generateGuestInfoReport")
    public void generateGuestInfoReport(HttpServletResponse response) throws IOException, WriteException {
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        // 下面是对中文文件名的处理
        String fname = "客户信息报表";
        response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
        fname = java.net.URLEncoder.encode(fname, "UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fname.getBytes("UTF-8"), "GBK") + ".xls");
        response.setContentType("application/msexcel");// 定义输出类型
        createGuestInfoExcel(os);
    }

    public void createGuestInfoExcel(OutputStream os) throws WriteException, IOException {

        WritableWorkbook workbook = Workbook.createWorkbook(os);
        // 创建新的一页
        WritableSheet sheet = workbook.createSheet("第一页", 0);
        // 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        List<Guest> guests = guestRepository.findAll();

        sheet.addCell(new Label(0, 0, "客户编号"));
        sheet.addCell(new Label(1, 0, "客户姓名"));
        sheet.addCell(new Label(2, 0, "一级单位"));
        sheet.addCell(new Label(3, 0, "二级单位"));
        sheet.addCell(new Label(4, 0, "三级单位"));
        sheet.addCell(new Label(5, 0, "电话"));
        sheet.addCell(new Label(6, 0, "手机"));
        sheet.addCell(new Label(7, 0, "QQ"));
        sheet.addCell(new Label(8, 0, "E-mail"));

        for (int i = 0; i < guests.size(); i++) {
            sheet.addCell(new Label(0, i + 1, String.valueOf(guests.get(i).getGuestNo())));
            sheet.addCell(new Label(1, i + 1, guests.get(i).getGuestName()));
            sheet.addCell(new Label(2, i + 1, guests.get(i).getPrimaryUnit()));
            sheet.addCell(new Label(3, i + 1, guests.get(i).getSecondaryUnit()));
            sheet.addCell(new Label(4, i + 1, guests.get(i).getTertiaryUnit()));
            sheet.addCell(new Label(5, i + 1, guests.get(i).getTelephone()));
            sheet.addCell(new Label(6, i + 1, guests.get(i).getMobilePhone()));
            sheet.addCell(new Label(7, i + 1, guests.get(i).getQq()));
            sheet.addCell(new Label(8, i + 1, guests.get(i).getEmail()));
        }

        // 把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
    }

    @RequestMapping("/generateStaffInfoReport")
    public void generateStaffInfoReport(HttpServletResponse response) throws IOException, WriteException {
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        // 下面是对中文文件名的处理
        String fname = "员工信息报表";
        response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
        fname = java.net.URLEncoder.encode(fname, "UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fname.getBytes("UTF-8"), "GBK") + ".xls");
        response.setContentType("application/msexcel");// 定义输出类型
        createStaffInfoExcel(os);
    }

    public void createStaffInfoExcel(OutputStream os) throws WriteException, IOException {

        WritableWorkbook workbook = Workbook.createWorkbook(os);
        // 创建新的一页
        WritableSheet sheet = workbook.createSheet("第一页", 0);
        // 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        List<Staff> staffs = staffRepository.findAll();

        sheet.addCell(new Label(0, 0, "员工编号"));
        sheet.addCell(new Label(1, 0, "员工姓名"));
        sheet.addCell(new Label(2, 0, "部门"));
        sheet.addCell(new Label(3, 0, "职位"));
        sheet.addCell(new Label(4, 0, "联系方式"));

        for (int i = 0; i < staffs.size(); i++) {
            sheet.addCell(new Label(0, i + 1, String.valueOf(staffs.get(i).getStaffNo())));
            sheet.addCell(new Label(1, i + 1, staffs.get(i).getName()));
            sheet.addCell(new Label(2, i + 1, staffs.get(i).getDepartment()));
            sheet.addCell(new Label(3, i + 1, staffs.get(i).getPost()));
            sheet.addCell(new Label(4, i + 1, staffs.get(i).getContact()));
        }
        // for(int i=0;i<sheet.length;i++){
        // for(int j=0;j<content[i].length;j++){
        // //根据内容自动设置列宽
        // lab = new Label(j,i+2,content[i][j],wcfcontent); //Label(col,row,str);
        // sheet.addCell(lab);
        // // sheet.setColumnView(j, new String(content[i][j]).length());
        // }
        // }
        // 把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
    }

    @RequestMapping("/generateProjectSummaryReport")
    public void generateProjectSummaryReport(HttpServletResponse response) throws IOException, WriteException {
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        // 下面是对中文文件名的处理
        String fname = "项目汇总报表";
        response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
        fname = java.net.URLEncoder.encode(fname, "UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fname.getBytes("UTF-8"), "GBK") + ".xls");
        response.setContentType("application/msexcel");// 定义输出类型
        createProjectSummaryReport(os);
    }

    public void createProjectSummaryReport(OutputStream os) throws WriteException, IOException {

        WritableWorkbook workbook = Workbook.createWorkbook(os);
        // 创建新的一页
        WritableSheet sheet = workbook.createSheet("第一页", 0);
        // 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        List<ProjectInfo> projectInfos = projectInfoRepository.findAll();

        sheet.addCell(new Label(0, 0, "项目编号"));
        sheet.addCell(new Label(1, 0, "项目名称"));
        sheet.addCell(new Label(2, 0, "客户姓名"));
        sheet.addCell(new Label(3, 0, "主负责人"));
        sheet.addCell(new Label(4, 0, "模型负责人"));
        sheet.addCell(new Label(5, 0, "模型状态"));
        sheet.addCell(new Label(6, 0, "渲染负责人"));
        sheet.addCell(new Label(7, 0, "渲染状态"));
        sheet.addCell(new Label(8, 0, "后期负责人"));
        sheet.addCell(new Label(9, 0, "后期状态"));
        sheet.addCell(new Label(10, 0, "开始时间"));
        sheet.addCell(new Label(11, 0, "结束时间"));
        sheet.addCell(new Label(12, 0, "项目报价"));
        sheet.addCell(new Label(13, 0, "当前状态"));
        sheet.addCell(new Label(14, 0, "文件数"));
        sheet.addCell(new Label(15, 0, "总审核数"));
        sheet.addCell(new Label(16, 0, "审核通过率"));
        sheet.addCell(new Label(17, 0, "总任务数"));
        sheet.addCell(new Label(18, 0, "任务通过率"));

        for (int i = 0; i < projectInfos.size(); i++) {
            ProjectInfoReturn projectInfoReturn = new ProjectInfoReturn(projectInfos.get(i), staffRepository,
                    guestRepository);
            sheet.addCell(new Label(0, i + 1, String.valueOf(projectInfoReturn.getProjectNo())));
            sheet.addCell(new Label(1, i + 1, projectInfoReturn.getProjectName()));
            sheet.addCell(new Label(2, i + 1, projectInfoReturn.getGuestName()));
            sheet.addCell(new Label(3, i + 1, projectInfoReturn.getPrincipalName()));
            sheet.addCell(new Label(4, i + 1, projectInfoReturn.getModelName()));
            sheet.addCell(new Label(5, i + 1, getStatus(projectInfoReturn.getModelStatus())));
            sheet.addCell(new Label(6, i + 1, projectInfoReturn.getRenderName()));
            sheet.addCell(new Label(7, i + 1, getStatus(projectInfoReturn.getRenderStatus())));
            sheet.addCell(new Label(8, i + 1, projectInfoReturn.getLateName()));
            sheet.addCell(new Label(9, i + 1, getStatus(projectInfoReturn.getLateStatus())));
            sheet.addCell(new Label(10, i + 1, projectInfoReturn.getStartTime().toString()));
            if (projectInfoReturn.getEndTime() == null) {
                sheet.addCell(new Label(11, i + 1, "进行中"));
            } else
                sheet.addCell(new Label(11, i + 1, projectInfoReturn.getEndTime().toString()));

            sheet.addCell(new Label(12, i + 1, projectInfoReturn.getOffer().toString()));
            sheet.addCell(new Label(13, i + 1, getStatus(projectInfoReturn.getStatus())));

            List<ProjectFile> projectFiles = projectFileRepository.findByProjectNo(projectInfoReturn.getProjectNo());
            List<ProjectFileReview> projectFileReviews = projectFileReviewRepository
                    .findByProjectNo(projectInfoReturn.getProjectNo());
            List<ProjectTask> projectTasks = projectTaskRepository.findByProjectNo(projectInfoReturn.getProjectNo());
            sheet.addCell(new Label(14, i + 1, String.valueOf(projectFiles.size())));
            sheet.addCell(new Label(15, i + 1, String.valueOf(projectFileReviews.size())));
            int temp = 0;
            for (ProjectFileReview projectFileReview : projectFileReviews) {
                if (projectFileReview.getStatus() == 2) {
                    temp++;
                }
            }
            sheet.addCell(
                    new Label(16, i + 1, getPersent(projectFileReviews.size() - temp, projectFileReviews.size())));
            sheet.addCell(new Label(17, i + 1, String.valueOf(projectTasks.size())));
            temp = 0;
            for (ProjectTask projectTask : projectTasks) {
                if (projectTask.getStatus() == 2) {
                    temp++;
                }
            }
            sheet.addCell(new Label(18, i + 1, getPersent(projectTasks.size() - temp, projectTasks.size())));
        }

        // 把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
    }

    @RequestMapping("/generateStaffPerformanceReport")
    public void generateStaffPerformanceReport(HttpServletResponse response) throws IOException, WriteException {
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        // 下面是对中文文件名的处理
        String fname = "员工绩效报表";
        response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
        fname = java.net.URLEncoder.encode(fname, "UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String(fname.getBytes("UTF-8"), "GBK") + ".xls");
        response.setContentType("application/msexcel");// 定义输出类型
        createStaffPerformanceReport(os);
    }

    public void createStaffPerformanceReport(OutputStream os) throws WriteException, IOException {

        WritableWorkbook workbook = Workbook.createWorkbook(os);
        // 创建新的一页
        WritableSheet sheet = workbook.createSheet("第一页", 0);
        // 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        List<Staff> staffs = staffRepository.findAll();

        sheet.addCell(new Label(0, 0, "员工编号"));
        sheet.addCell(new Label(1, 0, "员工姓名"));
        sheet.addCell(new Label(2, 0, "部门"));
        sheet.addCell(new Label(3, 0, "职位"));
        sheet.addCell(new Label(4, 0, "参与项目个数"));
        sheet.addCell(new Label(5, 0, "项目总价值"));
        sheet.addCell(new Label(6, 0, "已完成项目个数"));
        sheet.addCell(new Label(7, 0, "上传文件个数"));
        sheet.addCell(new Label(8, 0, "审核通过率"));
        sheet.addCell(new Label(9, 0, "任务总数"));
        sheet.addCell(new Label(10, 0, "任务接受率"));
        sheet.addCell(new Label(11, 0, "单任务价值"));

        for (int i = 0; i < staffs.size(); i++) {
            // 查找任务=>项目数=>
            List<ProjectTask> projectTasks = projectTaskRepository.findByReceiverNo(staffs.get(i).getStaffNo());
            List<ProjectInfo> projectInfos = new ArrayList<>();
            List<ProjectFileReview> projectFileReviews = projectFileReviewRepository
                    .findByUploader(staffs.get(i).getStaffNo());

            for (ProjectTask projectTask : projectTasks) {
                ProjectInfo temp = projectInfoRepository.findByProjectNo(projectTask.getProjectNo());
                System.out.println("projectNo="+temp.getProjectNo()+projectInfos.indexOf(temp));
                if (temp != null && projectInfos.indexOf(temp) == -1) {// 未存入projectInfos当中
                    projectInfos.add(temp);
                }
            }
            // System.out.println("staffNo="+staffs.get(i).getStaffNo());
            // System.out.println("projectTasks.size="+projectTasks.size());
            // System.out.println("projectInfo.size="+projectInfos.size());
            sheet.addCell(new Label(0, i + 1, String.valueOf(staffs.get(i).getStaffNo())));
            sheet.addCell(new Label(1, i + 1, staffs.get(i).getName()));
            sheet.addCell(new Label(2, i + 1, staffs.get(i).getDepartment()));
            sheet.addCell(new Label(3, i + 1, staffs.get(i).getPost()));
            sheet.addCell(new Label(4, i + 1, String.valueOf(projectInfos.size())));
            int temp = 0;
            double sumOffer = 0.0;
            for (ProjectInfo projectInfo : projectInfos) {
                sumOffer += projectInfo.getOffer();
                if (projectInfo.getStatus() == 2) {
                    temp++;
                }
            }
            sheet.addCell(new Label(5, i + 1, String.valueOf(sumOffer)));
            sheet.addCell(new Label(6, i + 1, String.valueOf(temp)));
            sheet.addCell(new Label(7, i + 1, String.valueOf(projectFileReviews.size())));
            temp = 0;
            for (ProjectFileReview projectFileReview : projectFileReviews) {
                if (projectFileReview.getStatus() == 2) {
                    temp++;
                }
            }
            sheet.addCell(new Label(8, i + 1, getPersent(projectFileReviews.size() - temp, projectFileReviews.size())));
            sheet.addCell(new Label(9, i + 1, String.valueOf(projectTasks.size())));
            temp = 0;
            for (ProjectTask projectTask : projectTasks) {
                if (projectTask.getStatus() == 2) {
                    temp++;
                }
            }
            sheet.addCell(new Label(10, i + 1, getPersent(projectTasks.size() - temp, projectTasks.size())));
            sheet.addCell(new Label(11, i + 1, getPersent(sumOffer,projectTasks.size())));
        }

        // 把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
    }

    String getPersent(int a, int b) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        String k = df.format(a * 100.00 / b) + "%";
        if(a==0||b==0){
            return "0";
        }
        return k;
    }

    String getPersent(double a, int b) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        String k = df.format(a  / b);
        if(a==0||b==0){
            return "0";
        }
        return k;
    }

    String getStatus(int status) {
        return stageStatus[status];
    }
}
