package com.example.todoapp;

import android.content.DialogInterface;

/**
 * Интерфейс, используемый для обратного вызова при закрытии диалоговых окон.
 */
public interface OnDialogCloseListener {

    /**
     * Метод вызывается при закрытии диалога.
     * @param dialogInterface интерфейс диалогового окна
     */
    void onDialogClose(DialogInterface dialogInterface);
}
