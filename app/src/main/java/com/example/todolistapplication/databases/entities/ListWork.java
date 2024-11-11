package com.example.todolistapplication.databases.entities;


import java.io.Serializable;

public class ListWork implements Serializable {
    //Table name
    public static final String TABLE_NAME = "list_work";
    //Columns name
    public static final String COLUMN_ID = "list_work_id";
    public static final String COLUMN_NAME = "name";
    //Variables
    private int list_work_id;
    private String name;

    public ListWork(int list_work_id, String name) {
        this.list_work_id = list_work_id;
        this.name = name;
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
    //SQL Query: Creating the table
    public static final String CREATE_TABLE =
            "CREATE TABLE " +
                    TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT" +
                    ")";

    public static final String GET_LIST_WORK_LIST = "SELECT * FROM " + TABLE_NAME;
}
