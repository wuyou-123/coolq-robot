package com.wuyou.robot.filter;

import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author wuyou
 */
@Component("boot1")
public class TestFilter implements ListenerFilter {
    @Override
    public boolean test(@NotNull FilterData data) {
        System.out.println("filter");
        return false;
    }
}
