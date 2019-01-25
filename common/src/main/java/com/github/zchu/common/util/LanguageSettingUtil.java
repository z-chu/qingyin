package com.github.zchu.common.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Locale;

public class LanguageSettingUtil implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final int LANGUAGE_TYPE_FOLLOW_SYSTEM = 0;
    public static final int LANGUAGE_TYPE_ENGLISH = 1;
    public static final int LANGUAGE_TYPE_SIMPLIFIED_CHINESE = 2;
    public static final int LANGUAGE_TYPE_KOREAN = 3;
    private static final String K_SELECTED_LANGUAGE = "selected_language";
    @SuppressLint("StaticFieldLeak")
    private static LanguageSettingUtil instance;
    private Context mContext;
    private SharedPreferences mPreferences;
    private MutableLiveData<Integer> liveData;
    private Locale sysLocale;

    private LanguageSettingUtil(Context context) {
        this.mContext = context;
        sysLocale = getSysLocale();
        mPreferences = mContext.getSharedPreferences("LanguageSetting", Context.MODE_PRIVATE);
        liveData = new MutableLiveData<>();
        mPreferences.registerOnSharedPreferenceChangeListener(this);
        liveData.setValue(getLanguageType());
        LanguageSettingUtil.changeAppLanguage(context, getLanguageLocale());
    }

    public static void init(Context mContext) {
        if (instance == null) {
            synchronized (LanguageSettingUtil.class) {
                if (instance == null) {
                    instance = new LanguageSettingUtil(mContext);
                }
            }
        }
    }

    public static LanguageSettingUtil getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You must be init MultiLanguageUtil first");
        }
        return instance;
    }

    public static void changeAppLanguage(Context context) {
        changeAppLanguage(context, getInstance().getLanguageLocale());
    }

    public static void changeAppLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, metrics);
    }

    public static String getSystemLanguage(Locale locale) {
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            getInstance().setConfiguration();
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getInstance().getLanguageLocale();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    public LiveData<Integer> getLanguageLiveData() {
        return liveData;
    }

    private void setConfiguration() {
        Locale targetLocale = getLanguageLocale();
        Configuration configuration = mContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
        } else {
            configuration.locale = targetLocale;
        }
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }

    private Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public void setSysLocale(Locale sysLocale) {
        this.sysLocale = sysLocale;
    }

    public void updateLanguage(int languageType) {
        mPreferences
                .edit()
                .putInt(K_SELECTED_LANGUAGE, languageType)
                .apply();
        setConfiguration();
    }

    public int getLanguageType() {
        return mPreferences.getInt(K_SELECTED_LANGUAGE, LANGUAGE_TYPE_FOLLOW_SYSTEM);
    }

    public Locale getLanguageLocale() {
        int languageType = getLanguageType();
        if (languageType == LANGUAGE_TYPE_FOLLOW_SYSTEM) {
            return sysLocale;
        } else if (languageType == LANGUAGE_TYPE_ENGLISH) {
            return Locale.ENGLISH;
        } else if (languageType == LANGUAGE_TYPE_SIMPLIFIED_CHINESE) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (languageType == LANGUAGE_TYPE_KOREAN) {
            return Locale.KOREAN;
        }
        return getSysLocale();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        liveData.setValue(getLanguageType());
    }
}
