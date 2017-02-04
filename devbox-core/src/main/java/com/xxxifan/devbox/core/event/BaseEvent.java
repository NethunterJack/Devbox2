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

package com.xxxifan.devbox.core.event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xifan on 3/30/16.
 */
public class BaseEvent {
    /**
     * @param targetClass specify target class that will receive this event
     */
    public final void post(Class... targetClass) {
        EventBus.getDefault().post(this);
    }

    /**
     * @param targetClass specify target class that will receive this event
     */
    public final void postSticky(Class... targetClass) {
        EventBus.getDefault().postSticky(this);
    }
}
