package com.xxxifan.devbox.library.base;

import android.content.Context;

/**
 * Created by xifan on 5/16/16.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);

    Context getContext();

    String getSimpleName();

    void showMessage(String msg);
}
