package br.com.wifi.chat.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {

    private static ProgressDialog dialog;

    public static void showLoading(Context ctx, String text) {
        dialog = ProgressDialog.show(ctx, "",
                text != null ? text : "Carregando...", true);
    }

    public static void hideLoading() {
        if (dialog != null)
            dialog.hide();
    }
}
