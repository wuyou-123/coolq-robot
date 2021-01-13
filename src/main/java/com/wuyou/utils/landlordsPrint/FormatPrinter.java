package com.wuyou.utils.landlordsPrint;

import com.wuyou.utils.SenderUtil;

import java.util.Formatter;

public class FormatPrinter {

	private FormatPrinter() {
	}

	public static void printNotice(String qq, String format, Object... args) {
		SenderUtil.sendPrivateMsg(qq,new Formatter().format(format, args).toString());
//		System.out.printf(format, args);
	}
}
