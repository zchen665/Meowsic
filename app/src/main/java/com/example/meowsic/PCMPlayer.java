package com.example.meowsic;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PCMPlayer {
    private String pathAudio;
    private AudioTrack audioPlayer;
    private Thread mThread;
    private int bytesread = 0, ret = 0;
    private int size;
    private FileInputStream in = null;
    private byte[] byteData = null;
    private int count = 1024 * 1024; // 512 kb
    private boolean isPlay = true;
    private boolean isLooping = false;
    private static Handler mHandler;

    public void prepare(String pathAudio){
        this.pathAudio = pathAudio;
        mHandler = new Handler();
    }

    public void play(){
        stop();

        isPlay = true;
        bytesread = 0;
        ret = 0;
        if (pathAudio == null)
            return;

        audioPlayer = createAudioPlayer();
        if (audioPlayer == null) return;
        audioPlayer.play();

        mThread = new Thread(new PlayerProcess());
        mThread.start();
        Log.i("PCMPlayer", "play!");
    }

    private final Runnable mLopingRunnable = new Runnable() {
        @Override
        public void run() {
            play();
        }
    };

    private AudioTrack createAudioPlayer(){
        int intSize = android.media.AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT)*2;
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, intSize, AudioTrack.MODE_STREAM);
        if (audioTrack == null) {
            Log.d("TCAudio", "audio track is not initialised ");
            return null;
        }

        File file = null;
        file = new File(pathAudio);

        byteData = new byte[(int) count];
        try {
            in = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        size = (int) file.length();
        return  audioTrack;
    }

    private class PlayerProcess implements Runnable{

        @Override
        public void run() {
            while (bytesread < size && isPlay) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                try {
                    ret = in.read(byteData, 0, count);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (ret != -1) { // Write the byte array to the track
                    audioPlayer.write(byteData,0, ret);
                    bytesread += ret;
                } else break;
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (audioPlayer!=null){
                if (audioPlayer.getState()!=AudioTrack.PLAYSTATE_STOPPED){
                    audioPlayer.stop();
                    audioPlayer.release();
                    mThread = null;
                }
            }
            if (isLooping && isPlay ) mHandler.postDelayed(mLopingRunnable,100);
        }
    }

//    public void setLooping(){
//        isLooping = !isLooping;
//    }
//
//    public void pause(){
//
//    }

    public void stop(){
        isPlay = false;
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
    }


    //melody change
    public boolean isPlaying(){
        return isPlay;
    }
    public void pause(){
        isPlay = false;
        if (mThread != null) {
            mThread.interrupt(); //need to check the time state of pause
        }
        if (audioPlayer != null) {
            audioPlayer.pause();
        }
    }
}
