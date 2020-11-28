package com.shine56.richtextx.bean;

import com.shine56.richtextx.api.DrawableGet;
import com.shine56.richtextx.api.ImageClick;
import com.shine56.richtextx.api.ImageDelete;

public class Image{
    private String imageUrl = "imageUrl";
    private ImageClick click;
    private ImageDelete delete;
    private DrawableGet drawableGet;
    private Integer deleteLogoId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageClick getClick() {
        return click;
    }

    public void setClick(ImageClick click) {
        this.click = click;
    }

    public ImageDelete getDelete() {
        return delete;
    }

    public void setDelete(ImageDelete delete) {
        this.delete = delete;
    }

    public DrawableGet getDrawableGet() {
        return drawableGet;
    }

    public void setDrawableGet(DrawableGet drawableGet) {
        this.drawableGet = drawableGet;
    }

    public Integer getDeleteLogoId() {
        return deleteLogoId;
    }

    public void setDeleteLogoId(Integer deleteLogoId) {
        this.deleteLogoId = deleteLogoId;
    }
}
