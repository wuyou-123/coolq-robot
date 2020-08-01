package com.wuyou.robot;

import com.forte.component.forcoolqhttpapi.CQHttpContext;
import com.forte.qqrobot.SimpleRobotApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Administrator<br>
 *         2020年3月13日
 *
 */

@SpringBootApplication
@SimpleRobotApplication(resources = "/resources/application.properties")
@MapperScan("com.wuyou.mapper")
@ComponentScan("com.wuyou")
public class BotRunApplication {
	public static CQHttpContext run;

	public static void main(String[] args){
		// new 一个cqhttp组件的启动器，并将你的启动器类放进去。
		ConfigurableApplicationContext a = SpringApplication.run(BotRunApplication.class, args);
		BootClass boot = a.getBean(BootClass.class);
		boot.boot();
	}
}
