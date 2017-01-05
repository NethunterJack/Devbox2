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

package com.xxxifan.devbox.components;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.xxxifan.devbox.core.base.SystemBarTintManager;
import com.xxxifan.devbox.core.base.UIComponent;
import com.xxxifan.devbox.core.base.component.ToolbarActivity;
import com.xxxifan.devbox.core.base.component.toolbar.ToolbarComponent;
import com.xxxifan.devbox.core.util.ViewUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Translucent status bar version of ToolbarActivity
 * add additional status bar height to toolbar and content,
 * so it can't display any content in status bar
 * <p/>
 * Created by xifan on 4/5/16.
 */
public abstract class TranslucentActivity extends ToolbarActivity {

    public static final int FIT_NONE = -1;
    public static final int FIT_TOOLBAR = 0;
    public static final int FIT_WINDOW_TOP = 1;
    public static final int FIT_WINDOW_BOTH = 2;

    private SystemBarTintManager mSystemBarManager;
    private boolean mTransparentStatusBar;
    private boolean mTranslucentNavBar;
    private int mFitWindowMode;
    private int mWindowOffset;
    private int mToolbarOffset;

    @Override protected void onConfigureActivity() {
        super.onConfigureActivity();
    }

    @Override @SuppressLint("NewApi")
    protected void attachContentView(View containerView, @LayoutRes int layoutResID) {
        super.attachContentView(containerView, layoutResID);
        if (isKitkat()) {
            mWindowOffset = mToolbarOffset = ViewUtils.getSystemBarHeight();
        }

        if (isLollipop() && getFitWindowMode() != FIT_WINDOW_BOTH) {
            getWindow().getDecorView()
                    .setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                        @Override
                        public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                            setWindowOffset($(BASE_CONTAINER_ID), insets.getStableInsetTop());
                            return v.onApplyWindowInsets(insets);
                        }
                    });
        }

        setTranslucentBar();
    }

    /**
     * set offset between container and window,
     * {@link #FIT_TOOLBAR} make both container and toolbar have space,
     * {@link #FIT_WINDOW_TOP} make container top have space with translucent bar,
     * {@link #FIT_WINDOW_BOTH} works like {@link View#setFitsSystemWindows(boolean)}
     *
     * @param containerView container view that need to fit window.
     * @param offset        pixels to margin
     */
    protected void setWindowOffset(View containerView, int offset) {
        mWindowOffset = offset;
        if (getFitWindowMode() == FIT_WINDOW_TOP) {
//            ((MarginLayoutParams) containerView.getLayoutParams()).topMargin = 0;
        } else if (getFitWindowMode() == FIT_TOOLBAR) {
            if ($(BASE_TOOLBAR_ID) != null) {
                ((MarginLayoutParams) $(BASE_TOOLBAR_ID).getLayoutParams()).topMargin = offset;
            }
        } else if (getFitWindowMode() == FIT_WINDOW_BOTH) {
            containerView.setFitsSystemWindows(true);
        }
    }

    @Override protected ArrayMap<String, UIComponent> getUIComponents() {
        ToolbarComponent toolbarComponent = new ToolbarComponent() {
            @Override protected void setupToolbar(View containerView, View toolbarView) {
                super.setupToolbar(containerView, toolbarView);
                if (getFitWindowMode() != FIT_WINDOW_BOTH) {
                    View contentView = ((ViewGroup) containerView).getChildAt(0);
                    if (getFitWindowMode() == FIT_TOOLBAR) {
                        int toolbarHeight = toolbarView.getResources()
                                .getDimensionPixelSize(R.dimen.toolbar_height);
                        int topMargin = ((MarginLayoutParams) contentView.getLayoutParams()).topMargin;
                        ((MarginLayoutParams) contentView.getLayoutParams()).topMargin =
                                Math.max(topMargin, toolbarHeight + mToolbarOffset);
                    } else if (getFitWindowMode() == FIT_WINDOW_TOP) {
                        ((MarginLayoutParams) contentView.getLayoutParams()).topMargin = 0;

                    }
                }
            }
        };
        ArrayMap<String, UIComponent> arrayMap = new ArrayMap<>();
        arrayMap.put(toolbarComponent.getTag(), toolbarComponent);
        return arrayMap;
    }

    /**
     * only set for kitkat and newer apis, more see from {@link #setWindowOffset(View, int)}
     *
     * @param mode one of {@link #FIT_NONE}, {@link #FIT_TOOLBAR}, {@link #FIT_WINDOW_TOP},
     *             default {@link #FIT_WINDOW_BOTH}.
     */
    @BeforeConfigActivity protected void setFitSystemWindowMode(@FitWindowMode int mode) {
        checkConfigured();
        mFitWindowMode = mode;
    }

    /**
     * make toolbar transparent, due to toolbar_container which has a shadow,
     * we can't simply make toolbar transparent by toolbar.setBackgroundColor()
     */
    protected void transparentToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            $(BASE_TOOLBAR_SHADOW_ID).setVisibility(View.GONE);
        }
    }

    /**
     * switch to full transparent status bar immediately, or configured in onConfigureActivity()
     */
    protected void transparentStatusBar() {
        mTransparentStatusBar = true;
        if (isConfigured()) {
            setTranslucentBar();
        }
    }

    protected void translucentNavBar() {
        mTranslucentNavBar = true;
        if (isConfigured()) {
            setTranslucentBar();
        }
    }

    protected void lightStatusBar() {
        ViewUtils.setStatusBarLightMode(this, true);
    }

    protected boolean isKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    protected boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @FitWindowMode
    protected int getFitWindowMode() {
        return mFitWindowMode;
    }

    protected void setTranslucentBar() {
        // setup translucent bar for kitkat devices
        if (!isKitkat()) {
            return;
        }

        if (isKitkat()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (mTranslucentNavBar && getFitWindowMode() != FIT_WINDOW_BOTH) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            if (mSystemBarManager == null) {
                mSystemBarManager = new SystemBarTintManager(this);
            }
            mSystemBarManager.setStatusBarTintEnabled(true);
            mSystemBarManager.setTintColor(getCompatColor(R.color.colorPrimary));
        }

        if (isLollipop()) {
            // always use translucent status bar
            Window window = getWindow();
            int uiFlag = window.getDecorView().getSystemUiVisibility() |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(uiFlag);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (mTransparentStatusBar) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    @SuppressLint("NewApi") @Override protected void onDestroy() {
        if (isLollipop() && getWindow() != null && getWindow().getDecorView() != null) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener(null);
        }
        super.onDestroy();
    }

    protected SystemBarTintManager getSystemBarManager() {
        return mSystemBarManager;
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @IntDef({FIT_NONE, FIT_TOOLBAR, FIT_WINDOW_TOP, FIT_WINDOW_BOTH})
    public @interface FitWindowMode {}
}
