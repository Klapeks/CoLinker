package com.klapeks.colinker;

import java.util.Arrays;

import com.klapeks.coserver.dFunctions;
import com.klapeks.funcs.dRSA;

public class Functions {
	
	public static String convertArgs(String cmd, String... args) {
		cmd = dRSA.base64_encode(cmd);
		for (String arg : args) {
			cmd += " " + dRSA.base64_encode(arg);
		}
		return cmd;
	}
	
	public static <T> T[] removeFirst(T[] t, int amount) {
		return Arrays.copyOfRange(t, amount, t.length);
	}
	static String prefix = "§d[§9CoLinker§d]§r ";
	public static void log(Object obj, Object... objs) {
		String s = prefix + obj;
		if (objs!=null && objs.length > 0) s = formater(s, objs);
		dFunctions.log_(s);
	}
	public static String formater(String str, Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			str = str.replace("{"+i+"}", objs[i]+"");
		}
		return str;
	}

	public static boolean contains(String[] ss, String s) {
		for (String sss : ss) {
			if (sss.equals(s)) return true;
		}
		return false;
	}
}
