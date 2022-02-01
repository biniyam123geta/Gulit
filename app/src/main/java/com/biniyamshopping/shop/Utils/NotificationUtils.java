package com.biniyamshopping.shop.Utils;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NotificationUtils {


    public static SweetAlertDialog showPermissionDialog(Context context, String title, String message,
                                                        String positiveLabel, SweetAlertDialog.OnSweetClickListener positiveOnClickListener,
                                                        String negativeLabel, SweetAlertDialog.OnSweetClickListener negativeOnClickListener) {

        SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.setTitle(title);
        mSweetAlertDialog.setContentText(message);
        mSweetAlertDialog.setConfirmButton(positiveLabel, positiveOnClickListener);
        mSweetAlertDialog.setCancelButton(negativeLabel, negativeOnClickListener);
        mSweetAlertDialog.setCancelable(false);

        return mSweetAlertDialog;
    }
    public static SweetAlertDialog createSuccessDialog(Context context, String title, String content) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitle(title);
        sweetAlertDialog.setContentText(content);


        return sweetAlertDialog;
    }
    public static SweetAlertDialog createErrorDialog(Context context, String title, String content) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitle(title);
        sweetAlertDialog.setContentText(content);


        return sweetAlertDialog;
    }
}
