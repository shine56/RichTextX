package com.shine56.richtextx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val richEditText = findViewById<RichEditText>(R.id.rich_edit_text)

//插入图片
        richEditText.insertPhoto("R.drawable.example"){
            //获取drawable逻辑
            val drawable = resources.getDrawable(R.drawable.photo)
            drawable
        }.apply()

    }
}