package com.example.songzeceng.firstjd.ArcAPI;

import com.example.songzeceng.firstjd.Face;

import java.util.List;

public interface CameraPreviewListener {
	void onPreviewData(byte[] data,List<Face> fsdkFaces);
	void onPreviewSize(int width,int height);
}