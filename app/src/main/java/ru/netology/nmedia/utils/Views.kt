package ru.netology.nmedia.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

internal fun View.hideKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, /*flags = */0)
}

internal fun View.showKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, /*flags = */0)
}
