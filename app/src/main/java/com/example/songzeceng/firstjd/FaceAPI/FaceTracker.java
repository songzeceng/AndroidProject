//package com.example.songzeceng.firstjd.FaceAPI;
//
//import android.util.Log;
//
//import com.arcsoft.facetracking.AFT_FSDKEngine;
//import com.arcsoft.facetracking.AFT_FSDKError;
//import com.arcsoft.facetracking.AFT_FSDKFace;
//import com.example.songzeceng.firstjd.Constants;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FaceTracker {
//
//	//传入的视频数据的长宽
//	private int width = 1080, height = 1920;
//
//	//人脸追踪SDK
//	private AFT_FSDKEngine ftEngine;
//
//
//	public FaceTracker() {
//		ftEngine = new AFT_FSDKEngine();
//		int ftInitErrorCode = ftEngine.AFT_FSDK_InitialFaceEngine(Constants.APP_ID,
//				Constants.FT_KEY, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT,
//				16, 5).getCode();
//		if (ftInitErrorCode != 0) {
//			Log.i("FT初始化失败，errorcode：", ftInitErrorCode + "");
//		}
//	}
//
//	public List<AFT_FSDKFace> getFtfaces(byte[] data) {
//		List<AFT_FSDKFace> ftFaceList = new ArrayList<>();
//		//视频FT检测人脸
//		int ftCode = ftEngine.AFT_FSDK_FaceFeatureDetect(data, width, height,
//				AFT_FSDKEngine.CP_PAF_NV21, ftFaceList).getCode();
//		// 虹软人脸检测只支持NV21？Camera2获取到的是YUV_420_888
//		if (ftCode != AFT_FSDKError.MOK) {
//			Log.i("FaceTracker", "AFT_FSDK_FaceFeatureDetect: errorcode " + ftCode);
//		}
//		return ftFaceList;
//	}
//
//
//	//销毁引擎
//	public void destoryEngine() {
//		ftEngine.AFT_FSDK_UninitialFaceEngine();
//	}
//
//	public void setSize(int width, int height) {
//		this.width = width;
//		this.height = height;
//	}
//
//	public int getWidth() {
//		return width;
//	}
//
//	public int getHeight() {
//		return height;
//	}
//
//	public void setWidth(int width) {
//		this.width = width;
//	}
//
//	public void setHeight(int height) {
//		this.height = height;
//	}
//}
