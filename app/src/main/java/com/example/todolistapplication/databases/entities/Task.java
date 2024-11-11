package com.example.todolistapplication.databases.entities;

import java.io.Serializable;

public class Task implements Serializable {
    //Table name
    public static final String TABLE_NAME = "task";
    //Columns name
    public static final String COLUMN_ID = "task_id";
    public static final String COLUMN_LIST_WORK_ID = "list_work_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IS_DONE = "is_done";
    public static final String COLUMN_IS_IMPORTANT = "is_important";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "description";

    //Variables
    private int task_id;
    private int list_work_id;
    private String name;
    private boolean is_done;
    private boolean is_important;
    private String date;
    private String description;

    public Task(int task_id , int list_work_id, String name, boolean is_done, boolean is_important, String date,String description) {
        this.description = description;
        this.is_done = is_done;
        this.is_important = is_important;
        this.list_work_id = list_work_id;
        this.date = date;
        this.name = name;
        this.task_id = task_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIs_important() {
        return is_important;
    }

    public void setIs_important(boolean is_important) {
        this.is_important = is_important;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_done() {
        return is_done;
    }

    public void setIs_done(boolean is_done) {
        this.is_done = is_done;
    }

    public int getList_work_id() {
        return list_work_id;
    }

    public void setList_work_id(int list_work_id) {
        this.list_work_id = list_work_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " +
                    TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LIST_WORK_ID + " INTEGER," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_IS_DONE + " BOOL," +
                    COLUMN_IS_IMPORTANT + " BOOL," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    "FOREIGN KEY (" + COLUMN_LIST_WORK_ID + ") REFERENCES " +
                    ListWork.TABLE_NAME + " (" + ListWork.COLUMN_ID + "));";

    public static final String GET_TASK_LIST = "SELECT * FROM " + TABLE_NAME;

}
