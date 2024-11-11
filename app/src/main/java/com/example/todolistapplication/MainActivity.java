package com.example.todolistapplication;

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

import com.example.todolistapplication.adapters.ListWorkAdapter;
import com.example.todolistapplication.databases.DatabaseHelper;
import com.example.todolistapplication.databases.entities.ListWork;
import com.example.todolistapplication.databases.entities.Task;
import com.example.todolistapplication.interfaces.IClickItemListener;
import com.example.todolistapplication.task.TaskActivity;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //widgets for activity
    private ListWorkAdapter listWorkAdapter;
    private RecyclerView rcv_list_work_default;
    private RecyclerView rcv_list_work_custom;
    private CardView cv_add_new_list;
    //widget for dialog
    private TextView tv_header_add;
    private EditText et_add_new_list_work;
    private Button btn_add_new_list_work, btn_cancel_dialog;
    //Database
    private DatabaseHelper databaseHelper;
    //Objects
    private List<ListWork> listWorkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
        //Database
        databaseHelper = new DatabaseHelper(MainActivity.this);
        if (databaseHelper.getAllListWork().isEmpty()) addDefaultListWork();
    }

    private void initUI() {
        listWorkList = getListWorkList();
        //Widgets
        rcv_list_work_default = findViewById(R.id.rcv_list_work_default);
        rcv_list_work_custom = findViewById(R.id.rcv_list_work_custom);
        LinearLayoutManager linearLayoutDefaultListWorkManager = new LinearLayoutManager(MainActivity.this);
        LinearLayoutManager linearLayoutCustomListWorkManager = new LinearLayoutManager(MainActivity.this);
        rcv_list_work_default.setLayoutManager(linearLayoutDefaultListWorkManager);
        rcv_list_work_custom.setLayoutManager(linearLayoutCustomListWorkManager);
        listWorkAdapter = new ListWorkAdapter(getListWorkList(), true, getTaskList(), new IClickItemListener() {
            @Override
            public void onClickItem(Object object) {
                Intent taskActivity = new Intent(MainActivity.this, TaskActivity.class);
                taskActivity.putExtra("list_work_object", (ListWork) object);
                startActivity(taskActivity);
            }
        });
        rcv_list_work_default.setAdapter(listWorkAdapter);
        listWorkAdapter = new ListWorkAdapter(getListWorkList(), false, getTaskList(), new IClickItemListener() {
            @Override
            public void onClickItem(Object object) {
                Intent taskActivity = new Intent(MainActivity.this, TaskActivity.class);
                taskActivity.putExtra("list_work_object", (ListWork) object);
                startActivity(taskActivity);
            }
        });
        rcv_list_work_custom.setAdapter(listWorkAdapter);
        cv_add_new_list = findViewById(R.id.cv_add_new_list);

    }

    private void setListenerForWidgets() {
        cv_add_new_list.setOnClickListener(new View.OnClickListener() {
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
        et_add_new_list_work = dialog.findViewById(R.id.et_add_new);
        btn_add_new_list_work = dialog.findViewById(R.id.btn_add_new);
        btn_cancel_dialog = dialog.findViewById(R.id.btn_cancel_dialog);

        tv_header_add.setText("Add new list");
        et_add_new_list_work.setHint("New list");
        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_add_new_list_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String new_list_work = et_add_new_list_work.getText().toString();
                for (ListWork listWork: listWorkList) {
                    if(listWork.getName().trim().equals(new_list_work.trim())) {
                        createToast("Duplicate name list work", R.drawable.baseline_warning_24);
                        return;
                    }
                }
                boolean result = databaseHelper.addListWork(new ListWork(-1, new_list_work));
                if (result) createToast("Create Successful", R.drawable.baseline_check_circle_24);
                initUI();
            }
        });

    }
    private List<Task> getTaskList() {
        return databaseHelper.getAllTask();
    }
    private List<ListWork> getListWorkList() {
        return databaseHelper.getAllListWork();
    }

    private void addDefaultListWork() {
        databaseHelper.addListWork(new ListWork(-1, "My day"));
        databaseHelper.addListWork(new ListWork(-1, "Completed"));
        databaseHelper.addListWork(new ListWork(-1, "Important"));
        databaseHelper.addListWork(new ListWork(-1, "Tasks"));
    }

    private void createToast(String input_text_to_toast, int imageResId) {
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

}