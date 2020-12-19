package com.shine56.richtextx.util

import android.text.*
import android.text.Html.TagHandler
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.shine56.richtextx.api.DrawableGet
import com.shine56.richtextx.api.HtmlTextX
import com.shine56.richtextx.api.HtmlTextX.TAG
import com.shine56.richtextx.api.ImageClick
import com.shine56.richtextx.api.ImageDelete
import com.shine56.richtextx.bean.Image
import com.shine56.richtextx.view.ClickableImageSpan
import kotlinx.coroutines.*
import org.xml.sax.XMLReader
import java.util.*
import kotlin.collections.HashMap

/**
 * 自定义解析，img 和 fontSize
 */
class RtTagHandler(private val image: Image,
                   private val editText: EditText,
                   private val isEditable: Boolean) :
    TagHandler {
    private val attributes = HashMap<String, String?>()
    private var startIndex = 0
    private var stopIndex = 0
    
    private val arrInsert = arrayListOf<Insert>()

    override fun handleTag(
        opening: Boolean,
        tag: String,
        output: Editable,
        xmlReader: XMLReader
    ) {
        processAttributes(xmlReader)
        if (tag === HtmlTextX.mySpan || tag === HtmlTextX.myImg) {
            if (opening) {
                startSpan(tag, output)
            } else {
                endSpan(output)
                attributes.clear()
            }
        }
    }

    private fun startSpan(tag: String, output: Editable) {
        startIndex = output.length
        if (tag.equals(
                HtmlTextX.myImg,
                ignoreCase = true
            ) && image != null && image.drawableGet != null
        ) {
            startImg(output, image.drawableGet)
        }
    }

    private fun endSpan(output: Editable) {
        stopIndex = output.length
        var size = attributes["size"]
        val style = attributes["style"]
        if (!TextUtils.isEmpty(style)) {
            analysisStyle(startIndex, stopIndex, output, style)
        }
        if (!TextUtils.isEmpty(size)) {
            size = size!!.split("px".toRegex()).toTypedArray()[0]
        }
        if (!TextUtils.isEmpty(size)) {
            val fontSize = Integer.valueOf(size!!)
            val absoluteSizeSpan = AbsoluteSizeSpan(fontSize, true)
            output.setSpan(
                absoluteSizeSpan,
                startIndex,
                stopIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun startImg(text: Editable, drawableGet: DrawableGet) {

        //图片+1
        val src = attributes["src"] ?: "null"
        val position: Int = arrInsert.size
        arrInsert.add(Insert(startIndex, src))

        //IO线程加载图片
        val scope = CoroutineUtil.getScope(editText.hashCode())
        val deferred = scope.async(Dispatchers.IO) {
            drawableGet.getDrawable(src)
        }

        //主线程更新UI
        scope.launch(Dispatchers.Main) {

            val drawable = deferred.await()

            //阻塞主线程，保持同一时刻只有一张图片在插入
            runBlocking{
                val width = drawable.intrinsicWidth
                val height = drawable.intrinsicHeight
                drawable.setBounds(0, 0, if (width > 0) width else 0, if (height > 0) height else 0)

                val imageSpan = ClickableImageSpan(drawable, src)
                val spannableString = SpannableString(src)
                spannableString.setSpan(
                    imageSpan,
                    0,
                    spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                //寻找插入位置
                var srcSum = arrInsert[position].startIndex //插入位置
                for (i in 0 until position){
                    if(arrInsert[i].isInsert){
                        srcSum += arrInsert[i].src.length
                    }
                }
                //插入图片
                editText.editableText.insert(srcSum, spannableString)
                arrInsert[position].isInsert = true
                //Log.d(TAG, "startImg: 第$position 张图片 起始位置：${arrInsert[position].startIndex} 插入位置 $srcSum, ")

                //点击事件
                if (image.click != null) {
                    if (isEditable) {
                        imageSpan.setOnCLickListener(ImageClick { view, imgUrl ->
                            //必须执行的逻辑 隐藏软键盘
                            editText.showSoftInputOnFocus = false
                            editText.isCursorVisible = false
                            //用户定义的逻辑
                            image.click.onClick(view, imgUrl)
                        })
                    } else {
                        imageSpan.setOnCLickListener(image.click)
                    }
                }

                //删除事件
                if (image.delete != null && isEditable) {
                    imageSpan.setOnDeleteListener(ImageDelete { view, imgUrl -> //必须执行的逻辑
                        val start = editText.text.getSpanStart(imageSpan)
                        val end = editText.text.getSpanEnd(imageSpan)
                        editText.text.delete(start, end)
                        editText.showSoftInputOnFocus = false

                        //用户定义的逻辑
                        image.delete.onDelete(view, imgUrl)
                    })
                }
            }
        }
    }

    private fun processAttributes(xmlReader: XMLReader) {
        try {
            val elementField =
                xmlReader.javaClass.getDeclaredField("theNewElement")
            elementField.isAccessible = true
            val element = elementField[xmlReader]
            val attsField = element.javaClass.getDeclaredField("theAtts")
            attsField.isAccessible = true
            val atts = attsField[element]
            val dataField = atts.javaClass.getDeclaredField("data")
            dataField.isAccessible = true
            val data =
                dataField[atts] as Array<String>
            val lengthField = atts.javaClass.getDeclaredField("length")
            lengthField.isAccessible = true
            val len = lengthField[atts] as Int
            /**
             * MSH: Look for supported attributes and add to hash map.
             * This is as tight as things can get :)
             * The data index is "just" where the keys and values are stored.
             */
            for (i in 0 until len) attributes[data[i * 5 + 1]] = data[i * 5 + 4]
        } catch (e: Exception) {
        }
    }

    /**
     * 解析style属性
     * @param startIndex
     * @param stopIndex
     * @param editable
     * @param style
     */
    private fun analysisStyle(
        startIndex: Int,
        stopIndex: Int,
        editable: Editable,
        style: String?
    ) {
        val attrArray = style!!.split(";".toRegex()).toTypedArray()
        val attrMap: MutableMap<String, String> =
            HashMap()
        if (null != attrArray) {
            for (attr in attrArray) {
                val keyValueArray =
                    attr.split(":".toRegex()).toTypedArray()
                if (null != keyValueArray && keyValueArray.size == 2) {
                    // 记住要去除前后空格
                    attrMap[keyValueArray[0].trim { it <= ' ' }] =
                        keyValueArray[1].trim { it <= ' ' }
                }
            }
        }
        var fontSize = attrMap["font-size"]
        if (!TextUtils.isEmpty(fontSize)) {
            fontSize = fontSize!!.split("px".toRegex()).toTypedArray()[0]
        }
        if (!TextUtils.isEmpty(fontSize)) {
            val fontSizeDp = Integer.valueOf(fontSize!!)
            val absoluteSizeSpan = AbsoluteSizeSpan(fontSizeDp, true)
            editable.setSpan(
                absoluteSizeSpan,
                startIndex,
                stopIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private inner class Insert(
        val startIndex: Int,
        val src: String,
        var isInsert: Boolean = false //记录图片是否已经加载
    )
    
}