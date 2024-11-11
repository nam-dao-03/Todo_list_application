package com.example.todolistapplication.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.R;
import com.example.todolistapplication.databases.DatabaseHelper;
import com.example.todolistapplication.databases.entities.Task;
import com.example.todolistapplication.interfaces.IClickItemListener;
import com.example.todolistapplication.task.TaskActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private DatabaseHelper databaseHelper;
    private List<Task> taskList;
    private IClickItemListener iClickItemListener;
    public TaskAdapter(List<Task> taskList, IClickItemListener iClickItemListener, DatabaseHelper databaseHelper){
        this.taskList = taskList;
        this.iClickItemListener = iClickItemListener;
        this.databaseHelper = databaseHelper;
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        if(task.isIs_done()) holder.img_check_done.setImageResource(R.drawable.round_check_circle_24);
        else holder.img_check_done.setImageResource(R.drawable.outline_circle_24);
        if(task.isIs_important()) holder.img_check_important.setImageResource(R.drawable.outline_star_24);
        else holder.img_check_important.setImageResource(R.drawable.outline_star_outline_24);
        if(task.getDate().isEmpty()) holder.ll_show_time.setVisibility(View.GONE);

        holder.tv_task_name.setText(task.getName());
        holder.tv_time.setText(task.getDate());
        holder.img_check_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setIs_done(!task.isIs_done());
                databaseHelper.updateTask(task);
                holder.img_check_done.setImageResource(task.isIs_done() ? R.drawable.round_check_circle_24 : R.drawable.outline_circle_24);
                    notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.img_check_important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setIs_important(!task.isIs_important());
                databaseHelper.updateTask(task);
                holder.img_check_important.setImageResource(task.isIs_important() ? R.drawable.outline_star_24 :R.drawable.outline_star_outline_24);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.card_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemListener.onClickItem(task);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(taskList != null) {
            return taskList.size();
        }
        return 0;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_check_done, img_check_important;
        private TextView tv_task_name, tv_time;
        private LinearLayout ll_show_time;
        private CardView card_task;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            img_check_done = itemView.findViewById(R.id.img_check_done);
            img_check_important = itemView.findViewById(R.id.img_check_important);
            tv_task_name = itemView.findViewById(R.id.tv_task_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            ll_show_time = itemView.findViewById(R.id.ll_show_time);
            card_task = itemView.findViewById(R.id.card_task);
        }
    }
}
