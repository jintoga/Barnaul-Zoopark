package com.dat.barnaulzoopark.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
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

    public static MaterialDialog.Builder createVerifyLogoutDialog(@NonNull Context context) {
        return new MaterialDialog.Builder(context).backgroundColorRes(R.color.white)
            .positiveColorRes(R.color.colorAccent)
            .contentColorRes(R.color.black)
            .content(context.getString(R.string.verify_logout))
            .positiveText(context.getString(R.string.logout))
            .negativeText(context.getString(R.string.cancel))
            .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            });
    }

    static MaterialDialog.Builder createEditUserNameDialog(@NonNull Context context,
        String currentUserName) {
        return new MaterialDialog.Builder(context).backgroundColorRes(R.color.white)
            .positiveColorRes(R.color.colorAccent)
            .contentColorRes(R.color.black)
            .content(context.getString(R.string.edit_name))
            .negativeText(context.getString(R.string.cancel))
            .inputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
            .input(context.getString(R.string.edit_name_hint), currentUserName,
                new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (input.length() == 0) {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        } else {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        }
                    }
                })
            .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            })
            .alwaysCallInputCallback();
    }
}
