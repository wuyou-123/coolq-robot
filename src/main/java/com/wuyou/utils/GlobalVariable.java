package com.wuyou.utils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
public class GlobalVariable {
	public static Map<String, Boolean> bootMap;
	public static Map<String, String> face;
	public static String kouzi;
	public static String menu;
	public static final BlockingQueue<String> setuQueue = new LinkedBlockingQueue<String>(5);
	public static final BlockingQueue<String> setuR18Queue = new LinkedBlockingQueue<String>(5);

}
