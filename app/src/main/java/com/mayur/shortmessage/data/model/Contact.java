package com.mayur.shortmessage.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable {
	public long id;
	public String name;
	public String number;
	public String photoUri = "";
	public List<String> allPhoneNumber = new ArrayList<>();
	public Contact() {
	}

	public Contact(long id, String name, String number) {
		this.id = id;
		this.name = name;
		this.number = number;
	}
	public Contact(long id, String name, String number, String photoUri) {
		this.id = id;
		this.name = name;
		this.number = number;
		this.photoUri = photoUri;
	}
	
	public static Contact parseCached(String s) {
		String[] line = s.split("\t");
		int recipientId = Integer.parseInt(line[0]);
		String name = line[1];
		String number = line[2];
		return new Contact(recipientId, name, number);
	}
		
//	@Override
//	public String toString() {
//		return id + "\t" + name + "\t" + number;
//	}
	
	public String getFormatted() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(" <");
		sb.append(number);
		sb.append(">");
		return sb.toString();
	}
	public String getNameOrNumber(){
		return name!=null ? name : number;
	}

	public void setAllPhoneNumber(List<String> allPhoneNumber) {
		this.allPhoneNumber = allPhoneNumber;
	}

	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}

}
