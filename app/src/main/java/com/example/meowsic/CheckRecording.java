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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_recording);

        counter = 0;

        Button retBtn = (Button) findViewById(R.id.button5);
        Button replaceBtn  = (Button) findViewById(R.id.button4);
        retBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBack();
            }
        });
        replaceBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startDSP();
            }
        });
    }

    public void goBack() {
        Intent intent = new Intent(this, NewRecording.class);
        startActivity(intent);
    }
    public void startDSP() {
        counter += 1;
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
        double rate = 2.0;
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() +"/Meowsic/";
        String inputFile = filePath + "20211212_051456.wav";
        TarsosDSPAudioFormat outputFormat = new TarsosDSPAudioFormat(44100, 16, 1, true, false);
        RandomAccessFile outputFile = new RandomAccessFile(filePath + "testResult" + counter + ".wav", "rw");

        FileInputStream fileInputStream = new FileInputStream(inputFile);
        RateTransposer rateTransposer;

        WaveformSimilarityBasedOverlapAdd wsola;

        dispatcher =  new AudioDispatcher(new UniversalAudioInputStream(fileInputStream, outputFormat), 2048, 0);
        rateTransposer = new RateTransposer(rate);
        wsola = new WaveformSimilarityBasedOverlapAdd(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(rate, 44100));
        WriterProcessor writer = new WriterProcessor(outputFormat, outputFile);

//        wsola.setDispatcher(dispatcher);
//        dispatcher.addAudioProcessor(wsola);
        dispatcher.addAudioProcessor(rateTransposer);
        dispatcher.addAudioProcessor(new AndroidAudioPlayer(dispatcher.getFormat()));
        dispatcher.setZeroPadFirstBuffer(true);
        dispatcher.setZeroPadLastBuffer(true);
        dispatcher.addAudioProcessor(writer);
        dispatcher.run();
        Toast.makeText(getApplicationContext(), "pitch end", Toast.LENGTH_LONG).show();
    }

//    public void startDSP(){
//        File inputFile = new File(source);
//        AudioFormat format = AudioSystem.getAudioFileFormat(inputFile).getFormat();
//        double sampleRate = format.getSampleRate();
//        double factor = PitchShiftingExample.centToFactor(cents);
//        RateTransposer rateTransposer = new RateTransposer(factor);
//        WaveformSimilarityBasedOverlapAdd wsola = new WaveformSimilarityBasedOverlapAdd(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(factor, sampleRate));
//        WriterProcessor writer = new WriterProcessor(outputFormat, outputFile);
//        AudioDispatcher dispatcher;
//        if(format.getChannels() != 1){
//            dispatcher = AudioDispatcherFactory.fromFile(inputFile,wsola.getInputBufferSize() * format.getChannels(),wsola.getOverlap() * format.getChannels());
//            dispatcher.addAudioProcessor(new MultichannelToMono(format.getChannels(),true));
//        }else{
//            dispatcher = AudioDispatcherFactory.fromFile(inputFile,wsola.getInputBufferSize(),wsola.getOverlap());
//        }
//        wsola.setDispatcher(dispatcher);
//        dispatcher.addAudioProcessor(wsola);
//        dispatcher.addAudioProcessor(rateTransposer);
//        dispatcher.addAudioProcessor(writer);
//        dispatcher.run();
//    }


}
