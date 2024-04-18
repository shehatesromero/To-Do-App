package com.example.todoapp.Model;

/**
 * Модель данных для представления задачи в приложении.
 * Содержит поля для идентификатора, текста задачи и статуса выполнения.
 */
public class ToDoModel {

    /**
     * Поле модели данных: текст задачи
     */
    private String task;
    /**
     * Поле модели данных: идентификатор
     */
    private int id;
    /**
     * Поля модели данных: статус выполнения
     */
    private int status;

    // Геттеры и сеттеры для доступа к полям модели
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
