package com.example.songzeceng.firstjd;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import permison.PermissonUtil;
import permison.listener.PermissionListener;

public class MainActivity extends Activity {
	private Button btn_register, btn_recognition;
	private ImageView imageView;

	public static File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("MainActivity", "手机指令集：" + getCpuAbi());
		btn_register = findViewById(R.id.btn_register);
		btn_recognition = findViewById(R.id.btn_recognition);
//		btn_picture = findViewById(R.id.btn_picture);
		setClick();

		PermissonUtil.checkPermission(this, new PermissionListener() {
			@Override
			public void havePermission() {
//				AFD_FSDKEngine fdEngine = new AFD_FSDKEngine();
//				fdEngine.AFD_FSDK_InitialFaceEngine(Constants.APP_ID, Constants.FD_KEY,
//						AFD_FSDKEngine.AFD_FOC_0, 16, 2);
			}

			@Override
			public void requestPermissionFail() {
				finish();
			}
		}, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
	}

	private String getCpuAbi() {
		String[] abis;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			abis = Build.SUPPORTED_ABIS;
		} else {
			abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
		}
		StringBuilder abiStr = new StringBuilder();
		for (String abi : abis) {
			abiStr.append(abi);
			abiStr.append(',');
		}
		return abiStr.toString();
	}

	private void setClick() {

		btn_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LivenessActivity.flag = 1;
				startActivity(new Intent(MainActivity.this, LivenessActivity.class));
			}
		});

		btn_recognition.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LivenessActivity.flag = 2;
				startActivity(new Intent(MainActivity.this, LivenessActivity.class));
			}
		});

//		btn_picture.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				PicturePickUtil.pick(MainActivity.this, new OnPickListener() {
//					@Override
//					public void pickPicture(final File file) {
//						final Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
//						showDialog();
//						imageView.setImageBitmap(bitmap);
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								byte[] pictureData = ImageUtils.getNV21(bitmap);
//								final FaceFinder faceFindService = new FaceFinder();
//								faceFindService.setSize(bitmap.getWidth(), bitmap.getHeight());
//								final Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//								Canvas canvas = new Canvas(bitmap1);
//								List<AFD_FSDKFace> afd_fsdkFaceList = faceFindService.findFace
//										(pictureData);
//								for (AFD_FSDKFace afd_fsdkFace : afd_fsdkFaceList) {
//									DrawUtils.drawFaceRect(canvas, afd_fsdkFace.getRect(), Color
//											.GREEN, 4);
//								}
//								MainActivity.this.runOnUiThread(new Runnable() {
//									@Override
//									public void run() {
//										imageView.setImageBitmap(bitmap1);
//										faceFindService.destroyEngine();
//									}
//								});
//							}
//						}).start();
//					}
//				});
//			}
//		});

	}

	private void showDialog() {
		AlertDialog.Builder di = new AlertDialog.Builder(MainActivity.this);
		di.setCancelable(true);
		imageView = new ImageView(MainActivity.this);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		di.setView(imageView);
		di.show();
	}
}