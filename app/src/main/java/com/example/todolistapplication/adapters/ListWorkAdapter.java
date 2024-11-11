package com.example.todolistapplication.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.R;
import com.example.todolistapplication.databases.entities.ListWork;
import com.example.todolistapplication.databases.entities.Task;
import com.example.todolistapplication.interfaces.IClickItemListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListWorkAdapter extends RecyclerView.Adapter<ListWorkAdapter.ListViewHolder> {
    private List<ListWork> listWorkList;
    private boolean isDefault;
    private List<ListWork> listWorkDefaultList;
    private List<ListWork> listWorkCustomList;
    private List<Task> taskList;
    private IClickItemListener iClickItemListener;
    public ListWorkAdapter(List<ListWork> listWorkList, boolean isDefault, List<Task> listTask, IClickItemListener iClickItemListener) {
        this.isDefault = isDefault;
        this.listWorkList = listWorkList;
        this.listWorkDefaultList = new ArrayList<>();
        this.listWorkCustomList = new ArrayList<>();
        this.taskList = listTask;
        this.iClickItemListener = iClickItemListener;
        applyFilter();
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_work, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListWork listWork;
        if(isDefault) listWork = listWorkDefaultList.get(position);
        else listWork = listWorkCustomList.get(position);
        if(listWork == null) return;
        holder.card_list_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemListener.onClickItem(listWork);
            }
        });
        switch (listWork.getName()) {
            case "My day":
                holder.img_logo_list_work.setImageResource(R.drawable.baseline_brightness_7_24);
                break;
            case "Important":
                holder.img_logo_list_work.setImageResource(R.drawable.baseline_star_border_24);
                break;
            case "Completed":
                holder.img_logo_list_work.setImageResource(R.drawable.baseline_check_circle_24);
                break;
            case "Tasks":
                holder.img_logo_list_work.setImageResource(R.drawable.baseline_home_24);
                break;
            default:
                holder.img_logo_list_work.setImageResource(R.drawable.baseline_format_list_bulleted_24);
        }
        int quantity_task = getQuantityTask(listWork);
        holder.tv_list_work_name.setText(listWork.getName());
        if(quantity_task > 0) holder.tv_quantity_task.setVisibility(View.VISIBLE);
        holder.tv_quantity_task.setText(String.valueOf(quantity_task));
    }

    private int getQuantityTask(ListWork listWork) {
        Set<Integer> uniqueTaskIds = new HashSet<>();
        for (Task task : taskList) {
            switch (listWork.getName()) {
                case "Important":
                    if (task.isIs_important()) {
                        uniqueTaskIds.add(task.getTask_id());
                    }
                    break;

                case "Completed":
                    if (task.isIs_done()) {
                        uniqueTaskIds.add(task.getTask_id());
                    }
                    break;

                case "My day":
                    if (!task.getDate().isEmpty()) {
                        uniqueTaskIds.add(task.getTask_id());
                    }
                    break;

                default:
                    if (task.getList_work_id() == listWork.getList_work_id()) {
                        uniqueTaskIds.add(task.getTask_id());
                    }
                    break;
            }
        }
        return uniqueTaskIds.size();
    }


    private void applyFilter(){
        for(ListWork listWork: listWorkList) {
            if(listWork.getName().equals("My day") || listWork.getName().equals("Important") || listWork.getName().equals("Completed") || listWork.getName().equals("Tasks")) {
                listWorkDefaultList.add(listWork);
            } else {
                listWorkCustomList.add(listWork);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(listWorkList == null) return 0;
        if(isDefault) return listWorkDefaultList.size();
        else return listWorkCustomList.size();

    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        private CardView card_list_work;
        private ImageView img_logo_list_work;
        private TextView tv_list_work_name, tv_quantity_task;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            card_list_work = itemView.findViewById(R.id.card_list_work);
            tv_list_work_name = itemView.findViewById(R.id.tv_list_work_name);
            img_logo_list_work = itemView.findViewById(R.id.img_logo_list_work);
            tv_quantity_task = itemView.findViewById(R.id.tv_quantity_task);
        }
    }
}
