package com.shine56.richtextx.view;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.shine56.richtextx.revoke.Revoker;
import com.shine56.richtextx.util.CoroutineUtil;
import com.shine56.richtextx.util.RichEditUtil;
import com.shine56.richtextx.util.RichTextXMovementMethod;
import com.shine56.richtextx.api.HtmlTextX;
import com.shine56.richtextx.image.api.ImageBuilder;
import com.shine56.richtextx.image.ImageBuilderImpl;
import com.shine56.richtextx.api.RichEditX;
import com.shine56.richtextx.image.Image;
import com.shine56.richtextx.util.RtTagHandler;

import org.jetbrains.annotations.NotNull;

public class RichEditText extends AppCompatEditText implements HtmlTextX, RichEditX {

    //判断是否需要更新文本
    private boolean needRefreshText = true;
    public boolean needPushRevokeStack = true;

    private RichEditUtil richEditUtil = new RichEditUtil(this);
    private Revoker revoker = new Revoker(this);

    private void init(){
        //设置span点击事件movementMethod
        setMovementMethod(RichTextXMovementMethod.Companion.getINSTANCE());

        //字号
        richEditUtil.setFontSize((int) getTextSize());

        //文本改变监听
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //从start开始的count个字符替换长度为before的旧文本，before==0 说明是新增
                //加粗与字号
                if(needRefreshText) {
                    needPushRevokeStack = false;
                    richEditUtil.setFontSizeAndBoldSpan(start, start+count, before);
                    needPushRevokeStack = true;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(needPushRevokeStack) revoker.add(new SpannableString(getText()));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean revoke() {
        return revoker.revoke();
    }

    @Override
    public void setTextFromHtml(String htmlText, Image image) {
        String customText;
        RtTagHandler tagHandler = new RtTagHandler(image,this, true);

        customText = htmlText.replace("span", mySpan);
        customText = customText.replace("img", myImg);

        needRefreshText = false;
        setText(Html.fromHtml(customText, null, tagHandler));
        needRefreshText = true;
    }

    /**
     * 每次调用创建一个新的Image
     * @return
     */
    @Override
    public ImageBuilder getImageBuilder() {
        return new ImageBuilderImpl();
    }

    @Override
    public String getHtmlText() {
        return Html.toHtml(getEditableText());
    }

    @Override
    public void insertPhoto(@NotNull Image image) {
        richEditUtil.insertPhoto(image);
    }

    @Override
    public void setBold(boolean isBold) {
        richEditUtil.setBold(isBold);
    }

    @Override
    public boolean getBold() {
        return richEditUtil.isBold();
    }

    @Override
    public void setFontSize(int fontSize) {
        richEditUtil.setFontSize(fontSize);
    }

    @Override
    public int getFontSize() {
        return richEditUtil.getFontSize();
    }

    @Override
    public void indent() {
        richEditUtil.indent();
    }

    public RichEditText(Context context) {
        super(context);
        init();
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(!getShowSoftInputOnFocus())
                setShowSoftInputOnFocus(true);
            if(!isCursorVisible())
                setCursorVisible(true);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        CoroutineUtil.INSTANCE.cancel(hashCode());
        super.onDetachedFromWindow();
    }

    @Override
    public boolean switchDeleteLineOnThisLine() {
        needRefreshText = false;
        boolean result = richEditUtil.switchDeleteLineOnThisLine();
        needRefreshText = true;
        return result;
    }

    @Override
    public boolean switchTextColorOnThisLine(int color) {
        needRefreshText = false;
        boolean result = richEditUtil.switchTextColorOnThisLine(color);
        needRefreshText = true;
        return result;
    }

    @Override
    public boolean switchToListOnThisLine() {
        needRefreshText = false;
        boolean result = richEditUtil.switchToListOnThisLine();
        needRefreshText = true;
        return result;
    }
}
