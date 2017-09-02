/*
 * Copyright(c) 2016 xxxifan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xxxifan.devbox

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES
import android.support.annotation.ColorInt
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import org.greenrobot.eventbus.EventBus

/**
 * Created by xifan on 17-8-28.
 */

fun Activity.getContext(): Context {
    return this
}

fun Activity.registerEventBus() {
    if (!EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().register(this)
    }
}

fun Activity.unregisterEventBus() {
    if (EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().unregister(this)
    }
}

fun Fragment.registerEventBus() {
    if (!EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().register(this)
    }
}

fun Fragment.unregisterEventBus() {
    if (EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().unregister(this)
    }
}

@TargetApi(VERSION_CODES.KITKAT)
fun AppCompatActivity.setTranslucentBar() {
    if (atLeast(VERSION_CODES.KITKAT)) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

@TargetApi(VERSION_CODES.LOLLIPOP)
fun AppCompatActivity.setTransparentBar() {
    setTranslucentBar()
    if (atLeast(VERSION_CODES.LOLLIPOP)) {
        transparentWindowBackground(window)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    }
}

@TargetApi(VERSION_CODES.KITKAT)
fun AppCompatActivity.setTranslucentNavBar() {
    if (atLeast(VERSION_CODES.KITKAT)) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }
}

@TargetApi(VERSION_CODES.LOLLIPOP)
fun AppCompatActivity.setNavBarColor(@ColorInt color: Int) {
    if (atLeast(VERSION_CODES.LOLLIPOP)) {
        window.navigationBarColor = color
    }
}

@TargetApi(VERSION_CODES.LOLLIPOP)
private fun transparentWindowBackground(window: Window) {
    val uiFlag = window.decorView.systemUiVisibility or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    window.decorView.systemUiVisibility = uiFlag
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
}

fun atLeast(sdk: Int): Boolean {
    return Build.VERSION.SDK_INT >= sdk
}

/**
 * debug block for easier debug state access
 * @param debug assign it if you want to use local variable as debug flag
 */
inline fun debug(debug: Boolean? = null, express: () -> Unit) {
    val customDebug = debug != null
    if (customDebug) {
        if (debug!!) {
            express.invoke()
        }
    } else {
        if (BuildConfig.DEBUG) {
            express.invoke()
        }
    }
}