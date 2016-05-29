package com.applicaster.RNYouTubePlayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final String TAG = "YoutubePlayerActivity";
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private String apiToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideNavigation();

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        layout.setLayoutParams(lp);

        youTubeView = new YouTubePlayerView(this);
        LinearLayout.LayoutParams ylp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ylp.gravity = Gravity.CENTER;
        youTubeView.setLayoutParams(ylp);

        layout.addView(youTubeView);

        setContentView(layout);
        Intent intent = getIntent();
        apiToken = intent.getExtras().getString("apiToken");

        youTubeView.initialize(apiToken, this);
    }

    private void hideNavigation() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                      | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    Override
    protected void onResume() {
        super.onResume();
        hideNavigation();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            Intent intent = getIntent();
            String videoURL = intent.getExtras().getString("videoURL");
            player.loadVideo(videoURL);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("player error!", errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Retry initialization if user performed a recovery action
        if ((requestCode == RECOVERY_REQUEST) && (apiToken != null))
            getYouTubePlayerProvider().initialize(apiToken, this);
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "on back pressed");
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent); // Tell the parent activity that the player stopped
        super.onBackPressed();
    }
}
