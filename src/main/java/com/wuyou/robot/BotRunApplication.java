package com.wuyou.robot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

/**
 * @author Administrator<br>
 *         2020年3月13日
 *
 */

@SpringBootApplication
@MapperScan("com.wuyou.mapper")
@ComponentScan("com.wuyou")
public class BotRunApplication {

	public static void main(String[] args) {
//		BaseApplication.runAuto(BotRunApplication.class, args);
		// new 一个cqhttp组件的启动器，并将你的启动器类放进去。
		ConfigurableApplicationContext a = SpringApplication.run(BotRunApplication.class, args);

		BootClass boot = a.getBean(BootClass.class);
		boot.boot();
	}
}
