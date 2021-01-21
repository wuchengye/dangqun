package com.dangqun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wcy
 */
@SpringBootApplication
@MapperScan("com.dangqun.mapper")
@EnableTransactionManagement
public class DangQunApplication extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(DangQunApplication.class);
    }
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
