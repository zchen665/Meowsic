package com.example.meowsic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recordingButton = (Button) findViewById(R.id.home_new_timbre);
        Button playListButton = (Button) findViewById(R.id.home_playlist);
        Button keyBoardButoon = (Button) findViewById(R.id.home_keyboard);

        recordingButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toNewRecording();
            }
        });
        playListButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toPlayList();
            }
        });
        keyBoardButoon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toKeyboard();
            }
        });
    }



    public void toNewRecording(){
        Intent intent = new Intent(this, NewRecording.class);

        startActivity(intent);
    }
    public void toPlayList(){
        Intent intent = new Intent(this, PlayList.class);

        startActivity(intent);
    }
    public void toKeyboard(){
        Intent intent = new Intent(this, Keyboard.class);

        startActivity(intent);
    }
}