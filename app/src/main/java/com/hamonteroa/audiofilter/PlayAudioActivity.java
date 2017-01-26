package com.hamonteroa.audiofilter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.audiofx.EnvironmentalReverb;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;

public class PlayAudioActivity extends AppCompatActivity {

    private static final String LOG_TAG = "PlayAudioActivity";
    private ImageButton mChipmunkButton;
    private ImageButton mDarthVaderButton;
    private ImageButton mEchoButton;
    private ImageButton mFastButton;
    private ImageButton mReverbButton;
    private ImageButton mSlowButton;
    private Button mRecordAgainButton;

    private String mAudioPath;

    private MediaPlayer mMediaPlayer;

    private enum SoundEffect {CHIPMUNK, DARTH_VADER, ECHO, FAST, REVERB, SLOW}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_play_audio);

        this.mChipmunkButton = (ImageButton) findViewById(R.id.chipmunk_button);
        this.mDarthVaderButton = (ImageButton) findViewById(R.id.darth_vader_button);
        this.mEchoButton = (ImageButton) findViewById(R.id.echo_button);
        this.mFastButton = (ImageButton) findViewById(R.id.fast_button);
        this.mReverbButton = (ImageButton) findViewById(R.id.reverb_button);
        this.mSlowButton = (ImageButton) findViewById(R.id.slow_button);
        this.mRecordAgainButton = (Button) findViewById(R.id.record_again_button);

        this.mAudioPath = getIntent().getStringExtra(Constants.CONST_SP_AUDIO_PATH);

        this.mChipmunkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioWithFiler(SoundEffect.CHIPMUNK);
            }
        });

        this.mDarthVaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioWithFiler(SoundEffect.DARTH_VADER);
            }
        });

        this.mEchoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioWithFiler(SoundEffect.ECHO);
            }
        });

        this.mFastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioWithFiler(SoundEffect.FAST);
            }
        });

        this.mReverbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioWithFiler(SoundEffect.REVERB);
            }
        });

        this.mSlowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudioWithFiler(SoundEffect.SLOW);
            }
        });

        this.mRecordAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void setEnabledButton(boolean enabled) {
        this.mChipmunkButton.setEnabled(enabled); setImageButtonFilter(this.mChipmunkButton);
        this.mDarthVaderButton.setEnabled(enabled); setImageButtonFilter(this.mDarthVaderButton);
        this.mEchoButton.setEnabled(enabled); setImageButtonFilter(this.mEchoButton);
        this.mFastButton.setEnabled(enabled); setImageButtonFilter(this.mFastButton);
        this.mReverbButton.setEnabled(enabled); setImageButtonFilter(this.mReverbButton);
        this.mSlowButton.setEnabled(enabled); setImageButtonFilter(this.mSlowButton);
        this.mRecordAgainButton.setEnabled(enabled);
    }

    private void setImageButtonFilter(ImageButton imageButton) {
        if (imageButton.isEnabled()) {
            imageButton.clearColorFilter();

        } else {
            imageButton.setColorFilter(0x77000000, PorterDuff.Mode.SRC_IN);
        }
    }

    private void playAudioWithFiler(SoundEffect effect) {
        Log.v(LOG_TAG, "playAudioWithFiler: " + effect.toString());
        setEnabledButton(false);

        this.mMediaPlayer = new MediaPlayer();

        try {
            this.mMediaPlayer.setDataSource(this.mAudioPath);

            // Attaching effect
            switch (effect) {
                case CHIPMUNK: //playAudio(pitch: 1000)
                    this.mMediaPlayer.setPlaybackParams(this.mMediaPlayer.getPlaybackParams().setPitch(4.0f));
                    break;

                case DARTH_VADER: //playAudio(pitch: -1000)
                    this.mMediaPlayer.setPlaybackParams(this.mMediaPlayer.getPlaybackParams().setPitch(0.75f));
                    break;

                case ECHO: //playAudio(echo: true)
                    PresetReverb presetReverb = new PresetReverb(1, this.mMediaPlayer.getAudioSessionId());
                    //this.mMediaPlayer.attachAuxEffect(presetReverb.getId());
                    presetReverb.setPreset(PresetReverb.PRESET_LARGEHALL);
                    presetReverb.setEnabled(true);
                    this.mMediaPlayer.setAuxEffectSendLevel(1.0f);

                    break;

                case REVERB: //playAudio(reverb: true)
                    EnvironmentalReverb environmentalReverb = new EnvironmentalReverb(1, this.mMediaPlayer.getAudioSessionId());
                    environmentalReverb.setDecayHFRatio((short) 1000);
                    environmentalReverb.setDecayTime(1000);
                    environmentalReverb.setDensity((short) 1000);
                    environmentalReverb.setDiffusion((short) 1000);
                    environmentalReverb.setReverbLevel((short) -1000);
                    environmentalReverb.setReverbDelay((short) 50);
                    environmentalReverb.setEnabled(true);
                    //this.mMediaPlayer.attachAuxEffect(environmentalReverb.getId());
                    this.mMediaPlayer.setAuxEffectSendLevel(1.0f);

                    break;

                case FAST: //playAudio(rate: 1.5)
                    this.mMediaPlayer.setPlaybackParams(this.mMediaPlayer.getPlaybackParams().setSpeed(1.75f));
                    //SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);


                    break;

                case SLOW: //playAudio(rate: 0.5)
                    this.mMediaPlayer.setPlaybackParams(this.mMediaPlayer.getPlaybackParams().setSpeed(0.25f));
                    break;
            }

            this.mMediaPlayer.prepare();
            this.mMediaPlayer.start();

            this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlaying();
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        setEnabledButton(true);

        this.mMediaPlayer.stop();
        this.mMediaPlayer.release();
        this.mMediaPlayer = null;
    }
}
