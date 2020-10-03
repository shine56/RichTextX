package com.shine56.richtextx.api

interface PhotoBuilder{
    fun setOnCLickListener(listener: ImageClick): PhotoBuilder
    fun setOnDeleteListener(listener: ImageDelete): PhotoBuilder
    fun apply()
}