package com.cubic.example.dialog.model;

import android.app.Dialog;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.cubic.example.R;

public abstract class BaseDialogFragment extends DialogFragment {

    private String mTitleText;
    private int mPositiveText;
    private int mNegativeText;

    protected void setPositiveText(int positiveText) {
        mPositiveText = positiveText;
    }

    protected void setNegativeText(int negativeText) {
        mNegativeText = negativeText;
    }

    public void setTitleText(String titleText) {
        mTitleText = titleText;
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected AlertDialog.Builder getDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setView(getLayoutRes());
        if (mPositiveText > 0) {
            builder.setPositiveButton(
                    getColoredText(getResources().getString(mPositiveText)),
                    (dialog, which) -> onPositiveClickButton()
            );
        }
        if (mNegativeText > 0) {
            builder.setNegativeButton(
                    getColoredText(getResources().getString(mNegativeText)),
                    (dialog, which) -> onNegativeClickButton()
            );
        }
        if (!TextUtils.isEmpty(mTitleText)) {
            builder.setTitle(getColoredText(mTitleText));
        }

        return builder;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return getDialogBuilder().create();
    }

    private SpannableStringBuilder getColoredText(@NonNull String text) {
        int color = getResources().getColor(R.color.colorWhite);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);

        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return ssBuilder;
    }

    public void onPositiveClickButton() {
    }

    public void onNegativeClickButton() {
        dismiss();
    }
}
