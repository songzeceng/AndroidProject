package com.example.songzeceng.firstjd.FaceAPI;

import android.util.Log;

import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;
import com.example.songzeceng.firstjd.Constants;

import java.util.ArrayList;
import java.util.List;

public class FaceFinder {
	//传入的视频数据的长宽
	private int mWidth = 1080, mHeight = 1920;

	private AFD_FSDKEngine mFdEngine;

	public FaceFinder() {
		mFdEngine = new AFD_FSDKEngine();
		AFD_FSDKError err = mFdEngine.AFD_FSDK_InitialFaceEngine(Constants.APP_ID, Constants.FD_KEY, AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5);
		Log.d("FaceFinder", "AFD_FSDK_InitialFaceEngine = "+err.getCode());
	}

	public List<AFD_FSDKFace> findFace(byte[] data) {
		// 用来存放检测到的人脸信息列表
		List<AFD_FSDKFace> result = new ArrayList<AFD_FSDKFace>();
		if (mFdEngine != null) {
			mFdEngine.AFD_FSDK_StillImageFaceDetection(data, mWidth, mHeight, AFD_FSDKEngine.CP_PAF_NV21, result);
		}
		return result;
	}

	public void destroyEngine() {
		mFdEngine.AFD_FSDK_UninitialFaceEngine();
	}

	public void setSize(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int width) {
		mWidth = width;
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int height) {
		mHeight = height;
	}
}