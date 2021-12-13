package com.example.meowsic;

import android.content.Intent;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.MultichannelToMono;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.io.android.AndroidAudioPlayer;
import be.tarsos.dsp.writer.WriterProcessor;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.resample.RateTransposer;

public class CheckRecording extends AppCompatActivity {
    private int counter;
    String latestFilePath;
    Toast dspDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_recording);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latestFilePath = extras.getString("latestRec");
        }

        Button retBtn = (Button) findViewById(R.id.button5);
        Button replaceBtn  = (Button) findViewById(R.id.button4);
        Button genBtn  = (Button) findViewById(R.id.button3);
        retBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBack();
            }
        });
        replaceBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                replaceNotes();
            }
        });
        genBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDSP();
            }
        });
    }
    public void replaceNotes(){
        File dir = new File(getApplicationContext().getFilesDir().getAbsolutePath());
        File[] files = dir.listFiles();

        if (files.length == 12) {
            Toast.makeText(getApplicationContext(), "JUMP to Keyboart in 3s", Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    toKeyboard();
                }
            }, 3000);
        }else{
            Toast.makeText(getApplicationContext(), "GEN audios not found", Toast.LENGTH_LONG).show();
        }
    }

    public void toKeyboard(){
        Intent intent = new Intent(this, Keyboard.class);
        intent.putExtra("mode", "NEW");
        startActivity(intent);
    }

    public void goBack() {
        Intent intent = new Intent(this, NewRecording.class);
        startActivity(intent);
    }

    //generate all 12 notes for replacement
    public void startDSP() {
        if (latestFilePath == null){
            Toast.makeText(getApplicationContext(), "Please record first", Toast.LENGTH_LONG).show();
            return;
        }
        Thread managerThread = new Thread(new Runnable() {
            ArrayList<Thread> threads = new ArrayList<>();
            @Override
            public void run() {
                for(int i = 0; i < 12; i++){
                    int finalI = i;
                    threads.add(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                pitchProcessing(0.5 + 0.1* finalI);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }));
                    threads.get(i).start();
                    try {
                        threads.get(i).join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Gen Audio Complete", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        managerThread.start();
    }


    public void pitchProcessing(double playRate) throws FileNotFoundException {
        double rate = playRate;
//        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +"/Meowsic/";
        RandomAccessFile outputFile = new RandomAccessFile(getApplicationContext().getFilesDir() + "/test_" + playRate + ".wav", "rw");

        TarsosDSPAudioFormat outputFormat = new TarsosDSPAudioFormat(44100, 16, 1, true, false);

        FileInputStream fileInputStream = new FileInputStream(latestFilePath);
        RateTransposer rateTransposer;

        WaveformSimilarityBasedOverlapAdd wsola;

        AudioDispatcher dispatcher =  new AudioDispatcher(new UniversalAudioInputStream(fileInputStream, outputFormat), 2048, 0);
        rateTransposer = new RateTransposer(rate);
        wsola = new WaveformSimilarityBasedOverlapAdd(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(rate, 44100));
        WriterProcessor writer = new WriterProcessor(outputFormat, outputFile);

        dispatcher.addAudioProcessor(new MultichannelToMono(1,true));
//        wsola.setDispatcher(dispatcher);
//        dispatcher.addAudioProcessor(wsola);
        dispatcher.addAudioProcessor(rateTransposer);
//        dispatcher.addAudioProcessor(new AndroidAudioPlayer(dispatcher.getFormat()));
        dispatcher.addAudioProcessor(writer);
        dispatcher.run();
//        Toast.makeText(getApplicationContext(), "pitch end", Toast.LENGTH_LONG).show();
    }


}
