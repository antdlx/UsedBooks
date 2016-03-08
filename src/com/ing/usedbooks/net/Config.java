package com.ing.usedbooks.net;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Config {

	

	public static String SERVER_URL_SEARCH = "http://1.myoldbooks.sinaapp.com/scan.php?";
	public static String SERVER_URL_UPLOAD = "http://myoldbooks-booksapk.stor.sinaapp.com/IceHoloReader1.0.apk";
	public static String SERVER_URL_TOKEN = "http://1.myoldbooks.sinaapp.com/upload_token.php?";
	public static String SERVER_URL_SUGGEST = "http://1.myoldbooks.sinaapp.com/suggest.php?";
	public static String SERVER_URL_BROWSE = "http://1.myoldbooks.sinaapp.com/scan.php?";
	public static String SERVER_URL_CLASSIFICATION = "http://1.myoldbooks.sinaapp.com/scan.php?";
	public static String SERVER_URL_GETVERSIONNAME = "http://1.myoldbooks.sinaapp.com/ser.php";
	public static String SERVER_URL_GETPICS = "http://oldbooks.qiniudn.com/";
	public static String SERVER_URL_SELLTIME="http://1.myoldbooks.sinaapp.com/saled_time.php?";
	public static String SERVER_URL_MYBOOKS="http://1.myoldbooks.sinaapp.com/myoldbooks.php?";
	public static String SERVER_URL_APK="http://1.myoldbooks.sinaapp.com/apk.php";
	
	public static String CHARSET="utf-8";
	
	public static String KEY_NAME="name";
	public static String KEY_IMEI="x_number";
	public static String KEY_MESSAGE="details";
	public static String KEY_PICTURE="picture";
	public static String KEY_PRICE="price";
	public static String KEY_TYPE="type";
	public static String KEY_CAMPUS="campus";
	public static String KEY_PHONE="phone";
	public static String KEY_QQ="qq";
	public static String KEY_DETAILS="details";
	public static String KEY_SUGGEST="suggest";
	public static String KEY_SIGNAL = "signal";
	public static String KEY_LAST_TIME = "last_time";
	public static  String KEY_PAGESIZE = "pagesize";
	public static  String KEY_PAGE = "page";
	public static  String KEY_DATE = "date";
	public static  String KEY_ID = "id";
	public static  String KEY_BID = "b_id";
	public static  String KEY_SCANNUM = "scan_number";
	public static  String KEY_SELLTIME = "saled_time";
	public static  String KEY_SALE = "sale";
	public static  String KEY_USERID = "user_id";
	
	public static String CLASSIFICATION_COM = "computer";
	public static String CLASSIFICATION_CET = "cet";
	public static String CLASSIFICATION_LITERATURE = "literature";
	public static String CLASSIFICATION_MAGAZINE = "magazine";
	public static String CLASSIFICATION_NOVEL = "novel";
	public static String CLASSIFICATION_CARTOON = "cartoon";
	public static String CLASSIFICATION_STUDY = "study";
	public static String CLASSIFICATION_ELSE = "else";
	
	public static String CAMPUS_SOFTWARE = "杞欢鍥�";
	
	public static String IMEI = "250";
	
	public void setIMEI(String s){
		IMEI = s;
	}
}
