package com.example.meowsic;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayList extends ListActivity {

    ListView listView;
    String currentSong;
    MediaPlayer song=null;
    HashMap<String, String> namePath = new HashMap<String, String>();//fileName - path

    public static final int REQUEST_STORAGE_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        if (checkPermissions()){
            createList();
        }else{
            requestPermissions();
        }
    }
    public void createList(){
        listView = getListView();
        ArrayList<String> nameList = new ArrayList<>();
        //1, create the list based on the songs in the storage
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(); // add directory name "meowisc?"
        Log.d("Files", "Path: " + path);
        File dir = new File(path, "Meowsic");
        if (!dir.exists()) {
            dir.mkdir();
            Log.i("files", "Meowsic is created");
        }
        path = dir.getPath();
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            nameList.add(files[i].getName());
            namePath.put(files[i].getName(),files[i].getAbsolutePath());
        }

        // load file name as listItem
        nameList.add("file1.mp3");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.playlist_item,nameList);
        listView.setAdapter(arrayAdapter);
    }

    public void returnMain(View view){
        if(song!=null){
            song.stop();
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void playThatSong(View view){
        currentSong = ( (TextView) view ).getText().toString();
        Toast tip = Toast.makeText(this, currentSong + " is playing", Toast.LENGTH_SHORT);
        tip.show();
        song = MediaPlayer.create(this, R.raw.song);
        // to play the audio from device's external storage
        // song = MediaPlayer.create(this, Uri.parse(namePath.get(currentSong));
        song.setVolume(1,1);
        song.start();
    }
    public void play(View view){
        if(song!=null){
            song.start();
        }
        else{
            Toast tip = Toast.makeText(this, "select a song", Toast.LENGTH_SHORT);
            tip.show();
        }
    }
    public void pauseThatSong(View view){
        if(song!=null){
            if(song.isPlaying()){
                song.pause();
            }
            else{
                Toast tip = Toast.makeText(this, "no song is playing", Toast.LENGTH_SHORT);
                tip.show();
            }
        }
        else{
            Toast tip = Toast.makeText(this, "select a song", Toast.LENGTH_SHORT);
            tip.show();
        }
    }
    public void shareThatSong(View view){
        // reference: https://stackoverflow.com/questions/13065838/what-are-the-possible-intent-types-for-intent-settypetype
        if(currentSong != null){
            Log.i("inshare","hi");
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, R.raw.song);
            //shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(namePath.get(currentSong)));
            shareIntent.setType("audio/*");
            startActivity(Intent.createChooser(shareIntent, "Share the fantastic song!"));

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        createList();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }
    }

    public boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(PlayList.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION_CODE);
    }
}
