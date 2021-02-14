package com.shine56.richtextx.api

import com.shine56.richtextx.image.Image

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
     * 为光标所在行设置删除线，如果已经存在则去除。返回转换状态，true表示设置，false表示去除
     * @return Boolean
     */
    fun switchDeleteLineOnThisLine(): Boolean

    /**
     * 为光标所在行设置颜色，如果已经存在则去除。返回转换状态，true表示设置，false表示去除
     * @param color Int
     * @return Boolean
     */
    fun switchTextColorOnThisLine(color: Int): Boolean

    /**
     * 为光标所转换成列表行，如果已经存在则去除。返回转换状态，true表示设置，false表示去除
     * @return Boolean
     */
    fun switchToListOnThisLine(): Boolean

    /**
     * 对光标所在位置插入缩进符号
     */
    fun indent()

    /**
     * 回退
     * @return Boolean 返回回退是否成功
     */
    fun revoke(): Boolean
}