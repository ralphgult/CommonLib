package com.xlg.commonlibs.ext

import java.net.URLEncoder

fun String.getVideoUrl(): String {
    val index = lastIndexOf("/") + 1
    val clientStr = substring(0, index)
    val fileStr = substring(index, length)
    val coveredFileStr = URLEncoder.encode(fileStr, "UTF-8")
    return clientStr + coveredFileStr
}

fun String.clean(): String {
    val indexStart = indexOf("<!--")
    val indexEnd = lastIndexOf("-->") + 2
    val firstPar = substring(0, indexStart)
    val lastPar = substring(indexEnd + 1, length)
    return (firstPar + lastPar)
}