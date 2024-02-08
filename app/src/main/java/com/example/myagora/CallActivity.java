package com.example.myagora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.RtcEngineConfig;
import io.agora.rtc2.video.VideoCanvas;

import android.Manifest;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class CallActivity extends AppCompatActivity {

    private int uid;

    private String channelId;

    private RtcEngine agoraEngine;

    private String appId, certificate;

    private static String TAG = "anyCall";

    private boolean isJoined = false;
    private RelativeLayout remoteVideoFrame;
    private FrameLayout localVideoFrame;
    private SurfaceView localView;
    private SurfaceView remoteView;

    private ImageView ivVideo, ivCallEnd;
    private boolean videoOn = true;

    private ConstraintLayout mainLayout;

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onError(int err) {
            super.onError(err);
            Log.i(TAG, "ERR in handler :: code > " + err + " DESC ::: " + RtcEngine.getErrorDescription(err));
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {//local user joined
            super.onJoinChannelSuccess(channel, uid, elapsed);
            Log.i(TAG, "Local user joined " + uid);
            isJoined = true;
            runOnUiThread(() -> setupLocalUserVideo(uid));
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {//local user leave
            super.onLeaveChannel(stats);
            Log.i(TAG, "Local user leave " + uid);
            finish();
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {//remote user joined
            super.onUserJoined(uid, elapsed);
            Log.i(TAG, "Remote user joined" + uid);
            runOnUiThread(() -> setupRemoteUserVideo(uid));
        }

        @Override
        public void onUserOffline(int uid, int reason) {//remote user leave
            super.onUserOffline(uid, reason);
            Log.i(TAG, "Remote user leave " + uid);
            runOnUiThread(() -> {
                if (remoteView != null){
                    remoteVideoFrame.setVisibility(View.GONE);
                    remoteView.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
            super.onRemoteVideoStateChanged(uid, state, reason, elapsed);

        }

        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            super.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
//            runOnUiThread(()-> setupRemoteUserVideo(uid));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

//        uid = getIntent().getIntExtra("uid", 0);

        channelId = getIntent().getStringExtra("id");
        uid = new Random().nextInt(10000);

        appId = "d6c2b36699b3441498216c4814c8a9a0";//d6c2b36699b3441498216c4814c8a9a0//9a374bf2a15946408be9b139f172926f
        certificate = "57010dc8a9384e39a0fd5eb3f7473c84";//57010dc8a9384e39a0fd5eb3f7473c84//620b46902d664377a2f2efaa79211aa4

        ivVideo = findViewById(R.id.ivVideo);
        ivCallEnd = findViewById(R.id.ivCallEnd);

        localVideoFrame = findViewById(R.id.localVideoFrame);
        remoteVideoFrame = findViewById(R.id.remoteVideoFrame);
        mainLayout = findViewById(R.id.mainLayout);

        Drawable on = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_video_on, getResources().newTheme());
        Drawable off = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_video_off, getResources().newTheme());

        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoOn = !videoOn;

                ivVideo.setImageDrawable(videoOn ? on : off);

                agoraEngine.muteLocalVideoStream(!videoOn);
            }
        });

        ivCallEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agoraEngine != null) {
                    agoraEngine.stopPreview();

                    agoraEngine.leaveChannel();
                    RtcEngine.destroy();

                    finish();
                }
            }
        });

        if (checkPermission(Manifest.permission.RECORD_AUDIO) && checkPermission(Manifest.permission.CAMERA)) {
            setupVoiceSDKEngine();

//            if (DevicePermissionsKt.requestPermission(AgoraVideoViewer.Companion, this)) {
            if (!isJoined)
                if (agoraEngine != null)
                    joinChannel();
                else Toast.makeText(this, "Agora engine is NULL", Toast.LENGTH_SHORT).show();

            else {
                if (agoraEngine != null)
                    agoraEngine.leaveChannel();
                isJoined = false;
            }

//            }
        } else {
            requestPermission(new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            }, 222);
        }
    }

    private void setupVoiceSDKEngine() {
        try {

            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            //9a374bf2a15946408be9b139f172926f
            //511353626a1f48c5b42d2601d4f16509
            //d6c2b36699b3441498216c4814c8a9a0
            config.mEventHandler = mRtcEventHandler;
            agoraEngine = RtcEngine.create(config);

            agoraEngine.enableAudio();
            agoraEngine.enableVideo();

//            agoraEngine = RtcEngine.create(getBaseContext(), "511353626a1f48c5b42d2601d4f16509", mRtcEventHandler);

        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            throw new RuntimeException("Check the error");
        }
    }

    private void joinChannel() {
        ChannelMediaOptions options = new ChannelMediaOptions();

        // Set both clients as the BROADCASTER.
//        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;

//        if (mCallType == CallConstants.Video || mCallType == CallConstants.Video_Meet) {
//            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;
//            options.autoSubscribeVideo = true;
//        } else {
//            // Set the channel profile as BROADCASTING.
//            options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
//            options.autoSubscribeAudio = true;
//        }

        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
        options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;
        options.autoSubscribeVideo = true;
//        options.autoSubscribeAudio = true;


//        int res = agoraEngine.joinChannel("","12345","",uid,options);
        Log.i(TAG, "Channel name::: " + channelId);
        TokenUtils.gen(this, appId, certificate, channelId, uid, responseToken -> {
            if (responseToken == null) {
                Toast.makeText(this, "Invalid token", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Token error:::: ");
                return;
            }

            Log.i(TAG, "Token:: " + responseToken);
            int res = agoraEngine.joinChannel(responseToken, channelId, uid, options);
            if (res != 0) {//failed
                Log.e(TAG, "Error join :: " + RtcEngine.getErrorDescription(Math.abs(res)));
                Toast.makeText(this, RtcEngine.getErrorDescription(Math.abs(res)), Toast.LENGTH_SHORT).show();
            }
        });
//        String token = "007eJxTYNhVukB0UszffQlBPKx3V6oxrWn4Or0l7P3pNsvWZY6LYlYpMKSYJRslGZuZWVomGZuYGJpYWhgZmiWbWBiaJFskWiYaHIz4kNIQyMhw3TWQiZEBAkF8TgZDQyMjY2MTUzMGBgD2+B++";
//        int res = agoraEngine.joinChannel(token, "112233456", uid, options);
//        if (res != 0) {//failed
//            Log.e(TAG, "Error join :: " + RtcEngine.getErrorDescription(Math.abs(res)));
//            Toast.makeText(this, RtcEngine.getErrorDescription(Math.abs(res)), Toast.LENGTH_SHORT).show();
//        }

        agoraEngine.startPreview();

        // Join the channel with a temp token.
        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
//        agoraEngine.joinChannel(token, channelName, uid, options);
//        if (mCallType == CallConstants.Video) {
//            channelName = "video" + fromUserId;
//            agoraEngine.enableVideo();
//        } else if (mCallType == CallConstants.Video_Meet) {
//            channelName = "videoMeet" + fromUserId;
//            agoraEngine.enableVideo();
//        } else if (mCallType == CallConstants.Audio_Meet) {
//            channelName = "audio" + fromUserId;
//        } else {
//            channelName = fromUserId;
//        }
//
//        Log.i(TAG,"channel Name => " + channelName);
//
//
//        TokenUtils.gen(this, channelName, uid, responseToken -> {
//            if (responseToken == null) {
//                Toast.makeText(this, getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
//                return;
//            }
//
////            agoraVideoViewer.join(channelName,responseToken,Constants.CLIENT_ROLE_BROADCASTER);
//
//            int res = agoraEngine.joinChannel(responseToken, channelName, uid, options);
//            if (res != 0) {//failed
//                Log.e("agora", "Error join :: " + RtcEngine.getErrorDescription(Math.abs(res)));
//                Toast.makeText(this, RtcEngine.getErrorDescription(Math.abs(res)), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String[] permissionList, int reqCode) {
        ActivityCompat.requestPermissions(this,
                permissionList,
                reqCode);

        ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.BLUETOOTH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 222) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setupVoiceSDKEngine();

                if (agoraEngine != null)
                    joinChannel();
                else Toast.makeText(this, "Agora engine is NULL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        localView = null;
        remoteView = null;

        if (agoraEngine != null) {
            agoraEngine.stopPreview();
            agoraEngine.leaveChannel();
        }

        new Thread(() -> {
            RtcEngine.destroy();
            agoraEngine = null;
        });

    }

    private void setupLocalUserVideo(int uid) {//local user joined video

        localView = new SurfaceView(getBaseContext());
//        localView = RtcEngine.CreateRendererView(getBaseContext());
        localView.setZOrderMediaOverlay(true);
        localView.setTag(uid);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int w = width / 2;
        int h = height / 4;

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(w,h);
        localVideoFrame.setLayoutParams(params);
        localVideoFrame.addView(localView, new ConstraintLayout.LayoutParams(w, h));

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        constraintSet.connect(localVideoFrame.getId(),ConstraintSet.END,mainLayout.getId(),ConstraintSet.END,20);
        constraintSet.connect(localVideoFrame.getId(),ConstraintSet.TOP,mainLayout.getId(),ConstraintSet.TOP,20);
        constraintSet.constrainDefaultHeight(localVideoFrame.getId(),h);
        constraintSet.constrainDefaultWidth(localVideoFrame.getId(),w);
        constraintSet.applyTo(mainLayout);

        agoraEngine.setupLocalVideo(new VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, uid));

        localView.setVisibility(View.VISIBLE);

        agoraEngine.startPreview();
    }

    private void setupRemoteUserVideo(int uid) {//remote user joined video

        remoteView = new SurfaceView(getBaseContext());
//        remoteView = RtcEngine.CreateRendererView(getBaseContext());
//        remoteView.setZOrderMediaOverlay(true);
        remoteView.setTag(uid);

        remoteVideoFrame.addView(remoteView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        agoraEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        remoteView.setVisibility(View.VISIBLE);
        remoteVideoFrame.setVisibility(View.VISIBLE);

        if(localView != null){
            Log.i(TAG,"Local view available!!!");
            localView.setVisibility(View.VISIBLE);

            localView.post(new Runnable() {
                @Override
                public void run() {
                    int width = localView.getWidth();
                    int height = localView.getHeight();

                    Log.i(TAG,"Local view width and height are => " + width + " and " + height);
                    Log.i(TAG,"Local view user id is => " + localView.getTag());
                }
            });
        }else{
            Log.i(TAG,"Local view is null!!!");
        }

        agoraEngine.startPreview();

    }
}