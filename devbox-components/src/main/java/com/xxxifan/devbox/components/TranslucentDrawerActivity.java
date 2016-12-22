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

import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.xxxifan.devbox.core.base.UIComponent;
import com.xxxifan.devbox.core.base.component.toolbar.ToolbarComponent;

/**
 * Created by xifan on 5/16/16.
 */
public abstract class TranslucentDrawerActivity extends TranslucentActivity {

    private DrawerComponent mDrawerComponent;

    @Override
    protected void onConfigureActivity() {
        super.onConfigureActivity();
        setRootLayoutId(R.layout._internal_activity_drawer_base);
    }

    @Override protected ArrayMap<String, UIComponent> getUIComponents() {
        mDrawerComponent = new DrawerComponent(getDrawerView());
        ToolbarComponent toolbarComponent = new ToolbarComponent();
        ArrayMap<String, UIComponent> arrayMap = new ArrayMap<>();
        arrayMap.put(toolbarComponent.getTag(), toolbarComponent);
        arrayMap.put(mDrawerComponent.getTag(), mDrawerComponent);
        return arrayMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && getDrawerLayout() != null) {
            getDrawerLayout().openDrawer(Gravity.LEFT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerComponent.getDrawerLayout();
    }

    protected abstract View getDrawerView();
}
