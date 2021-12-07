package com.example.meowsic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class NewRecording extends AppCompatActivity {
    private MediaRecorder mMediaRecorder;
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 200;
    private static String mFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recording);

        //TextView textView2 = findViewById(R.id.textView2);
        Intent intent = getIntent();
        //textView2.setText("Recording Screen");

        ImageButton start = (ImageButton) findViewById(R.id.start);
        ImageButton stop = (ImageButton) findViewById(R.id.stop);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toCheckRecording();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRecord();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopRecord();
            }
        });
    }

    public void toCheckRecording() {
        Intent intent = new Intent(this, CheckRecording.class);

        startActivity(intent);
    }

    public boolean CheckPermissions() {
        // this method is used to check permission
        int s = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int a = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return s == PackageManager.PERMISSION_GRANTED && a == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    public void startRecord() {
        if (CheckPermissions()) {
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/AudioRecording.3gp";
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mMediaRecorder.setOutputFile(mFileName);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    mMediaRecorder.prepare();
                } catch (IOException e) {
                    Log.e("TAG", "prepare() failed");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }

            } else {
                stopRecord();
                mMediaRecorder = new MediaRecorder();
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mMediaRecorder.setOutputFile(mFileName);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    mMediaRecorder.prepare();
                } catch (IllegalStateException | IOException e) {
//                    Log.e("TAG", "prepare() failed");
                    e.printStackTrace();
                }
            }
            mMediaRecorder.start();

        } else {
            RequestPermissions();
        }
    }

    public void stopRecord() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

}
