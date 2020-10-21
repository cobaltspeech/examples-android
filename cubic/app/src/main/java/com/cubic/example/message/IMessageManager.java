package com.cubic.example.message;

import androidx.annotation.NonNull;

public interface IMessageManager {

    void showMessage(@NonNull String message);

    void cancelToast();
}
