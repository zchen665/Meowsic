package com.example.meowsic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewRecording extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recording);

        TextView textView2 = findViewById(R.id.textView2);
        Intent intent = getIntent();
        textView2.setText("Recording Screen");

        Button checkRecording = (Button) findViewById(R.id.button);

        checkRecording.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v){
            toCheckRecording();
        }
    });
}

    public void toCheckRecording(){
        Intent intent = new Intent(this, CheckRecording.class);

        startActivity(intent);
    }
}
