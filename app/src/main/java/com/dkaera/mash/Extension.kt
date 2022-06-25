package com.dkaera.mash

fun Iterable<Boolean>.sumOf(selector: (value: Boolean) -> Boolean): Boolean {
    var sum = true
    for (element in this) {
        sum = sum && selector(element)
    }
    return sum
}