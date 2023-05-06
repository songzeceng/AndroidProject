package com.example.test1.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.example.test1.utils.ScreenUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RecordScreenService : Service() {
    companion object {
        private const val TAG = "ScreenRecordService"
    }

    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private var mScreenDensity = 0

    private var mResultCode: Int = 0
    private var mResultData: Intent? = null

    private var mMediaProjection: MediaProjection? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var mVirtualDisplay: VirtualDisplay? = null

    private val mFormatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
    private val mMovieDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mResultCode = intent?.getIntExtra("resultCode", 1) ?: 1
        mResultData = intent?.getParcelableExtra("data")

        getScreenBaseInfo()

        createMediaProjection()
        createMediaRecorder()
        createVirtualDisplay()

        mMediaRecorder?.start()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        mVirtualDisplay?.release()
        mVirtualDisplay = null

        mMediaRecorder?.stop()
        mMediaRecorder?.reset()
        mMediaRecorder?.release()
        mMediaRecorder = null

        mMediaProjection?.stop()
        mMediaProjection = null
    }

    private fun getScreenBaseInfo() {
        mScreenWidth = ScreenUtils.getScreenWidth()
        mScreenHeight = ScreenUtils.getScreenHeight()
        mScreenDensity = ScreenUtils.getScreenDensityDpi().toInt()
    }

    private fun createMediaProjection() {
        Log.i(TAG, "Create MediaProjection")
        (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager)?.apply {
            mResultData?.let { intent ->
                mMediaProjection = getMediaProjection(mResultCode, intent)
            }
        }
    }

    private fun createMediaRecorder() {
        val curDate = Date(System.currentTimeMillis())
        val curTime = mFormatter.format(curDate).replace(" ", "")
        val outputFilePath = "$mMovieDirectoryPath${File.pathSeparator}SD_$curTime.mp4"

        Log.i(TAG, "Create MediaRecorder")

        kotlin.runCatching {
            MediaRecorder().apply {
                setVideoSource(MediaRecorder.VideoSource.SURFACE)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(outputFilePath)
                setVideoSize(mScreenWidth, mScreenHeight)  // after setVideoSource(), setOutFormat()
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)  // after setOutputFormat()
                setVideoEncodingBitRate(mScreenWidth * mScreenHeight)
                setVideoFrameRate(30)

                prepare()
            }
        }.onSuccess { mMediaRecorder = it }
    }

    private fun createVirtualDisplay() {
        Log.i(TAG, "Create VirtualDisplay")
        mMediaProjection?.let {
            mMediaRecorder?.let { mediaRecorder ->
                mVirtualDisplay =  it.createVirtualDisplay(TAG, mScreenWidth, mScreenHeight, mScreenDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.surface, null, null)
            }
        }
    }
}