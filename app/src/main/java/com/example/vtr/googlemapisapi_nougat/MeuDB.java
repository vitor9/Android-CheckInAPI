package com.example.vtr.googlemapisapi_nougat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeuDB extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "MeuDB";
    public static final int VERSION = 1;
    public static  final String TB_ENDERECO = "endereco";

    public MeuDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TB_ENDERECO + "(" +
                "`id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "`descricao`TEXT NOT NULL," +
                "`data`TEXT NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertEndereco(Endereco endereco) {
        SQLiteDatabase db = getWritableDatabase();
        // Dado usado para transferir valores no Android
        ContentValues cv = new ContentValues();
        cv.put("descricao", endereco.getDescricao());
        cv.put("data", endereco.getDateTime());
        db.insert(TB_ENDERECO, null, cv);
    }

    public void deleteCliente(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                TB_ENDERECO, "id = ?", new String[] {
                        String.valueOf(id)
                });
    }

    public List<Endereco> getAllEnderecos() {
        List<Endereco> enderecos = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(
                TB_ENDERECO,
                new String[]{"id, descricao"},
                null,
                null,
                null,
                null,
                "data DESC"
        );

        while(cursor.moveToNext()){
            Endereco endereco = new Endereco();
            endereco.setId( cursor.getInt(0) );
            endereco.setDescricao( cursor.getString(1) );

            enderecos.add(endereco);
        }
        return enderecos;
    }

    public String findLastEnderecoDescription() {
        SQLiteDatabase db = getWritableDatabase();
        String id = "1";
        Cursor cursor = null;
        String descricao = "Ainda não foi feito Check-In";
        try {
            cursor = db.rawQuery("SELECT descricao FROM " + TB_ENDERECO + " WHERE id=?", new String[] {id + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            }
            return descricao;
        }finally {
            cursor.close();
        }
    }

}
