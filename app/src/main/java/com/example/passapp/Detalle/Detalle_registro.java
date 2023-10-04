package com.example.passapp.Detalle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passapp.BaseDeDatos.BDHelper;
import com.example.passapp.BaseDeDatos.Constants;
import com.example.passapp.R;

import java.util.Calendar;
import java.util.Locale;

public class Detalle_registro extends AppCompatActivity {
    TextView D_Titulo, D_Cuenta, D_NombreUsuario, D_Password, D_SitioWeb, D_Nota, D_Tiempo_registro, D_Tiempo_actualizacion;
    ImageView D_Imagen;
    String id_registro;
    BDHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_registro);

        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();
        id_registro = intent.getStringExtra("Id_registro");
        Toast.makeText(this,"Id del registro: "+id_registro,Toast.LENGTH_SHORT).show();

        /*Inicializamos la BD*/
        helper = new BDHelper(this);

        InicializarVariables();
        MostrarInformacionRegistro();

        /*Obtener titulo del registro*/
        String titulo_registro = D_Titulo.getText().toString();
        assert actionBar != null;
        actionBar.setTitle(titulo_registro);
        /*Esto hara posible que se cree la flecha dentro del action bar*/
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
    private void InicializarVariables(){
        D_Titulo = findViewById(R.id.D_Titulo);
        D_Cuenta = findViewById(R.id.D_Cuenta);
        D_NombreUsuario = findViewById(R.id.D_NombreUsuario);
        D_Password = findViewById(R.id.D_Password);
        D_SitioWeb = findViewById(R.id.D_SitioWeb);
        D_Nota = findViewById(R.id.D_Nota);
        D_Tiempo_registro = findViewById(R.id.D_Tiempo_registro);
        D_Tiempo_actualizacion = findViewById(R.id.D_Tiempo_actualizacion);
        D_Imagen = findViewById(R.id.D_Imagen);

    }
    private void MostrarInformacionRegistro() {
        /*Esta consulta va a comparar los id que contiene cada registro con el id que estamos recuperando SI SON IGUALES SE MUESTRA LA INFORMACION*/
        String consulta = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\"" + id_registro + "\"";

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(consulta, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID));
                @SuppressLint("Range") String titulo = "" + cursor.getString(cursor.getColumnIndex(Constants.C_TITULO));
                @SuppressLint("Range") String cuenta = "" + cursor.getString(cursor.getColumnIndex(Constants.C_CUENTA));
                @SuppressLint("Range") String nombre_usuario = "" + cursor.getString(cursor.getColumnIndex(Constants.C_NOMBRE_USUARIO));
                @SuppressLint("Range") String password = "" + cursor.getString(cursor.getColumnIndex(Constants.C_PASSWORD));
                @SuppressLint("Range") String sitio_web = "" + cursor.getString(cursor.getColumnIndex(Constants.C_SITIO_WEB));
                @SuppressLint("Range") String nota = "" + cursor.getString(cursor.getColumnIndex(Constants.C_NOTA));
                @SuppressLint("Range") String imagen = ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGEN));
                @SuppressLint("Range") String t_registro = "" + cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_REGISTRO));
                @SuppressLint("Range") String t_actualizacion = "" + cursor.getString(cursor.getColumnIndex(Constants.C_TIEMPO_ACTUALIZACION));

                /*Convertiremos a (DD/MM/AA) EL TIEMPO*/

                /*tiempo registro*/
                Calendar calendar_t_r = Calendar.getInstance(Locale.getDefault());
                calendar_t_r.setTimeInMillis(Long.parseLong(t_registro));

                String tiempo_registro = "" + DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar_t_r);

                /*tiempo actualizacion*/
                Calendar calendar_t_a = Calendar.getInstance(Locale.getDefault());
                calendar_t_a.setTimeInMillis(Long.parseLong(t_actualizacion));

                String tiempo_actualizacion = "" + DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar_t_a);

                /*setear la informacion en las vistas*/
                D_Titulo.setText(titulo);
                D_Cuenta.setText(cuenta);
                D_NombreUsuario.setText(nombre_usuario);
                D_Password.setText(password);
                D_SitioWeb.setText(sitio_web);
                D_Nota.setText(nota);
                D_Tiempo_registro.setText(tiempo_registro);
                D_Tiempo_actualizacion.setText(tiempo_actualizacion);

                if (imagen.equals("null")){
                    D_Imagen.setImageResource(R.drawable.imagen);
                }else{
                    D_Imagen.setImageURI(Uri.parse(imagen));
                }


            } while (cursor.moveToNext());
        }
        db.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}