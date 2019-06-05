package com.example.bomberman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LayoutKeyActivity extends AppCompatActivity {
    private Intent intent;
    static String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_key);
        final Button btn_sr = (Button) findViewById(R.id.btn_sr);
        final Button btn_mr = (Button) findViewById(R.id.btn_mr);


        final EditText sr = (EditText) findViewById(R.id.sr);
        btn_sr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_sr.getText().toString().equals("返回")) {
                    btn_mr.setText("默认key连接");
                    btn_sr.setText("输入key连接");
                    sr.setVisibility(View.GONE);
                } else {
                    btn_mr.setText("确定");
                    btn_sr.setText("返回");
                    sr.setVisibility(View.VISIBLE);
                }

            }
        });


        btn_mr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_mr.getText().toString().equals("确认")) {
                    key = sr.getText().toString().trim();
                    intent = new Intent(LayoutKeyActivity.this, ChatActivity.class);
                    intent.putExtra("key", key);
                    startActivity(intent);
                } else {
                    key = "dc8443ef16bf4c00ab44c16f352edc21";
                    intent = new Intent(LayoutKeyActivity.this, ChatActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
