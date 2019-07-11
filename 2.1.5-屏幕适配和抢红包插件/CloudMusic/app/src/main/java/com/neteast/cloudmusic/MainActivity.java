package com.neteast.cloudmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.neteast.cloudmusic.ui.UIUtils;
import com.neteast.cloudmusic.ui.ViewCalculateUtil;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = findViewById(R.id.text);
        UIUtils.getInstance(this);
        ViewCalculateUtil.setViewRelativeLayoutParam(text, 150, 30, 0, 0, 0, 0, true);
    }
}
