package com.example.passapp.Fragmentos;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passapp.BaseDeDatos.BDHelper;
import com.example.passapp.MainActivity;
import com.example.passapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link F_Ajustes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class F_Ajustes extends Fragment {


    TextView Eliminar_Todos_Registros;
    Dialog dialog;
    BDHelper bdHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f__ajustes, container, false);

        Eliminar_Todos_Registros = view.findViewById(R.id.Eleminar_Todos_Registros);
        dialog = new Dialog(getActivity());
        bdHelper = new BDHelper(getActivity());
        Eliminar_Todos_Registros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_Eliminar_Registros();
            }
        });
        return view;
    }

    private void Dialog_Eliminar_Registros() {
        Button Btn_Si, Btn_Cancelar;

        dialog.setContentView(R.layout.cuadro_dialogo_eliminar_todos_registros);

        /*Inicializamos las vistas*/
        Btn_Si= dialog.findViewById(R.id.Btn_Si);
        Btn_Cancelar = dialog.findViewById(R.id.Btn_Cancelar);

        /*Asignamos evento*/
        Btn_Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bdHelper.EliminarTodosRegistros();
                startActivity(new Intent(getActivity(), MainActivity.class));
                Toast.makeText(getActivity(),"Se han eliminado todos las contraseñas correctamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Btn_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCancelable(false);
    }
}