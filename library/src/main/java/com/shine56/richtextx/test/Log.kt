package com.shine56.richtextx.test

import android.util.Log

/**
 *Created by Shine56 on 2021/2/7 11:29
 *Description：打印一些数据
 */
object PrintTest {
    val TAG = "调试"
    /**
     * 打印光标所在行信息
     * @param intArray IntArray getThisLineInformation()：0所在行数， 1行起始位置， 2行结束位置, 3光标位置
     */
    fun printLineInformation(intArray: IntArray){
        Log.d(TAG, "printLineInformation: " +
                "所在行数: ${intArray[0]} \n " +
                "行起始位置: ${intArray[1]} \n " +
                "行结束位置: ${intArray[2]} \n " +
                "光标位置: ${intArray[3]} \n")
    }
}