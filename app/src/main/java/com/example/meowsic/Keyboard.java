package com.example.meowsic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.meowsic.Constants.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Keyboard extends AppCompatActivity {
    private boolean state_start;
    private boolean recording;
    private MediaProjectionManager mediaProjectionManager;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private String fileName = null;

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

    private float LEFT_VOL = 2.0f;
    private float RIGHT_VOL = 2.0f;
    private int PRIORITY = 1;
    private int LOOP = 0;
    private float ONPRESS_RATE = 0.4f;
    private float ONRELEASE_RATE = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_board);

        state_start = true;
        recording = false;
        fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        fileName += "/AudioRecording.3gp";

        AudioAttributes audio_attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPool = new SoundPool.Builder().setMaxStreams(12)
                .setAudioAttributes(audio_attr).build();
//                new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        asound = mSoundPool.load(getApplicationContext(), R.raw.a, 1);
        bsound = mSoundPool.load(getApplicationContext(), R.raw.b, 1);
        csound = mSoundPool.load(getApplicationContext(), R.raw.c, 1);
        dsound = mSoundPool.load(getApplicationContext(), R.raw.d, 1);
        esound = mSoundPool.load(getApplicationContext(), R.raw.e, 1);
        fsound = mSoundPool.load(getApplicationContext(), R.raw.f, 1);
        gsound = mSoundPool.load(getApplicationContext(), R.raw.g, 1);
        cssound = mSoundPool.load(getApplicationContext(), R.raw.c_hash, 1);
        csssound = mSoundPool.load(getApplicationContext(), R.raw.c_hash, 1);
        ccsound = mSoundPool.load(getApplicationContext(), R.raw.c2, 1);
        assound = mSoundPool.load(getApplicationContext(), R.raw.a_hash, 1);
        fssound = mSoundPool.load(getApplicationContext(), R.raw.f_hash, 1);
        gssound = mSoundPool.load(getApplicationContext(), R.raw.g_hash, 1);
        dssound = mSoundPool.load(getApplicationContext(), R.raw.d_hash, 1);


        Button c_btn = findViewById(R.id.note_c);
        int[] streamIDs = new int[12];
//        c_btn.setOnTouchListener(new View.OnTouchListener(){
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction()== MotionEvent.ACTION_DOWN){
//                    int tmp = mSoundPool.play(csound, LEFT_VOL, RIGHT_VOL, PRIORITY, LOOP, ONPRESS_RATE);
//                    Log.i("gg", "onpress " + tmp);
//                    streamIDs[2] = tmp;
//
//                    mSoundPool.setRate(asound,ONRELEASE_RATE);
//                }
//                else if (event.getAction()== MotionEvent.ACTION_UP){
//                    Log.i("gg", "on release "+ streamIDs[2]);
//                    mSoundPool.pause(streamIDs[2]);
//                    mSoundPool.resume(streamIDs[2]);
//                }
//
//                return true;
//            }
//        });

    }

    public void hit_a(View view) {
        mSoundPool.play(asound, LEFT_VOL, RIGHT_VOL, PRIORITY, LOOP, ONPRESS_RATE);
    }

    public void hit_b(View view) {
        mSoundPool.play(bsound, LEFT_VOL, RIGHT_VOL, PRIORITY, LOOP, ONPRESS_RATE);
    }

    public void hit_c(View view) {
        mSoundPool.play(csound, LEFT_VOL, RIGHT_VOL, PRIORITY, LOOP, ONPRESS_RATE);
    }

    public void hit_d(View view) {
        mSoundPool.play(dsound, LEFT_VOL, RIGHT_VOL, PRIORITY, LOOP, ONPRESS_RATE);
    }


    public void click_start_pause(View view) {

        if (checkPermissions()) {
            ImageButton curBtn = findViewById(R.id.kpanel_first_btn);

            recording = true;
            if (state_start == true) {
//                File newAudio = new File(fileName);

                Log.i("path", fileName);
                startMediaProjectionRequest();


                Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
                curBtn.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                curBtn.setImageResource(android.R.drawable.ic_media_play);
            }
            state_start = !state_start;
            monitor_stop_btn();
        } else {
            requestPermissions();
        }
    }

    public void click_stop(View view) {
        ImageButton curBtn = findViewById(R.id.kpanel_first_btn);
        curBtn.setImageResource(android.R.drawable.ic_media_play);
        state_start = true;
        recording = false;
        monitor_stop_btn();

        Log.i("recorder", "recording stop");
        Intent serviceIntent = new Intent(getApplicationContext(), RecorderService.class);
        serviceIntent.setAction(ACTION_STOP);
        startService(serviceIntent);

    }


    private void monitor_stop_btn() {
        ImageButton stopBtn = findViewById(R.id.kpanel_stop_btn);

        if (recording) {
            stopBtn.setVisibility(View.VISIBLE);

        } else {
            stopBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void startMediaProjectionRequest() {
        mediaProjectionManager = (MediaProjectionManager) getApplication()
                .getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                CAPTURE_MEDIA_PROJECTION_REQUEST_CODE
        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }

    public boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(Keyboard.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_MEDIA_PROJECTION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this,
                        "MediaProjection permission obtained. Foreground service will be started to capture audio.",
                        Toast.LENGTH_SHORT
                ).show();

                Intent audioCaptureIntent = new Intent(this, RecorderService.class);
                audioCaptureIntent.setAction(ACTION_START);
                audioCaptureIntent.putExtra(EXTRA_RESULT_DATA, data);
                Log.i("recorder", "recording start");
                startForegroundService(audioCaptureIntent);
                Log.i("recorder", "recording start");
            } else {
                Toast.makeText(this,
                        "Request to obtain MediaProjection denied.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        } else if (requestCode == SELECT_OUTPUT_DIRECTORY_REQUEST_CODE) {
            getSharedPreferences(PREFERENCES_APP_NAME, MODE_PRIVATE)
                    .edit()
                    .putString(PREFERENCES_KEY_OUTPUT_DIRECTORY, data == null ? "" : data.getData().toString())
                    .apply();
        }
    }
}