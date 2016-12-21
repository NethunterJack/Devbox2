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

package com.xxxifan.devbox.demo.ui.view.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.ArrayMap;

import com.xxxifan.devbox.core.base.BaseActivity;
import com.xxxifan.devbox.core.base.component.ToolbarActivity;
import com.xxxifan.devbox.demo.R;

/**
 * Created by xifan on 12/20/16.
 */

public class ColorBarActivity extends ToolbarActivity{
    @Override protected int getLayoutId() {
        return 0;
    }

    @Override protected void onSetupActivity(Bundle savedInstanceState) {

    }

    @Override public String getSimpleName() {
        return null;
    }
}
