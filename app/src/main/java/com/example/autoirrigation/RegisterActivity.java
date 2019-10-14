package com.example.autoirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.autoirrigation.Tools.DBUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password1;
    private EditText password2;
    private EditText phonenum;
    private EditText invitecode;
    private ImageView psdvisible1;
    private ImageView psdvisible2;
    private DBUtil dbUtil;
    private Button register;
    private int clicktimes1;
    private int clicktimes2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        register.setOnClickListener(this);
        psdvisible1.setOnClickListener(this);
        psdvisible2.setOnClickListener(this);
    }

    public void init() {
        username = findViewById(R.id.username);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        psdvisible1 = findViewById(R.id.psdvisible1);
        psdvisible2 = findViewById(R.id.psdvisible2);
        invitecode = findViewById(R.id.invitecode);
        register = findViewById(R.id.register);
        phonenum = findViewById(R.id.phonenum);
        clicktimes1 = clicktimes2 = 0;
        dbUtil = new DBUtil();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                String name = username.getText().toString();
                String pwd1 = password1.getText().toString();
                String pwd2 = password2.getText().toString();
                String phone = phonenum.getText().toString();
                String icode = invitecode.getText().toString();

                String nameregex = "^[a-zA-Z][a-zA-Z0-9]{5,9}$";
                String pwdregex = "^[a-zA-Z0-9@\\$\\^\\.\\*\\\\?]{6,16}$";
                String phoneregex = "^([1][3,4,5,6,7,8,9])\\d{9}$";

                Pattern patternphone = Pattern.compile(phoneregex);
                Matcher matcherphone = patternphone.matcher(phone.trim());
                if (phone.trim().equals("")) {
                    Toast.makeText(this, "请您输入手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matcherphone.matches()) {
                    Toast.makeText(this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!dbUtil.QueryUser(phone).equals("0")) {
                    Toast.makeText(this, "该用户已存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!icode.equals("njfucs123456")) {
                    Toast.makeText(this, "您不是邀请用户", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (name.trim().equals("")) {
                    Toast.makeText(this, "请您输入用户名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern patternname = Pattern.compile(nameregex);
                Matcher matchername = patternname.matcher(name.trim());
                if (!matchername.matches()) {
                    Toast.makeText(this, "用户名6~10位字母或数字且字母开头！", Toast.LENGTH_SHORT).show();
                    return;
                }


                Pattern patternpwd = Pattern.compile(pwdregex);
                Matcher matcherpwd = patternpwd.matcher(pwd1.trim());
                if (pwd1.trim().equals("")) {
                    Toast.makeText(this, "请您输入密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matcherpwd.matches()) {
                    Toast.makeText(this, "密码6~16位字母数字或字符！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd2.trim().equals("")) {
                    Toast.makeText(this, "请您确认密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd1.trim().equals(pwd2.trim())) {
                    Toast.makeText(this, "两次密码输入不相同！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbUtil.InsertUser(phone, name, pwd1, "njfucs123456")) {
                    Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } break;

        }

    }
}
