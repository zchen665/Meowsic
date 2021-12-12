package com.example.meowsic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheckRecording extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_recording);

        Button ret = (Button) findViewById(R.id.button5);

        ret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBack();
            }
        });
    }

    public void goBack() {
        Intent intent = new Intent(this, NewRecording.class);
        startActivity(intent);
    }


}
