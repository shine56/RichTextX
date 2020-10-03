package com.shine56.richtextx.api

import android.text.Html

interface HtmlTextX {
    /**
     * 设置Html文本，它包含两个参数：第一个参数是需要显示的html文本，
     * 如果该html文本包含图片需要将Html.ImageGetter对象作为第二个参数传入，如果html文本中不包含图片则传入null
     * @param htmlText String
     * @param imageGetter ImageGetter
     */
    fun setTextFromHtml(htmlText: String, imageGetter: Html.ImageGetter?): PhotoBuilder
}