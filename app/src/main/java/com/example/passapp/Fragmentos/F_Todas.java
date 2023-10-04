package com.example.passapp.Fragmentos;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passapp.Adaptador.Adaptador_password;
import com.example.passapp.BaseDeDatos.BDHelper;
import com.example.passapp.BaseDeDatos.Constants;
import com.example.passapp.OpcionesPassword.Agregar_Actualizar_Registro;
import com.example.passapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class F_Todas extends Fragment {

    BDHelper helper;
    RecyclerView recyclerView_Registros;
    FloatingActionButton FAB_AgregarPassword;

    Dialog dialog;
    Dialog dialog_ordenar;

    String  ordenarNuevos = Constants.C_TIEMPO_REGISTRO + " DESC";
    String ordenarPasados = Constants.C_TIEMPO_REGISTRO + " ASC";
    String ordenarTituloAsc = Constants.C_TITULO + " ASC";
    String OrdenarTituloDes = Constants.C_TITULO + " DESC";

    String EstadoOrden = ordenarTituloAsc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_f__todas, container, false);
        recyclerView_Registros = view.findViewById(R.id.recyclerView_Registros); /*Implementamos el recicler view en el fragmento*/
        FAB_AgregarPassword = view.findViewById(R.id.FAB_AgregarPassword);
        helper = new BDHelper(getActivity());
        dialog = new Dialog(getActivity());
        dialog_ordenar = new Dialog(getActivity());

        CargarRegistros(ordenarTituloAsc);

        FAB_AgregarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**/
               Intent intent = new Intent(getActivity(), Agregar_Actualizar_Registro.class);
               intent.putExtra("MODO_EDICION",false);
               startActivity(intent);
            }
        });
        return view;
    }

    private void CargarRegistros(String orderby) {
        EstadoOrden = orderby;
        Adaptador_password adaptadorPassword = new Adaptador_password(getActivity(),helper.ObtenerTodosLosRegistros(
                orderby));
        recyclerView_Registros.setAdapter(adaptadorPassword);
    }
    private  void BuscarRegistros(String consulta){
        Adaptador_password adaptadorPassword = new Adaptador_password(getActivity(), helper.BuscarRegistros(consulta));
        recyclerView_Registros.setAdapter(adaptadorPassword);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //Implementamos el menu al fragmento
        inflater.inflate(R.menu.menu_fragmento_todas, menu);
        MenuItem item = menu.findItem(R.id.menu_Buscar_registros);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            /*Este metodo se ejecuta cuando los usuarios presionan en el boton de busqueda*/
            public boolean onQueryTextSubmit(String query) {
                BuscarRegistros(query);
                return true;
            }

            @Override
            /*Nos permite realizar la busqueda de un registro conforme el usuario este escribiendo en el teclado*/
            public boolean onQueryTextChange(String newText) {
                BuscarRegistros(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_Ordenar_registros){
            Ordenar_Registros();
            return true;
        }
        if (id==R.id.menu_Numero_registros){
            Visualizar_Total_Registros();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //De esta manera se vizualiza el menu en nuestro proyecto
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        CargarRegistros(EstadoOrden);
        super.onResume();
    }

    private void Visualizar_Total_Registros(){
        TextView Total;
        Button BtnEntendidoTotal;

        //Realizamos conexion del cuadro dialogo

        dialog.setContentView(R.layout.cuadro_dialogo_total_registros);
        Total = dialog.findViewById(R.id.Total);
        BtnEntendidoTotal = dialog.findViewById(R.id.btnEntendidoTotal);

        //Obtener el valor entero de registros
        int total = helper.ObtenerNumeroDeRegistros();
        //Convertir a cadena el numero total de registros
        String total_string = String.valueOf(total);
        Total.setText(total_string);

        //Asignamos evento a nuestro boton
        BtnEntendidoTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cuando presionemos el boton se cierra el cuadro de dialogo
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }

    private void Ordenar_Registros(){
        Button Btn_Nuevos, Btn_Pasados, Btn_Asc, Btn_Des ;

        dialog_ordenar.setContentView(R.layout.cuadro_dialogo_ordenar_registro);

        Btn_Nuevos = dialog_ordenar.findViewById(R.id.Btn_Nuevos);
        Btn_Pasados = dialog_ordenar.findViewById(R.id.Btn_Pasados);
        Btn_Asc = dialog_ordenar.findViewById(R.id.Btn_Asc);
        Btn_Des = dialog_ordenar.findViewById(R.id.Btn_Des);

        /*Asignamos eventos a los botones*/
        Btn_Nuevos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarRegistros(ordenarNuevos);
                dialog_ordenar.dismiss();
            }
        });
        Btn_Pasados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarRegistros(ordenarPasados);
                dialog_ordenar.dismiss();
            }
        });
        Btn_Asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarRegistros(ordenarTituloAsc);
                dialog_ordenar.dismiss();
            }
        });
        Btn_Des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarRegistros(OrdenarTituloDes);
                dialog_ordenar.dismiss();
            }
        });
        dialog_ordenar.show();
        dialog_ordenar.setCancelable(true);

    }
}