package com.example.meowsic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class NewRecording extends AppCompatActivity {
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mPlayer;
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 200;
    private static String fileName = null;

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
        ImageButton play = (ImageButton) findViewById(R.id.play);
        ImageButton p_stop = (ImageButton) findViewById(R.id.p_stop);

//        start.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
//        stop.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
//        play.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
//        p_stop.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));

        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toCheckRecording();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                start.setBackgroundColor(getResources().getColor(R.color.design_default_color_secondary));
//                stop.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
//                play.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
//                p_stop.setBackgroundColor(getResources().getColor(R.color.cardview_dark_background));
                start.setBackground(getDrawable(R.drawable.button_style2));
                stop.setBackground(getDrawable(R.drawable.button_style));
                play.setBackground(getDrawable(R.drawable.button_style));
                p_stop.setBackground(getDrawable(R.drawable.button_style));
                startRecord();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start.setBackground(getDrawable(R.drawable.button_style));
                stop.setBackground(getDrawable(R.drawable.button_style));
                play.setBackground(getDrawable(R.drawable.button_style));
                p_stop.setBackground(getDrawable(R.drawable.button_style));
                stopRecord();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start.setBackground(getDrawable(R.drawable.button_style));
                stop.setBackground(getDrawable(R.drawable.button_style));
                play.setBackground(getDrawable(R.drawable.button_style2));
                p_stop.setBackground(getDrawable(R.drawable.button_style));
                play();
            }
        });

        p_stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start.setBackground(getDrawable(R.drawable.button_style));
                stop.setBackground(getDrawable(R.drawable.button_style));
                play.setBackground(getDrawable(R.drawable.button_style));
                p_stop.setBackground(getDrawable(R.drawable.button_style));
                stop();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        if (CheckPermissions()) {
            fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            File dir = new File(fileName, "Meowsic");
            if (!dir.exists()) {
                dir.mkdir();
                Log.i("files", "Meowsic is created");
            }
            fileName += "/Meowsic/" + currentTime + ".3gp";
            if (mMediaRecorder == null) {
                mMediaRecorder = new MediaRecorder();
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mMediaRecorder.setOutputFile(fileName);
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
                mMediaRecorder.setOutputFile(fileName);
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
//        mMediaRecorder.stop();
//        mMediaRecorder.release();
//        mMediaRecorder = null;
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
            }catch(IllegalStateException e) {
//                mMediaRecorder = null;
//                mMediaRecorder = new MediaRecorder();
            }
            mMediaRecorder.release();
//            mMediaRecorder = null;
        }
    }

    public void play() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        }catch(IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }


    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
