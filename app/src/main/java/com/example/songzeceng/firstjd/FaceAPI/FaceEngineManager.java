package com.example.songzeceng.firstjd.FaceAPI;

import android.content.Context;
import android.util.Log;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.example.songzeceng.firstjd.Constants;
import com.example.songzeceng.firstjd.Face;

import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.FaceEngine.CP_PAF_NV21;

public class FaceEngineManager {
	public static final String TAG = "FaceEngineManager";
	private FaceEngine mFaceEngine;
	private List<FaceInfo> mFaceInfoList = new ArrayList<>();
	private List<AgeInfo> mAgeInfoList = new ArrayList<>();
	private List<GenderInfo> mGenderInfoList = new ArrayList<>();
	private List<Face3DAngle> mFace3DAngleList = new ArrayList<>();
	private List<LivenessInfo> mLivenessInfoList = new ArrayList<>();
	private ArrayList<Face> mFaceResults = new ArrayList<>();
	private List<FaceFeature> mFaceFeatureList = new ArrayList<>();
	private FaceFeature mFaceFeature = new FaceFeature();
	private int mWidth, mHeight;
	private FaceSimilar mFaceSimilar = new FaceSimilar();

	public FaceEngineManager(Context context) {
		mFaceEngine = new FaceEngine();
		int activeCode = mFaceEngine.active(context,
				Constants.APP_ID,
				Constants.SDK_KEY_2);
		if (activeCode == ErrorInfo.MOK) {
			Log.d(TAG, "人脸引擎激活成功");
		} else {
			Log.d(TAG, "人脸引擎激活失败 " + activeCode);
		}

		int engineCode = mFaceEngine.init(context,
				FaceEngine.ASF_DETECT_MODE_VIDEO,
				FaceEngine.ASF_OP_270_ONLY,
				16,
				1,
				FaceEngine.ASF_FACE_RECOGNITION |
						FaceEngine.ASF_FACE_DETECT |
						FaceEngine.ASF_AGE |
						FaceEngine.ASF_GENDER |
						FaceEngine.ASF_FACE3DANGLE);
		if (engineCode == ErrorInfo.MOK) {
			Log.d(TAG, "人脸引擎初始化成功");
		} else {
			Log.d(TAG, "人脸引擎初始化失败 " + engineCode);
		}
	}

	public List<Face> detectFaces(byte[] bytes) {
		mFaceInfoList.clear();
		mFaceResults.clear();
		mAgeInfoList.clear();
		mGenderInfoList.clear();
		mFace3DAngleList.clear();
		mLivenessInfoList.clear();
		mFaceFeatureList.clear();

		int detectFacesCode = mFaceEngine.detectFaces(
				bytes,
				mWidth,
				mHeight,
				CP_PAF_NV21, mFaceInfoList);
		if (detectFacesCode != ErrorInfo.MOK) {
			Log.i(TAG, "人脸检测失败 " + detectFacesCode);
			return null;
		}
		Log.d(TAG, "人脸检测成功 ");
		if (null == mFaceInfoList || mFaceInfoList.size() <= 0) {
			Log.i(TAG, "检测不到人脸数据");
			return mFaceResults;
		}
		Log.d(TAG, "检测到人脸数据 " + mFaceInfoList.size() + mFaceInfoList.toString());
		//循环遍历提取每个人脸特征
		for (FaceInfo faceInfo : mFaceInfoList) {
			extractFaceFeature(bytes, CP_PAF_NV21, faceInfo);
		}
		//检测人脸年龄，性别等
		detectFaceInfo(bytes, CP_PAF_NV21);
		return mFaceResults;
	}

	private void extractFaceFeature(byte[] bytes, int format,
	                                FaceInfo faceInfo) {
		Log.d(TAG, "extractFaceFeature");
		FaceFeature faceFeature = mFaceFeature.clone();
		int extractFaceFeature = mFaceEngine.extractFaceFeature(
				bytes,
				mWidth,
				mHeight,
				format,
				faceInfo,
				faceFeature);
		if (extractFaceFeature != ErrorInfo.MOK) {
			Log.i(TAG, "人脸特征检测失败 " + extractFaceFeature);
			mFaceFeatureList.add(new FaceFeature());
		}
		Log.i(TAG, "人脸特征 " + faceFeature.toString());
		mFaceFeatureList.add(faceFeature);
	}

