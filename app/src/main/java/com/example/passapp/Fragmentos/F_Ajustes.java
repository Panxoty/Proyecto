package com.example.passapp.Fragmentos;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passapp.BaseDeDatos.BDHelper;
import com.example.passapp.BaseDeDatos.Constants;
import com.example.passapp.Login.Login_u;
import com.example.passapp.MainActivity;
import com.example.passapp.Modelo.Password;
import com.example.passapp.R;
import com.opencsv.CSVReader;

import org.w3c.dom.Text;

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


    TextView Eliminar_Todos_Registros, Exportar_Archivo, Importar_Archivo, Cambiar_password_maestra;
    Dialog dialog, dialog_p_m;
    BDHelper bdHelper;

    String ordenarTituloAsc = Constants.C_TITULO + " ASC";

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF = "mi_pref";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_C_PASSWORD = "c_password";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.fragment_f__ajustes, container, false);

        Eliminar_Todos_Registros = view.findViewById(R.id.Eleminar_Todos_Registros);
        Exportar_Archivo = view.findViewById(R.id.Exportar_Archivo);
        Importar_Archivo = view.findViewById(R.id.Importar_Archivo);
        Cambiar_password_maestra =  view.findViewById(R.id.Cambiar_password_maestra);
        dialog = new Dialog(getActivity());
        dialog_p_m = new Dialog(getActivity());
        bdHelper = new BDHelper(getActivity());

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Eliminar_Todos_Registros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_Eliminar_Registros();
            }
        });

        /*-------------------- Exportar e importar archivo --------------------*/
        Exportar_Archivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"Exportar archivo",Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    ExportarRegistro();
                }else{
                    SolicitudPermisoExportar.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
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
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                            bdHelper.EliminarTodosRegistros();
                            ImportarRegistro();
                        }else{
                            SolicitudPermisoImportar.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }

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

        Cambiar_password_maestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Cambiar contraseña maestra", Toast.LENGTH_SHORT).show();
                CuadroDialogoPassMaestra();
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
    private void CuadroDialogoPassMaestra() {
        //Establecemos las vistas
        EditText Password_maestra;
        EditText Et_nueva_pass_maestra, Et_C_nueva_pass_maestra;
        Button Btn_cambiar_pass_maestra, Btn_cancelar_pass_maestra;

        String password_maestra_recuperada = sharedPreferences.getString(KEY_PASSWORD, null);
        //Hacemos la conexion con el cueadro de dialogo
        dialog_p_m.setContentView(R.layout.cuadro_dialogo_password_maestra);

        //Inicializamos las vistas
        Password_maestra = dialog_p_m.findViewById(R.id.Password_maestra);
        Et_nueva_pass_maestra = dialog_p_m.findViewById(R.id.Et_nueva_pass_maestra);
        Et_C_nueva_pass_maestra = dialog_p_m.findViewById(R.id.Et_C_nueva_pass_maestra);
        Btn_cambiar_pass_maestra = dialog_p_m.findViewById(R.id.Btn_cambiar_pass_maestra);
        Btn_cancelar_pass_maestra = dialog_p_m.findViewById(R.id.Btn_cancelar_pass_maestra);

        Btn_cambiar_pass_maestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos los datos de los Editext
                String S_nueva_password = Et_nueva_pass_maestra.getText().toString().trim();
                String S_c_nueva_password = Et_C_nueva_pass_maestra.getText().toString().trim();

                /* Validacion de datos */
                if(S_nueva_password.equals("")){
                    Toast.makeText(getActivity(), "Ingrese nueva contraseña", Toast.LENGTH_SHORT).show();
                } else if (S_c_nueva_password.equals("")) {
                    Toast.makeText(getActivity(), "Confirme nueva contraseña", Toast.LENGTH_SHORT).show();
                }
                else if(S_nueva_password.length()<6){
                    Toast.makeText(getActivity(), "La contraseña debe tener mas de 6 caracteres", Toast.LENGTH_SHORT).show();
                } else if (!S_nueva_password.equals(S_c_nueva_password)) {
                    Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();

                }else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    /*Pasar los nuevos datos a las llaves */
                    editor.putString(KEY_PASSWORD, S_nueva_password);
                    editor.putString(KEY_C_PASSWORD, S_c_nueva_password);
                    editor.apply();
                    //Salimos de la app para iniciar sesion con la nueva contraseña
                    startActivity(new Intent(getActivity(), Login_u.class));
                    getActivity().finish();
                    Toast.makeText(getActivity(), "La contraseña maestra se ha cambiado", Toast.LENGTH_SHORT).show();
                    dialog_p_m.dismiss();
                }
            }
        });

        Btn_cancelar_pass_maestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                dialog_p_m.dismiss();
            }
        });

        Password_maestra.setText(password_maestra_recuperada);
        Password_maestra.setEnabled(false);
        Password_maestra.setBackgroundColor(Color.TRANSPARENT);
        Password_maestra.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog_p_m.show();
        dialog_p_m.setCancelable(false);
    }


    //Permiso para exportar un registro
    private ActivityResultLauncher<String> SolicitudPermisoExportar =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), Concede_permiso_exportar -> {
                if(Concede_permiso_exportar){
                    ExportarRegistro();
                }else{
                    Toast.makeText(getActivity(),"Permiso denegado",Toast.LENGTH_SHORT).show();
                }
            });
    //Permiso para importar un registro
    private ActivityResultLauncher<String> SolicitudPermisoImportar =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), Concede_permiso_importar -> {
                if(Concede_permiso_importar){
                    ImportarRegistro();
                }else{
                    Toast.makeText(getActivity(),"Permiso denegado",Toast.LENGTH_SHORT).show();
                }
            });

}