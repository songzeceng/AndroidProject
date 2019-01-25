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

import com.example.songzeceng.firstjd.ArcAPI.ArcFaceCamera;
import com.example.songzeceng.firstjd.ArcAPI.CameraPreviewListener;
import com.example.songzeceng.firstjd.View.OverlayView;
import com.example.songzeceng.firstjd.utils.ImageUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import permison.PermissonUtil;
import permison.listener.PermissionListener;

public class LivenessActivity extends AppCompatActivity implements CameraPreviewListener,
		ImageReader.OnImageAvailableListener {
	public static final String TAG = "LivenessActivity";
	private static final float TEXT_SIZE_DIP = 12;
	private static Size DESIRED_PREVIEW_SIZE = new Size(800, 600);
	public static List<Face> faces = new ArrayList<>();
	SurfaceView surfce_preview, surfce_rect;
	TextView tv_status, tv_name, tv_age, tv_gender;
	ImageView iv_face;
	private View dialogView;
	private EditText input;

	//相机的位置
	private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
	private String cameraIdFor2;
	//相机的方向
	private int cameraOri = 90;
	public static int flag = 0;

	//	FaceRecognizer faceRecognitionService;
	private boolean useCamera2API;

	//	private ArcFaceCamera2 arcFaceCamera2 = null;
	private int previewHeight;
	private int previewWidth;
	private int sensorOrientation;
	private boolean debug;
	private Bitmap textureCopyBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cameraIdFor2 = chooseCamera();
		// int layout = useCamera2API ? R.layout.activity_liveness_2 : R.layout.activity_liveness;
		int layout = R.layout.activity_liveness;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(layout);
		tv_status = findViewById(R.id.tv_status);
		tv_name = findViewById(R.id.tv_name);
		tv_gender = findViewById(R.id.tv_gender);
		tv_age = findViewById(R.id.tv_age);
		surfce_preview = findViewById(R.id.surfce_preview);
		surfce_rect = findViewById(R.id.surfce_rect);
		iv_face = findViewById(R.id.iv_face);

//		faceRecognitionService = new FaceRecognizer();

		ArcFaceCamera.getInstance().setCameraPreviewListener(this);
		ArcFaceCamera.getInstance().init(cameraId);

//		if (!useCamera2API) {
//			ArcFaceCamera.getInstance().setCameraPreviewListener(this);
//			ArcFaceCamera.getInstance().init(cameraId);
//		} else {
//			arcFaceCamera2 = new ArcFaceCamera2(new ArcFaceCamera2.ConnectionCallback() {
//				@Override
//				public void onPreviewSizeChosen(final Size size, final int rotation) {
//					previewHeight = size.getHeight();
//					previewWidth = size.getWidth();
//					LivenessActivity.this.onPreviewSizeChosen(size, rotation);
//				}
//			}, this, getDesiredPreviewFrameSize(), this, (AutoTextureView) findViewById(R.id
//					.texture));
//			arcFaceCamera2.setCamera(cameraIdFor2);
//		}

		PermissonUtil.checkPermission(this, new PermissionListener() {
			@Override
			public void havePermission() {
				//if (!useCamera2API) {
				ArcFaceCamera.getInstance().openCamera(LivenessActivity.this, surfce_preview,
						surfce_rect);
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
				useCamera2API = (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
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
	public synchronized void detect(final byte[] data, final List<Face> fsdkFaces) {
		if (fsdkFaces.size() <= 0) {
			return;
		}
		//如果有人脸进行注册、识别
//			final FaceInfo aft_fsdkFace = fsdkFaces.get(0).getFaceInfo().clone();
		if (flag == 1) {
			flag = -1;
			final Face faceData = fsdkFaces.get(0);
			if (dialogView == null) {
				dialogView = getLayoutInflater().inflate(R.layout.regist_dialog, null);
			}
			new AlertDialog.Builder(this).setTitle("输入名字").setView(dialogView)
					.setPositiveButton("确定", new
							DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									if (input == null) {
										input = dialogView.findViewById(R.id.input_name);
									}
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
			Face faceData = fsdkFaces.get(0);

			List<Face> faceList = new ArrayList<>();
//				for (Face face : faces) {
//					faceList.add(face.getData());
//				}
			faceList.addAll(faces);
			Object[] results = ArcFaceCamera.getInstance().searchFace(faceData, faceList);
			if (results != null) {
				Face result = (Face) results[0];
				float score = (Float) results[1];
				if (result != null) {
					tv_name.setText(result.getName() + "：相似度：" +
							score);
					tv_age.setText("" + result.getAge());
					String genderStr = "";
					switch (result.getGender()) {
						case 0:
							genderStr = "男";
							break;
						case 1:
							genderStr = "女";
							break;
						default:
							genderStr = "未知";
							break;
					}
					tv_gender.setText(genderStr);
					Bitmap bitmap = ImageUtils.cropFace(data, faceData.getFaceInfo().getRect(),
							mWidth, mHeight, cameraOri);
					if (bitmap != null) {
						iv_face.setImageBitmap(bitmap);
					}
				} else {
					tv_name.setText("");
				}
			}

//				faceRecognitionService.faceSerch(faceData.getData(), faceList, new
//						FaceSearchListener() {
//							@Override
//							public void serchFinish(float sorce, int position) {
//								Log.e("LivenessActivity", "sorce：" + sorce + "，position：" +
//										position);
//								if (sorce > 0.7) {
//									Face face = faces.get(position);
//									tv_name.setText(face.getName() + "：相似度：" +
//											sorce);
//									tv_age.setText("" + face.getAge());
//									tv_gender.setText(face.getGender() == 0 ? "男" : "女");
//									iv_face.setImageBitmap(ImageUtils.cropFace(data, aft_fsdkFace
//											.getRect
//													(), mWidth, mHeight, cameraOri));
//								} else {
//									tv_name.setText("");
//								}
//							}
//						});
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
//		faceRecognitionService.destroyEngine();
//		IdCardVerifyManager.getInstance().unInit();
		ArcFaceCamera.getInstance().destroyEngine();
	}

	@Override
	public void onPreviewData(byte[] data, List<Face> fsdkFaces) {
		detect(data, fsdkFaces);
	}

	int mWidth, mHeight;

	@Override
	public void onPreviewSize(int width, int height) {
		mHeight = height;
		mWidth = width;
//		faceRecognitionService.setSize(width, height);
	}

	public void onPreviewSizeChosen(final Size size, final int rotation) {
		previewWidth = size.getWidth();
		previewHeight = size.getHeight();
		final Display display = getWindowManager().getDefaultDisplay();
		final int screenOrientation = display.getRotation();

		Log.i(TAG, "Sensor orientation: " + rotation + " Screen orientation: " +
				screenOrientation);

		sensorOrientation = rotation + screenOrientation;

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
		if (!debug) {
			return;
		}
		Bitmap textureBitmap = textureCopyBitmap;
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

//		detect(bytes, arcFaceCamera2.getRawFaces(bytes));
		image.close();
	}
}