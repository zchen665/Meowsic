package com.example.meowsic;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Keyboard extends AppCompatActivity {

    private SoundPool mSoundPool;
    private int csound;
    private int dsound;
    private int esound;
    private int fsound;
    private int gsound;
    private int asound;
    private int bsound;
    private int ccsound;
    private int cssound;
    private int csssound;
    private int dssound;
    private int gssound;
    private int assound;
    private int fssound;

    private float LEFT_VOL = 1.0f;
    private float RIGHT_VOL = 1.0f;
    private int PRIORITY = 1;
    private int LOOP = 0;
    private float RATE = 1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_board);

        AudioAttributes audio_attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPool = new SoundPool.Builder().setMaxStreams(12)
                .setAudioAttributes(audio_attr).build();
//                new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        asound = mSoundPool.load(getApplicationContext(),R.raw.a,1);
        bsound = mSoundPool.load(getApplicationContext(),R.raw.b,1);
        csound = mSoundPool.load(getApplicationContext(),R.raw.c,1);
        dsound = mSoundPool.load(getApplicationContext(),R.raw.d,1);
        esound = mSoundPool.load(getApplicationContext(),R.raw.e,1);
        fsound = mSoundPool.load(getApplicationContext(),R.raw.f,1);
        gsound = mSoundPool.load(getApplicationContext(),R.raw.g,1);
        cssound = mSoundPool.load(getApplicationContext(),R.raw.c_hash,1);
        csssound = mSoundPool.load(getApplicationContext(),R.raw.c_hash,1);
        ccsound = mSoundPool.load(getApplicationContext(),R.raw.c2,1);
        assound = mSoundPool.load(getApplicationContext(),R.raw.a_hash,1);
        fssound = mSoundPool.load(getApplicationContext(),R.raw.f_hash,1);
        gssound = mSoundPool.load(getApplicationContext(),R.raw.g_hash,1);
        dssound = mSoundPool.load(getApplicationContext(),R.raw.d_hash,1);

    }
    public void hit_a(View view){
        mSoundPool.play(asound,LEFT_VOL,RIGHT_VOL,PRIORITY,LOOP,RATE);
    }
}
