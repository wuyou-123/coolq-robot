package com.wuyou.robot;

import com.wuyou.utils.GlobalVariable;
import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.annotation.SimbotResource;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Administrator<br>
 * 2020年3月13日
 */

@SpringBootApplication
@EnableSimbot
@MapperScan("com.wuyou.mapper")
@ComponentScan("com.wuyou")
@SimbotApplication({
        @SimbotResource(value = "application.properties", orIgnore = true)
})
public class BotRunApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BotRunApplication.class, args);
        BootClass boot = context.getBean(BootClass.class);
        GlobalVariable.botManager = context.getBean(BotManager.class);
        boot.boot();
    }
}
