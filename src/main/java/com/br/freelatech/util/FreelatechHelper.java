package com.br.freelatech.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FreelatechHelper {

	public static String getCurrentMySQLDate() {
		return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
	}

	public static String nl2br(String str) {
		return str.replaceAll("(\\r\\n|\\n)", "<br />");
	}
}
