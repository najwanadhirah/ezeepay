package com.rt.qpay99.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import com.rt.qpay99.R;


/**
 * Created by User on 3/6/2017.
 */

public class AlertBoxUtil {

    private Context mContext;
    private RTAlertDialogInterface mRTAlertDialogInterface;
    public AlertBoxUtil(Context mContext, RTAlertDialogInterface mRTAlertDialogInterface){
        this.mContext=mContext;
        this.mRTAlertDialogInterface=mRTAlertDialogInterface;
    }


    public void showAlertDialog(String mTitle,String msgText,String positiveText,String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom));
        builder.setTitle(mTitle);
        builder.setMessage(msgText);

        if(FunctionUtil.isSet(positiveText)) {
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRTAlertDialogInterface.BUTTON_POSITIVE_CLICK();
                        }
                    });
        }

        if(FunctionUtil.isSet(negativeText)){
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRTAlertDialogInterface.BUTTON_NEGATIVE_CLICK();
                        }
                    });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showAlertDialog(String mTitle,String msgText,String positiveText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mTitle);
        builder.setMessage(msgText);


        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRTAlertDialogInterface.BUTTON_POSITIVE_CLICK();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showAlertDialog(String mTitle,String msgText,String positiveText,String negativeText,String neutralText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mTitle);
        builder.setMessage(msgText);

        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRTAlertDialogInterface.BUTTON_POSITIVE_CLICK();
                    }
                });


        if(FunctionUtil.isSet(negativeText)){
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mRTAlertDialogInterface.BUTTON_NEGATIVE_CLICK();
                        }
                    });
        }


        builder.setPositiveButton(neutralText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRTAlertDialogInterface.BUTTON_NEUTRAL_CLICK();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface RTAlertDialogInterface {
        public void BUTTON_POSITIVE_CLICK();

        public void BUTTON_NEGATIVE_CLICK();

        public void BUTTON_NEUTRAL_CLICK();
    }
}
