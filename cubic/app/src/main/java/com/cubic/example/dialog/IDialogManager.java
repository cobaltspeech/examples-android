package com.cubic.example.dialog;

import android.os.Bundle;

public interface IDialogManager<D> {

    void showDialog(Class<D> clazz);

    void showDialog(Class<D> clazz, Bundle bundle);

    void hideDialog(Class<D> clazz);

    void onDestroy();
}
