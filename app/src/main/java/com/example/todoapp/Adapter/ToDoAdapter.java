package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DataBaseHelper;

import java.util.List;

/**
 * Адаптер для RecyclerView, отображающий список задач.
 * Связывает данные задач с виджетами в элементах списка и обрабатывает пользовательские действия.
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB, MainActivity activity) {
        // Конструктор адаптера с передачей объекта базы данных и главной активити
        this.activity = activity;
        this.myDB = myDB;
    }

    /**
     * Создает новый экземпляр ViewHolder при необходимости.
     * @param parent родительский ViewGroup
     * @param viewType тип представления элемента списка
     * @return новый объект ViewHolder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создание нового элемента списка путем загрузки макета из XML
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * Привязывает данные к элементу списка при прокрутке.
     * @param holder объект ViewHolder элемента списка
     * @param position позиция элемента в списке
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Получение данных задачи по позиции
        final ToDoModel item = mList.get(position);
        // Установка текста задачи и состояния CheckBox
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        // Обработчик изменения состояния CheckBox
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Обновление статуса задачи при изменении CheckBox
                if (isChecked) {
                    myDB.updateStatus(item.getId(), 1);
                } else
                    myDB.updateStatus(item.getId(), 0);
            }
        });
    }

    /**
     * Преобразует целочисленное значение в логическое.
     * @param num целочисленное значение (0 или 1)
     * @return true, если значение не равно 0, и false в противном случае
     */
    public boolean toBoolean(int num) {
        return num != 0;
    }

    /**
     * Получает контекст текущей активности.
     * @return контекст текущей активности
     */
    public Context getContext() {
        return activity;
    }

    /**
     * Устанавливает список задач для отображения.
     * @param mList список задач для отображения
     */
    public void setTasks(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    /**
     * Удаляет задачу из списка по указанной позиции.
     * @param position позиция задачи в списке
     */
    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Открывает диалоговое окно для редактирования задачи.
     * @param position позиция задачи в списке
     */
    public void editItem(int position) {
        ToDoModel item = mList.get(position);

        // Создание объекта Bundle для передачи данных о редактируемой задаче
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        // Отображение диалогового окна для редактирования задачи
        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
