package com.example.tlenguajes2023;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tlenguajes2023.configuracion.ConfigDB;
import com.example.tlenguajes2023.configuracion.CustomAdapter;
import com.example.tlenguajes2023.configuracion.SQLiteConnection;
import com.example.tlenguajes2023.configuracion.personas;

import java.util.ArrayList;

public class ActivityList extends AppCompatActivity
{
    SQLiteConnection conexion;
    ListView list;

    ArrayList<personas> listpersonas;
    ArrayList<personas> arreglopersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        conexion = new SQLiteConnection(this, ConfigDB.namebd, null, 1);
        list = (ListView) findViewById(R.id.Lista);

        ObtenerTabla();

        CustomAdapter apd = new CustomAdapter(listpersonas, ActivityList.this, R.layout.row_personas);
        list.setAdapter(apd);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityList.this);
                builder.setTitle("Confirmar");
                builder.setMessage("¿Desea eliminar esta persona?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarPersona(position);
                    }


                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                personas personaSeleccionada = arreglopersonas.get(position);
                mostrarDialogoActualizar(personaSeleccionada);
                return true;
            }
        });
    }
    private void mostrarDialogoActualizar(final personas personaSeleccionada) {
        final Dialog dialog = new Dialog(ActivityList.this);
        dialog.setContentView(R.layout.dialog_actualizar_persona);

        final EditText editNombre = dialog.findViewById(R.id.txtnomb);
        final EditText editDescripcion = dialog.findViewById(R.id.txtdescrip);
        Button btnActualizar = dialog.findViewById(R.id.btnActualizar);

        editNombre.setText(personaSeleccionada.getNombres());
        editDescripcion.setText(personaSeleccionada.getDescripcion());

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = editNombre.getText().toString();
                String nuevaDescripcion = editDescripcion.getText().toString();
                actualizarPersona(personaSeleccionada.getId(), nuevoNombre, nuevaDescripcion);
                dialog.dismiss();
            }


        });

        dialog.show();
    }

    private void actualizarPersona(Integer id, String nuevoNombre, String nuevaDescripcion) {
        // Código para actualizar la persona en la base de datos y refrescar la lista
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombres", nuevoNombre);
        valores.put("descripcion", nuevaDescripcion);
        db.update(ConfigDB.tblpersonas, valores, "id=?", new String[]{String.valueOf(id)});
        db.close();

        ObtenerTabla();

    }




    private void eliminarPersona(int position) {
        SQLiteDatabase db = conexion.getWritableDatabase();
        int idPersona = arreglopersonas.get(position).getId();
        db.delete(ConfigDB.tblpersonas, "id=?", new String[]{String.valueOf(idPersona)});
        db.close();

        arreglopersonas.remove(position);


    }
    private void ObtenerTabla()
    {
        SQLiteDatabase db = conexion.getReadableDatabase();
        personas person = null;
        listpersonas = new ArrayList<personas>();

        // Cursor de Base de Datos
        Cursor cursor = db.rawQuery(ConfigDB.SelectTBPersonas,null);

        // recorremos el cursor
        while(cursor.moveToNext())
        {
            person = new personas();
            person.setId(cursor.getInt(0));
            person.setNombres(cursor.getString(1));
            person.setDescripcion(cursor.getString(2));
            person.setFoto(cursor.getString(3));
            listpersonas.add(person);
        }

        cursor.close();

        fillData();
    }

    private void fillData() {
        arreglopersonas = listpersonas;
    }

    public void ActivityNuevaPersona(View view){
        Intent nuevo = new Intent(this, ActivityIngresar.class);
        startActivity(nuevo);
    }

}