package com.group.realmanagement;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
public class RealmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealmanagementApplication.class, args);
	}
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        // 超出大小限制的异常怎么catch
        // 单个文件大小不得超过20KB
        multipartConfigFactory.setMaxFileSize(DataSize.parse("1GB"));
        // 多个文件总大小不得超过20KB
        multipartConfigFactory.setMaxRequestSize(DataSize.parse("1GB"));
        // 怎么限制上传文件类型
        return multipartConfigFactory.createMultipartConfig();
    }

}
