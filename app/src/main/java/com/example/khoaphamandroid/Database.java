package com.example.khoaphamandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String NOTE_SQLITE = "note.sqlite";
    public static final String TABLE_CONGVIEC = "CONGVIEC";
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_TEN_CV = "TenCV";
    private Context context;

    public Database(@Nullable Context context) {
        super(context, NOTE_SQLITE, null, 1);
        this.context = context;
    }

    //truy vấn không trả về kết quả: CREATE, INSERT, UPDATE, DELETE...
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        //để thực thi lệnh cần gọi
        database.execSQL(sql);
    }

    //truy vấn có trả về kết quả: SELECT
    //dữ liệu trả về dưới dạng con trỏ từng dòng (cursor)
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    //Tìm kiếm bằng query
    public Cursor searchUsers(String text){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CONGVIEC + " WHERE " + COLUMN_TEN_CV + " Like '%"+ text +"%'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + TABLE_CONGVIEC + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TEN_CV + " TEXT)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONGVIEC);
        onCreate(sqLiteDatabase);
    }

    public boolean addCongViec(CongViec congViec){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TEN_CV, congViec.getTenCV());

        long insert = db.insert(TABLE_CONGVIEC, null, cv);
        db.close();
        if(insert == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean addCongViec(String tenCv){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TEN_CV, tenCv);

        long insert = db.insert(TABLE_CONGVIEC, null, cv);
        db.close();
        if(insert == -1){
            return false;
        }else {
            return true;
        }
    }

    public int UpdateCongViec(CongViec congViec){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TEN_CV,congViec.getTenCV());
        return db.update(TABLE_CONGVIEC,values,COLUMN_ID +"=?",
                new String[] { String.valueOf(congViec.getIdCV())});
    }

    public int UpdateCongViec(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TEN_CV, name);
        return db.update(TABLE_CONGVIEC,values,COLUMN_ID +"=?",
                new String[] { String.valueOf(id)});
    }

    public boolean deleteOneCongViec(CongViec congViec){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TABLE_CONGVIEC + " WHERE " + COLUMN_ID + " = " + congViec.getIdCV();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }

    // Delete a person by ID
    public void deleteCongViec(CongViec congViec) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONGVIEC, COLUMN_ID + " = ?",
                new String[] { String.valueOf(congViec.getIdCV()) });
        db.close();
    }

    public void  deleteCongViec(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONGVIEC, COLUMN_TEN_CV + " = ?",
                new String[] { String.valueOf(name) });
        db.close();
    }

    //Select a person by ID
    public CongViec getContactById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONGVIEC, new String[] { COLUMN_ID, COLUMN_TEN_CV },
                COLUMN_ID + "=?",new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        CongViec congViec = new CongViec(id , cursor.getString(1));
        cursor.close();
        db.close();
        return congViec;
    }

    public CongViec getContactByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONGVIEC, new String[] { COLUMN_ID, COLUMN_TEN_CV},
                COLUMN_TEN_CV + "=?",new String[] { String.valueOf(name) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        CongViec congViec = new CongViec(Integer.parseInt(cursor.getString(0).toString()) , name);
        cursor.close();
        db.close();
        return congViec;
    }

    public List<CongViec> getEveryone(){
        List<CongViec> returnList = new ArrayList<CongViec>();
        //get data from database
        String selectQuery = "SELECT * FROM " + TABLE_CONGVIEC;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                CongViec congViec = new CongViec();
                congViec.setIdCV(cursor.getInt(0));
                congViec.setTenCV(cursor.getString(1));
                returnList.add(congViec);
            }while (cursor.moveToNext());
        }else {

        }
        cursor.close();
        db.close();
        return returnList;
    }

    // Get Count person in Table Person
    public int getCongViecCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONGVIEC;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
}
