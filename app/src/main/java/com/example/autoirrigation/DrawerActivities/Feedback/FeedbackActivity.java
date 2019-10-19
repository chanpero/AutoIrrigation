package com.example.autoirrigation.DrawerActivities.Feedback;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoirrigation.R;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private Button Feedbacksubmit;
    private TextView Feedbacktext;
    private TextView Feedbacktitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        init();
        Feedbacksubmit.setOnClickListener(this);
    }

    public void init() {
        mToolbar = findViewById(R.id.feedback_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("反馈建议");

        Feedbacksubmit = findViewById(R.id.feedback_submit);
        Feedbacktext = findViewById(R.id.feedback_text);
        Feedbacktitle = findViewById(R.id.feedback_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_submit:
                String title = Feedbacktitle.getText().toString();
                String text = Feedbacktext.getText().toString();
                if (title.trim().equals("")) {
                    Toast.makeText(FeedbackActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (text.trim().equals("")) {
                    Toast.makeText(FeedbackActivity.this, "请输入详细内容", Toast.LENGTH_SHORT).show();
                    break;
                }
                Uri uri = Uri.parse("mailto:qianchaosolo@gmail.com");
                String[] email = {"qianchaosolo@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT, title); // 主题
                intent.putExtra(Intent.EXTRA_TEXT, text); // 正文
                startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
