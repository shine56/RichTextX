package com.shine56.richtextx
//
//import android.text.*
//import android.text.style.AbsoluteSizeSpan
//import android.view.View
//import android.widget.EditText
//import com.shine56.richtextx.api.HtmlTextX
//import com.shine56.richtextx.api.ImageClick
//import com.shine56.richtextx.api.ImageDelete
//import com.shine56.richtextx.bean.Image
//import org.xml.sax.XMLReader
//import java.util.HashMap
//
//class RichTextXTagHandler(private val image: Image) : Html.TagHandler {
//
//    private val attributes: HashMap<String, String> = HashMap()
//    private var startIndex = 0
//    private var stopIndex = 0
//
//    override fun handleTag(
//        opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader
//    ) {
//        processAttributes(xmlReader)
//        if (tag == HtmlTextX.mySpan || tag == HtmlTextX.myImg) {
//            if (opening) {
//                startSpan(tag, output)
//            } else {
//                endSpan(output)
//                attributes.clear()
//            }
//        }
//    }
//
//    private fun startSpan(tag: String, output: Editable) {
//        startIndex = output.length
//        if (tag.equals(HtmlTextX.myImg, ignoreCase = true) && image.drawableGet!= null) {
//            imageGetter?.let { startImg(output, it) }
//        }
//    }
//
//    private fun endSpan( output: Editable) {
//        stopIndex = output.length
//        var size = attributes["size"]
//        val style = attributes["style"]
//        if (!TextUtils.isEmpty(style)) {
//            analysisStyle(startIndex, stopIndex, output, style)
//        }
//        if (!TextUtils.isEmpty(size)) {
//            size = size!!.split("px".toRegex()).toTypedArray()[0]
//        }
//        if (!TextUtils.isEmpty(size)) {
//            val fontSize = size!!.toInt()
//
//            val absoluteSizeSpan = AbsoluteSizeSpan(fontSize, true)
//
//            output.setSpan(
//                absoluteSizeSpan,
//                startIndex,
//                stopIndex,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        }
//    }
//
//    private fun startImg(text: Editable, imageGetter: Html.ImageGetter) {
//        val src = attributes["src"]
//
//        val d = imageGetter.getDrawable(src)
//        val imageSpan = ClickableImageSpan(d, src?:"null")
//
//        val spannableString = SpannableString(src)
//        spannableString.setSpan(
//            imageSpan,
//            0,
//            spannableString.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        text.append(spannableString)
//
//        onImageClick?.let {
//            imageSpan.setOnCLickListener (it)
//        }
//
//        if(isEdit){
//            onImageDelete?.let { delete ->
//                imageSpan.setOnDeleteListener(ImageDelete { view, url ->
//                    val start = text.getSpanStart(imageSpan)
//                    val end = text.getSpanEnd(imageSpan)
//                    text.delete(start, end)
//                    delete.onDelete(view, url)
//                })
//            }
//        }
//
//    }
//
//    private fun processAttributes(xmlReader: XMLReader) {
//        try {
//            val elementField =
//                xmlReader.javaClass.getDeclaredField("theNewElement")
//            elementField.isAccessible = true
//            val element = elementField[xmlReader]
//            val attsField = element.javaClass.getDeclaredField("theAtts")
//            attsField.isAccessible = true
//            val atts = attsField[element]
//            val dataField = atts.javaClass.getDeclaredField("data")
//            dataField.isAccessible = true
//            val data =
//                dataField[atts] as Array<String>
//            val lengthField = atts.javaClass.getDeclaredField("length")
//            lengthField.isAccessible = true
//            val len = lengthField[atts] as Int
//            /**
//             * MSH: Look for supported attributes and add to hash map.
//             * This is as tight as things can get :)
//             * The data index is "just" where the keys and values are stored.
//             */
//            for (i in 0 until len) attributes[data[i * 5 + 1]] = data[i * 5 + 4]
//        } catch (e: Exception) {
//        }
//    }
//
//    /**
//     * 解析style属性
//     * @param startIndex
//     * @param stopIndex
//     * @param editable
//     * @param style
//     */
//    private fun analysisStyle(
//        startIndex: Int,
//        stopIndex: Int,
//        editable: Editable,
//        style: String?
//    ) {
//        val attrArray = style?.split(";".toRegex())?.toTypedArray()
//        val attrMap: MutableMap<String, String> =
//            HashMap()
//        if (null != attrArray) {
//            for (attr in attrArray) {
//                val keyValueArray = attr.split(":".toRegex()).toTypedArray()
//                if (keyValueArray.size == 2) {
//                    // 去除前后空格
//                    attrMap[keyValueArray[0].trim { it <= ' ' }] =
//                        keyValueArray[1].trim { it <= ' ' }
//                }
//            }
//        }
//
//        var fontSize = attrMap["font-size"]
//        if (!TextUtils.isEmpty(fontSize)) {
//            fontSize = fontSize!!.split("px".toRegex()).toTypedArray()[0]
//        }
//        if (!TextUtils.isEmpty(fontSize)) {
//            val fontSizeDp = fontSize!!.toInt()
//
//            val absoluteSizeSpan = AbsoluteSizeSpan(fontSizeDp, true)
//
//            editable.setSpan(
//                absoluteSizeSpan,
//                startIndex,
//                stopIndex,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        }
//    }
//
//}