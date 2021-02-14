package com.shine56.richtextx.revoke

import android.text.SpannableString
import android.util.Log
import android.widget.EditText
import com.shine56.richtextx.test.PrintTest.TAG
import com.shine56.richtextx.view.RichEditText
import java.util.*
import kotlin.math.min

/**
 *Created by Shine56 on 2021/2/9 0:04
 *Description：维护一个栈，实现返回上一步
 */
class Revoker(private val editText: RichEditText) {
    private val stack = Stack<SpannableString>()

    fun add(spannableString: SpannableString){
        //Log.d(TAG, "add: $spannableString")
        stack.push(spannableString)
    }

    private fun getBeforeText(): SpannableString?{
        return if (!stack.empty()){
            stack.pop()
        }else null
    }

    fun revoke(): Boolean{
        val spannableString = getBeforeText()

        spannableString?.let {
            editText.needPushRevokeStack = false
            val index = editText.selectionStart
            editText.setText(it)
            editText.setSelection(min(index, it.length))
            editText.needPushRevokeStack = true
            return true
        }

        Log.d(TAG, "revoke: 撤销失败")
        return false
    }
}