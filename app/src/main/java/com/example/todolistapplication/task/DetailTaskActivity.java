package com.example.todolistapplication.task;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapplication.R;
import com.example.todolistapplication.adapters.StepAdapter;
import com.example.todolistapplication.databases.DatabaseHelper;
import com.example.todolistapplication.databases.entities.ListWork;
import com.example.todolistapplication.databases.entities.Step;
import com.example.todolistapplication.databases.entities.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class DetailTaskActivity extends AppCompatActivity {
    //Widgets
    private ConstraintLayout cl_main;
    private TextView tv_name_list, tv_task_name, tv_description, tv_date_task, tv_complete_step_btn, tv_add_to_my_day;
    private ImageView img_check_done, img_check_important, img_delete, img_add_to_my_day, img_more;
    private LinearLayout ll_back_to_task, ll_add_step_btn, ll_add_to_my_day_btn, ll_add_step_edit_text;
    private EditText et_add_new_step;
    //Objects
    private DatabaseHelper databaseHelper;
    private RecyclerView rcv_step_list;
    private StepAdapter stepAdapter;
    private Task task;
    private ListWork listWork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_task);
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
        databaseHelper = new DatabaseHelper(this);
        task = (Task) getIntent().getSerializableExtra("task_object");
        if(task == null) return;
        listWork = getListWorkById(task.getList_work_id());
    }

    private void initUI(){
        cl_main = findViewById(R.id.main);
        tv_name_list = findViewById(R.id.tv_name_list);
        tv_task_name = findViewById(R.id.tv_task_name);
        tv_description = findViewById(R.id.tv_description);
        tv_date_task = findViewById(R.id.tv_date_task);
        tv_complete_step_btn = findViewById(R.id.tv_complete_step_btn);
        tv_add_to_my_day = findViewById(R.id.tv_add_to_my_day);
        img_check_done = findViewById(R.id.img_check_done);
        img_check_important = findViewById(R.id.img_check_important);
        img_add_to_my_day = findViewById(R.id.img_add_to_my_day);
        img_delete = findViewById(R.id.img_delete);
        img_more = findViewById(R.id.img_more);
        et_add_new_step = findViewById(R.id.et_add_new_step);
        ll_back_to_task = findViewById(R.id.ll_back_to_task);
        ll_add_step_btn = findViewById(R.id.ll_add_step_btn);
        ll_add_to_my_day_btn = findViewById(R.id.ll_add_to_my_day_btn);
        ll_add_step_edit_text = findViewById(R.id.ll_add_step_edit_text);
        rcv_step_list = findViewById(R.id.rcv_step_list);
        //signing variables
        tv_name_list.setText(listWork.getName());
        tv_task_name.setText(task.getName());
        if (!task.getDate().isEmpty()) {
            img_add_to_my_day.setImageResource(R.drawable.round_brightness_7_24);
            tv_add_to_my_day.setTextColor(ContextCompat.getColor(DetailTaskActivity.this, R.color.royal_blue));
        }
        else {
            task.setDate("");
            img_add_to_my_day.setImageResource(R.drawable.baseline_brightness_7_24);
            tv_add_to_my_day.setTextColor(ContextCompat.getColor(DetailTaskActivity.this, R.color.gray));
        }
        tv_date_task.setText(task.getDate());
        String description = task.getDescription();
        if(description.isEmpty()) tv_description.setTextColor(ContextCompat.getColor(this, R.color.gray));
        else tv_description.setTextColor(ContextCompat.getColor(this, R.color.light_black));
        tv_description.setText(description.isEmpty() ? "Add Note" : task.getDescription());
        img_check_done.setImageResource(task.isIs_done() ? R.drawable.round_check_circle_24 : R.drawable.outline_circle_24);
        img_check_important.setImageResource(task.isIs_important() ? R.drawable.outline_star_24 : R.drawable.outline_star_outline_24);
        //Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_step_list.setLayoutManager(linearLayoutManager);
        stepAdapter = new StepAdapter(getStepList(), databaseHelper, DetailTaskActivity.this);
        rcv_step_list.setAdapter(stepAdapter);
    }


    private void setListenerForWidgets() {
        cl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_add_step_edit_text.setVisibility(View.GONE);
                tv_complete_step_btn.setVisibility(View.GONE);
                ll_add_step_btn.setVisibility(View.VISIBLE);
                img_more.setVisibility(View.VISIBLE);
                et_add_new_step.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        ll_back_to_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent task_activity = new Intent(DetailTaskActivity.this, TaskActivity.class);
                task_activity.putExtra("list_work_object", listWork);
                startActivity(task_activity);
            }
        });
        img_check_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setIs_done(!task.isIs_done());
                databaseHelper.updateTask(task);
                img_check_done.setImageResource(task.isIs_done() ? R.drawable.round_check_circle_24 : R.drawable.outline_circle_24);
            }
        });
        img_check_important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setIs_important(!task.isIs_important());
                databaseHelper.updateTask(task);
                img_check_important.setImageResource(task.isIs_important() ? R.drawable.outline_star_24 : R.drawable.outline_star_outline_24);
            }
        });
        img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBottom();
            }
        });
        tv_complete_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_add_step_edit_text.setVisibility(View.GONE);
                tv_complete_step_btn.setVisibility(View.GONE);
                img_more.setVisibility(View.VISIBLE);
                ll_add_step_btn.setVisibility(View.VISIBLE);
                et_add_new_step.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                String step_name = et_add_new_step.getText().toString();
                List<Step> stepList = getStepList();
                if(step_name.trim().isEmpty()) return;
                for(Step step: stepList) {
                    if(step.getName().equals(step_name.trim())) {
                        createToast("Duplicate step", R.drawable.baseline_warning_24);
                        return;
                    }
                }
                boolean result = databaseHelper.addStep(new Step(-1, task.getTask_id(), step_name, false));
                if(result) createToast("Create Success", R.drawable.baseline_check_circle_24);
                initUI();
            }
        });
        ll_add_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_add_step_edit_text.setVisibility(View.VISIBLE);
                ll_add_step_btn.setVisibility(View.GONE);
                tv_complete_step_btn.setVisibility(View.VISIBLE);
                img_more.setVisibility(View.GONE);
                et_add_new_step.requestFocus();
                et_add_new_step.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(et_add_new_step, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        ll_add_to_my_day_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = dateFormat.format(calendar.getTime());

                if (task.getDate().isEmpty()) {
                    task.setDate(currentDate);
                    img_add_to_my_day.setImageResource(R.drawable.round_brightness_7_24);
                    tv_add_to_my_day.setTextColor(ContextCompat.getColor(DetailTaskActivity.this, R.color.royal_blue));
                    createToast("Added to my day", R.drawable.baseline_check_circle_24);
                }
                else {
                    task.setDate("");
                    img_add_to_my_day.setImageResource(R.drawable.baseline_brightness_7_24);
                    tv_add_to_my_day.setTextColor(ContextCompat.getColor(DetailTaskActivity.this, R.color.gray));
                    createToast("Removed from my day", R.drawable.baseline_check_circle_24);
                }
                databaseHelper.updateTask(task);
                initUI();
            }
        });
        tv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDescription();
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDelete(task.getName());
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void showDialogBottom(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog_bottom);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogBottomAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        //signing variable
        TextView tv_update_btn = dialog.findViewById(R.id.tv_update_btn);
        TextView tv_delete_btn = dialog.findViewById(R.id.tv_delete_btn);
        tv_update_btn.setText("Update task name");
        tv_delete_btn.setText("Delete task");
        tv_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showUpdateDialog();
            }
        });
        tv_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showDialogDelete(task.getName());
            }
        });
    }
    private void showDialogDelete(String name){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog_delete);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        //Text View
        TextView tv_delete_name = dialog.findViewById(R.id.tv_delete_name);
        TextView tv_action_no = dialog.findViewById(R.id.tv_action_no);
        TextView tv_action_yes = dialog.findViewById(R.id.tv_action_yes);
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
                dialog.dismiss();
                deleteAllStepByTaskId(task.getTask_id());
                boolean result = databaseHelper.deleteTask(task);
                if(result) createToast("Deleted " + name, R.drawable.baseline_check_circle_24);
                Intent task_activity = new Intent(DetailTaskActivity.this, TaskActivity.class);
                task_activity.putExtra("list_work_object", listWork);
                startActivity(task_activity);
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
        TextView tv_header_add = dialog.findViewById(R.id.tv_header_add);
        EditText et_add_new_task = dialog.findViewById(R.id.et_add_new);
        Button btn_add_new_task = dialog.findViewById(R.id.btn_add_new);
        Button btn_cancel_dialog = dialog.findViewById(R.id.btn_cancel_dialog);

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
                String new_task_name = et_add_new_task.getText().toString();
                if(new_task_name.trim().isEmpty()) return;
                task.setName(new_task_name);
                boolean result = databaseHelper.updateTask(task);
                if (result) createToast("Change name successful", R.drawable.baseline_check_circle_24);
                dialog.dismiss();
                initUI();
            }
        });
    }

    private void showDialogDescription(){
        //Define dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_custom_dialog_create_description);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogBottomAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView dialog_note_task_name = dialog.findViewById(R.id.dialog_note_task_name);
        TextView tv_dialog_complete_note_btn = dialog.findViewById(R.id.tv_dialog_complete_note_btn);
        EditText et_add_description_task = dialog.findViewById(R.id.et_add_description_task);
        dialog_note_task_name.setText(task.getName());
        tv_dialog_complete_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String description = et_add_description_task.getText().toString().trim();
                task.setDescription(description);
                boolean result = databaseHelper.updateTask(task);
                if (result) createToast("Text Note successes", R.drawable.baseline_check_circle_24);
                initUI();
            }
        });
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

    private List<Step> getStepList() {
        List<Step> list = databaseHelper.getAllStep();
        List<Step> stepList = new ArrayList<>();
        for(Step step: list) {
            if(step.getTask_id() == task.getTask_id()) {
                stepList.add(step);
            }
        }
        return stepList;
    }

    private ListWork getListWorkById(int id) {
        List<ListWork> listWorkList = databaseHelper.getAllListWork();
        for(ListWork listWork: listWorkList) {
            if(listWork.getList_work_id() == id) {
                return listWork;
            }
        }
        return null;
    }
    private void deleteAllStepByTaskId(int task_id){
        List<Step> stepList = databaseHelper.getAllStep();
        for(Step step: stepList) {
            if(step.getTask_id() == task_id) {
                databaseHelper.deleteStep(step);
            }
        }
    }
}