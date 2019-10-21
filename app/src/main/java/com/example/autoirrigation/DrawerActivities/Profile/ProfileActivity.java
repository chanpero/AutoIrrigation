package com.example.autoirrigation.DrawerActivities.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.autoirrigation.R;
import com.example.autoirrigation.Tools.DBUtil;


public class ProfileActivity extends AppCompatActivity{
    private SharedPreferences sp;
    private TextView username;
    private TextView userphone;
    private TextView deviceid;
    private DBUtil dbUtil;
    private Toolbar mToolbar;

    private ImageView userlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("我的");
        init();
    }
    void init(){
         sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
         username = findViewById(R.id.user_name);
         userphone = findViewById(R.id.user_phone);
         deviceid = findViewById(R.id.device_id);
         userlogo = findViewById(R.id.user_logo);
         dbUtil = new DBUtil();

         String userid = sp.getString("uphone",null);
         username.setText(dbUtil.getUser(userid,"njfucs123456"));
         userphone.setText(userid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


}
