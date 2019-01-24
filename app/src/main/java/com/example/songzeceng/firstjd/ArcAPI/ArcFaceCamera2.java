package com.example.songzeceng.firstjd.ArcAPI;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.arcsoft.facetracking.AFT_FSDKFace;
import com.example.songzeceng.firstjd.FaceAPI.FaceTracker;
import com.example.songzeceng.firstjd.View.AutoTextureView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ArcFaceCamera2 {
	public static final String TAG = "ArcFaceCamera2";
	private static final int MINIMUM_PREVIEW_SIZE = 320;

	private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
	private static final String FRAGMENT_DIALOG = "dialog";

	static {
		ORIENTATIONS.append(Surface.ROTATION_0, 90);
		ORIENTATIONS.append(Surface.ROTATION_90, 0);
		ORIENTATIONS.append(Surface.ROTATION_180, 270);
		ORIENTATIONS.append(Surface.ROTATION_270, 180);
	}

	private final TextureView.SurfaceTextureListener mSurfaceTextureListener =
			new TextureView.SurfaceTextureListener() {
				@Override
				public void onSurfaceTextureAvailable(
						final SurfaceTexture texture, final int width, final int height) {
					openCamera(width, height);
				}

				@Override
				public void onSurfaceTextureSizeChanged(
						final SurfaceTexture texture, final int width, final int height) {
					configureTransform(width, height);
				}

				@Override
				public boolean onSurfaceTextureDestroyed(final SurfaceTexture texture) {
					return true;
				}

				@Override
				public void onSurfaceTextureUpdated(final SurfaceTexture texture) {
				}
			};

	public interface ConnectionCallback {
		void onPreviewSizeChosen(Size size, int cameraRotation);
	}

	private String mCameraId;
	private AutoTextureView mTextureView;
	private CameraCaptureSession mCaptureSession;
	private CameraDevice mCameraDevice;
	private Integer mSensorOrientation;
	private Size mPreviewSize;

	private final CameraDevice.StateCallback mStateCallback =
			new CameraDevice.StateCallback() {
				@Override
				public void onOpened(final CameraDevice cd) {
					// cd: CameraDeviceImpl
					mCameraOpenCloseLock.release();
					mCameraDevice = cd;
					createCameraPreviewSession();
				}

				@Override
				public void onDisconnected(final CameraDevice cd) {
					mCameraOpenCloseLock.release();
					cd.close();
					mCameraDevice = null;
				}

				@Override
				public void onError(final CameraDevice cd, final int error) {
					mCameraOpenCloseLock.release();
					cd.close();
					mCameraDevice = null;
					if (null != mActivity) {
						mActivity.finish();
					}
				}
			};
	private final CameraCaptureSession.CaptureCallback mCaptureCallback =
			new CameraCaptureSession.CaptureCallback() {
				@Override
				public void onCaptureProgressed(
						final CameraCaptureSession session,
						final CaptureRequest request,
						final CaptureResult partialResult) {
				}

				@Override
				public void onCaptureCompleted(
						final CameraCaptureSession session,
						final CaptureRequest request,
						final TotalCaptureResult result) {
				}
			};

	private HandlerThread mBackgroundThread;
	private Handler mBackgroundHandler;
	private ImageReader mPreviewReader;
	private CaptureRequest.Builder mPreviewRequestBuilder;
	private CaptureRequest mPreviewRequest;
	private final Semaphore mCameraOpenCloseLock = new Semaphore(1);
	private final ImageReader.OnImageAvailableListener mImageListener;
	private final Size mInputSize;
	private final ConnectionCallback mCameraConnectionCallback;
	private Activity mActivity;
	private FaceTracker mFaceTracker;

	public ArcFaceCamera2(ConnectionCallback connectionCallback, ImageReader
			.OnImageAvailableListener imageListener, Size inputSize, Activity activity,
	                      AutoTextureView textureView) {
		mCameraConnectionCallback = connectionCallback;
		mImageListener = imageListener;
		mInputSize = inputSize;
		mActivity = activity;
		mTextureView = textureView;
		mFaceTracker = new FaceTracker();
		startBackgroundThread();

		if (textureView.isAvailable()) {
			openCamera(textureView.getWidth(), textureView.getHeight());
		} else {
			textureView.setSurfaceTextureListener(mSurfaceTextureListener);
		}
	}

	public List<AFT_FSDKFace> getRawFaces(byte[] bytes) {
		return mFaceTracker.getFtfaces(bytes);
	}

	public void setCamera(String cameraId) {
		this.mCameraId = cameraId;
	}

	private void createCameraPreviewSession() {
		try {
			final SurfaceTexture texture = mTextureView.getSurfaceTexture();
			assert texture != null;

			texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

			final Surface surface = new Surface(texture);

			mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice
					.TEMPLATE_PREVIEW);
			mPreviewRequestBuilder.addTarget(surface);

			Log.i(TAG, "Opening camera preview: " + mPreviewSize.getWidth() + "x" + mPreviewSize
					.getHeight());

			mPreviewReader =
					ImageReader.newInstance(
							mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat
									.NV21, 2);

			mPreviewReader.setOnImageAvailableListener(mImageListener, mBackgroundHandler);
			mPreviewRequestBuilder.addTarget(mPreviewReader.getSurface());

			mCameraDevice.createCaptureSession(
					Arrays.asList(surface, mPreviewReader.getSurface()),
					new CameraCaptureSession.StateCallback() {

						@Override
						public void onConfigured(final CameraCaptureSession cameraCaptureSession) {
							if (null == mCameraDevice) {
								return;
							}

							mCaptureSession = cameraCaptureSession;
							try {
								mPreviewRequestBuilder.set(
										CaptureRequest.CONTROL_AF_MODE,
										CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

								mPreviewRequestBuilder.set(
										CaptureRequest.CONTROL_AE_MODE, CaptureRequest
												.CONTROL_AE_MODE_ON_AUTO_FLASH);

								mPreviewRequest = mPreviewRequestBuilder.build();
								mCaptureSession.setRepeatingRequest(
										mPreviewRequest, mCaptureCallback, mBackgroundHandler);
							} catch (final CameraAccessException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onConfigureFailed(final CameraCaptureSession
								                              cameraCaptureSession) {
							showToast("Failed");
						}
					},
					null);
		} catch (final CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void showToast(final String text) {
		if (mActivity != null) {
			mActivity.runOnUiThread(
					new Runnable() {
						@Override
						public void run() {
							Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
						}
					});
		}
	}

	private void configureTransform(final int viewWidth, final int viewHeight) {
		if (null == mTextureView || null == mPreviewSize || null == mActivity) {
			return;
		}
		final int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
		final Matrix matrix = new Matrix();
		final RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
		final RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
		final float centerX = viewRect.centerX();
		final float centerY = viewRect.centerY();
		if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
			bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
			matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
			final float scale =
					Math.max(
							(float) viewHeight / mPreviewSize.getHeight(),
							(float) viewWidth / mPreviewSize.getWidth());
			matrix.postScale(scale, scale, centerX, centerY);
			matrix.postRotate(90 * (rotation - 2), centerX, centerY);
		} else if (Surface.ROTATION_180 == rotation) {
			matrix.postRotate(180, centerX, centerY);
		}
		mTextureView.setTransform(matrix);
	}

	private void openCamera(final int width, final int height) {
		setUpCameraOutputs();
		configureTransform(width, height);
		final CameraManager manager = (CameraManager) mActivity.getSystemService(Context
				.CAMERA_SERVICE);
		try {
			if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
				throw new RuntimeException("Time out waiting to lock camera opening.");
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (mActivity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager
						.PERMISSION_GRANTED) {
					return;
				}
			}
			manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
		} catch (final CameraAccessException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
		}
	}

	private void closeCamera() {
		try {
			mCameraOpenCloseLock.acquire();
			if (null != mCaptureSession) {
				mCaptureSession.close();
				mCaptureSession = null;
			}
			if (null != mCameraDevice) {
				mCameraDevice.close();
				mCameraDevice = null;
			}
			if (null != mPreviewReader) {
				mPreviewReader.close();
				mPreviewReader = null;
			}
		} catch (final InterruptedException e) {
			throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
		} finally {
			mCameraOpenCloseLock.release();
		}
	}

	private void startBackgroundThread() {
		mBackgroundThread = new HandlerThread("ImageListener");
		mBackgroundThread.start();
		mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
	}

	private void stopBackgroundThread() {
		mBackgroundThread.quitSafely();
		try {
			mBackgroundThread.join();
			mBackgroundThread = null;
			mBackgroundHandler = null;
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void setUpCameraOutputs() {
		final CameraManager manager = (CameraManager) mActivity.getSystemService(Context
				.CAMERA_SERVICE);
		try {
			final CameraCharacteristics characteristics = manager.getCameraCharacteristics
					(mCameraId);

			final StreamConfigurationMap map =
					characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

			final Size largest =
					Collections.max(
							Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)),
							new CompareSizesByArea());

			mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

			mPreviewSize =
					chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
							mInputSize.getWidth(),
							mInputSize.getHeight());

			final int orientation = mActivity.getResources().getConfiguration().orientation;
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
			} else {
				mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
			}
		} catch (final CameraAccessException e) {
			e.printStackTrace();
		} catch (final NullPointerException e) {
			throw new RuntimeException("This device doesn't support Camera2 API");
		}

		mCameraConnectionCallback.onPreviewSizeChosen(mPreviewSize, mSensorOrientation);
	}

	protected static Size chooseOptimalSize(final Size[] choices, final int width, final int
			height) {
		final int minSize = Math.max(Math.min(width, height), MINIMUM_PREVIEW_SIZE);
		final Size desiredSize = new Size(width, height);

		boolean exactSizeFound = false;
		final List<Size> bigEnough = new ArrayList<Size>();
		final List<Size> tooSmall = new ArrayList<Size>();
		for (final Size option : choices) {
			if (option.equals(desiredSize)) {
				exactSizeFound = true;
			}

			if (option.getHeight() >= minSize && option.getWidth() >= minSize) {
				bigEnough.add(option);
			} else {
				tooSmall.add(option);
			}
		}

		Log.i(TAG, "Desired size: " + desiredSize + ", min size: " + minSize + "x" + minSize);
		Log.i(TAG, "Valid preview sizes: [" + TextUtils.join(", ", bigEnough) + "]");
		Log.i(TAG, "Rejected preview sizes: [" + TextUtils.join(", ", tooSmall) + "]");

		if (exactSizeFound) {
			Log.i(TAG, "Exact size match found.");
			return desiredSize;
		}

		if (bigEnough.size() > 0) {
			final Size chosenSize = Collections.min(bigEnough, new CompareSizesByArea());
			Log.i(TAG, "Chosen size: " + chosenSize.getWidth() + "x" + chosenSize.getHeight());
			return chosenSize;
		} else {
			Log.e(TAG, "Couldn't find any suitable preview size");
			return choices[0];
		}
	}

	static class CompareSizesByArea implements Comparator<Size> {
		@Override
		public int compare(final Size lhs, final Size rhs) {
			// We cast here to ensure the multiplications won't overflow
			return Long.signum(
					(long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs
							.getHeight());
		}
	}
}
