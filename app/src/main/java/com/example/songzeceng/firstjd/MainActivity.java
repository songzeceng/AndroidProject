package com.example.songzeceng.firstjd;

import android.Manifest;
import android.accessibilityservice.FingerprintGestureController;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jwkj.libzxing.OnQRCodeScanCallback;
import com.jwkj.libzxing.QRCodeManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import permison.PermissonUtil;
import permison.listener.PermissionListener;

public class MainActivity extends Activity {
	private Button btn_register, btn_recognition;
	private ImageView imageView;

	public static File file;
	private static int sCount = 5;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		    System.out.println("sCount = 0，from thread:" + msg.obj.toString());
		}
	};

	private QRCodeManager mQRCodeManager;
	private Resources mRes;

	private FingerprintManagerCompat mFingerManager;
	private FingerprintGestureController controller;

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

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			PermissonUtil.checkPermission(this, new PermissionListener() {
				@Override
				public void havePermission() {
					mFingerManager = FingerprintManagerCompat.from(MainActivity.this.getApplicationContext());
					if (mFingerManager.isHardwareDetected() && mFingerManager.hasEnrolledFingerprints()) {
						startAuthenticate();
					}
				}

				@Override
				public void requestPermissionFail() {

				}
			}, Manifest.permission.USE_FINGERPRINT);
		}

		mQRCodeManager = QRCodeManager.getInstance();
		mRes = MainActivity.this.getResources();
	}

	private void startAuthenticate() {
		mFingerManager.authenticate(null, 0, new CancellationSignal(), new FingerprintManagerCompat.AuthenticationCallback() {
			@Override
			public void onAuthenticationError(int errMsgId, CharSequence errString) {
				/*
					应用层不能实现指纹的注册，需要让客户自行注册
					指纹识别出错，传感器会有一段时间不可用，30s-1min左右
				 */
				MainActivity.this.getWindow().getDecorView().postDelayed(new Runnable() {
					@Override
					public void run() {
						startAuthenticate();
					}
				}, 5 * 1000);
			}

			@Override
			public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
				System.out.println("指纹识别成功");
				startAuthenticate();

			}

			@Override
			public void onAuthenticationFailed() {
				System.out.println("指纹识别失败");
				startAuthenticate();
			}
		}, null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		startAuthenticate();
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized (MainActivity.this) {
						sCount--;
						if (sCount == 0) {
							Message msg = mHandler.obtainMessage(0);
							msg.obj = Thread.currentThread().getName();
							mHandler.sendMessage(msg);
						}
					}
				}
			}).start();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File destFile = new File(MainActivity.this.getExternalCacheDir().getParentFile(), "movie.mp4");
					if (!destFile.exists()) {
						destFile.createNewFile();
					} else {
						destFile.delete();
					}
					BufferedInputStream inputStream = new BufferedInputStream(MainActivity.this.getAssets().open("movie.mp4"));
					BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
					byte[] bytes = new byte[1024];
					int len;
					while ((len = inputStream.read(bytes)) != -1) {
						outputStream.write(bytes, 0, len);
						outputStream.flush();
					}

					inputStream.close();
					outputStream.close();
					System.out.println("拷贝assets文件完毕");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("出现异常");
				}
			}
		}).start();

		final EditText editText = findViewById(R.id.content_str);
		final ImageView contentZxing = findViewById(R.id.content_zxing);
		final Button btnGenerateQRCode = findViewById(R.id.btn_generateZxing);
		btnGenerateQRCode.setText(mRes.getString(R.string.generate_qr_code));
		btnGenerateQRCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (btnGenerateQRCode.getText().toString().equals(mRes.getString(R.string.generate_qr_code))) {
					String text = TextUtils.isEmpty(editText.getText()) ? "szc" : editText.getText().toString();
					contentZxing.setImageBitmap(mQRCodeManager.createQRCode(text, 500, 500));
					btnGenerateQRCode.setText(mRes.getString(R.string.begin_scan));
				} else {
					mQRCodeManager.with(MainActivity.this).setReqeustType(0).scanningQRCode(new OnQRCodeScanCallback() {
						@Override
						public void onCompleted(String s) {
							System.out.println("扫描完成:" + s);
						}

						@Override
						public void onError(Throwable throwable) {
							throwable.printStackTrace();
						}

						@Override
						public void onCancel() {
							System.out.println("扫描取消");
						}
					});
					btnGenerateQRCode.setText(mRes.getString(R.string.generate_qr_code));
				}
			}
		});
		contentZxing.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Context context = MainActivity.this;
							contentZxing.setDrawingCacheEnabled(true);
							final Bitmap qrCodeBitmap = contentZxing.getDrawingCache();

							File destFile = new File(getRootIntervalStorage(), "qrCode.jpg");
							if (destFile.exists()) {
								destFile.delete();
							}
							destFile.createNewFile();
							BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
							qrCodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
							outputStream.flush();
							outputStream.close();

							MediaScannerConnection.scanFile(context, new String[]{destFile.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
								@Override
								public void onScanCompleted(String s, Uri uri) {
									System.out.println("扫描完成：" + s + ",uri:" + uri.toString());
								}
							});
                            contentZxing.setDrawingCacheEnabled(false);
							System.out.println("保存成功");
						} catch (Exception e) {
							e.printStackTrace();
							// java.lang.IllegalArgumentException:
							// Failed to find configured root that contains /storage/emulated/0/Android/data/com.example.songzeceng.firstjd/qrCode.jpeg
						}
					}
				}).start();
				return false;
			}
		});

		try {
		    int a = 1/0;
        } catch (Exception e) {

        }
	}

	private File getRootIntervalStorage() {
		File appCacheFile = getExternalCacheDir();
		while (!appCacheFile.getAbsolutePath().endsWith("0") && !appCacheFile.getAbsolutePath().endsWith("0" + File.separator)) {
			appCacheFile = appCacheFile.getParentFile();
		}
		return appCacheFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mQRCodeManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
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