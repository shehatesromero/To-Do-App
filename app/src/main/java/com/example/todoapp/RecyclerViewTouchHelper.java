package com.example.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.ToDoAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * Класс, обеспечивающий поддержку свайпов в RecyclerView для удаления или редактирования элементов списка.
 * Реализует интерфейс ItemTouchHelper.SimpleCallback для обработки свайпов.
 */
public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;

    /**
     * Конструктор класса с передачей адаптера для обработки событий
     */
    public RecyclerViewTouchHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    /**
     * Метод вызывается при перемещении элементов списка
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Обрабатывает свайп вправо или влево для удаления или редактирования элементов списка.
     * @param viewHolder объект ViewHolder элемента списка
     * @param direction направление свайпа (ItemTouchHelper.LEFT или ItemTouchHelper.RIGHT)
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        // Создание диалогового окна для подтверждения удаления задачи при свайпе вправо
        if (direction == ItemTouchHelper.RIGHT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are You Sure ?");
            // Обработчик нажатия кнопки "Yes"
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteTask(position);
                }
            });

            //Обработчик нажатия кнопки "Cancel"
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(position);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Редактирование задачи при свайпе влево
            adapter.editItem(position);
        }
    }

    /**
     * Настраивает отображение при свайпе элементов списка.
     * @param c объект Canvas для рисования
     * @param recyclerView RecyclerView, в котором отображаются элементы списка
     * @param viewHolder объект ViewHolder элемента списка
     * @param dX смещение по оси X (горизонтальное)
     * @param dY смещение по оси Y (вертикальное)
     * @param actionState состояние действия (активное, пассивное и т. д.)
     * @param isCurrentlyActive является ли действие активным в данный момент времени
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // Оформление свайпа элемента списка с помощью библиотеки RecyclerViewSwipeDecorator
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark))
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_edit)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeRightActionIcon(R.drawable.ic_baseline_delete)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
