package com.example.todoapp;

import android.content.DialogInterface;

/**
 * ���������, ������������ ��� ��������� ������ ��� �������� ���������� ����.
 */
public interface OnDialogCloseListener {

    /**
     * ����� ���������� ��� �������� �������.
     * @param dialogInterface ��������� ����������� ����
     */
    void onDialogClose(DialogInterface dialogInterface);
}
