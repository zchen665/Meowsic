package com.example.meowsic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class NewRecording extends AppCompatActivity {
    PCMPlayer pcm;
    private AudioRecord audioRecord;
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 200;
    private static String fileName = null;
    private int FREQUENCY = 44100;
    private boolean recording;
    int bufferSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recording);

        //TextView textView2 = findViewById(R.id.textView2);
        Intent intent = getIntent();
        recording = false;

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
        intent.putExtra("latestRec", fileName);
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

    public void getFilename() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        if (CheckPermissions()) {
            fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            File dir = new File(fileName, "Meowsic");
            if (!dir.exists()) {
                dir.mkdir();
                Log.i("files", "Meowsic is created");
            }
            fileName += "/Meowsic/" + currentTime + ".wav";

        } else {
            RequestPermissions();
        }
    }

    public void startRecord() {
        final int CHANNELCONFIG = AudioFormat.CHANNEL_IN_MONO;
        recording = true;
        if (fileName == null) {
            getFilename();
        }
        bufferSize = AudioRecord.getMinBufferSize(FREQUENCY, CHANNELCONFIG, AudioFormat.ENCODING_PCM_16BIT);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            RequestPermissions();
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, FREQUENCY, CHANNELCONFIG, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    recording();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        audioThread.start();
        audioRecord.startRecording();
    }


    public void stopRecord() {
        recording = false;
        Toast.makeText(getApplicationContext(), "stored as " + fileName, Toast.LENGTH_LONG).show();

    }

    private void recording() throws FileNotFoundException {
        OutputStream os = null;
        os = new FileOutputStream(fileName);

        byte[] audioData = new byte[bufferSize];
        int read = 0;

        while (recording) {
            read = audioRecord.read(audioData,0,bufferSize);
            if(AudioRecord.ERROR_INVALID_OPERATION != read){
                try {
                    os.write(audioData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            os.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
        Log.e("TAGG", "recording ended");
    }


    public void play() {
        if (fileName != null) {
            pcm = new PCMPlayer();
            pcm.prepare(fileName);
            pcm.play();
        }
    }

    public void stop() {
        if (pcm != null) {
            pcm.stop();
        }
    }

}
