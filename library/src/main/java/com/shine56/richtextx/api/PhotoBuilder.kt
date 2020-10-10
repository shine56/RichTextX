package com.shine56.richtextx.api

import android.view.View

interface PhotoBuilder{
    fun setOnCLickListener(listener: ImageClick): PhotoBuilder
    fun setOnDeleteListener(listener: ImageDelete): PhotoBuilder
    fun setOnCLickListener(block: (view: View, imgUrl: String) -> Unit): PhotoBuilder
    fun setOnDeleteListener(block: (view: View, imgUrl: String) -> Unit): PhotoBuilder
    fun apply()
}