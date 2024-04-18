package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Фрагмент, отображающий диалоговое окно для добавления новой задачи или редактирования существующей.
 * Реализует BottomSheetDialogFragment для отображения диалога снизу экрана.
 */
public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    //widgets
    private EditText mEditText;
    private Button mSaveButton;

    private DataBaseHelper myDb;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    /**
     * Фрагмент для добавления новой задачи.
     * Отображает диалоговое окно с полем ввода и кнопкой "Save" для добавления новой задачи.
     * Позволяет также редактировать существующую задачу, если переданы соответствующие параметры.
     * После сохранения или редактирования вызывает обновление списка задач.
     * @param inflater           Объект LayoutInflater, используемый для загрузки макета фрагмента.
     * @param container          Родительский ViewGroup, к которому привязывается макет фрагмента.
     * @param savedInstanceState Объект Bundle, содержащий данные о предыдущем состоянии фрагмента (если таковое имеется).
     * @return Возвращает созданный и настроенный интерфейс фрагмента для отображения диалогового окна.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Загрузка макета фрагмента для отображения диалогового окна
        View v = inflater.inflate(R.layout.add_newtask, container, false);
        return v;
    }

    /**
     * Настройка элементов диалогового окна добавления/редактирования задачи.
     * Включает в себя установку текста в EditText, добавление обработчика нажатия на кнопку "Save" и обработку закрытия диалога.
     * @param view корневой вид фрагмента
     * @param savedInstanceState сохраненное состояние фрагмента (если есть)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация EditText и Button для ввода новой задачи и кнопки сохранения
        mEditText = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);
        myDb = new DataBaseHelper(getActivity());

        // Получение данных о редактируемой задаче (если есть)
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);
            // Отключение кнопки сохранения, если задача уже существует
            if (task.length() > 0) {
                mSaveButton.setEnabled(false);
            }


        }

        // Обработчик изменения текста в EditText
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Включение/выключение кнопки сохранения в зависимости от наличия текста в EditText
                if (s.toString().equals("")) {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                } else {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Обработчик нажатия на кнопку сохранения
        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение текста новой задачи из EditText
                String text = mEditText.getText().toString();

                // Обновление существующей задачи или добавление новой задачи в базу данных
                if (finalIsUpdate) {
                    myDb.updateTask(bundle.getInt("id"), text);
                } else {
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDb.insertTask(item);
                }
                // Закрытие диалогового окна
                dismiss();

            }
        });
    }

    /**
     * Обрабатывает закрытие диалогового окна и обновляет список задач.
     * Вызывается при закрытии диалогового окна.
     * @param dialog интерфейс диалогового окна
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
