package com.example.todolistapplication.task;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.MainActivity;
import com.example.todolistapplication.R;
import com.example.todolistapplication.adapters.TaskAdapter;
import com.example.todolistapplication.databases.DatabaseHelper;
import com.example.todolistapplication.databases.entities.ListWork;
import com.example.todolistapplication.databases.entities.Step;
import com.example.todolistapplication.databases.entities.Task;
import com.example.todolistapplication.interfaces.IClickItemListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TaskActivity extends AppCompatActivity {
    private LinearLayout ll_back_to_list;
    private ImageView img_more;
    private TextView tv_task_name;
    private RecyclerView rcv_task_list;
    private CardView cv_add_new_task;

    private TextView tv_header_add;
    private EditText et_add_new_task;
    private Button btn_add_new_task, btn_cancel_dialog;
    //Objects
    private DatabaseHelper databaseHelper;
    private ListWork listWork;
    private List<Task> taskList;
    private List<Step> stepList;
    private TaskAdapter taskAdapter;
    //Dialog bottom
    private TextView tv_delete_btn, tv_update_btn;
    //Dialog delete
    private TextView tv_delete_name, tv_action_no, tv_action_yes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        instantiateObject();
        initUI();
        setListenerForWidgets();
    }

    private void instantiateObject() {
        //objects
        databaseHelper = new DatabaseHelper(this);
        listWork = (ListWork) getIntent().getSerializableExtra("list_work_object");
        taskList = databaseHelper.getAllTask();
        stepList = databaseHelper.getAllStep();
    }

    private void initUI() {
        //Widgets
        ll_back_to_list = findViewById(R.id.ll_back_to_list);
        img_more = findViewById(R.id.img_more);
        tv_task_name = findViewById(R.id.tv_task_name);
        rcv_task_list = findViewById(R.id.rcv_task_list);
        cv_add_new_task = findViewById(R.id.cv_add_new_task);
        tv_task_name.setText(listWork.getName());
        if(listWork.getName().equals("Completed")) cv_add_new_task.setVisibility(View.GONE);
        if(listWork.getName().equals("My day")) cv_add_new_task.setVisibility(View.GONE);
        if(listWork.getName().equals("Important")) cv_add_new_task.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_task_list.setLayoutManager(linearLayoutManager);
        taskAdapter = new TaskAdapter(getTaskList(listWork.getName()), new IClickItemListener() {
            @Override
            public void onClickItem(Object object) {
                Intent detail_task_activity = new Intent(TaskActivity.this, DetailTaskActivity.class);
                detail_task_activity.putExtra("task_object", (Task) object);
                startActivity(detail_task_activity);
            }
        }, databaseHelper);
        rcv_task_list.setAdapter(taskAdapter);
    }

    private void setListenerForWidgets(){
        ll_back_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_activity = new Intent(TaskActivity.this, MainActivity.class);
                startActivity(main_activity);
            }
        });
        img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBottom();
            }
        });
        cv_add_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddListWork();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void showAddListWork() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog_add_new);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        //Widgets
        tv_header_add = dialog.findViewById(R.id.tv_header_add);
        et_add_new_task = dialog.findViewById(R.id.et_add_new);
        btn_add_new_task = dialog.findViewById(R.id.btn_add_new);
        btn_cancel_dialog = dialog.findViewById(R.id.btn_cancel_dialog);

        tv_header_add.setText("Add new task");
        et_add_new_task.setHint("New task");
        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_add_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String new_task = et_add_new_task.getText().toString();
                if (new_task.trim().isEmpty()) {
                    createToast("Empty name task", R.drawable.baseline_warning_24);
                    return;
                }
                Task task = new Task(-1, listWork.getList_work_id(), new_task, false, false, "", "");
                boolean result = databaseHelper.addTask(task);
                if (result) createToast("Add Successful", R.drawable.baseline_check_circle_24);
                initUI();
            }
        });
    }
    private void showDialogBottom(){
        //Define dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog_bottom);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogBottomAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        //signing variable
        tv_delete_btn = dialog.findViewById(R.id.tv_delete_btn);
        tv_update_btn = dialog.findViewById(R.id.tv_update_btn);
        tv_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String nameListWork = listWork.getName();
                switch (nameListWork) {
                    case "My day":
                    case "Completed":
                    case "Important":
                    case "Tasks":
                        createToast("Can't delete " + nameListWork, R.drawable.baseline_warning_24);
                        break;
                    default:
                        showDeleteDialog(listWork.getName());
                        break;
                }
            }
        });
        tv_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String nameListWork = listWork.getName();
                switch (nameListWork) {
                    case "My day":
                    case "Completed":
                    case "Important":
                    case "Tasks":
                        createToast("Can't change name " + nameListWork, R.drawable.baseline_warning_24);
                        break;
                    default:
                        showUpdateDialog();
                        break;
                }
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void showUpdateDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog_add_new);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        //Widgets
        tv_header_add = dialog.findViewById(R.id.tv_header_add);
        et_add_new_task = dialog.findViewById(R.id.et_add_new);
        btn_add_new_task = dialog.findViewById(R.id.btn_add_new);
        btn_cancel_dialog = dialog.findViewById(R.id.btn_cancel_dialog);

        tv_header_add.setText("Change name");
        et_add_new_task.setHint("New name");
        btn_add_new_task.setText("Change");
        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_add_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_name_list_work = et_add_new_task.getText().toString();
                if(new_name_list_work.trim().isEmpty()) return;
                listWork.setName(new_name_list_work);
                boolean result = databaseHelper.updateListWork(listWork);
                if (result) createToast("Change name successful", R.drawable.baseline_check_circle_24);
                dialog.dismiss();
                initUI();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void showDeleteDialog(String name){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog_delete);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        //Text View
        tv_delete_name = dialog.findViewById(R.id.tv_delete_name);
        tv_action_no = dialog.findViewById(R.id.tv_action_no);
        tv_action_yes = dialog.findViewById(R.id.tv_action_yes);
        //Signing variable
        tv_delete_name.setText("Delete: " + name);
        //Set Listener for widgets
        tv_action_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_action_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nameListWork = listWork.getName();
                    deleteAllTaskByListWorkId(listWork.getList_work_id());
                    boolean result = databaseHelper.deleteListWork(listWork);
                    Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                    if(result) {
                        createToast("Deleted " + nameListWork, R.drawable.baseline_check_circle_24);
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    createToast("Error", R.drawable.baseline_warning_24);
                }
            }
        });
    }
    private List<Task> getTaskList(String type) {
        List<Task> taskList = databaseHelper.getAllTask();
        Set<Task> filteredSet = new LinkedHashSet<>();
        for (Task task : taskList) {
            switch (type) {
                case "Important":
                    if (task.isIs_important()) {
                        filteredSet.add(task);
                    }
                    break;

                case "Completed":
                    if (task.isIs_done()) {
                        filteredSet.add(task);
                    }
                    break;

                case "My day":
                    if (!task.getDate().isEmpty()) {
                        filteredSet.add(task);
                    }
                    break;

                default:
                    if (task.getList_work_id() == listWork.getList_work_id()) {
                        filteredSet.add(task);
                    }
                    break;
            }
        }
        return new ArrayList<>(filteredSet);
    }
    private void createToast(String input_text_to_toast, int imageResId){
        Toast toast = new Toast(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_custom_toast, findViewById(R.id.layout_custom_toast));
        TextView text_toast = view.findViewById(R.id.text_toast);
        ImageView img_icon_toast = view.findViewById(R.id.img_icon_toast);
        text_toast.setText(input_text_to_toast);
        img_icon_toast.setImageResource(imageResId);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
    private void deleteAllTaskByListWorkId(int list_work_id){
        for(Task task: taskList) {
            if(task.getList_work_id() == list_work_id) {
                //Delete Step
                for (Step step: stepList) {
                    if(step.getTask_id() == task.getTask_id()) {
                        databaseHelper.deleteStep(step);
                    }
                }
                //Delete Task
                databaseHelper.deleteTask(task);
            }
        }
    }
}