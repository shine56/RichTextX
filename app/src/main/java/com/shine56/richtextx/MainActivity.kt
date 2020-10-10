package com.shine56.richtextx

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shine56.richtextx.api.DrawableGet
import com.shine56.richtextx.api.ImageClick
import com.shine56.richtextx.api.ImageDelete

class MainActivity : AppCompatActivity() {

    private val richEditText by lazy {
        findViewById<RichEditText>(R.id.rich_edit_text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val insertPhotoBt = findViewById<Button>(R.id.insert_photo)
        val indentBt = findViewById<Button>(R.id.indent)
        val fontSizeBt = findViewById<Button>(R.id.font_size)
        val boldBt = findViewById<Button>(R.id.bold)

        insertPhotoBt.setOnClickListener {
            insertPhoto()
        }

        indentBt.setOnClickListener {
            indent()
        }

        fontSizeBt.setOnClickListener {
            fontSize()
        }

        boldBt.setOnClickListener {
            bold()
        }
    }

    private fun insertPhoto(){
        richEditText.insertPhoto("图片$i", DrawableGet {
            resources.getDrawable(R.drawable.photo)
        }).setOnCLickListener(ImageClick { l, l1 ->
            Toast.makeText(this, "点击$l1", Toast.LENGTH_SHORT).show()
        }).setOnDeleteListener(ImageDelete{l, l1 ->
            Toast.makeText(this, "删除$l1", Toast.LENGTH_SHORT).show()
        }).apply()
    }
    private fun bold(){
        richEditText.setBold(!richEditText.getBold())
    }

    private fun fontSize(){
        richEditText.setFontSize(28)
    }

    private fun indent(){
        richEditText.indent()
    }
}