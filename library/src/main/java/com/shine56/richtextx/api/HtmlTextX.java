package com.shine56.richtextx.api;

import com.shine56.richtextx.image.Image;
import com.shine56.richtextx.image.api.ImageBuilder;

public interface HtmlTextX {

    String mySpan = "myspan";
    String myImg = "myImg";

    /**
     * 设置Html文本，它包含两个参数：第一个参数是需要显示的html文本，
     * 如果该html文本包含图片需要将Image对象作为第二个参数传入，如果html文本中不包含图片则传入null
     * @param htmlText String
     * @param image Image
     */
    void setTextFromHtml(String htmlText, Image image);

    /**
     * 构建html文本中图片属性
     * @return
     */
    ImageBuilder getImageBuilder();

    /**
     * 获取html文本
     * @return
     */
    String getHtmlText();

}
