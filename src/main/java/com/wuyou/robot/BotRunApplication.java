package com.wuyou.robot;

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
@MapperScan("com.wuyou.mapper")
@ComponentScan("com.wuyou")
public class BotRunApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BotRunApplication.class, args);
		BootClass boot = context.getBean(BootClass.class);
		boot.boot();
	}
}
