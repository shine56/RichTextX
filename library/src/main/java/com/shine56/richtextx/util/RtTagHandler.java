package com.shine56.richtextx.util;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.shine56.richtextx.api.DrawableGet;
import com.shine56.richtextx.api.HtmlTextX;
import com.shine56.richtextx.api.ImageClick;
import com.shine56.richtextx.api.ImageDelete;
import com.shine56.richtextx.bean.Image;
import com.shine56.richtextx.view.ClickableImageSpan;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.shine56.richtextx.api.HtmlTextX.TAG;

public class RtTagHandler implements Html.TagHandler {

    private Image image;
    private HashMap<String, String> attributes = new HashMap();
    private int startIndex = 0;
    private int stopIndex = 0;
    private EditText editText = null;

    public RtTagHandler(Image image, EditText editText) {
        this.image = image;
        this.editText = editText;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        processAttributes(xmlReader);
        if (tag == HtmlTextX.mySpan || tag == HtmlTextX.myImg) {
            if (opening) {
                startSpan(tag, output);
            } else {
                endSpan(output);
                attributes.clear();
            }
        }
    }

    private void startSpan(String tag, Editable output) {
        startIndex = output.length();
        if (tag.equalsIgnoreCase(HtmlTextX.myImg) && image != null  && image.getDrawableGet() != null) {
            startImg(output, image.getDrawableGet());
        }
    }

    private void endSpan(Editable output) {
        stopIndex = output.length();
        String size = attributes.get("size");
        String style = attributes.get("style");
        if (!TextUtils.isEmpty(style)) {
            analysisStyle(startIndex, stopIndex, output, style);
        }
        if (!TextUtils.isEmpty(size)) {
            size = size.split("px")[0];
        }
        if (!TextUtils.isEmpty(size)) {
            int fontSize = Integer.valueOf(size);

            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(fontSize, true);

            output.setSpan(
                    absoluteSizeSpan,
                    startIndex,
                    stopIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    private void startImg(Editable text, DrawableGet drawableGet) {
        String src = attributes.get("src");

        Drawable drawable = drawableGet.getDrawable(src);
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        drawable.setBounds(0, 0, width > 0? width: 0, height > 0? height : 0);

        ClickableImageSpan imageSpan = new ClickableImageSpan(drawable, src);
        SpannableString spannableString = new SpannableString(src);

        spannableString.setSpan(
                imageSpan,
                0,
                spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        text.append(spannableString);

        //点击事件
        if(image.getClick()!=null){
            if(editText != null){
                imageSpan.setOnCLickListener(new ImageClick() {
                    @Override
                    public void onClick(View view, String imgUrl) {
                        //必须执行的逻辑
                        editText.setShowSoftInputOnFocus(false);
                        editText.setCursorVisible(false);
                        //用户定义的逻辑
                        image.getClick().onClick(view, imgUrl);
                    }
                });
            }else {
                imageSpan.setOnCLickListener(image.getClick());
            }
        }

        //删除事件
        if(image.getDelete()!=null && editText != null){
            imageSpan.setOnDeleteListener(new ImageDelete() {
                @Override
                public void onDelete(View view, String imgUrl) {
                    //必须执行的逻辑
                    int start = editText.getText().getSpanStart(imageSpan);
                    int end = editText.getText().getSpanEnd(imageSpan);
                    editText.getText().delete(start, end);

                    editText.setShowSoftInputOnFocus(false);

                    //用户定义的逻辑
                    image.getDelete().onDelete(view, imgUrl);
                }
            });
        }

    }

    private void processAttributes(final XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[])dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer)lengthField.get(atts);

            /**
             * MSH: Look for supported attributes and add to hash map.
             * This is as tight as things can get :)
             * The data index is "just" where the keys and values are stored.
             */
            for(int i = 0; i < len; i++)
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
        } catch (Exception e) {
        }
    }

    /**
     * 解析style属性
     * @param startIndex
     * @param stopIndex
     * @param editable
     * @param style
     */
    /**
     * 解析style属性
     * @param startIndex
     * @param stopIndex
     * @param editable
     * @param style
     */
    private void analysisStyle(int startIndex,int stopIndex,Editable editable,String style){
        String[] attrArray = style.split(";");
        Map<String,String> attrMap = new HashMap<>();
        if (null != attrArray){
            for (String attr:attrArray){
                String[] keyValueArray = attr.split(":");
                if (null != keyValueArray && keyValueArray.length == 2){
                    // 记住要去除前后空格
                    attrMap.put(keyValueArray[0].trim(),keyValueArray[1].trim());
                }
            }
        }

        String fontSize = attrMap.get("font-size");
        if (!TextUtils.isEmpty(fontSize)) {
            fontSize = fontSize.split("px")[0];
        }

        if (!TextUtils.isEmpty(fontSize)) {
            int fontSizeDp = Integer.valueOf(fontSize);

            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(fontSizeDp, true);

            editable.setSpan(
                    absoluteSizeSpan,
                    startIndex,
                    stopIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }
}
