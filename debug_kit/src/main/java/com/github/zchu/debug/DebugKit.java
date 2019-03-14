package com.github.zchu.debug;

import android.app.Application;
import com.didichuxing.doraemonkit.DoraemonKit;

public class DebugKit {

    public static void install(final Application app) {
        DoraemonKit.install(app);
        DoraemonKit.hide();
    }
}
