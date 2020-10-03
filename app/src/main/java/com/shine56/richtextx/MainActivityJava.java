package com.shine56.richtextx;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.shine56.richtextx.api.DrawableGet;
import com.shine56.richtextx.api.ImageClick;
import com.shine56.richtextx.api.ImageDelete;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);

        RichEditText richEditText = findViewById(R.id.rich_edit_text);
        //getResources().getDrawable(R.drawable.photo);
    }
}