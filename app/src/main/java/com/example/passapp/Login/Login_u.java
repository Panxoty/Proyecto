package com.example.passapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passapp.MainActivity;
import com.example.passapp.R;

public class Login_u extends AppCompatActivity {
    EditText EtPasswordU;
    Button btnIngresar;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF = "mi_pref";
    private static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InicializarVariables();
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String S_password = EtPasswordU.getText().toString().trim();
                //Obtener la contrasena contrasena maestra almacenada en sharedPreference
                String password_SP = sharedPreferences.getString(KEY_PASSWORD,null);

                if(S_password.equals("")){
                    Toast.makeText(Login_u.this, "Campo Obligatorio!", Toast.LENGTH_SHORT).show();
                } else if (!S_password.equals(password_SP)) {
                    Toast.makeText(Login_u.this, "La contraseña no es la correcta", Toast.LENGTH_SHORT).show();

                }else{
                    Intent intent = new Intent(Login_u.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
    private void InicializarVariables(){
        EtPasswordU = findViewById(R.id.EtPasswordU);
        btnIngresar = findViewById(R.id.btnIngresar);
        sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
    }

}