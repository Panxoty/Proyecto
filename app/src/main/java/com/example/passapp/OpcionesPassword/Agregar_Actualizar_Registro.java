package com.example.passapp.OpcionesPassword;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.passapp.BaseDeDatos.BDHelper;
import com.example.passapp.MainActivity;
import com.example.passapp.R;

public class Agregar_Actualizar_Registro extends AppCompatActivity {
    EditText EtTitulo,EtCuenta,EtNombreUsuario,EtPassword,EtSitioWeb,EtNota;
    ImageView Imagen;
    Button Btn_Adjuntar_Imagen;

    String id, titulo, cuenta, nombre_usuario, password, sitio_web, nota, tiempo_registro, tiempo_actualizacion;

    private boolean MODO_EDICION = false;

    private BDHelper bdHelper;
    Uri imagenUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarr_password);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");

        InicializarVariables();

        ObtenerInformacion();

        Btn_Adjuntar_Imagen.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                TomarFoto();
            }else{
                SolicitudPermisoCamara.launch(Manifest.permission.CAMERA);
            }

        });
    }



    private void InicializarVariables(){
        EtTitulo = findViewById(R.id.EtTitulo);
        EtCuenta= findViewById(R.id.EtCuenta);
        EtNombreUsuario= findViewById(R.id.EtNombreUsuario);
        EtPassword = findViewById(R.id.EtPassword);
        EtSitioWeb = findViewById(R.id.EtSitioWeb);
        EtNota = findViewById(R.id.EtNota);

        Imagen = findViewById(R.id.Imagen);
        Btn_Adjuntar_Imagen = findViewById(R.id.Btn_Adjuntar_Imagen);

        bdHelper = new BDHelper(this);
    }

    private void ObtenerInformacion(){
        Intent intent = getIntent();
        MODO_EDICION = intent.getBooleanExtra("MODO_EDICION",false);

        if (MODO_EDICION ){
            //Verdadero OBTENEMOS LOS DATOS

            id = intent.getStringExtra("ID");
            titulo = intent.getStringExtra("TITULO");
            cuenta = intent.getStringExtra("CUENTA");
            nombre_usuario = intent.getStringExtra("NOMBRE_USUARIO");
            password = intent.getStringExtra("PASSWORD");
            sitio_web = intent.getStringExtra("SITIO_WEB");
            nota = intent.getStringExtra("NOTA");
            imagenUri = Uri.parse(intent.getStringExtra("IMAGEN"));
            tiempo_registro = intent.getStringExtra("T_REGISTRO");
            tiempo_actualizacion = intent.getStringExtra("T_ACTUALIZACION");
            //SETEAMOS LA INFORMACION EN LAS VISTAS
            EtTitulo.setText(titulo);
            EtCuenta.setText(cuenta);
            EtNombreUsuario.setText(nombre_usuario);
            EtPassword.setText(password);
            EtSitioWeb.setText(sitio_web);
            EtNota.setText(nota);
            /*Si la imagen no existe */
            if (imagenUri.toString().equals("null")){
                Imagen.setImageResource(R.drawable.imagen);
            }
            /*Si la imagen existe*/
            else{
                Imagen.setImageURI(imagenUri);
            }

        }else{
            //Falso el usuario agrega nuevo registro


        }
    }

    /*Metodo el cual nos permite realizar el registro de contraseñas*/
    private void Agregar_Actualizar_R(){
        /*Obtenemos datos de entrada*/
        titulo = EtTitulo.getText().toString().trim();
        cuenta = EtCuenta.getText().toString().trim();
        nombre_usuario = EtNombreUsuario.getText().toString().trim();
        password = EtPassword.getText().toString().trim();
        sitio_web = EtSitioWeb.getText().toString().trim();
        nota = EtNota.getText().toString().trim();

        if(MODO_EDICION){
            //Si es verdadero = Actualizar el registro
            /*Tiempo del dispositivo*/
            String tiempo_actual = ""+ System.currentTimeMillis();
            bdHelper.actualizarRegistro(
                    ""+id,
                    "" + titulo,
                    ""+cuenta,
                    "" + nombre_usuario,
                    ""+password,
                    ""+sitio_web,
                    ""+ nota,
                    ""+ imagenUri,
                    ""+ tiempo_registro,
                    ""+tiempo_actual
            );
            Toast.makeText(this, "Datos Actualizados con éxito",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Agregar_Actualizar_Registro.this, MainActivity.class));
            finish();

        }else{
            //Agregar un nuevo registro

            /*Establecemos una condicion si el campo titulo esta vacio no se podra realizar el registro*/
            if(!titulo.equals("")){
                /*Obtenemos el tiempo del dispositivo | Para asi añadirlo en la bd tiempo registro*/
                String tiempo = ""+System.currentTimeMillis();
                long id = bdHelper.insertarRegistro(
                        "" + titulo,
                        "" + cuenta,
                        "" + nombre_usuario,
                        "" + password,
                        "" + sitio_web,
                        "" + nota,
                        ""+imagenUri,
                        "" + tiempo,
                        "" + tiempo
                );
                Toast.makeText(this,"Se ha guardado con éxito: " ,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Agregar_Actualizar_Registro.this, MainActivity.class)); //PARA QUE NOS DIRIGA AL MAIN CUANDO GUARDEMOS
            }
            else{
                EtTitulo.setError("Campo Obligatorio");
                EtTitulo.setFocusable(true);
            }
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_guardar, menu); /*Se llama el menu guardar para que aparezca en el activity*/
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.Guardar_Password){
            Agregar_Actualizar_R();
        }
        return super.onOptionsItemSelected(item);
    }
    private void TomarFoto() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nueva Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion");
        imagenUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);

        camaraActivityResultLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent>camaraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Imagen.setImageURI(imagenUri);
                    }
                    else{
                        Toast.makeText(Agregar_Actualizar_Registro.this,"Cancelado por el usuario",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private final ActivityResultLauncher<String> SolicitudPermisoCamara =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), Concede_permiso ->{
                if (Concede_permiso){
                    TomarFoto();
                }else{
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });
}