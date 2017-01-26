package com.hamonteroa.audiofilter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private ImageButton mRecordButton;
    private ImageButton mStopRecordingButton;
    private MediaRecorder mMediaRecorder;

    private String mAudioPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        this.mRecordButton = (ImageButton) findViewById(R.id.record_button);
        this.mStopRecordingButton = (ImageButton) findViewById(R.id.stop_recording_button);
        this.mAudioPath = "";

        enableButtons(true, false);

        this.mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        this.mStopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();

                Intent intent = new Intent(getApplicationContext(), PlayAudioActivity.class);
                intent.putExtra(Constants.CONST_SP_AUDIO_PATH, mAudioPath);
                startActivity(intent);
            }
        });
    }

    private void enableButtons(boolean setRecordButtonEnabled, boolean setStopRecordingButtonEnabled) {
        this.mRecordButton.setEnabled(setRecordButtonEnabled);
        setImageButtonFilter(this.mRecordButton);
        this.mStopRecordingButton.setEnabled(setStopRecordingButtonEnabled);
        setImageButtonFilter(this.mStopRecordingButton);
    }

    private void setImageButtonFilter(ImageButton imageButton) {
        if (imageButton.isEnabled()) {
            imageButton.clearColorFilter();

        } else {
            imageButton.setColorFilter(0x77000000, PorterDuff.Mode.SRC_IN);
        }
    }

    private void startRecording() {
        enableButtons(false, true);

        this.mAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.3gp";

        this.mMediaRecorder = new MediaRecorder();
        this.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        this.mMediaRecorder.setOutputFile(this.mAudioPath);
        this.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            this.mMediaRecorder.prepare();
        } catch (IOException ex) {
            Log.v(LOG_TAG, "exception: " + ex.getMessage());
        }

        this.mMediaRecorder.start();
    }

    private void stopRecording() {
        enableButtons(false, false);

        this.mMediaRecorder.stop();
        this.mMediaRecorder.release();
        this.mMediaRecorder = null;
    }
}
