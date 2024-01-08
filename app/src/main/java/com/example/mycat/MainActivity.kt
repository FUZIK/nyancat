package com.example.mycat

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import com.example.mycat.nya.NyanPallete
import com.example.mycat.nya.NyanSpaceView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.setBackgroundColor(NyanPallete.BACKGROUND)
        setContentView(NyanSpaceView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        })
    }
}
