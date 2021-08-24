package com.example.nagwatask.interfaces

interface ClickListener<T> {
    fun  onClickListener(model: T, position: Int)
}