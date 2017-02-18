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

package com.xxxifan.devbox.core.base.uicomponent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.xxxifan.devbox.core.R;
import com.xxxifan.devbox.core.base.BaseActivity;
import com.xxxifan.devbox.core.util.ViewUtils;


/**
 * Created by xifan on 12/21/16.
 */

public class ToolbarComponent implements UIComponent {
    public static final String TAG = "ToolbarComponent";

    private boolean useLightToolbar;
    private boolean translucentToolbar;
    private boolean useCenterTitle;
    private int centerTitleSize;

    public ToolbarComponent() {
    }

    public ToolbarComponent(boolean useLightToolbar) {
        this.useLightToolbar = useLightToolbar;
    }

    @Override public void inflate(View containerView) {
        ViewStub toolbarStub = (ViewStub) containerView.findViewById(BaseActivity.BASE_TOOLBAR_STUB_ID);
        if (toolbarStub != null) {
            toolbarStub.setLayoutResource(useLightToolbar ? R.layout._internal_view_toolbar_light : R.layout._internal_view_toolbar_dark);
            toolbarStub.inflate();
            View toolbarView = containerView.findViewById(BaseActivity.BASE_TOOLBAR_ID);
            if (toolbarView != null) {
                setupToolbar(containerView, toolbarView);
            } else {
                throw new IllegalStateException("Can't find toolbar");
            }
        }
    }

    protected void setupToolbar(View containerView, View toolbarView) {
        BaseActivity activity = ((BaseActivity) containerView.getContext());
        if (activity.getSupportActionBar() instanceof WindowDecorActionBar) {
            throw new IllegalStateException("You must make your app theme extends from Devbox.AppTheme or manually set windowActionBar to false.");
        }

        // fix content position if toolbar exists.
        View contentView = ((ViewGroup) containerView).getChildAt(0);
        ((ViewGroup.MarginLayoutParams) contentView.getLayoutParams()).topMargin = toolbarView
                .getResources().getDimensionPixelSize(R.dimen.toolbar_height);

        // set support actionbar
        Toolbar toolbar = (Toolbar) toolbarView;
        toolbar.setBackgroundColor(translucentToolbar ? Color.TRANSPARENT :
                                           ViewUtils.getCompatColor(R.color.colorPrimary));
        activity.setSupportActionBar(toolbar);

        if (translucentToolbar) {
            activity.findViewById(BaseActivity.BASE_TOOLBAR_SHADOW_ID).setVisibility(View.GONE);
        }

        // set home as up key
        if (!activity.isTaskRoot() && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        activity.getSupportActionBar().setDisplayShowTitleEnabled(!useCenterTitle);
    }

    /**
     * make toolbar transparent, due to toolbar_container which has a shadow,
     * we can't simply make toolbar transparent by toolbar.setBackgroundColor()
     */
    public void transparentToolbar(@NonNull BaseActivity activity) {
        translucentToolbar = true;
        if (activity.isFinishing()) {
            return;
        }
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar()
                    .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            activity.findViewById(BaseActivity.BASE_TOOLBAR_SHADOW_ID).setVisibility(View.GONE);
        }
    }

    public void setToolbarCustomView(BaseActivity activity, View customView) {

    }

    protected void useLightToolbar() {
        useLightToolbar = true;
    }

    public boolean isUseCenterTitle() {
        return useCenterTitle;
    }

    public void onTitleChanged(BaseActivity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (useCenterTitle) {
            Toolbar toolbar = (Toolbar) activity.findViewById(BaseActivity.BASE_TOOLBAR_ID);
            TextView  textView= null;
            for (int i = 0, s = toolbar.getChildCount(); i < s; i++) {
                if (toolbar.getChildAt(i) instanceof TextView) {
                    textView = (TextView) toolbar.getChildAt(i);
                    if (textView.getTag() == null || !textView.getTag().toString().equals(TAG)) {
                        textView = null;
                    }
                }
            }
            if (textView == null) {
                setCenterTitle(activity, 0);
            }
        }
    }

    private void setCenterTitle(BaseActivity activity, int pxSize) {
        useCenterTitle = true;
        centerTitleSize = pxSize;
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar == null) {
            return;
        }

        supportActionBar.setDisplayShowTitleEnabled(false);

        int[] attrs = {android.R.attr.textColorPrimary, android.R.attr.windowTitleSize};
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(activity, attrs,
                                                                       android.support.v7.appcompat.R.styleable.Toolbar, android.support.v7.appcompat.R.attr.toolbarStyle, 0);

        mTitleTextAppearance = a.getResourceId(android.support.v7.appcompat.R.styleable.Toolbar_titleTextAppearance, 0);
        if (pxSize == 0) {

        }
        Toolbar toolbar = (Toolbar) activity.findViewById(BaseActivity.BASE_TOOLBAR_ID);
        TextView textView = new TextView(activity);
        textView.setText(activity.getTitle());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, pxSize);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER);
        textView.setTag(TAG);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, activity
                .getResources().getDimensionPixelSize(R.dimen.toolbar_height));
        params.gravity = Gravity.CENTER_HORIZONTAL;
        toolbar.addView(textView, params);
    }

    @Override public <T extends UIComponent> void loadConfig(T component) {
        if (component != null && component instanceof ToolbarComponent) {
            ToolbarComponent toolbarComponent = (ToolbarComponent) component;
            translucentToolbar = toolbarComponent.translucentToolbar;
            useLightToolbar = toolbarComponent.useLightToolbar;
        }
    }

    @Override public String getTag() {
        return TAG;
    }

}
