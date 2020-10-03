package com.shine56.richtextx

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.shine56.richtextx.api.DrawableGet
import com.shine56.richtextx.api.ImageClick
import com.shine56.richtextx.api.ImageDelete

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val richEditText = findViewById<RichEditText>(R.id.rich_edit_text)

        richEditText.insertPhoto("mk", DrawableGet {
            val drawable = resources.getDrawable(R.drawable.photo)
            val rect = Rect(0, 0, 200, 300)
            drawable.bounds = rect
            drawable
        }).setOnDeleteListener(ImageDelete{l, l2 ->
            Log.d("sjwd", "onCreate: 被删除")
        }).setOnCLickListener(ImageClick { view, imgUrl ->
            Log.d("sjwd", "onCreate: 被点击")
        }).apply()
    }

}