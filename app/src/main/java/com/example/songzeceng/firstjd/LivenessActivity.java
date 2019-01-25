package com.example.songzeceng.firstjd;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.facetracking.AFT_FSDKFace;
import com.example.songzeceng.firstjd.ArcAPI.ArcFaceCamera;
import com.example.songzeceng.firstjd.ArcAPI.CameraPreviewListener;
import com.example.songzeceng.firstjd.FaceAPI.FaceRecognizer;
import com.example.songzeceng.firstjd.FaceAPI.FaceSearchListener;
import com.example.songzeceng.firstjd.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import permison.PermissonUtil;
import permison.listener.PermissionListener;

public class LivenessActivity extends AppCompatActivity implements CameraPreviewListener {
	public static final String TAG = "LivenessActivity";
	public static List<Face> faces = new ArrayList<>();
	SurfaceView surfce_preview, surfce_rect;
	TextView tv_status, tv_name, tv_age, tv_gender;
	ImageView iv_face;

	//相机的位置
	private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
	//相机的方向
	private int cameraOri = 90;
	public static int flag = 0;

	FaceRecognizer faceRecognitionService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		faceRecognitionService = new FaceRecognizer();

		ArcFaceCamera.getInstance().setCameraPreviewListener(this);
		ArcFaceCamera.getInstance().init(cameraId);

		PermissonUtil.checkPermission(this, new PermissionListener() {
			@Override
			public void havePermission() {
				ArcFaceCamera.getInstance().openCamera(LivenessActivity.this, surfce_preview,
						surfce_rect);
			}

			@Override
			public void requestPermissionFail() {
				finish();
			}
		}, Manifest.permission.CAMERA);
	}

	//开始检测
	public synchronized void detect(final byte[] data, final List<AFT_FSDKFace> fsdkFaces) {


		if (fsdkFaces.size() <= 0) {
			return;
		}//如果有人脸进行注册、识别
		final AFT_FSDKFace aft_fsdkFace = fsdkFaces.get(0).clone();
		if (flag == 1) {
			flag = -1;
			final Face faceData = faceRecognitionService.faceData(data, aft_fsdkFace
					.getRect(), aft_fsdkFace.getDegree());
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
			Face faceData = faceRecognitionService.faceData(data, aft_fsdkFace
					.getRect(), aft_fsdkFace.getDegree());

			List<byte[]> faceList = new ArrayList<>();
			for (Face face : faces) {
				faceList.add(face.getData());
			}

			faceRecognitionService.faceSerch(faceData.getData(), faceList, new
					FaceSearchListener() {
						@Override
						public void serchFinish(float sorce, int position) {
							Log.e("LivenessActivity", "sorce：" + sorce + "，position：" +
									position);
							if (sorce > 0.7) {
								Face face = faces.get(position);
								String genderStr = "";
								switch (face.getGender()) {
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
								tv_name.setText(face.getName() + "：相似度：" +
										sorce);
								tv_gender.setText(genderStr);
								tv_age.setText("" + face.getAge());
								Bitmap bitmap = ImageUtils.cropFace(data, aft_fsdkFace.getRect(),
										mWidth, mHeight, cameraOri);
								if (bitmap != null) {
									iv_face.setImageBitmap(bitmap);
								}
							} else {
								tv_name.setText("");
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
		faceRecognitionService.destroyEngine();
//		IdCardVerifyManager.getInstance().unInit();
	}

	@Override
	public void onPreviewData(byte[] data, List<AFT_FSDKFace> fsdkFaces) {
		detect(data, fsdkFaces);
	}

	int mWidth, mHeight;

	@Override
	public void onPreviewSize(int width, int height) {
		mHeight = height;
		mWidth = width;
		faceRecognitionService.setSize(width, height);
	}
}