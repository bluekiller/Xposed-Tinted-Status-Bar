package com.mohammadag.colouredstatusbar.hooks;

import android.widget.ImageView;

import com.mohammadag.colouredstatusbar.ColourChangerMod;

import java.util.Locale;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class SignalClusterHook {
    private static final String[] SIGNAL_CLUSTER_ICON_NAMES = {
            "mMobile", "mMobileActivity", "mMobileType",
            "mMobileRoaming", "mWifi", "mWifiActivity",
            "mEthernet", "mEthernetActivity", "mAirplane",
            "mPhoneSignal"
    };

    private static final String[] MOTO_G_ICON_NAMES = {
            "mMobileActivityView", "mMobileActivityView2",
            "mMobileRoamingView", "mMobileRoamingView2",
            "mMobileSimView", "mMobileSimView2",
            "mMobileStrengthView", "mMobileStrengthView2",
            "mMobileTypeView", "mMobileTypeView2",
            "mWifiActivityView", "mWifiStrengthView"
    };

    private static final String[] LG_ICON_NAMES = {
            "mThirdType", "mThirdType2", "mThirdActivity"
    };

    private static final String[] NUBIA_ICON_NAMES = {
            "mWifi", "mWifiActivity", "CdmaBottomType", "CdmaTopType",
            "mAirplane", "mMobile", "mMobileActivity", "mMobileActivityTop",
            "mMobileType", "mNoSimSlot", "mRoaming"
    };

    private static final String[] NUBIA_ICON_ARRAY_NAMES = {
            "CdmaBottomType", "CdmaTopType", "mMobile", "mMobileActivity",
            "mMobileType", "mMobiletopActivity", "mNoSimSlot", "mRoaming"
    };

    private ColourChangerMod mInstance;
    private XC_MethodHook mSignalClusterHook = new XC_MethodHook() {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            for (String name : SIGNAL_CLUSTER_ICON_NAMES) {
                try {
                    ImageView view = (ImageView) XposedHelpers.getObjectField(param.thisObject, name);
                    mInstance.addSystemIconView(view);
                } catch (NoSuchFieldError e) {
                    mInstance.log("Couldn't find field " + name + " in class " + param.method.getDeclaringClass().getName());
                } catch (ClassCastException e) {
                    mInstance.log("Field " + name + " in class " + param.method.getDeclaringClass().getName() + " is not extend class ImageView");
                }
            }

            for (String name : MOTO_G_ICON_NAMES) {
                try {
                    ImageView view = (ImageView) XposedHelpers.getObjectField(param.thisObject, name);
                    mInstance.addSystemIconView(view);
                } catch (NoSuchFieldError e) {
                    mInstance.log("Couldn't find field " + name + " in class " + param.method.getDeclaringClass().getName());
                } catch (ClassCastException e) {
                    mInstance.log("Field " + name + " in class " + param.method.getDeclaringClass().getName() + " is not extend class ImageView");
                }
            }

            for (String name : LG_ICON_NAMES) {
                try {
                    ImageView view = (ImageView) XposedHelpers.getObjectField(param.thisObject, name);
                    mInstance.addSystemIconView(view);
                } catch (NoSuchFieldError e) {
                    mInstance.log("Couldn't find field " + name + " in class " + param.method.getDeclaringClass().getName());
                } catch (ClassCastException e) {
                    mInstance.log("Field " + name + " in class " + param.method.getDeclaringClass().getName() + " is not extend class ImageView");
                }
            }

            for (String name : NUBIA_ICON_NAMES) {
                try {
                    ImageView view = (ImageView) XposedHelpers.getObjectField(param.thisObject, name);
                    mInstance.addSystemIconView(view);
                } catch (NoSuchFieldError e) {
                    mInstance.log("Couldn't find field " + name + " in class " + param.method.getDeclaringClass().getName());
                } catch (ClassCastException e) {
                    mInstance.log("Field " + name + " in class " + param.method.getDeclaringClass().getName() + " is not extend class ImageView");
                }
            }

            for (String name : NUBIA_ICON_ARRAY_NAMES) {
                try {
                    ImageView[] views = (ImageView[]) XposedHelpers.getObjectField(param.thisObject, name);
                    for (int i = views.length; i > 0; i--) {
                        mInstance.addSystemIconView(views[i - 1]);
                    }
                } catch (NoSuchFieldError e) {
                    mInstance.log("Couldn't find field " + name + "in class " + param.method.getDeclaringClass().getName());
                } catch (ClassCastException e) {
                    mInstance.log("Field " + name + " in class " + param.method.getDeclaringClass().getName() + " is not extend class ImageView[]");
                }
            }
        }
    };

    public SignalClusterHook(ColourChangerMod instance, ClassLoader classLoader) {
        mInstance = instance;
        doHooks(classLoader);
    }

    private void doHooks(ClassLoader classLoader) {
        String className = "com.android.systemui.statusbar.SignalClusterView";
        String methodName = "onAttachedToWindow";
        try {
            Class<?> SignalClusterView = XposedHelpers.findClass(className, classLoader);

            try {
                findAndHookMethod(SignalClusterView, methodName, mSignalClusterHook);
            } catch (NoSuchMethodError e) {
                mInstance.log("Not hooking method " + className + "." + methodName);
            }
        } catch (ClassNotFoundError e) {
            // Really shouldn't happen, but we can't afford a crash here.
            mInstance.log("Not hooking class: " + className);
        }

        try {
            Class<?> MSimSignalClusterView = XposedHelpers.findClass("com.android.systemui.statusbar.MSimSignalClusterView",
                    classLoader);
            findAndHookMethod(MSimSignalClusterView, methodName, mSignalClusterHook);
        } catch (Throwable t) {
            // Not a Moto G
        }

		/* HTC Specific hook */
        if (!android.os.Build.MANUFACTURER.toLowerCase(Locale.getDefault()).contains("htc"))
            return;

        try {
            Class<?> HTCClusterView =
                    XposedHelpers.findClass("com.android.systemui.statusbar.HtcGenericSignalClusterView", classLoader);

            findAndHookMethod(HTCClusterView, methodName, mSignalClusterHook);
        } catch (Throwable t) {
        }
    }
}