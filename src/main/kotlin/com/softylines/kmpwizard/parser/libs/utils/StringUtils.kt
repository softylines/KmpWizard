package com.softylines.kmpwizard.parser.libs.utils

fun String.removeSurroundingQuotes(): String {
    return removeSurrounding("\"").removeSurrounding("'")
}