package com.cnts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RuoYiApplication
{
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("////////////////////////////////////////////");
        System.out.println("//          校园网故障系统启动成功～          //");
        System.out.println("//----------------------------------------//");
        System.out.println("//    #####   ##    ##  ########   #####  //");
        System.out.println("//  ##    ##  ###   ##     ##     ##      //");
        System.out.println("// ##         ####  ##     ##     ##      //");
        System.out.println("// ##         ## ## ##     ##      #####  //");
        System.out.println("// ##         ##  ####     ##          ## //");
        System.out.println("//  ##    ##  ##   ###     ##          ## //");
        System.out.println("//    ####    ##    ##     ##     ######  //");
        System.out.println("//                                        //");
        System.out.println("//     Campus Network Trouble System      //");
        System.out.println("//         欢迎使用校园网故障管理系统         //");
        System.out.println("////////////////////////////////////////////");
    }
}
