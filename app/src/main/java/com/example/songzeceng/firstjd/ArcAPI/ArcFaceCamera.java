package com.example.songzeceng.firstjd.ArcAPI;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.arcsoft.facetracking.AFT_FSDKFace;
import com.example.songzeceng.firstjd.FaceAPI.FaceTracker;
import com.example.songzeceng.firstjd.LivenessActivity;
import com.example.songzeceng.firstjd.utils.DrawUtils;

import java.util.List;

public class ArcFaceCamera implements SurfaceHolder.Callback {
	public static final String TAG = "ArcFaceCamera";
	public static int previewSizeX, previewSizeY;
	private static ArcFaceCamera instance;

	SurfaceView mSurfcePreview, mSurfceRect;
	SurfaceHolder mHolder;
	private Camera mCamera;
	private Activity mActivity;
	private CameraPreviewListener mCameraPreviewListener;
	FaceTracker mFaceTracker;

	//相机的位置
	private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
	//相机的方向
	private int mCameraOri = 90;

	public void init(int cameraId) {
		mCameraId = cameraId;
	}

	private ArcFaceCamera() {

	}


	public void openCamera(Activity activity, SurfaceView surfacePreview, SurfaceView
			surfaceViewRect) {
		mActivity = activity;
		mSurfcePreview = surfacePreview;
		mSurfceRect = surfaceViewRect;
		mSurfcePreview.getHolder().addCallback(this);
		mSurfceRect.setZOrderMediaOverlay(true);
		mSurfceRect.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mFaceTracker = new FaceTracker();
	}


	public static ArcFaceCamera getInstance() {
		if (instance == null) {
			synchronized (ArcFaceCamera.class) {
				if (instance == null) {
					instance = new ArcFaceCamera();
				}
			}
		}
		return instance;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mHolder = holder;
		start();
	}


	private void start() {
		//选择摄像头ID
		mCamera = Camera.open(mCameraId);
		try {
			DisplayMetrics metrics = new DisplayMetrics();
			mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			Camera.Parameters parameters = mCamera.getParameters();

			Camera.Size previewSize = getBestSupportedSize(parameters.getSupportedPreviewSizes(),
					metrics);
            /*previewSize.width = 800;
            previewSize.height = 600;*/
			parameters.setPreviewSize(previewSize.width, previewSize.height);
			parameters.setPreviewFormat(ImageFormat.NV21);
			mCamera.setParameters(parameters);
			previewSizeX = previewSize.width;
			previewSizeY = previewSize.height;
			mFaceTracker.setSize(previewSize.width, previewSize.height);
			if (mCameraPreviewListener != null) {
				mCameraPreviewListener.onPreviewSize(previewSize.width, previewSize.height);
			}
			//mCamera.setDisplayOrientation(mCameraOri);

			setCameraDisplayOrientation(mActivity, mCameraId, mCamera);

			mCamera.setPreviewDisplay(mHolder);
			mCamera.setPreviewCallback(new Camera.PreviewCallback() {
				@Override
				public void onPreviewFrame(byte[] data, Camera camera) {
					clearReact();
					//获取人脸的位置信息
					List<AFT_FSDKFace> fsdkFaces = mFaceTracker.getFtfaces(data);
					//画出人脸的位置
					drawFaceRect(fsdkFaces);
					//输出数据进行其他处理
					if ((mCameraPreviewListener != null && fsdkFaces.size() > 0) ||
							LivenessActivity.flag == 0) {
						mCameraPreviewListener.onPreviewData(data.clone(), fsdkFaces);
					}
				}
			});
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void clearReact() {
		final Canvas canvas = mSurfceRect.getHolder().lockCanvas();
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		mSurfceRect.getHolder().unlockCanvasAndPost(canvas);
	}

	private void drawFaceRect(List<AFT_FSDKFace> fsdkFaces) {
		final SurfaceHolder rectHolder = mSurfceRect.getHolder();
		final Canvas canvas = rectHolder.lockCanvas();

		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(5);
		paint.setTextSize(80);

		for (AFT_FSDKFace aft_fsdkFace : fsdkFaces) {
			if (aft_fsdkFace.getRect() == null) {
				continue;
			}
			Rect rect = new Rect(aft_fsdkFace.getRect());
			//画人脸框
			Log.i(TAG, "cameraOrientation:" + mCameraOri + "---mCameraId:" + mCameraId);
			Rect adjustedRect = DrawUtils.adjustRect(rect, mFaceTracker.getWidth(),
					mFaceTracker.getHeight(),
					canvas.getWidth(), canvas.getHeight(), mCameraOri, mCameraId);
			DrawUtils.drawFaceRect(canvas, adjustedRect, Color.YELLOW, 4);
		}

		rectHolder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		closeCamera();
	}

	private void closeCamera() {
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		mFaceTracker.destoryEngine();
	}

	private static class SingletonHolder {
		public static ArcFaceCamera INSTANCE = new ArcFaceCamera();
	}

	//设置相机预览分辨率，可以自己设置
	private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, DisplayMetrics metrics) {
		Camera.Size bestSize = sizes.get(0);
		float screenRatio = (float) metrics.widthPixels / (float) metrics.heightPixels;
		if (screenRatio > 1) {
			screenRatio = 1 / screenRatio;
		}

		for (Camera.Size s : sizes) {
			if (Math.abs((s.height / (float) s.width) - screenRatio) < Math.abs(bestSize.height /
					(float) bestSize.width - screenRatio)) {
				bestSize = s;
			}
		}
		return bestSize;
	}

	public void setCameraPreviewListener(CameraPreviewListener cameraPreviewListener) {
		this.mCameraPreviewListener = cameraPreviewListener;
	}


	//设置相机方向
	private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware
			.Camera camera) {
		android.hardware.Camera.CameraInfo info =
				new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;  // compensate the mirror
		} else {  // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		mCameraOri = result;
		camera.setDisplayOrientation(result);
	}
}
