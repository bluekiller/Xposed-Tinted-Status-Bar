package com.mohammadag.colouredstatusbar.hooks.oemhooks;

import android.widget.TextView;

import com.mohammadag.colouredstatusbar.ColourChangerMod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;

public class NubiaHook {
    private ColourChangerMod mInstance;

    public NubiaHook(ColourChangerMod instance, ClassLoader classLoader) {
        mInstance = instance;
        final String[] textViewClassNames = {"com.android.systemui.statusbar.policy.BatteryLevel", "com.android.systemui.statusbar.policy.NetSpeedView"};

        for (final String name : textViewClassNames) {
            try {

                Class<?> textView = XposedHelpers.findClass(name, classLoader);

                if (!TextView.class.isAssignableFrom(textView)) {
                    continue;
                }

                try {
                    XposedBridge.hookAllConstructors(textView, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            mInstance.addTextLabel((TextView) param.thisObject);
                            mInstance.log("Hooking class " + name);
                        }
                    });
                } catch (NoSuchMethodError e) {
                    mInstance.log("Not hooking constructor for class " + name);
                }

            } catch (ClassNotFoundError e) {
                mInstance.log("Not hooking class: " + name + " " + e.getMessage());
            }
        }
    }
}
