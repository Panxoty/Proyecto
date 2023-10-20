package com.example.passapp.Registro_usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passapp.Login.Login;
import com.example.passapp.MainActivity;
import com.example.passapp.R;

public class Registro extends AppCompatActivity {
    EditText EtPasswordU, EtCPassword;
    Button btnRegistrar;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF = "mi_pref";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_C_PASSWORD = "c_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        InicializarVariables();
        VerificarPasswordMaestra();

        //Asignamos un evento al boton para hacer el registro de la contra
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String S_password = EtPasswordU.getText().toString().trim();
                String S_C_password = EtCPassword.getText().toString().trim();

                //Validamos los campos
                /*Si el campo password esta vacio*/
                if(TextUtils.isEmpty(S_password)){
                    Toast.makeText(Registro.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                } else if (S_password.length()<6) {
                    Toast.makeText(Registro.this, "Contraseña muy corta", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(S_C_password)) {
                    Toast.makeText(Registro.this, "Confirme contraseña", Toast.LENGTH_SHORT).show();

                } else if (!S_password.equals(S_C_password)) {
                    Toast.makeText(Registro.this, "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                }else {
                    //Con esto pasamos los datos introducidos en los edit text al sharedpreference
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_PASSWORD,S_password);
                    editor.putString(KEY_C_PASSWORD,S_C_password);
                    editor.apply();

                    Toast.makeText(Registro.this, "KEY PASSWORD" + S_password, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Registro.this, "KEY_C_PASSWORD" + S_C_password, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Registro.this, MainActivity.class);
                    startActivity(intent);
                    finish();  //Para que el usuario salga de la app y no regrese a este apartado
                }
            }
        });
    }
    private void InicializarVariables(){
        EtPasswordU = findViewById(R.id.EtPasswordU);
        EtCPassword = findViewById(R.id.EtCPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
    }
    private void VerificarPasswordMaestra(){
        //Verificamos  si la contra fue creada previamente
        String mipassword = sharedPreferences.getString(KEY_PASSWORD,null);

        //Si el usuario ya tiene una contrasena maestra registrada que lo mande a otro activity e introduzca su contrasena
        if(mipassword !=null){
            Intent intent = new Intent(Registro.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}