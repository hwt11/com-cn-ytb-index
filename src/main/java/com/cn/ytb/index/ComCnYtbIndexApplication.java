package com.cn.ytb.index;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.cn.red.point.o2c.mapper")
public class ComCnYtbIndexApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComCnYtbIndexApplication.class, args);
    }

}
