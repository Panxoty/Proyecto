package com.example.passapp.Fragmentos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passapp.BaseDeDatos.BDHelper;
import com.example.passapp.BaseDeDatos.Constants;
import com.example.passapp.MainActivity;
import com.example.passapp.Modelo.Password;
import com.example.passapp.R;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class F_Ajustes extends Fragment {


    TextView Eliminar_Todos_Registros, Exportar_Archivo, Importar_Archivo;
    Dialog dialog;
    BDHelper bdHelper;

    String ordenarTituloAsc = Constants.C_TITULO + " ASC";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f__ajustes, container, false);

        Eliminar_Todos_Registros = view.findViewById(R.id.Eleminar_Todos_Registros);
        Exportar_Archivo = view.findViewById(R.id.Exportar_Archivo);
        Importar_Archivo = view.findViewById(R.id.Importar_Archivo);
        dialog = new Dialog(getActivity());
        bdHelper = new BDHelper(getActivity());
        Eliminar_Todos_Registros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_Eliminar_Registros();
            }
        });
        Exportar_Archivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"Exportar archivo",Toast.LENGTH_SHORT).show();
                ExportarRegistro();
            }
        });

        Importar_Archivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("¿Importar CSV?");
                builder.setMessage("¡Se eliminaran todos los registros actuales!.");
                builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bdHelper.EliminarTodosRegistros();
                        ImportarRegistro();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(),"Importacion cancelada",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
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

    private void ExportarRegistro() {
        //Creamos el nombre de la carpeta donde almacenaremos nuestros registros
        File carpeta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Password App");
        boolean carpetaCreada = false;

        if(!carpeta.exists()){
            //Si la carpeta no existe, creamos una nueva
            carpetaCreada = carpeta.mkdirs();
        }
        //Nombre del archivo
        String csvnombreArchivo = "Registros.csv";
        //Concatenamos el nombre de la carpeta y del archivo
        String Carpeta_Archivo = carpeta + "/" + csvnombreArchivo;

        /*Obtenemos el registro que vamos a exportar*/
        ArrayList<Password> registroList = new ArrayList<>();
        registroList = bdHelper.ObtenerTodosLosRegistros(ordenarTituloAsc);

        try {
            //Escribir en el archivo
            FileWriter fileWriter = new FileWriter(Carpeta_Archivo);
            for (int i = 0; i < registroList.size(); i++){
                fileWriter.append("" + registroList.get(i).getId());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getTitulo());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getCuenta());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getNombre_usuario());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getPassword());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getSitio_web());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getNota());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getImagen());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getT_registro());
                fileWriter.append(",");
                fileWriter.append("" + registroList.get(i).getT_actualizacion());
                fileWriter.append("\n");
            }
            fileWriter.flush();
            fileWriter.close();
            Toast.makeText(getActivity(), "Se ha exportado el archivo exitosamente!",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void ImportarRegistro() {
        //Establecemos la ruta donde se encuentra almacenado el registro
        String Carpeta_Archivo = Environment.getExternalStorageDirectory()+ "/Documents/" + "/Password App/" + "Registros.csv";
        File file = new File(Carpeta_Archivo);
        if(file.exists()){
            //Si el respaldo existe
            try {
                CSVReader csvReader = new CSVReader(new FileReader(file.getAbsoluteFile()));
                String[] nextLine;
                while (( nextLine = csvReader.readNext() ) !=null){
                    String ids = nextLine[0];
                    String titulo = nextLine[1];
                    String cuenta = nextLine[2];
                    String nombre_usuario = nextLine[3];
                    String password = nextLine[4];
                    String sitio_web = nextLine[5];
                    String nota = nextLine[6];
                    String imagen = nextLine[7];
                    String tiempoR = nextLine[8];
                    String tiempoA = nextLine[9];
                    long id = bdHelper.insertarRegistro(
                            ""+titulo,
                            ""+cuenta,
                            ""+nombre_usuario,
                            ""+password,
                            ""+sitio_web,
                            ""+nota,
                            ""+imagen,
                            ""+tiempoR,
                            ""+tiempoA);
                }
                Toast.makeText(getActivity(),"Archivo csv importado con exito", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getActivity(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getActivity(),"No existe un respaldo",Toast.LENGTH_SHORT).show();
        }

    }

}