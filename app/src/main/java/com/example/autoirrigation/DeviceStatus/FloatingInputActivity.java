package com.example.autoirrigation.DeviceStatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.autoirrigation.R;

public class FloatingInputActivity extends AppCompatActivity {
    private Button cancelButton;
    private Button sumbitButton;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_input);

        initView();
    }

    private void initView(){
        cancelButton = findViewById(R.id.floating_input_cancelButton);
        sumbitButton = findViewById(R.id.floating_input_submitButton);
        editText = findViewById(R.id.floating_input_EditText);

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FloatingInputActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });

        sumbitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                Intent newIntent = new Intent();
                newIntent.putExtra("content", content);

                FloatingInputActivity.this.setResult(RESULT_OK, newIntent);
                finish();
            }
        });
    }
}
