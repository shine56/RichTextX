package com.shine56.richtextx.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

/**
 * 顶层函数充当scope工厂
 */
object CoroutineUtil {
    private val scopeMap = HashMap<Int, CoroutineScope>()

    @Synchronized
    fun getScope(viewHashCode: Int): CoroutineScope{
        val scope = scopeMap[viewHashCode]

        if (scope == null){
            val newScope = CoroutineScope(Job())
            scopeMap.put(viewHashCode, newScope)

            return newScope
        }
        return scope
    }

    fun cancel(viewHashCode: Int) {
        scopeMap[viewHashCode]?.let {
            it.cancel()
            scopeMap.remove(viewHashCode, it)
        }
    }

//    fun hasScope(viewHashCode: Int) = scopeMap.containsKey(viewHashCode)
//
//    fun isEmpty() = scopeMap.isEmpty()
}