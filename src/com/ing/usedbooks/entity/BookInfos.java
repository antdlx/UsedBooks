package com.ing.usedbooks.entity;

import android.graphics.Bitmap;

public class BookInfos {
	public String picture;
	public String details;
	public String price;
	public String phone;
	public String qq;
	public String name;
	public String id;
	public String scan_number;
	public String saled_time;
	public String x_number;
	public String user_id;
	public String type;
	public String campus;
	public String time;
	
//	public BookInfos(){
//		
//	}
//	
//	public BookInfos(String scan_number,String saled_time,String time,String x_number,String id,String user_id,String name){
//		this.scan_number = scan_number;
//		this.saled_time = saled_time;
//		this.time = time;
//		this.x_number = x_number;
//		this.id = id;
//		this.user_id = user_id;
//		this.name = name;
//		
//	}
//	
//	public BookInfos(String details, String type, String campus,
//			String price, String id,String qq, String phone,
//			String scan_number,String user_id,String name,String time) {
//		
//		this.details = details;
//		this.type = type;
//		this.campus = campus;
//		this.price = price;
//		this.id = id;
//		this.qq = qq;
//		this.phone = phone;
//		this.time = time;
//		this.scan_number  = scan_number;
//		this.user_id = user_id;
//		this.name = name;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScan_number() {
		return scan_number;
	}
	public void setScan_number(String scan_number) {
		this.scan_number = scan_number;
	}
	public String getSaled_time() {
		return saled_time;
	}
	public void setSaled_time(String saled_time) {
		this.saled_time = saled_time;
	}
	public String getX_number() {
		return x_number;
	}
	public void setX_number(String x_number) {
		this.x_number = x_number;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPicture() {
		return picture;
	}

	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	
}