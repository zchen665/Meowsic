package com.example.meowsic;

import android.content.Intent;
import android.media.AudioFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.writer.WriterProcessor;
import be.tarsos.dsp.WaveformSimilarityBasedOverlapAdd;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.resample.RateTransposer;

public class CheckRecording extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_recording);

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
        double rate = 1.0;
//        RateTransposer rateTransposer;
//        AudioDispatcher dispatcher;
//        WaveformSimilarityBasedOverlapAdd wsola;
//
//        dispatcher = AudioDispatcherFactory.fromPipe(mAudiopath, 44100, 5000, 2500);
//        rateTransposer = new RateTransposer(rate);
//        wsola = new WaveformSimilarityBasedOverlapAdd(WaveformSimilarityBasedOverlapAdd.Parameters.musicDefaults(rate, 44100));
//        WaveformWriter writer = new WaveformWriter((TarsosDSPAudioFormat) dispatcher.getFormat(), BASE_PATH + "Recorded" + File.separator + "PITCHED_" + mVideoFileName + ".mp3");
//
//        wsola.setDispatcher(dispatcher);
//        dispatcher.addAudioProcessor(wsola);
//        dispatcher.addAudioProcessor(rateTransposer);
//        dispatcher.addAudioProcessor(new AndroidAudioPlayer(dispatcher.getFormat()));
//        dispatcher.setZeroPadFirstBuffer(true);
//        dispatcher.setZeroPadLastBuffer(true);
//        dispatcher.addAudioProcessor(writer);
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
