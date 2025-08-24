package com.health;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 健康管理小程序主启动类
 * 
 * @author Health Team
 * @since 2024-01-20
 */
@SpringBootApplication
@MapperScan("com.health.mapper")
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class HealthManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthManagementApplication.class, args);
        System.out.println("");
        System.out.println("  _    _            _ _   _       __  __                                                   _   ");
        System.out.println(" | |  | |          | | | | |     |  \\/  |                                                 | |  ");
        System.out.println(" | |__| | ___  __ _| | |_| |__   | |\\/| | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ ");
        System.out.println(" |  __  |/ _ \\/ _` | | __| '_ \\  | |  | |/ _` | '_ \\ / _` |/ _` |/ _ \\ '_ ` _ \\ / _ \\ '_ \\| __|");
        System.out.println(" | |  | |  __/ (_| | | |_| | | | | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ ");
        System.out.println(" |_|  |_|\\___\\__,_|_|\\__|_| |_| |_|  |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_| |_| |_|\\___|_| |_|\\__|");
        System.out.println("                                                          __/ |                            ");
        System.out.println("                                                         |___/                             ");
        System.out.println("");
        System.out.println("健康管理小程序后端服务启动成功！");
        System.out.println("访问地址: http://localhost:8080/api");
        System.out.println("");
    }
}