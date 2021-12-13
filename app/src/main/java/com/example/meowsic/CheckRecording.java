package com.example.meowsic;

import android.content.Intent;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
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
    AudioDispatcher dispatcher;
    String latestFilePath;
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
        Button genBtn  = (Button) findViewById(R.id.button4);
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
        Toast.makeText(getApplicationContext(), "Replace notes", Toast.LENGTH_LONG).show();
    }

    public void goBack() {
        Intent intent = new Intent(this, NewRecording.class);
        startActivity(intent);
    }
    public void startDSP() {
        if (latestFilePath == null){
            Toast.makeText(getApplicationContext(), "Please record first", Toast.LENGTH_LONG).show();
            return;
        }
        Thread dspThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pitchProcessing();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        dspThread.run();
        try {
            dspThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void pitchProcessing() throws FileNotFoundException {
        if (dispatcher != null){
            dispatcher.stop();
            dispatcher = null;
        }
        Toast.makeText(getApplicationContext(), "pitch start", Toast.LENGTH_LONG).show();
        double rate = 1.5;
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +"/Meowsic/";

        TarsosDSPAudioFormat outputFormat = new TarsosDSPAudioFormat(44100, 16, 1, true, false);
        RandomAccessFile outputFile = new RandomAccessFile(filePath + "testResult" + counter + ".wav", "rw");

        FileInputStream fileInputStream = new FileInputStream(latestFilePath);
        RateTransposer rateTransposer;

        WaveformSimilarityBasedOverlapAdd wsola;

        dispatcher =  new AudioDispatcher(new UniversalAudioInputStream(fileInputStream, outputFormat), 2048, 0);
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
