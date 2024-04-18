package com.example.todoapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ������� �������� ����������, ������������ ������ �����.
 * �������������� RecyclerView � ������� ��� ����������� ������ �����.
 */
public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    private RecyclerView mRecyclerview;
    private FloatingActionButton fab;
    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;

    /**
     * ��������� ������� ���������� ��� ������� ����������.
     * �������� ��������� RecyclerView, ���������� ����������� ������� �� ������ "Add" � ��������� ItemTouchHelper ��� ��������� ������� ��������� ������.
     * @param savedInstanceState ����������� ��������� ���������� (���� ����)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ������������� RecyclerView � �������� ��� ����������� ������ ����
        mRecyclerview = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        myDB = new DataBaseHelper(MainActivity.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB, MainActivity.this);

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);

        // �������� ������ ����� �� ���� ������ � ����������� �� ������
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        // ���������� ������� �� ������ ���������� ����� ������
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ����������� ����������� ���� ��� ���������� ����� ������
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        // ����������� RecyclerView � ItemTouchHelper ��� ��������� �������
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);
    }

    /**
     * ��������� ������ ����� ����� �������� ����������� ���� ���������� ����� ������.
     * ���������� ��� �������� ����������� ����.
     * @param dialogInterface ��������� ����������� ����
     */
    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        // ���������� ������ ����� ����� �������� ����������� ���� ����������/�������������� ������
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }
}