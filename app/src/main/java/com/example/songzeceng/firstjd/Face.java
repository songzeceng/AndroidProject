package com.example.songzeceng.firstjd;

import java.io.Serializable;

public class Face implements Serializable {
	private String name;
	private byte[] data;
	private int age = -1;
	private int gender = -1;

	public Face(String name, byte[] data, int age, int gender) {
		this.name = name;
		this.data = data;
		this.age = age;
		this.gender = gender;
	}

	public Face(String name, byte[] data) {
		this.name = name;
		this.data = data;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}