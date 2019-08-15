package com.example.autoirrigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";  //For Debug

    private EditText uname;
    private EditText password;
    private CheckBox rememberPwd;
    private Button login;
    private Button register;
    private SharedPreferences sp;
    private DBUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseTool.setStatusTransparent(this.getWindow());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //使用StrictMode, 阻止抛出 NetworkOnMainThreadException，SDK3以上不允许在主线程中执行网络操作
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
        uname.setText("njfu");
        password.setText("123456");
    }

    public void init(){
        uname = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rememberPwd = findViewById(R.id.rememberPwd_checkbox);
        login = findViewById(R.id.login);
        register = findViewById(R.id.reg_button);
        dbUtil = new DBUtil();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);

        //if remember_pwd stored in sharedPreferences
        if(sp.getBoolean("rememberPwd", false)){
            uname.setText(sp.getString("uname", null));
            password.setText(sp.getString("password", null));
            rememberPwd.setChecked(true);
        }

        //register Listener
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    //Override onclick()
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:
                String name = uname.getText().toString();
                String pwd = password.getText().toString();
                if(name.trim().equals("")){
                    Toast.makeText(this, "请您输入用户名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.trim().equals("")){
                    Toast.makeText(this, "请您输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String ID = dbUtil.CheckUser(name.trim(), pwd.trim());



//                if(name.equals("njfu") && pwd.equals("123456")){   //For test
//                    Intent intent = new Intent(this, OperateActivity.class);
//                    startActivity(intent);
//                }

                if(!ID.equals("0")){        //账号密码正确
                    //将登录账户信息保存在SharedPreferences中
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("uname", name);
                    editor.putString("password", pwd);
                    if(rememberPwd.isChecked())
                        editor.putBoolean("rememberPwd", true);
                    else
                        editor.putBoolean("rememberPwd", false);
                    editor.apply();

                    Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();

                    //跳转到操作页面
                    Intent intent = new Intent(this, OperateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("uname", name);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    //结束LoginActivity，使其onDestroy(), 而不是onStop()
                    finish();
                }
                else{       //账号密码错误
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("uname", null);
                    editor.putString("password", null);
                    editor.putBoolean("rememberPwd", false);
                    editor.apply();
                    Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.reg_button:
                Log.d(TAG, "onClick: R.id.reg_button");
                //register
                /**
                 *  .....
                 */
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;
        }
    }
}


