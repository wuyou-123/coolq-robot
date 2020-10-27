package com.wuyou.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
public class GlobalVariable {
	public static Map<String, Boolean> bootMap;
	public static Map<String, String> face;
	public static Map<String, String> webCookie;
	public static Map<String, Map<String, Object>> groupDragon;
	public static List<String> administrator;
	public static String kouzi;
	public static String menu;
	public static final BlockingQueue<String> setuQueue = new LinkedBlockingQueue<>(5);
	public static final BlockingQueue<String> setuR18Queue = new LinkedBlockingQueue<>(5);
	public static final ExecutorService threadPool = Executors.newFixedThreadPool(50);
}
