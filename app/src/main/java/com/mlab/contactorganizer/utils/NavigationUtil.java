package com.mlab.contactorganizer.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mlab.contactorganizer.ForgotPasswordActivity;
import com.mlab.contactorganizer.MainActivity;
import com.mlab.contactorganizer.MainNavigationActivity;
import com.mlab.contactorganizer.SignupActivity;
import com.mlab.contactorganizer.SmsActivity;
import com.mlab.contactorganizer.obj.UserObj;
import com.mlab.contactorganizer.services.SendingService;

public class NavigationUtil {

    public static void navigateForgotPassword(AppCompatActivity activity) {
        Intent intent = new Intent(activity, ForgotPasswordActivity.class);
        activity.startActivity(intent);
    }

    public static void navigateSignup(AppCompatActivity activity) {
        Intent intent = new Intent(activity, SignupActivity.class);
        activity.startActivity(intent);
    }

    public static void navigateMainNavigation(AppCompatActivity activity, UserObj userObj) {
        Intent intent = new Intent(activity, MainNavigationActivity.class);

        intent.putExtra("userObj", userObj);

        activity.startActivity(intent);
    }

    public static void navigateLogin(AppCompatActivity activity, boolean logout) {
        Intent intent = new Intent(activity, MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean("logout", logout);
        intent.putExtras(bundle); //Put your id to your next Intent

        activity.startActivity(intent);
    }

    /**
     * TODO usunac
     */
    public static void navigateSms(AppCompatActivity activity) {
        Intent intent = new Intent(activity, SmsActivity.class);
        activity.startActivity(intent);
    }
}
