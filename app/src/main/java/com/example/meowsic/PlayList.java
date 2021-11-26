package com.example.meowsic;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayList extends ListActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        listView = getListView();
        ArrayList<String> nameList = new ArrayList<>();
        // load file name as listItem
        nameList.add("file1.mp3");
        nameList.add("file2.mp3");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.playlist_item,nameList);
        listView.setAdapter(arrayAdapter);
    }
}
