package com.example.songzeceng.firstjd;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.facetracking.AFT_FSDKFace;
import com.example.songzeceng.firstjd.ArcAPI.ArcFaceCamera;
import com.example.songzeceng.firstjd.ArcAPI.ArcFaceCamera2;
import com.example.songzeceng.firstjd.ArcAPI.CameraPreviewListener;
import com.example.songzeceng.firstjd.FaceAPI.FaceRecognizer;
import com.example.songzeceng.firstjd.FaceAPI.FaceSearchListener;
import com.example.songzeceng.firstjd.View.OverlayView;
import com.example.songzeceng.firstjd.utils.ImageUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import permison.PermissonUtil;
import permison.listener.PermissionListener;

public class LivenessActivity extends AppCompatActivity implements CameraPreviewListener,
		ImageReader.OnImageAvailableListener {
	private static final String TAG = "LivenessActivity";
	private static final float TEXT_SIZE_DIP = 12;
	private static Size DESIRED_PREVIEW_SIZE = new Size(800, 600);
	private static List<Face> faces = new ArrayList<>();
	public static int flag = 0;

	private SurfaceView mSurfcePreview, mSurfceRect;
	private TextView mTvStatus, mTvName, mTvAge, mTvGender;
	private ImageView mIvFace;

	//相机的位置
	private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
	private String mCameraIdFor2;
	//相机的方向
	private int mCameraOri = 90;

	FaceRecognizer mFaceRecognitionService;
	private boolean mUseCamera2API;
	int mWidth, mHeight;
	private ArcFaceCamera2 mArcFaceCamera2 = null;
	private int mPreviewHeight;
	private int mPreviewWidth;
	private int mSensorOrientation;
	private boolean mDebug;
	private Bitmap mTextureCopyBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCameraIdFor2 = chooseCamera();
		// int layout = mUseCamera2API ? R.layout.activity_liveness_2 : R.layout.activity_liveness;
		int layout = R.layout.activity_liveness;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(layout);
		mTvStatus = findViewById(R.id.tv_status);
		mTvName = findViewById(R.id.tv_name);
		mTvGender = findViewById(R.id.tv_gender);
		mTvAge = findViewById(R.id.tv_age);
		mSurfcePreview = findViewById(R.id.surfce_preview);
		mSurfceRect = findViewById(R.id.surfce_rect);
		mIvFace = findViewById(R.id.iv_face);

		mFaceRecognitionService = new FaceRecognizer();

		ArcFaceCamera.getInstance().setCameraPreviewListener(this);
		ArcFaceCamera.getInstance().init(mCameraId);

//		if (!mUseCamera2API) {
//			ArcFaceCamera.getInstance().setCameraPreviewListener(this);
//			ArcFaceCamera.getInstance().init(mCameraId);
//		} else {
//			mArcFaceCamera2 = new ArcFaceCamera2(new ArcFaceCamera2.ConnectionCallback() {
//				@Override
//				public void onPreviewSizeChosen(final Size size, final int rotation) {
//					mPreviewHeight = size.getHeight();
//					mPreviewWidth = size.getWidth();
//					LivenessActivity.this.onPreviewSizeChosen(size, rotation);
//				}
//			}, this, getDesiredPreviewFrameSize(), this, (AutoTextureView) findViewById(R.id
//					.texture));
//			mArcFaceCamera2.setCamera(mCameraIdFor2);
//		}

		PermissonUtil.checkPermission(this, new PermissionListener() {
			@Override
			public void havePermission() {
				//if (!mUseCamera2API) {
				ArcFaceCamera.getInstance().openCamera(LivenessActivity.this, mSurfcePreview,
						mSurfceRect);
				//}
			}

			@Override
			public void requestPermissionFail() {
				finish();
			}
		}, Manifest.permission.CAMERA);
	}

	private String chooseCamera() {
		final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		try {
			for (final String cameraId : manager.getCameraIdList()) {
				final CameraCharacteristics characteristics = manager.getCameraCharacteristics
						(cameraId);

				// We don't use a front facing camera in this sample.
				final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
				if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
					continue;
				}

				final StreamConfigurationMap map =
						characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

				if (map == null) {
					continue;
				}
				mUseCamera2API = (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
						|| isHardwareLevelSupported(characteristics,
						CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
				return cameraId;
			}
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isHardwareLevelSupported(
			CameraCharacteristics characteristics, int requiredLevel) {
		int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
		if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
			return requiredLevel == deviceLevel;
		}
		return requiredLevel <= deviceLevel;
	}

	//开始检测
	public synchronized void detect(final byte[] data, final List<AFT_FSDKFace> fsdkFaces) {
		if (fsdkFaces == null || fsdkFaces.isEmpty()) {
			return;
		}

		final AFT_FSDKFace aft_fsdkFace = fsdkFaces.get(0).clone();
		long startTime = System.currentTimeMillis();
		final Face faceData = mFaceRecognitionService.faceData(data, aft_fsdkFace
				.getRect(), aft_fsdkFace.getDegree());
		long endTime = System.currentTimeMillis();
		Log.i(TAG, "faceData()用时：" + (endTime - startTime));
		if (flag == 1) {
			flag = -1;
			final View dialogView = getLayoutInflater().inflate(R.layout.regist_dialog, null);
			new AlertDialog.Builder(this).setTitle("输入名字").setView(dialogView)
					.setPositiveButton("确定", new
							DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									EditText input = dialogView.findViewById(R.id.input_name);
									faceData.setName(input.getText().toString());
									faces.add(faceData);
									toast("注册成功，姓名为：" + faceData.getName());
									finish();
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					toast("注册取消");
					finish();
					dialog.dismiss();
				}
			}).show();
		} else if (flag == 2) {
			List<byte[]> faceList = new ArrayList<>();
			for (Face face : faces) {
				faceList.add(face.getData());
			}

			final long finalStartTime = System.currentTimeMillis();
			mFaceRecognitionService.faceSearch(faceData.getData(), faceList, new
					FaceSearchListener() {
						@Override
						public void searchFinish(float score, int position) {
							long endTime = System.currentTimeMillis();
							Log.i(TAG, "faceSearch()用时：" + (endTime - finalStartTime));
							Log.e("LivenessActivity", "score：" + score + "，position：" +
									position);
							if (score > 0.7) {
								Face face = faces.get(position);
								mTvName.setText(face.getName() + "，相似度：" +
										score);
								mTvAge.setText("" + face.getAge());
								mTvGender.setText(face.getGender() == 0 ? "男" : "女");
								mIvFace.setImageBitmap(ImageUtils.cropFace(data, aft_fsdkFace
										.getRect
												(), mWidth, mHeight, mCameraOri));
							} else {
								mTvName.setText("");
							}
						}
					});
			flag = -1;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
						flag = 2;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public void toast(final String test) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(LivenessActivity.this, test, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		livenessService.destoryEngine();
		mFaceRecognitionService.destroyEngine();
//		IdCardVerifyManager.getInstance().unInit();
	}

	@Override
	public void onPreviewData(byte[] data, List<AFT_FSDKFace> fsdkFaces) {
		detect(data, fsdkFaces);
	}

	@Override
	public void onPreviewSize(int width, int height) {
		mHeight = height;
		mWidth = width;
		mFaceRecognitionService.setSize(width, height);
	}

	public void onPreviewSizeChosen(final Size size, final int rotation) {
		mPreviewWidth = size.getWidth();
		mPreviewHeight = size.getHeight();
		final Display display = getWindowManager().getDefaultDisplay();
		final int screenOrientation = display.getRotation();

		Log.i(TAG, "Sensor orientation: " + rotation + " Screen orientation: " +
				screenOrientation);

		mSensorOrientation = rotation + screenOrientation;

		addCallback(
				new OverlayView.DrawCallback() {
					@Override
					public void drawCallback(final Canvas canvas) {
						renderDebug(canvas);
					}
				});
	}

	public void addCallback(final OverlayView.DrawCallback callback) {
		final OverlayView overlay = findViewById(R.id.debug_overlay);
		if (overlay != null) {
			overlay.addCallback(callback);
		}
	}

	private void renderDebug(final Canvas canvas) {
		if (!mDebug) {
			return;
		}
		Bitmap textureBitmap = mTextureCopyBitmap;
		if (textureBitmap == null) {
			return;
		}
		final Matrix matrix = new Matrix();
		// 绘制具有红色矩形框的图像
		canvas.drawBitmap(textureBitmap, matrix, new Paint());
	}

	protected Size getDesiredPreviewFrameSize() {
		return DESIRED_PREVIEW_SIZE;
	}

	@Override
	public void onImageAvailable(ImageReader reader) {
		Image image = reader.acquireNextImage();
		Image.Plane[] planes = image.getPlanes();
		ByteBuffer buffer = planes[0].getBuffer();
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);

		Log.i(TAG, "Camera2获取图片的格式：" + image.getFormat()); // 35:YUV_420_888

		detect(bytes, mArcFaceCamera2.getRawFaces(bytes));
		image.close();
	}
}