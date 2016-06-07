package com.xxxifan.devbox.library.util.logger;

import java.util.List;

public interface Printer {

    Printer t(String tag, int methodCount);

    Settings init(String tag);

    Settings getSettings();

    void d(String message, Object... args);

    void e(String message, Object... args);

    void e(Throwable throwable, String message, Object... args);

    void w(String message, Object... args);

    void wtf(String message, Object... args);

    void json(String json);

    void xml(String xml);

    void clear();

    void list(List list);
}
