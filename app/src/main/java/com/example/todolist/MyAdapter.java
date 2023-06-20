package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Database.DatabaseHandler;
import com.example.todolist.Model.ToDo;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ToDo> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public MyAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_layuout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder( ViewHolder holder, int position) {
        db.openDatabase();
        ToDo item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(ConvertToBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }
    public int getItemCount() {
        return todoList.size();
    }
    public Context getContext() {
        return activity;
    }
    public void setTasks(List<ToDo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    public void editItem(int position) {
        ToDo item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        newTask fragment = new newTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), newTask.TAG);
    }

    public void deleteItem(int position) {
        ToDo item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    private boolean ConvertToBoolean(int n) {
        return n != 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.check);
        }
    }

}
