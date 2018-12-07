/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 4:18 PM.
 */

package com.iamsdt.hs1.ext

fun CharSequence.toDouble(): Double {
    val sequence = this.toString()
    return sequence.toDoubleOrNull() ?: 0.0
}

//convert model to word table
//fun ColorSpace.Model.toWordTable() = WordTable(word = word, des = des)

fun CharSequence.toCapFirst(): String {
    val c = this[0].toUpperCase()
    val s = this.toString()
    return s.replaceFirst(this[0], c)
}