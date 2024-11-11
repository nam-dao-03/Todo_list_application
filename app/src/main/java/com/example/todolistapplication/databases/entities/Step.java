package com.example.todolistapplication.databases.entities;

import java.io.Serializable;

public class Step implements Serializable {
    //Table name
    public static final String TABLE_NAME = "step";
    //Columns name
    public static final String COLUMN_ID = "step_id";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IS_DONE = "is_done";

    //Variables
    private int step_id;
    private int task_id;
    private String name;
    private boolean is_done;

    public Step(int step_id, int task_id, String name,boolean is_done) {
        this.is_done = is_done;
        this.name = name;
        this.step_id = step_id;
        this.task_id = task_id;
    }

    public boolean isIs_done() {
        return is_done;
    }

    public void setIs_done(boolean is_done) {
        this.is_done = is_done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStep_id() {
        return step_id;
    }

    public void setStep_id(int step_id) {
        this.step_id = step_id;
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
                    COLUMN_TASK_ID + " INTEGER," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_IS_DONE + " BOOL," +
                    "FOREIGN KEY (" + COLUMN_TASK_ID + ") REFERENCES " +
                    Task.TABLE_NAME + " (" + Task.COLUMN_ID + "));";

    public static final String GET_STEP_LIST = "SELECT * FROM " + TABLE_NAME;

}
