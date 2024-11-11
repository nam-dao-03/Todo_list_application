package com.example.todolistapplication.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolistapplication.databases.entities.ListWork;
import com.example.todolistapplication.databases.entities.Step;
import com.example.todolistapplication.databases.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo_list_application_db";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ListWork.CREATE_TABLE);
        db.execSQL(Task.CREATE_TABLE);
        db.execSQL(Step.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ListWork.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Task.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Step.TABLE_NAME);

        onCreate(db);
    }
    //ListWork
    public boolean addListWork(@NonNull ListWork listWork) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ListWork.COLUMN_NAME, listWork.getName());

        long insert = db.insert(ListWork.TABLE_NAME, null, cv);
        return insert != -1;
    }
    public List<ListWork> getAllListWork(){
        List<ListWork> listWorkList = new ArrayList<>();
        String queryString = ListWork.GET_LIST_WORK_LIST;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int list_work_id = cursor.getInt(0);
                String name = cursor.getString(1);

                ListWork newListWork = new ListWork(list_work_id, name);
                listWorkList.add(newListWork);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listWorkList;
    }
    public boolean updateListWork(@NonNull ListWork listWork){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ListWork.COLUMN_NAME, listWork.getName());

        long update = db.update(ListWork.TABLE_NAME, cv, ListWork.COLUMN_ID + " = ?", new String[]{String.valueOf(listWork.getList_work_id())});
        return update > 0;
    }
    public boolean deleteListWork(@NonNull ListWork listWork) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = true;
        int delete = db.delete(ListWork.TABLE_NAME, ListWork.COLUMN_ID + " = ?", new String[]{String.valueOf(listWork.getList_work_id())});
        if(delete == 0) result = false;
        db.close();
        return result;
    }

    //Task
    public boolean addTask(@NonNull Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Task.COLUMN_LIST_WORK_ID, task.getList_work_id());
        cv.put(Task.COLUMN_NAME, task.getName());
        cv.put(Task.COLUMN_IS_DONE, task.isIs_done());
        cv.put(Task.COLUMN_IS_IMPORTANT, task.isIs_important());
        cv.put(Task.COLUMN_DATE, task.getDate());
        cv.put(Task.COLUMN_DESCRIPTION, task.getDescription());

        long insert =  db.insert(Task.TABLE_NAME, null, cv);
        return insert != -1;
    }
    public List<Task> getAllTask(){
        List<Task> taskList = new ArrayList<>();
        String queryString = Task.GET_TASK_LIST;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do{
                int task_id = cursor.getInt(0);
                int list_work_id = cursor.getInt(1);
                String name = cursor.getString(2);
                boolean is_done = cursor.getInt(3) == 1;
                boolean is_important = cursor.getInt(4) == 1;
                String date = cursor.getString(5);
                String description = cursor.getString(6);
                Task newTask = new Task(task_id, list_work_id, name, is_done, is_important, date,description);
                taskList.add(newTask);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return taskList;
    }
    public boolean updateTask(@NonNull Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Task.COLUMN_LIST_WORK_ID, task.getList_work_id());
        cv.put(Task.COLUMN_NAME, task.getName());
        cv.put(Task.COLUMN_IS_DONE, task.isIs_done());
        cv.put(Task.COLUMN_IS_IMPORTANT, task.isIs_important());
        cv.put(Task.COLUMN_DATE, task.getDate());
        cv.put(Task.COLUMN_DESCRIPTION, task.getDescription());

        long update = db.update(Task.TABLE_NAME, cv, Task.COLUMN_ID + " = ?", new String[]{String.valueOf(task.getTask_id())});
        return update > 0;
    }
    public boolean deleteTask(@NonNull Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = true;
        int delete = db.delete(Task.TABLE_NAME, Task.COLUMN_ID + " = ?", new String[]{String.valueOf(task.getTask_id())});
        if(delete == 0) result = false;
        db.close();
        return result;
    }

    //Step
    public boolean addStep(@NonNull Step step) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Step.COLUMN_TASK_ID, step.getTask_id());
        cv.put(Step.COLUMN_NAME, step.getName());
        cv.put(Step.COLUMN_IS_DONE, step.isIs_done());

        long insert =  db.insert(Step.TABLE_NAME, null, cv);
        return insert != -1;
    }
    public List<Step> getAllStep(){
        List<Step> stepList = new ArrayList<>();

        String queryString = Step.GET_STEP_LIST;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do{
                int step_id = cursor.getInt(0);
                int task_id = cursor.getInt(1);
                String name = cursor.getString(2);
                boolean is_done = cursor.getInt(3) == 1;
                Step newStep = new Step(step_id, task_id, name, is_done);
                stepList.add(newStep);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return stepList;
    }
    public boolean updateStep(@NonNull Step step){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Step.COLUMN_TASK_ID, step.getTask_id());
        cv.put(Step.COLUMN_NAME, step.getName());
        cv.put(Step.COLUMN_IS_DONE, step.isIs_done());

        long update = db.update(Step.TABLE_NAME, cv, Step.COLUMN_ID + " = ?", new String[]{String.valueOf(step.getStep_id())});
        return update > 0;
    }
    public boolean deleteStep(@NonNull Step step) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = true;
        int delete = db.delete(Step.TABLE_NAME, Step.COLUMN_ID + " = ?", new String[]{String.valueOf(step.getStep_id())});
        if(delete == 0) result = false;
        db.close();
        return result;
    }

}
