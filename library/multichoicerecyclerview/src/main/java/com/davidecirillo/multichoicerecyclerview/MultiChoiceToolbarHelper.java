package com.davidecirillo.multichoicerecyclerview;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.PluralsRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;

class MultiChoiceToolbarHelper {

    private static final String TOOLBAR_ERROR_MESSAGE = "Toolbar not implemented via getSupportActionBar method";
    private static final String SELECTED_TOOLBAR_TITLE_FORMAT = "%d %s";
    private static final int ZERO = 0;

    private final ActionBar mSupportActionBar;
    private MultiChoiceToolbar mMultiChoiceToolbar;
    private AppCompatActivity mAppCompatActivity;

    MultiChoiceToolbarHelper(MultiChoiceToolbar multiChoiceToolbar) {
        mMultiChoiceToolbar = multiChoiceToolbar;
        mAppCompatActivity = multiChoiceToolbar.getAppCompatActivity();
        mSupportActionBar = mAppCompatActivity.getSupportActionBar();

        if (mSupportActionBar == null) {
            throw new IllegalStateException(TOOLBAR_ERROR_MESSAGE);
        }
    }

    void updateToolbar(int itemNumber) throws IllegalStateException {
        if (itemNumber == ZERO) {
            showDefaultToolbar();
        } else if (itemNumber > ZERO) {
            showMultiChoiceToolbar();
        }
        updateToolbarTitle(itemNumber);
    }

    private void showMultiChoiceToolbar() {
        mSupportActionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);

        int multi_primaryColor = mMultiChoiceToolbar.getMultiPrimaryColor();
        if (multi_primaryColor != 0) {
            mSupportActionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mAppCompatActivity, multi_primaryColor)));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mAppCompatActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            int multiPrimaryColorDark = mMultiChoiceToolbar.getMultiPrimaryColorDark();
            if (multiPrimaryColorDark != 0) {
                window.setStatusBarColor(ContextCompat.getColor(mAppCompatActivity, multiPrimaryColorDark));
            }
        }

        mMultiChoiceToolbar.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiChoiceToolbar.Listener listener = mMultiChoiceToolbar.getToolbarListener();
                if (listener != null) {
                    listener.onClearButtonPressed();
                }
            }
        });
    }

    private void showDefaultToolbar() {
        showDefaultIcon();
        mSupportActionBar.setBackgroundDrawable(new ColorDrawable(mMultiChoiceToolbar.getDefaultPrimaryColor()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mAppCompatActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mMultiChoiceToolbar.getDefaultPrimaryColorDark());
        }
    }

    private void showDefaultIcon() {
        int icon = mMultiChoiceToolbar.getIcon();
        if (icon != 0) {
            mSupportActionBar.setHomeAsUpIndicator(icon);
            mSupportActionBar.setDisplayHomeAsUpEnabled(true);
            mMultiChoiceToolbar.getToolbar().setNavigationOnClickListener(mMultiChoiceToolbar.getIconAction());
        } else {
            mSupportActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    private void updateToolbarTitle(int itemNumber) {
        if (itemNumber > 0) {
            @PluralsRes int selectedToolbarQuantityTitle = mMultiChoiceToolbar.getSelectedToolbarQuantityTitle();
            String selectedToolbarTitle = mMultiChoiceToolbar.getSelectedToolbarTitle();

            // if set, prefer the quantity-res over the given string or just use the number of selected items
            if (selectedToolbarQuantityTitle != 0 && selectedToolbarQuantityTitle != Constants.INVALID_RES) {
                mSupportActionBar.setTitle(mAppCompatActivity.getResources().getQuantityString(selectedToolbarQuantityTitle, itemNumber, itemNumber));

            } else if (selectedToolbarTitle != null) {
                mSupportActionBar.setTitle(String.format(Locale.UK, SELECTED_TOOLBAR_TITLE_FORMAT, itemNumber, selectedToolbarTitle));

            } else {
                mSupportActionBar.setTitle(String.valueOf(itemNumber));
            }

        } else {
            String defaultToolbarTitle = mMultiChoiceToolbar.getDefaultToolbarTitle();
            if (defaultToolbarTitle != null) {
                mSupportActionBar.setTitle(defaultToolbarTitle);
            } else {
                mSupportActionBar.setTitle(mAppCompatActivity.getTitle());
            }
        }
    }
}