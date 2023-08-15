package com.example.tlenguajes2023;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tlenguajes2023.configuracion.ConfigDB;
import com.example.tlenguajes2023.configuracion.SQLiteConnection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ActivityIngresar extends AppCompatActivity {

    public static final int peticion_acceso_camara = 101;
    public static final int peticion_toma_fotografia = 102;
    ImageView fotoview;

    EditText id, nombres, descripcion, foto;
    Button btningresar, btnfoto;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);

        id = (EditText) findViewById(R.id.txtid);
        nombres = (EditText) findViewById(R.id.txtnomb);
        descripcion = (EditText) findViewById(R.id.txtdescrip);
        foto=(EditText) findViewById(R.id.txtfoto);
        btningresar = (Button) findViewById(R.id.btningresar);
        btnfoto = (Button) findViewById(R.id.btnfoto);
        fotoview = (ImageView) findViewById(R.id.imageView);

        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertar_datos();
            }
        });

        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permisos();
            }
        });

    }

    private void Permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA},peticion_acceso_camara);
        }
        else
        {
            TomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            TomarFoto();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Permiso no otorgado",Toast.LENGTH_LONG).show();
        }
    }

    private void TomarFoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(intent,peticion_toma_fotografia);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == peticion_toma_fotografia)
        {
            Bundle extras = data.getExtras();
            Bitmap imagen = (Bitmap) extras.get("data");
            fotoview.setImageBitmap(imagen);

            foto.setText(ImagetoBase64()); ;
        }
    }

    private String ImagetoBase64()
    {
        Bitmap imagen = ((BitmapDrawable) fotoview.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();
        String Base64Image = Base64.encodeToString(image, Base64.DEFAULT);

        return Base64Image;
    }

    public String convertPDFToBase64(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            fileInputStream.close();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void insertar_datos()
    {
        SQLiteConnection conexion = new SQLiteConnection(this, ConfigDB.namebd, null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConfigDB.nombres,nombres.getText().toString());
        values.put(ConfigDB.descripcion,descripcion.getText().toString());
        values.put(ConfigDB.foto, ImagetoBase64());

        Long resultado = db.insert(ConfigDB.tblpersonas, ConfigDB.id, values);
        if(resultado > 0)
        {
            Toast.makeText(getApplicationContext(), "Registro ingresado con éxito.",Toast.LENGTH_LONG).show();

            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Registro no se ingresó.",Toast.LENGTH_LONG).show();
        }

        db.close();

        ClearScreen();
    }

    private void ClearScreen()
    {
        nombres.setText(ConfigDB.Empty);
        descripcion.setText(ConfigDB.Empty);
        foto.setText(ConfigDB.Empty);
        fotoview.setImageDrawable(null);
    }
}