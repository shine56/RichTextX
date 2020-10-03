package com.shine56.richtextx.api

import android.graphics.drawable.Drawable

/**
 * 这是一个包含所有富文本操作方法地接口类
 */
interface RichEditX {

    /**
     * 插入图片
     * @param imgUrl String
     * @param drawableGet DrawableGet
     * @return InsertPhotoBuilder
     */
    fun insertPhoto(imgUrl: String, drawableGet: DrawableGet): PhotoBuilder
   // fun insertPhoto(imgUrl: String, block: (imgUrl: String) -> Drawable): PhotoBuilder

    /**
     * 设置字体为粗体
     * @param isBold Boolean
     */
    fun setBold(isBold: Boolean)
    fun getBold(): Boolean

    /**
     * 设置字号
     * @param fontSize Int
     */
    fun setFontSize(fontSize: Int)
    fun getFontSize(): Int


    /**
     * 对光标所在位置插入缩进符号
     */
    fun indent()
}