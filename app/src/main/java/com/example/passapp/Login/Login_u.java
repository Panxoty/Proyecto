package com.example.passapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.passapp.MainActivity;
import com.example.passapp.R;

public class Login_u extends AppCompatActivity {
    EditText EtPasswordU;
    Button btnIngresar, btnInicioSesionBiometrico;
    SharedPreferences sharedPreferences;

    ImageButton Ib_Aviso;
    private static final String SHARED_PREF = "mi_pref";
    private static final String KEY_PASSWORD = "password";
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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
        
        biometricPrompt = new BiometricPrompt(Login_u.this, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(Login_u.this, "No existen huellas dactilares registradas", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(Login_u.this, "Autenticación biométrica exitosa, ¡Bienvenido(a)!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login_u.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(Login_u.this, "Falló la autenticación", Toast.LENGTH_SHORT).show();

            }
        });
        /*Configurar comportamiento del aviso biometrico*/
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setNegativeButtonText("Cancelar")
                .build();

        btnInicioSesionBiometrico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        Ib_Aviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login_u.this);
                builder.setTitle("Importante");
                builder.setMessage("Esta funcionalidad solo esta disponible con huellas dactilares previamente registradas en el dispositivo.");
                builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
    }
    private void InicializarVariables(){
        EtPasswordU = findViewById(R.id.EtPasswordU);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnInicioSesionBiometrico = findViewById(R.id.btnInicioSesionBiometrico);
        sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
        Ib_Aviso = findViewById(R.id.Ib_Aviso);
    }

}