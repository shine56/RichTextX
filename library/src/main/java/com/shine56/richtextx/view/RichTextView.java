package com.shine56.richtextx.view;


import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.shine56.richtextx.RichTextXMovementMethod;
import com.shine56.richtextx.RtTagHandler;
import com.shine56.richtextx.api.HtmlTextX;
import com.shine56.richtextx.api.ImageBuilder;
import com.shine56.richtextx.util.ImageBuilderImpl;
import com.shine56.richtextx.bean.Image;

public class RichTextView extends AppCompatTextView implements HtmlTextX {

    /**
     * 构建富文本图片属性
     */
    private ImageBuilder imageBuilder;

    /**
     * 初始化
     */
    private void init(){
        setMovementMethod(RichTextXMovementMethod.Companion.getINSTANCE());
        imageBuilder = new ImageBuilderImpl();
    }

    @Override
    public void setTextFromHtml(String htmlText, Image image) {
        String customText;
        RtTagHandler tagHandler = new RtTagHandler(image);

        customText = htmlText.replace("span", mySpan);
        customText = customText.replace("img", myImg);

        setText(Html.fromHtml(customText, null, tagHandler));
    }

    @Override
    public ImageBuilder getImageBuilder() {
        return imageBuilder;
    }

    public RichTextView(Context context) {
        super(context);
        init();
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
