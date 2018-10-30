package com.didacsoftware.mybooks.BDSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // genera la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CampoTabla.CREAR_TABLA_BOOKS);


    }

    // verifica la versio y lo reemplasa
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {

        //db.execSQL("DROP TABLE IF EXISTS "+ Tablas.TABLA_BOOKS);

        //onCreate(db);
    }
}
