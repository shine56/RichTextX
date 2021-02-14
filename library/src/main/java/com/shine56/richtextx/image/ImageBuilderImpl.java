package com.shine56.richtextx.image;

import com.shine56.richtextx.image.api.DrawableGet;
import com.shine56.richtextx.image.api.ImageBuilder;
import com.shine56.richtextx.image.api.ImageClick;
import com.shine56.richtextx.image.api.ImageDelete;

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
