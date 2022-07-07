package ru.netology.nmedia

fun Long.thousandKChanger(): String =
    when (this) {
        0L -> ""
        in 1..999 -> this.toString()
        in 1000..9999 -> "${String.format("%.1f", (this.toDouble() / 1000))}K"
        in 10_000..999_999 -> "${ this / 1000}K"
        else -> "${ this / 1_000_000}M"
    }