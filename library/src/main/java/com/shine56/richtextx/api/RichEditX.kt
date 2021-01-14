package com.shine56.richtextx.api

import android.graphics.drawable.Drawable
import com.shine56.richtextx.bean.Image

/**
 * 这是一个包含所有富文本操作方法地接口类
 */
interface RichEditX {

    /**
     * 插入图片
     * @param imgUrl String
     * @param drawableGet DrawableGet
     */
    fun insertPhoto(image: Image)

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
     * 为光标所在行设置删除线
     */
    fun setDeleteLineOnThisLine()
    fun removeDeleteLineOnThisLine()

    /**
     * 为光标所在行设置文本颜色
     * @param color Int
     */
    fun setTextColorOnThisLine(color: Int)
    fun removeTextColorOnThisLine()

    /**
     * 对光标所在位置插入缩进符号
     */
    fun indent()
}