package com.ing.usedbooks.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidityCheck {
	public boolean isUseable(String s) {
		Pattern p = Pattern.compile("[\\w\\u4e00-\\u9fa5]{1,15}");
		Matcher m = p.matcher(s);
		return m.matches();
		}
}
