package com.example.tlenguajes2023.configuracion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tlenguajes2023.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<personas> implements View.OnClickListener
{
    private List<personas> DataSet;
    private Context context;

    private int layoutResourceId;


    private static class ViewHolder
    {
        TextView txtnombres;
        TextView txtdescripcion;
        ImageView imageperson;
    }

    public CustomAdapter(List<personas> data, @NonNull Context context, int resource) {
       // super(context, R.layout.row_personas);
        super(context, resource, data);
        this.DataSet = data;
        this.context = context;
        this.layoutResourceId = resource;
    }

    @Override
    public void onClick(View view) {

    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        personas dataModel = getItem(position);

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_personas, parent, false);

            viewHolder.txtnombres = convertView.findViewById(R.id.PersonName);
            viewHolder.txtdescripcion =  convertView.findViewById(R.id.PersonDescripcion);
            viewHolder.imageperson =  convertView.findViewById(R.id.ImagePerson);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
         //   result=convertView;
        }

        viewHolder.txtnombres.setText(dataModel.getNombres());
        viewHolder.txtdescripcion.setText(dataModel.getDescripcion());
        viewHolder.imageperson.setImageBitmap(ConvertBase64toImage(dataModel.getFoto()));

        return convertView;
    }

    /*private Bitmap ConvertBase64toImage(String Base64String)
    {
        String base64Image = Base64String.split(",")[1];
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }*/

    private Bitmap ConvertBase64toImage(String Base64String) {
        if (Base64String == null || Base64String.isEmpty()) {
            return null;
        }

        String[] parts = Base64String.split(",");
        if (parts.length < 2) {
            return null;
        }

        String base64Image = parts[1];
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
