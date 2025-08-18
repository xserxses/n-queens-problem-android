package com.github.xserxses.nqueensproblem.utils

fun getExcelColumnName(colIndex: Int): String {
    var num = colIndex
    val columnName = StringBuilder()
    if (num < 0) throw IllegalArgumentException("Column index needs to be 0 or positive.")

    do {
        columnName.append(('A'.code + num % 26).toChar())
        num = num / 26 - 1
    } while (num >= 0)

    return columnName.reverse().toString()
}
