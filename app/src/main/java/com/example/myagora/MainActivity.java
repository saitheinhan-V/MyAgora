package com.example.myagora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Random;

//import io.agora.agorauikit_android.AgoraConnectionData;
//import io.agora.agorauikit_android.AgoraSettings;
//import io.agora.agorauikit_android.AgoraVideoViewer;
//import io.agora.agorauikit_android.DevicePermissionsKt;
//import io.agora.rtc2.ChannelMediaOptions;
//import io.agora.rtc2.Constants;
//import io.agora.rtc2.IRtcEngineEventHandler;
//import io.agora.rtc2.RtcEngine;
//import io.agora.rtc2.RtcEngineConfig;
//import io.agora.rtm.jni.IRtmCallEventHandler;

public class MainActivity extends AppCompatActivity {

//    private String appId;
//    private String channelName;
//    private String token;
//    private int uid;
//    private String certificate;
//    private RtcEngine agoraEngine;
//
//    private AgoraVideoViewer agoraVideoViewer;
//    private EditText editText;
//    private Button btnJoin;
//
//    private static final String TAG = "agoraCall";
//
//    private boolean isJoined = false;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        appId = "9a374bf2a15946408be9b139f172926f";
//        certificate = "620b46902d664377a2f2efaa79211aa4";
////        uid = new Random().nextInt(10000);
//        if (getIntent() != null) {
//            uid = Integer.parseInt(getIntent().getStringExtra("uid"));
//        } else {
//            uid = new Random().nextInt(10000);
//        }
//
//        if (checkPermission(Manifest.permission.RECORD_AUDIO) && checkPermission(Manifest.permission.CAMERA)) {
//            setupVoiceSDKEngine();
//
//            if (DevicePermissionsKt.requestPermission(AgoraVideoViewer.Companion, this)) {
//                if (!isJoined)
//                    joinChannel();
//                else {
//                    if (agoraEngine != null)
//                        agoraEngine.leaveChannel();
//                    isJoined = false;
//                }
//
//            }
//        } else {
//            requestPermission(new String[]{
//                    Manifest.permission.RECORD_AUDIO,
//                    Manifest.permission.CAMERA
//            }, 222);
//        }
//
//
//    }
//
//    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
//        @Override
//        public void onError(int err) {
//            super.onError(err);
//            Log.i(TAG,"ERR in handler :: code > " + err + " DESC ::: " + RtcEngine.getErrorDescription(err));
//        }
//
//        @Override
//        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {//local user joined
//            super.onJoinChannelSuccess(channel, uid, elapsed);
//            Log.i(TAG, "Local user joined " + uid);
//            isJoined = true;
//        }
//
//        @Override
//        public void onLeaveChannel(RtcStats stats) {//local user leave
//            super.onLeaveChannel(stats);
////            Log.i(TAG,"Local user leave " + uid);
//            finish();
//        }
//
//        @Override
//        public void onUserJoined(int uid, int elapsed) {//remote user joined
//            super.onUserJoined(uid, elapsed);
//            Log.i(TAG, "Remote user joined" + uid);
//        }
//
//        @Override
//        public void onUserOffline(int uid, int reason) {//remote user leave
//            super.onUserOffline(uid, reason);
//            Log.i(TAG, "Remote user leave " + uid);
//        }
//    };
//
//    private void setupVoiceSDKEngine() {
//        try {
//
//            RtcEngineConfig config = new RtcEngineConfig();
//            config.mContext = getBaseContext();
//            config.mAppId = appId;
//            config.mEventHandler = mRtcEventHandler;
//            agoraEngine = RtcEngine.create(config);
//        } catch (Exception e) {
//            throw new RuntimeException("Check the error");
////            Log.e(TAG,e.getMessage());
//        }
//    }
//
//    private void initVideoViewer(String token) {
//        try {
//            AgoraSettings agoraSettings = new AgoraSettings();
//            agoraVideoViewer = new AgoraVideoViewer(this, new AgoraConnectionData(appId, token,String.valueOf(uid)), AgoraVideoViewer.Style.FLOATING, new AgoraSettings(), null);
//
//            // Add the AgoraVideoViewer to the Activity layout
//            this.addContentView(agoraVideoViewer, new FrameLayout.LayoutParams(
//                    FrameLayout.LayoutParams.MATCH_PARENT,
//                    FrameLayout.LayoutParams.MATCH_PARENT)
//            );
//        } catch (Exception e) {
//            Log.e("AgoraVideoViewer",
//                    "Could not initialize AgoraVideoViewer. Check that your app Id is valid.");
//            Log.e("Exception", e.toString());
//        }
//    }
//
//    private void joinChannel() {
//        ChannelMediaOptions options = new ChannelMediaOptions();
//
//        // Set both clients as the BROADCASTER.
//        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
//
////        if (mCallType == CallConstants.Video || mCallType == CallConstants.Video_Meet) {
////            options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION;
////            options.autoSubscribeVideo = true;
////        } else {
//        // Set the channel profile as BROADCASTING.
//        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
//        options.autoSubscribeAudio = true;
//        options.autoSubscribeVideo = true;
////        }
//
//
//        // Join the channel with a temp token.
//        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
////        agoraEngine.joinChannel(token, channelName, uid, options);
////        if (mCallType == CallConstants.Video) {
////            channelName = "video" + fromUserId;
////            agoraEngine.enableVideo();
////        } else if (mCallType == CallConstants.Video_Meet) {
////            channelName = "videoMeet" + fromUserId;
////            agoraEngine.enableVideo();
////        } else if (mCallType == CallConstants.Audio_Meet) {
////            channelName = "audio" + fromUserId;
////        } else {
////            channelName = fromUserId;
////        }
//
//        channelName = "11111111";
//
//        Log.i(TAG,"channel Name => " + channelName);
//
//
//        TokenUtils.gen(this, appId, certificate, channelName, uid, responseToken -> {
//            if (responseToken == null) {
//                Toast.makeText(this, "Invalid token", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            Log.i(TAG,"Token >> " + responseToken);
//            initVideoViewer(responseToken);
//
//
//            int res = agoraEngine.joinChannel(responseToken, channelName, uid, options);
//
//            agoraVideoViewer.join(channelName, responseToken, Constants.CLIENT_ROLE_BROADCASTER);
//            agoraEngine.startPreview();
//
//            if (res != 0) {//failed
//                Log.e(TAG, "Error join :: code" + res + " ::Reason:: " + RtcEngine.getErrorDescription(Math.abs(res)));
//                Toast.makeText(this, RtcEngine.getErrorDescription(Math.abs(res)), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    private boolean checkPermission(String permission) {
//        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermission(String[] permissionList, int reqCode) {
//        ActivityCompat.requestPermissions(this,
//                permissionList,
//                reqCode);
//
//        ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.BLUETOOTH);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 222) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
//                setupVoiceSDKEngine();
//
//                joinChannel();
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        if (agoraEngine != null) {
//            agoraEngine.stopPreview();
//            agoraEngine.leaveChannel();
//        }
//
//        new Thread(() -> {
//            RtcEngine.destroy();
//            agoraEngine = null;
//        });
//
//        super.onDestroy();
//
//    }
}