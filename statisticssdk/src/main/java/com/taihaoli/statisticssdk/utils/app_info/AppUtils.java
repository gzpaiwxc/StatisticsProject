package com.taihaoli.statisticssdk.utils.app_info;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.taihaoli.statisticssdk.utils.Utils;

/**
 * author: Gzp
 * Create on 2018/7/6
 * Description:
 */
public class AppUtils {


    /**
     * Return the application's version name.
     *
     * @return the application's version name
     */
    public static String getAppVersionName() {
        return getAppVersionName(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's version name.
     *
     * @param packageName The name of the package.
     * @return the application's version name
     */
    public static String getAppVersionName(final String packageName) {
        if (isSpace(packageName)) return "";
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Return the application's version code.
     *
     * @return the application's version code
     */
    public static int getAppVersionCode() {
        return getAppVersionCode(Utils.getApp().getPackageName());
    }

    /**
     * Return the application's version code.
     *
     * @param packageName The name of the package.
     * @return the application's version code
     */
    public static int getAppVersionCode(final String packageName) {
        if (isSpace(packageName)) return -1;
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
