package com.example.autoirrigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoirrigation.Tools.DBUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";  //For Debug
    private EditText uphone;
    private EditText password;
    private Button login;
    private TextView regbtn;
    private SharedPreferences sp;
    private DBUtil dbUtil;
    private ImageView psdvisible;
    private int clicktimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //BaseTool.setStatusTransparent(this.getWindow());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //使用StrictMode, 阻止抛出 NetworkOnMainThreadException，SDK3以上不允许在主线程中执行网络操作
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
        //uname.setText("njfu");
        //password.setText("123456");
    }

    public void init(){
        uphone = findViewById(R.id.userphone);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        regbtn = findViewById(R.id.regbtn);
        psdvisible = findViewById(R.id.psdvisible);
        clicktimes=0;
        dbUtil = new DBUtil();
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);

        uphone.setText(sp.getString("uphone", null));
        password.setText(sp.getString("password", null));

        //register Listener
        login.setOnClickListener(this);
        regbtn.setOnClickListener(this);
        psdvisible.setOnClickListener(this);
    }

    //Override onclick()
    //@Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:
                String phone = uphone.getText().toString();
                String pwd = password.getText().toString();
                String name;
                if(phone.trim().equals("")){
                    Toast.makeText(this, "请您输入手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwd.trim().equals("")){
                    Toast.makeText(this, "请您输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }

                name = dbUtil.CheckUser(phone.trim(), pwd.trim());
                if(!name.equals("0")){        //账号密码正确
                    //将登录账户信息保存在SharedPreferences中
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("uphone", phone);
                    editor.putString("password", pwd);
                    editor.apply();

                    Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();

                    //跳转到操作页面
                    Intent intent = new Intent(this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("uname",name);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    //结束LoginActivity，使其onDestroy(), 而不是onStop()
                    finish();
                }
                else{       //账号密码错误
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("uphone", null);
                    editor.putString("password", null);
                    editor.putBoolean("rememberPwd", false);
                    editor.apply();
                    Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.regbtn:
                Toast.makeText(this, "注册", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.psdvisible:
                clicktimes++;
                if(clicktimes%2==1){
                    psdvisible.setImageResource(R.drawable.visible);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    psdvisible.setImageResource(R.drawable.invisible);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            default:
                break;
        }
    }
}


