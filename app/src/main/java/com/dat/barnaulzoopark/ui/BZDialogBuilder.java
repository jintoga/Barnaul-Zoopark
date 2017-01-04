package com.dat.barnaulzoopark.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 1/3/2017.
 */

public class BZDialogBuilder {
    public static void createSimpleErrorDialog(@NonNull Context context, String error) {
        new MaterialDialog.Builder(context).title("Error!!!")
            .backgroundColorRes(R.color.white)
            .titleColorRes(R.color.black)
            .positiveColorRes(R.color.colorAccent)
            .contentColorRes(R.color.black)
            .content(error)
            .positiveText("OK")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            })
            .show();
    }

    public static MaterialDialog createSimpleProgressDialog(@NonNull Context context) {
        return new MaterialDialog.Builder(context).backgroundColorRes(R.color.white)
            .contentColorRes(R.color.black)
            .content("Loading...")
            .progress(true, 0)
            .show();
    }
}
