package com.example.vtr.googlemapisapi_nougat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
                "`descricao`TEXT NOT NULL" +
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
        db.insert(TB_ENDERECO, null, cv);
    }

    public void deleteCliente(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                TB_ENDERECO, "id = ?", new String[] {
                        String.valueOf(id)
                });
    }

    public List<Endereco> getAllClientes() {
        List<Endereco> enderecos = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(
                TB_ENDERECO,
                new String[]{"id, descricao"},
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            Endereco endereco = new Endereco();
            endereco.setId( cursor.getInt(0) );
            endereco.setDescricao( cursor.getString(1) );

            enderecos.add(endereco);
        }
        return enderecos;
    }
}
