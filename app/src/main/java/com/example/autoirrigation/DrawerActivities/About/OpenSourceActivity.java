package com.example.autoirrigation.DrawerActivities.About;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.autoirrigation.R;

import java.util.ArrayList;
import java.util.List;

public class OpenSourceActivity extends AppCompatActivity {
    private List<String> openSourceList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source);

        openSourceList = new ArrayList<>();
        listView = findViewById(R.id.opensource_listview);
        openSourceList.add("de.hdodenhof:circleimageview:3.0.0");
        openSourceList.add("com.github.mcxtzhang:SwipeDelMenuLayout:V1.2.1");
        openSourceList.add("jp.wasabeef:recyclerview-animators:2.2.7");
        openSourceList.add("com.github.arcadefire:nice-spinner:1.4.3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, openSourceList);
        listView.setAdapter(adapter);
    }
}
