package com.shine56.richtextx;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shine56.richtextx.api.DrawableGet;
import com.shine56.richtextx.api.ImageClick;
import com.shine56.richtextx.api.ImageDelete;

public class MainActivityJava extends AppCompatActivity {

    RichEditText richEditText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);

        richEditText = findViewById(R.id.rich_edit_text);
        //

        Button insertPhotoBt = findViewById(R.id.insert_photo);
        Button indentBt = findViewById(R.id.indent);
        Button fontSizeBt = findViewById(R.id.font_size);
        Button boldBt = findViewById(R.id.bold);

        insertPhotoBt.setOnClickListener(view -> insertPhoto());

        indentBt.setOnClickListener(view -> indent());

        fontSizeBt.setOnClickListener(view -> fontSize());

        boldBt.setOnClickListener(view -> bold());
    }

    private void insertPhoto(){
        richEditText.insertPhoto("R.drawable.photo", (DrawableGet) imgUrl ->
                getResources().getDrawable(R.drawable.photo)
        ).setOnCLickListener((ImageClick) (view, imgUrl) ->
                Toast.makeText(this, "点击$l1", Toast.LENGTH_SHORT).show()
        ).setOnDeleteListener((ImageDelete) (view, imgUrl) ->
                Toast.makeText(this, "删除$l1", Toast.LENGTH_SHORT).show()
        ).apply();
    }
    private void bold(){
        richEditText.setBold(!richEditText.getBold());
    }

    private void fontSize(){
        richEditText.setFontSize(28);
    }

    private void indent(){
        richEditText.indent();
    }
}