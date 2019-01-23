package com.github.zchu.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;


public class DeepLinkUtils {
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String MARKET = "market";
    private static final String MARKET_ANDROID_COM = "market.android.com";
    private static final String PLAY_GOOGLE_COM = "play.google.com";

    public static boolean canOpenDeepLink(Context context, String deepLink) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }

    public static boolean openDeepLinkCheck(Context context, String deepLink) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isDeepLink(deepLink) && deviceCanHandleIntent(context, intent)) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    public static void openDeepLink(Context context, String deepLink) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static boolean deviceCanHandleIntent(Context context, Intent intent) {
        try {
            return !context.getPackageManager().queryIntentActivities(intent, 0).isEmpty();
        } catch (NullPointerException e) {
            return false;
        }
    }

    private static boolean isAppStoreUrl(String url) {
        if (url == null) {
            return false;
        }
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (PLAY_GOOGLE_COM.equals(host) || MARKET_ANDROID_COM.equals(host)) {
            return true;
        }
        return MARKET.equals(scheme);
    }

    public static boolean isDeepLink(String url) {
        return isAppStoreUrl(url) || !isHttpUrl(url);
    }

    public static boolean isHttpUrl(String url) {
        if (url == null) {
            return false;
        }
        String scheme = Uri.parse(url).getScheme();
        return HTTP.equals(scheme) || HTTPS.equals(scheme);
    }
}
