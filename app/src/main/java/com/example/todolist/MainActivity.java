package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.todolist.Database.DatabaseHandler;
import com.example.todolist.Model.ToDo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasks;
    private MyAdapter myAdapter;
    private List<ToDo> taskList;
    private DatabaseHandler db;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>() ;
        tasks = findViewById(R.id.takList);
        tasks.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(db,MainActivity.this);
        tasks.setAdapter(myAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new ItemTouch(myAdapter));
        itemTouchHelper.attachToRecyclerView(tasks);


        fab = findViewById(R.id.fab);
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        myAdapter.setTasks(taskList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTask.newInstance().show(getSupportFragmentManager(), newTask.TAG);
            }
        });

    }

    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        myAdapter.setTasks(taskList);
        myAdapter.notifyDataSetChanged();
    }
}