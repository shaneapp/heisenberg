package com.appleby.pinner;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

public class CustomDialogs {

    public static void addFolder(Activity context, IAddFolderClickListener addFolderClickListener) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("New Collection");

        View view = context.getLayoutInflater().inflate(R.layout.dialog_add_item, null);
        final EditText edittext = (EditText) view.findViewById(R.id.edittextMainField);
//        edittext.setTypeface(MyApplication.getTypefaceRalewayMedium());
        edittext.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});
        alert.setView(view);

        alert.setPositiveButton("Add", (dialog, whichButton) -> {

            addFolderClickListener.positiveClicked(edittext.getText().toString());

        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {});

        Dialog dialog = alert.show();

        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.darkGray));
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.lightGray));

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

}
