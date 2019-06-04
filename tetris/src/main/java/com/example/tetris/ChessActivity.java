package com.example.tetris;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChessActivity extends Activity {


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main); //LinearLayout里包裹了一个TextView
            TextView text = (TextView) findViewById(R.id.text);
            text.setText("象棋");
            LinearLayout linear = (LinearLayout) findViewById(R.id.linear);

            ChessView ccView = new ChessView(ChessActivity.this);   //自定义View --- 也可使用SurfaceView(需要循环draw) https://blog.csdn.net/android_cmos/article/details/68955134
            ccView.setText(text);
            linear.addView(ccView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

}
