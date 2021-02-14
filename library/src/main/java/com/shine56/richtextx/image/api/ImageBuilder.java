package com.shine56.richtextx.image.api;

import com.shine56.richtextx.image.Image;

public interface ImageBuilder {
    ImageBuilder setImageUrl(String imageUrl);
    ImageBuilder setDrawableGet(DrawableGet drawableGet);
    ImageBuilder setOnImageCLickListener(ImageClick imageClick);
    ImageBuilder setOnImageDeleteListener(ImageDelete imageDelete);
    ImageBuilder setDeleteLogoId(Integer deleteLogoId);
    Image create();
}
