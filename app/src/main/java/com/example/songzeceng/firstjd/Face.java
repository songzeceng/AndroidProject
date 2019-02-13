package com.example.songzeceng.firstjd;

import java.io.Serializable;

public class Face implements Serializable {
	private String mName;
	private byte[] mData;
	private int mAge = -1;
	private int mGender = -1;

	public Face(String name, byte[] data, int age, int gender) {
		mName = name;
		mData = data;
		mAge = age;
		mGender = gender;
	}

	public Face(String name, byte[] data) {
		mName = name;
		mData = data;
	}

	public int getAge() {
		return mAge;
	}

	public void setAge(int age) {
		mAge = age;
	}

	public int getGender() {
		return mGender;
	}

	public void setGender(int gender) {
		mGender = gender;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public byte[] getData() {
		return mData;
	}

	public void setData(byte[] data) {
		mData = data;
	}
}