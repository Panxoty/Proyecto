package com.example.passapp.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.passapp.Modelo.Password;

import java.util.ArrayList;

public class BDHelper extends SQLiteOpenHelper {
    public BDHelper(@Nullable Context context) {
        super(context, Constants.BD_NAME, null, Constants.BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public long insertarRegistro(String titulo, String cuenta, String nombre_usuario, String password, String sitio_web, String nota, String imagen, String T_registro, String T_Actualizacion){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        /*INSERTAMOS LOS DATOS*/
        values.put(Constants.C_TITULO,titulo);
        values.put(Constants.C_CUENTA,cuenta);
        values.put(Constants.C_NOMBRE_USUARIO,nombre_usuario);
        values.put(Constants.C_PASSWORD,password);
        values.put(Constants.C_SITIO_WEB,sitio_web);
        values.put(Constants.C_NOTA,nota);
        values.put(Constants.C_IMAGEN,imagen);
        values.put(Constants.C_TIEMPO_REGISTRO,T_registro);
        values.put(Constants.C_TIEMPO_ACTUALIZACION,T_Actualizacion);
        /*INSETAR FILA*/
        long id = db.insert(Constants.TABLE_NAME,null,values);
        /*CERRAR CONEXION DE BD*/
        db.close();
        return id;
    }

    public void actualizarRegistro(String id, String titulo, String cuenta, String nombre_usuario, String password, String sitio_web, String nota, String imagen, String T_registro, String T_Actualizacion){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        /*INSERTAMOS LOS DATOS*/
        values.put(Constants.C_TITULO,titulo);
        values.put(Constants.C_CUENTA,cuenta);
        values.put(Constants.C_NOMBRE_USUARIO,nombre_usuario);
        values.put(Constants.C_PASSWORD,password);
        values.put(Constants.C_SITIO_WEB,sitio_web);
        values.put(Constants.C_NOTA,nota);
        values.put(Constants.C_IMAGEN,imagen);
        values.put(Constants.C_TIEMPO_REGISTRO,T_registro);
        values.put(Constants.C_TIEMPO_ACTUALIZACION,T_Actualizacion);
        /*ACTUALIZAR LA FILA*/
         db.update(Constants.TABLE_NAME,values,Constants.C_ID + " =? ", new String[]{id});
        /*CERRAR CONEXION DE BD*/
        db.close();

    }

    public ArrayList<Password> ObtenerTodosLosRegistros(String orderby){
        ArrayList<Password> passwordList = new ArrayList<>();
        //Consulta para seleccionar el registro
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderby;

        SQLiteDatabase db = this.getWritableDatabase();

        //Con el cursor recorremos todos los registros de la base de datos para que se pueda añadir a la lista
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                @SuppressLint("Range") Password modelo_password = new Password(
                        "" +cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TITULO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CUENTA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_NOMBRE_USUARIO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_PASSWORD)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_SITIO_WEB)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_NOTA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGEN)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_REGISTRO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_ACTUALIZACION)));
                passwordList.add(modelo_password);//De esta forma añadimos cada registro a la lista
            }while(cursor.moveToNext());
        }
        db.close();
        return passwordList;
    }
    public ArrayList<Password> BuscarRegistros(String consulta){
        ArrayList<Password> passwordList = new ArrayList<>();
        //Consulta para seleccionar el registro
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_TITULO + " LIKE '%" + consulta + "%'";

        SQLiteDatabase db = this.getWritableDatabase();

        //Con el cursor recorremos todos los registros de la base de datos para que se pueda añadir a la lista
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                @SuppressLint("Range") Password modelo_password = new Password(
                        "" +cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TITULO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CUENTA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_NOMBRE_USUARIO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_PASSWORD)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_SITIO_WEB)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_NOTA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGEN)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_REGISTRO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_ACTUALIZACION)));
                passwordList.add(modelo_password);//De esta forma añadimos cada registro a la lista
            }while(cursor.moveToNext());
        }
        db.close();
        return passwordList;
    }

    public int ObtenerNumeroDeRegistros(){
        String countquery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countquery,null);
        int contador = cursor.getCount();

        cursor.close();

        return contador;
    }

    public void EliminarRegistro(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.C_ID + " = ?", new String[]{id});
        db.close();
    }
    public void EliminarTodosRegistros(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.TABLE_NAME);
        db.close();

    }
}
