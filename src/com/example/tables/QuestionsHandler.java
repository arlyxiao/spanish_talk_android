package com.example.tables;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class QuestionsHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "spanish_talk";
 
    // Questions table name
    private static final String TABLE_QUESTIONS = "questions";
 
    // Questions Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
 
    public QuestionsHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new question
    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, question.getTitle()); // Question Title
        values.put(KEY_CONTENT, question.getContent()); // Question Content
 
        // Inserting Row
        db.insert(TABLE_QUESTIONS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single question
    Question getQuestion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_QUESTIONS, new String[] { KEY_ID,
                KEY_TITLE, KEY_CONTENT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Question question = new Question(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return question
        return question;
    }
 
    // Getting All Questions
    public List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTIONS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setID(Integer.parseInt(cursor.getString(0)));
                question.setTitle(cursor.getString(1));
                question.setContent(cursor.getString(2));
                // Adding question to list
                questionList.add(question);
            } while (cursor.moveToNext());
        }
 
        // return question list
        return questionList;
    }
 
    // Updating single question
    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, question.getTitle());
        values.put(KEY_CONTENT, question.getContent());
 
        // updating row
        return db.update(TABLE_QUESTIONS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getID()) });
    }
 
    // Deleting single question
    public void deleteQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(question.getID()) });
        db.close();
    }
 
    // Getting questions Count
    public int getQuestionsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_QUESTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}