	private void detectFaceInfo(byte[] bytes, int format) {
		int process = mFaceEngine.process(
				bytes,
				mWidth,
				mHeight,
				format,
				mFaceInfoList, FaceEngine.ASF_AGE |
						FaceEngine.ASF_GENDER |
						FaceEngine.ASF_FACE3DANGLE);
		//如果是个人认证删除 ASF_LIVENESS，该值只对企业认证有效，否则报错
		if (process != ErrorInfo.MOK) {
			Log.d(TAG, "人脸信息检测失败 " + process);
			return;
		}
		Log.d(TAG, "人脸信息检测成功");
		int ageCode = mFaceEngine.getAge(mAgeInfoList);
		int genderCode = mFaceEngine.getGender(mGenderInfoList);
		int angleCode = mFaceEngine.getFace3DAngle(mFace3DAngleList);
		if ((ageCode | genderCode | angleCode) != ErrorInfo.MOK) {
			Log.d(TAG, "获取信息失败,错误码" + "年龄" + ageCode +
					" 性别 " + genderCode +
					" 角度 " + angleCode);
			return;
		}
		for (int i = 0; i < mFaceInfoList.size(); i++) {
			Log.i(TAG, "人脸信息 " + mFaceInfoList.get(i).toString() + " " +
					"年龄" + mAgeInfoList.get(i).getAge() +
					" 性别 " + mGenderInfoList.get(i).getGender() +
					" 角度 " + mFace3DAngleList.get(i).toString());
		}
		//将检测到的信息存储到FaceDetectInfo中
		for (FaceInfo faceInfo : mFaceInfoList) {
			int index = mFaceInfoList.indexOf(faceInfo);
			Face faceDetectInfo = new Face();
			faceDetectInfo.setFaceInfo(mFaceInfoList.get(index));
			faceDetectInfo.setFace3DAngle(mFace3DAngleList.get(index));
			faceDetectInfo.setAge(mAgeInfoList.get(index).getAge());
			faceDetectInfo.setGender(mGenderInfoList.get(index).getGender());
//			faceDetectInfo.setLivenessInfo(mLivenessInfoList.get(index));
			faceDetectInfo.setFaceFeature(mFaceFeatureList.get(index));
			mFaceResults.add(faceDetectInfo);
		}
	}

	public Object[] searchFace(Face target, List<Face> source) {
		if (target == null || source == null || source.size() == 0) {
			return null;
		}
		Face result = new Face();
		FaceFeature targetFeature = target.getFaceFeature();
		float maxGoal = 0f;
		for (int i = 0; i < source.size(); i++) {
			Face currentFace = source.get(i);
			FaceFeature currentFeature = currentFace.getFaceFeature();
			float currentResult = compareFaceFeature(targetFeature, currentFeature);
			if (maxGoal < currentResult) {
				maxGoal = currentResult;
				result = source.get(i);
			}
		}

		return new Object[]{result, maxGoal};
	}

	public float compareFaceFeature(FaceFeature faceFeature1, FaceFeature faceFeature2) {
		int compareFaceFeature = mFaceEngine.compareFaceFeature(faceFeature1, faceFeature2,
				mFaceSimilar);
		if (compareFaceFeature == ErrorInfo.MOK) {
			Log.d(TAG, "人脸比对成功 " + mFaceSimilar.getScore());
			return mFaceSimilar.getScore();
		}
		Log.d(TAG, "人脸比对失败 " + compareFaceFeature);
		return 0;
	}

	public void destory() {
		if (null != mFaceEngine) {
			mFaceEngine.unInit();
		}
	}

	public void setSize(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	public int getmWidth() {
		return mWidth;
	}

	public int getmHeight() {
		return mHeight;
	}
}
