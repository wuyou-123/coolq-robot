package com.wuyou.robot.listeners;

import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnPrivate;
import org.springframework.stereotype.Component;

/**
 * @author wuyou
 */
@Component
public class TestListener {
    @OnPrivate
    @Filters(customFilter = "boot1")
    public void test(){
        System.out.println("receive message!");
    }
}
