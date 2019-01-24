package com.example.songzeceng.firstjd.ArcAPI;

import com.arcsoft.facetracking.AFT_FSDKFace;

import java.util.List;

public interface CameraPreviewListener {
	void onPreviewData(byte[] data,List<AFT_FSDKFace> fsdkFaces);
	void onPreviewSize(int width,int height);
}