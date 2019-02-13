package com.example.songzeceng.firstjd.FaceAPI;

import android.graphics.Rect;
import android.util.Log;

import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;
import com.example.songzeceng.firstjd.Constants;
import com.example.songzeceng.firstjd.Face;

import java.util.List;

public class FaceRecognizer {
	public static final String TAG = "FaceRecognizer";
	//传入的视频数据的长宽
	private int mWidth = 1080, mHeight = 1920;

	//人脸对比引擎
	private AFR_FSDKEngine mFrEngine;

	public FaceRecognizer() {
		mFrEngine = new AFR_FSDKEngine();
		AFR_FSDKError fr_error = mFrEngine.AFR_FSDK_InitialEngine(Constants.APP_ID, Constants
				.FR_KEY);
		Log.i(TAG, "AFR_FSDK_InitialEngine = " + fr_error.getCode());
	}

	/**
	 * 获取人脸特征
	 *
	 * @param data   图像数据
	 * @param rect   人脸位置
	 * @param degree 人脸角度
	 * @return
	 */
	public Face faceData(byte[] data, Rect rect, int degree) {
		AFR_FSDKFace afr_fsdkFace = new AFR_FSDKFace();
		mFrEngine.AFR_FSDK_ExtractFRFeature(data, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21, rect,
				degree, afr_fsdkFace);
		return FaceIntergrator.getInstance().intergrate(data, rect, degree, mWidth, mHeight, afr_fsdkFace);
	}


	/**
	 * 人脸对比
	 *
	 * @param faceData1 人脸特征
	 * @param faceData2
	 * @return
	 */
	public float faceRecognition(byte[] faceData1, byte[] faceData2) {
		//score用于存放人脸对比的相似度值
		AFR_FSDKMatching score = new AFR_FSDKMatching();
		//用来存放提取到的人脸信息, face_1是注册的人脸，face_2是要识别的人脸
		AFR_FSDKFace face1 = new AFR_FSDKFace();
		AFR_FSDKFace face2 = new AFR_FSDKFace();
		face1.setFeatureData(faceData1);
		face2.setFeatureData(faceData2);
		mFrEngine.AFR_FSDK_FacePairMatching(face1, face2, score);
		return score.getScore();
	}


	/**
	 * 人脸搜索
	 */
	public void faceSearch(byte[] faceData, List<byte[]> faceDataList, FaceSearchListener
			listener) {
		AFR_FSDKMatching score = new AFR_FSDKMatching();
		AFR_FSDKFace face1 = new AFR_FSDKFace();
		face1.setFeatureData(faceData);
		AFR_FSDKFace face2 = new AFR_FSDKFace();
		int positon = 0;
		float max = 0.0f;
		for (int i = 0; i < faceDataList.size(); i++) {
			float like = 0.0f;
			face2.setFeatureData(faceDataList.get(i));
			mFrEngine.AFR_FSDK_FacePairMatching(face1, face2, score);
			like = score.getScore();
			if (like > max) {
				max = like;
				positon = i;
			}
		}
		listener.searchFinish(max, positon);
	}


	public void destroyEngine() {
		mFrEngine.AFR_FSDK_UninitialEngine();
		FaceIntergrator.destroyIntergrator();
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
