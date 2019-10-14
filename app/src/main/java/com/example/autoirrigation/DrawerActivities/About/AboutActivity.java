package com.example.autoirrigation.DrawerActivities.About;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.autoirrigation.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private LinearLayout openSourceLayout,checkUpdateLayout;
    private GetVersion version1 = new GetVersion();
    private PermisionUtils permisionUtils1 = new PermisionUtils();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mToolbar = findViewById(R.id.about_toolbar);
        checkUpdateLayout = findViewById(R.id.checkUpdateLayout);
        openSourceLayout = findViewById(R.id.opensourceLayout);
        checkUpdateLayout.setOnClickListener(this);
        openSourceLayout.setOnClickListener(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("关于");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkUpdateLayout:
                int newcode = version1.getUpdatecode();
                int oldcode = version1.getVersionCode(this);
                if(newcode>oldcode) {
                    permisionUtils1.verifyStoragePermissions(this);
                    version1.loadNewVersionAlertDiaLog(this);
                }else{
                    Toast.makeText(this,"当前已是最新版本",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.opensourceLayout:
                startActivity(new Intent(AboutActivity.this, OpenSourceActivity.class));
                break;
        }
    }

}
