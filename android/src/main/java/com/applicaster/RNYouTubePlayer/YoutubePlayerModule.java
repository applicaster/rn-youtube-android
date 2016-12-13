package com.applicaster.RNYouTubePlayer;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Map;

public class YoutubePlayerModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    protected static final String TAG = "YoutubePlayerModule";
    private static final String STATE_STOPPED = "stopped";
    private static final int YOUTUBE_PLAYER_ACTIVITY = 987654; // TODO: should be moved to values.xml

    final ReactApplicationContext reactContext;
    private Promise promise;

    public YoutubePlayerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "RNYouTubePlayer";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    public void play(String apiToken, String videoURL, Promise promise) {
        this.promise = promise;

        Activity currentActivity = getCurrentActivity();
        final Intent intent = new Intent(currentActivity, YoutubePlayerActivity.class);
        intent.putExtra("apiToken", apiToken);
        intent.putExtra("videoURL", videoURL);
        currentActivity.startActivityForResult(intent, YOUTUBE_PLAYER_ACTIVITY);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "on activity result");
        if (requestCode == YOUTUBE_PLAYER_ACTIVITY && resultCode == Activity.RESULT_CANCELED) {
            Log.d(TAG, "promise resolved");
            WritableMap map = Arguments.createMap();
            map.putString("state", STATE_STOPPED);
            promise.resolve(map);
        }
    }
    public void onNewIntent(Intent intent) {

    }
}
