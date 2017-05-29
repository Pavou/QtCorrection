package com.avoupavou.qtcorrection;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import com.avoupavou.qtcorrection.R;

import static android.content.Context.MODE_PRIVATE;

public class MessageDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(Html.fromHtml(getString(R.string.disclaimer)))
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmSeenBefore();
                    }
                })
                .setNegativeButton(R.string.negtive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmSeenBefore();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void confirmSeenBefore(){
        SharedPreferences preferences = getActivity().getPreferences(MODE_PRIVATE);
        boolean disclaimerSeen = preferences.getBoolean("DisclaimerSeen", false);
        if (!disclaimerSeen ) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("DisclaimerSeen", true);
            editor.commit();
        }
    }
}