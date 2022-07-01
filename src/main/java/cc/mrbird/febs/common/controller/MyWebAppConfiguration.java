package cc.mrbird.febs.common.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebAppConfiguration extends WebMvcConfigurerAdapter {
    /**
     * Spring Boot中有默认的静态资源访问路径，浏览器也不允许访问项目目录外的资源文件
     * 添加一些虚拟路径的映射
     * 设置静态资源路径和上传文件的路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /** 表示该目录下所有文件
        registry.addResourceHandler("/PathImage/**").addResourceLocations("file:/D:/science-2.0/");
        registry.addResourceHandler("/PathFile/**").addResourceLocations("file:/D:/science-2.0/");
        super.addResourceHandlers(registry);
    }
}
