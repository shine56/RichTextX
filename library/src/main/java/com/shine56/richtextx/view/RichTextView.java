package com.shine56.richtextx.view;


import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.shine56.richtextx.util.CoroutineUtil;
import com.shine56.richtextx.util.RichTextXMovementMethod;
import com.shine56.richtextx.api.HtmlTextX;
import com.shine56.richtextx.image.api.ImageBuilder;
import com.shine56.richtextx.image.ImageBuilderImpl;
import com.shine56.richtextx.image.Image;
import com.shine56.richtextx.util.RtTagHandler;

public class RichTextView extends AppCompatEditText implements HtmlTextX {

    /**
     * 构建富文本图片属性
     */
    private ImageBuilder imageBuilder;

    /**
     * 初始化
     */
    private void init(){
        setMovementMethod(RichTextXMovementMethod.Companion.getINSTANCE());

        setShowSoftInputOnFocus(false);
        setCursorVisible(false);
        setBackground(null);
        setLongClickable(false);

        imageBuilder = new ImageBuilderImpl();
    }

    @Override
    public void setTextFromHtml(String htmlText, Image image) {
        String customText;
        RtTagHandler tagHandler = new RtTagHandler(image, this, false);

        customText = htmlText.replace("span", mySpan);
        customText = customText.replace("img", myImg);

        setText(Html.fromHtml(customText, null, tagHandler));
    }

    @Override
    public ImageBuilder getImageBuilder() {
        return imageBuilder;
    }

    @Override
    public String getHtmlText() {
        return Html.toHtml(getEditableText());
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

    @Override
    protected void onDetachedFromWindow() {
        CoroutineUtil.INSTANCE.cancel(hashCode());
        super.onDetachedFromWindow();
    }
}
