package com.shine56.richtextx

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shine56.richtextx.api.DrawableGet
import com.shine56.richtextx.api.ImageClick
import com.shine56.richtextx.api.ImageDelete

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val richEditText = findViewById<RichEditText>(R.id.rich_edit_text)

        richEditText.insertPhoto("res/drawable/photo.jpg", DrawableGet {
            resources.getDrawable(R.drawable.photo)
        }).setOnCLickListener(ImageClick { l, l1 ->
            Toast.makeText(this, "点击图片1", Toast.LENGTH_SHORT).show()
        }).setOnDeleteListener(ImageDelete{l, l1 ->
            Toast.makeText(this, "删除图片1", Toast.LENGTH_SHORT).show()
        }).apply()

        richEditText.insertPhoto("res/drawable/photo.jpg", DrawableGet {
            resources.getDrawable(R.drawable.photo)
        }).setOnCLickListener(ImageClick { l, l1 ->
            Toast.makeText(this, "点击图片2", Toast.LENGTH_SHORT).show()
        }).setOnDeleteListener(ImageDelete{l, l1 ->
            Toast.makeText(this, "删除图片2", Toast.LENGTH_SHORT).show()
        }).apply()

//        richEditText.post {
//            Log.d("调试", "edit宽${richEditText.width}")
//        }
    }

}