package com.shine56.richtextx.util;

import com.shine56.richtextx.api.DrawableGet;
import com.shine56.richtextx.api.ImageBuilder;
import com.shine56.richtextx.api.ImageClick;
import com.shine56.richtextx.api.ImageDelete;
import com.shine56.richtextx.bean.Image;

public class ImageBuilderImpl implements ImageBuilder {
    private Image image = new Image();

    @Override
    public ImageBuilder setImageUrl(String imageUrl) {
        image.setImageUrl(imageUrl);
        return this;
    }

    @Override
    public ImageBuilder setDrawableGet(DrawableGet drawableGet){
        image.setDrawableGet(drawableGet);
        return this;
    }

    @Override
    public ImageBuilder setOnImageCLickListener(ImageClick imageClick){
        image.setClick(imageClick);
        return this;
    }

    @Override
    public ImageBuilder setOnImageDeleteListener(ImageDelete imageDelete){
        image.setDelete(imageDelete);
        return this;
    }

    @Override
    public ImageBuilder setDeleteLogoId(Integer deleteLogoId) {
        image.setDeleteLogoId(deleteLogoId);
        return this;
    }

    @Override
    public Image create(){
        return image;
    }
}
