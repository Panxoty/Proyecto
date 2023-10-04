package com.example.passapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.example.passapp.Fragmentos.F_Ajustes;
import com.example.passapp.Fragmentos.F_Todas;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    boolean DobleToqueParaSalir = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view); //Llamamos al nav_view creado en activity_main.xml
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null); //Para que los iconos mantengan el color original

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer , toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*Fragmento al ejecutar la aplicacion*/
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new F_Todas()).commit();
            navigationView.setCheckedItem(R.id.Opcion_Todas);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Opcion_Todas){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new F_Todas()).commit();
        }
        if(id == R.id.Opcion_Ajustes){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new F_Ajustes()).commit();
        }
        if(id== R.id.Opcion_Salir){
            Toast.makeText(this, "Cerraste sesión", Toast.LENGTH_SHORT).show();

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(DobleToqueParaSalir){ /*En esta condicion establecemos que si dobletoque es verdadero nos sacara de la app*/
            super.onBackPressed();
            Toast.makeText(this,"Saliste de la Aplicacion", Toast.LENGTH_SHORT).show();
            return;
        }

        /*Al presionar una vez en el boton de retroseso*/
        this.DobleToqueParaSalir = true;
        Toast.makeText(this,"Presione 2 veces para salir", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override /*Si pasan 2 segundos la variable pasaria a falso y tendria que volver a dar 2 toques*/
            public void run() {
                DobleToqueParaSalir = false;
            }
        },2000);

    }
}