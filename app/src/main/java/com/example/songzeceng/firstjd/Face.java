package com.example.songzeceng.firstjd;

import android.content.pm.FeatureInfo;

import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.LivenessInfo;

import java.io.Serializable;

public class Face implements Serializable {
	private String name;
	private byte[] data;
	private int age = -1;
	private int gender = -1;
	private FaceInfo faceInfo;
	private Face3DAngle face3DAngle;
	private LivenessInfo livenessInfo;
	private FaceFeature faceFeature;

	public Face() {
	}

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

	public FaceInfo getFaceInfo() {
		return faceInfo;
	}

	public void setFaceInfo(FaceInfo faceInfo) {
		this.faceInfo = faceInfo;
	}

	public Face3DAngle getFace3DAngle() {
		return face3DAngle;
	}

	public void setFace3DAngle(Face3DAngle face3DAngle) {
		this.face3DAngle = face3DAngle;
	}

//	public void setLivenessInfo(LivenessInfo livenessInfo) {
//		this.livenessInfo = livenessInfo;
//	}
//
	public FaceFeature getFaceFeature() {
		return faceFeature;
	}

	public void setFaceFeature(FaceFeature faceFeature) {
		this.faceFeature = faceFeature;
	}
}