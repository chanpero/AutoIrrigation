package com.example.autoirrigation.DrawerActivities.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.autoirrigation.LoginActivity;
import com.example.autoirrigation.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private TextView changepwd,exitApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }
    public void init() {
        mToolbar = findViewById(R.id.setting_toolbar);
        exitApp = findViewById(R.id.exit_app);
        changepwd = findViewById(R.id.changepwd);
        exitApp.setOnClickListener(this);
        changepwd.setOnClickListener(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("hahaha");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.changepwd:
                Intent intent = getIntent();
                String pwd = intent.getStringExtra("upwd");
                String id = intent.getStringExtra("uid");
                Intent intent1 = new Intent(this,ChangePwdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("upwd",pwd);
                bundle.putString("uid",id);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.exit_app:
                startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }
}
