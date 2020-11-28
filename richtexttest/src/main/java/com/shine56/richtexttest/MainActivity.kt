package com.shine56.richtexttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.toHtml
import com.shine56.richtextx.bean.Image
import com.shine56.richtextx.view.RichEditText
import com.shine56.richtextx.view.RichTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addImgBt = findViewById<Button>(R.id.add_img)
        val editText = findViewById<RichEditText>(R.id.edit)

        var i = 0
        var images = arrayListOf<Image>()

        addImgBt.setOnClickListener {
            i++
            val image = editText.imageBuilder
                .setImageUrl("图片$i")
                .setOnImageCLickListener { view, imgUrl ->
                    Log.d("调试", "点击$imgUrl, "+this.javaClass)
                    showToast("点击$imgUrl")
                }
                .setOnImageDeleteListener { view, imgUrl ->
                    showToast("删除$imgUrl")
                }
                .setDrawableGet {
                    resources.getDrawable(R.drawable.test)
                }
                .create()
            images.add(image)
            editText.insertPhoto(image)
        }

        findViewById<Button>(R.id.tem).setOnClickListener {
            val text = Html.toHtml(editText.editableText)
            val tv = findViewById<RichTextView>(R.id.tv)
            val et = findViewById<RichEditText>(R.id.edit2)

            val image = tv.imageBuilder
                .setOnImageCLickListener { view, imgUrl ->
                    Log.d("调试", "点击$imgUrl, "+this.javaClass)
                    showToast("点击$imgUrl")
                }
                .setOnImageDeleteListener { view, imgUrl ->
                    showToast("删除$imgUrl")
                }
                .setDrawableGet {
                    Log.d("调试", "路径"+it)
                    resources.getDrawable(R.drawable.test)
                }
                .create()

            tv.setTextFromHtml(text,image)
            et.setTextFromHtml(text, image)
        }
    }

    private fun showToast(str: String){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}
