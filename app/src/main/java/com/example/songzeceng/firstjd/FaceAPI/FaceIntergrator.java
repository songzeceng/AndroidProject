package com.example.songzeceng.firstjd.FaceAPI;

import android.graphics.Rect;
import android.util.Log;

import com.arcsoft.ageestimation.ASAE_FSDKAge;
import com.arcsoft.ageestimation.ASAE_FSDKEngine;
import com.arcsoft.ageestimation.ASAE_FSDKError;
import com.arcsoft.ageestimation.ASAE_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.genderestimation.ASGE_FSDKEngine;
import com.arcsoft.genderestimation.ASGE_FSDKError;
import com.arcsoft.genderestimation.ASGE_FSDKFace;
import com.arcsoft.genderestimation.ASGE_FSDKGender;
import com.example.songzeceng.firstjd.Constants;
import com.example.songzeceng.firstjd.Face;

import java.util.ArrayList;
import java.util.List;

public class FaceIntergrator {
	private static final String TAG = "FaceIntergrator";
	private static ASGE_FSDKEngine genderEngine;
	private static ASAE_FSDKEngine ageEngine;
	private static volatile FaceIntergrator intergrator;

	private FaceIntergrator() {
		genderEngine = new ASGE_FSDKEngine();
		ageEngine = new ASAE_FSDKEngine();
		ASGE_FSDKError ge_error = genderEngine.ASGE_FSDK_InitgGenderEngine(Constants.APP_ID,
				Constants.GENDER_KEY);
		ASAE_FSDKError age_error = ageEngine.ASAE_FSDK_InitAgeEngine(Constants.APP_ID,
				Constants.AGE_KEY);
		Log.i(TAG, "ASGE_FSDK_InitgGenderEngine = " + ge_error.getCode());
		Log.i(TAG, "ASAE_FSDK_InitAgeEngine = " + age_error.getCode());
	}

	public static FaceIntergrator getInstance() {
		if (intergrator == null) {
			synchronized (FaceIntergrator.class) {
				if (intergrator == null) {
					intergrator = new FaceIntergrator();
				}
			}
		}
		return intergrator;
	}

	public Face intergrate(byte[] data, Rect rect, int degree, int width, int height, AFR_FSDKFace afr_fsdkFace) {
		List<ASGE_FSDKFace> facesForGender = new ArrayList<>();
		List<ASAE_FSDKFace> facesForAge = new ArrayList<>();
		facesForGender.add(new ASGE_FSDKFace(rect, degree));
		facesForAge.add(new ASAE_FSDKFace(rect, degree));
		List<ASGE_FSDKGender> genders = new ArrayList<>();
		List<ASAE_FSDKAge> ages = new ArrayList<>();

		genderEngine.ASGE_FSDK_GenderEstimation_Image(data, width, height, AFT_FSDKEngine
				.CP_PAF_NV21, facesForGender, genders);
		ageEngine.ASAE_FSDK_AgeEstimation_Image(data, width, height, AFT_FSDKEngine
				.CP_PAF_NV21, facesForAge, ages);
		Face result = new Face("", afr_fsdkFace.getFeatureData(), ages.get(0).getAge(), genders.get(0).getGender());
		return result;
	}

	public static void destroyIntergrator() {
		if (intergrator != null) {
			synchronized (FaceIntergrator.class) {
				if (intergrator != null) {
					genderEngine.ASGE_FSDK_UninitGenderEngine();
					ageEngine.ASAE_FSDK_UninitAgeEngine();
					intergrator = null;
				}
			}
		}
	}
}
