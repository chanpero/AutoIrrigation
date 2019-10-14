package com.example.autoirrigation.DrawerActivities.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoirrigation.LoginActivity;
import com.example.autoirrigation.R;
import com.example.autoirrigation.Tools.DBUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePwdActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private Button changebtn;
    private TextView oldpwd, newpwd1, newpwd2;
    private DBUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        mToolbar = findViewById(R.id.changepwd_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("修改密码");

        init();

    }
    public void init(){
        changebtn = findViewById(R.id.changebtn);
        oldpwd = findViewById(R.id.oldpwd);
        newpwd1 = findViewById(R.id.newpwd1);
        newpwd2 = findViewById(R.id.newpwd2);
        dbUtil = new DBUtil();
        changebtn.setOnClickListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changebtn:
                Intent intent = getIntent();
                String pwd = intent.getStringExtra("upwd");
                String id = intent.getStringExtra("uid");
                String opwd = oldpwd.getText().toString();
                String npwd1 = newpwd1.getText().toString();
                String npwd2 = newpwd2.getText().toString();
                if(opwd.equals(pwd)){
                    String pwdregex = "^[a-zA-Z0-9@\\$\\^\\.\\*\\\\?]{6,16}$";
                    Pattern patternpwd = Pattern.compile(pwdregex);
                    Matcher matcherpwd = patternpwd.matcher(npwd1.trim());
                    if (!matcherpwd.matches()) {
                        Toast.makeText(this, "密码6~16位字母数字或字符！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!npwd1.equals(npwd2)) {
                        Toast.makeText(this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(dbUtil.ChangePwd(id,opwd,npwd1,"njfucs123456")){
                        Toast.makeText(this,"修改密码成功",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        return;
                    }else{
                        Toast.makeText(this,"修改密码失败",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    Toast.makeText(this,"输入旧密码错误",Toast.LENGTH_SHORT).show();
                    return;
                }
        }
    }
}
