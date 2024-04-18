package com.example.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, обеспечивающий доступ к базе данных SQLite для хранения задач.
 * Содержит методы для создания и обновления таблицы задач, а также для выполнения операций вставки, обновления и удаления задач.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";

    /**
     * Класс для работы с базой данных SQLite.
     * Обеспечивает создание и обновление таблицы для хранения задач, а также методы для вставки, обновления, удаления и выборки данных.
     * Предоставляет методы для работы с задачами.
     */
    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Создает таблицу для хранения задач, если она не существует.
     * Вызывается при создании экземпляра класса DataBaseHelper или при обновлении базы данных.
     * @param db объект базы данных SQLite
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Выполняет SQL-запрос для создания таблицы задач
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER)");
    }

    /**
     * Обновляет структуру базы данных при изменении версии.
     * Удаляет существующую таблицу и создает новую.
     * @param db объект базы данных SQLite
     * @param oldVersion предыдущая версия базы данных
     * @param newVersion новая версия базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаление существующей таблицы задач и создание новой таблицы при обновлении версии базы данных
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Вставляет новую задачу в базу данных.
     * @param model объект задачи для вставки
     */
    public void insertTask(ToDoModel model) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getTask());
        values.put(COL_3, 0);
        db.insert(TABLE_NAME, null, values);
    }

    /**
     * Обновляет существующую задачу в базе данных.
     * @param id идентификатор задачи
     * @param task новое значение задачи
     */
    public void updateTask(int id, String task) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    /**
     * Обновляет статус задачи в базе данных.
     * @param id идентификатор задачи
     * @param status новый статус задачи
     */
    public void updateStatus(int id, int status) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, status);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    /**
     * Удаляет задачу из базы данных.
     * @param id идентификатор задачи
     */
    public void deleteTask(int id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    /**
     * Метод для получения списка всех задач из базы данных
     */
    public List<ToDoModel> getAllTasks() {

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        // Запрос к базе данных для получения списка задач
        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        // Создание объекта ToDoModel для каждой задачи и добавление его в список
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(task);

                    } while (cursor.moveToNext());
                }
            }
        } finally {
            // Завершение транзакции и закрытие курсора
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

}







