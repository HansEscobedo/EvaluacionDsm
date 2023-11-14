package com.example.evaluaciondsm.ui.home;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.evaluaciondsm.AdminSQLiteOpenHelper;
import com.example.evaluaciondsm.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private EditText txt_code, txt_description, txt_price;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        txt_code = binding.txtCode;
        txt_description = binding.txtDescription;
        txt_price = binding.txtPrice;

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        binding.btnSearchP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Metodo para guardar los productos
    public void save(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String code = txt_code.getText().toString();
        String description = txt_description.getText().toString();
        String price = txt_price.getText().toString();
        if(!code.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
            Cursor fila = db.rawQuery("select codigo from articulos where codigo = " + code, null);
            if(fila.moveToFirst()) {
                Toast.makeText(this.getActivity(), "Ya existe un artículo con ese código", Toast.LENGTH_SHORT).show();
                db.close();
                return;
            }
            ContentValues registro = new ContentValues();
            registro.put("codigo", code);
            registro.put("descripcion", description);
            registro.put("precio", price);
            db.insert("articulos", null, registro);
            db.close();
            cleanForm();
            Toast.makeText(this.getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this.getActivity(), "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para buscar productos
    public void search() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String code = txt_code.getText().toString();
        if(!code.isEmpty()) {
            Cursor fila = db.rawQuery("select descripcion, precio from articulos where codigo = " + code, null);
            if(fila.moveToFirst()) {
                txt_description.setText(fila.getString(0));
                txt_price.setText(fila.getString(1));
                db.close();
            }
            else{
                Toast.makeText(this.getActivity(), "No existe el articulo", Toast.LENGTH_SHORT).show();
                db.close();
            }
        }
        else{
            Toast.makeText(this.getActivity(), "Debes introducir el codigo del articulo", Toast.LENGTH_SHORT).show();
        }

    }

    public void delete() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String code = txt_code.getText().toString();
        if (!code.isEmpty())
        {
            int count = db.delete("articulos", "codigo=" + code, null);
            if(count == 1) {
                Toast.makeText(this.getActivity(), "Articulo eliminado", Toast.LENGTH_SHORT).show();
                db.close();
                cleanForm();
            }
            else{
                Toast.makeText(this.getActivity(), "El articulo no existe", Toast.LENGTH_SHORT).show();
                db.close();
            }
        }

    }

    public void edit() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String code = txt_code.getText().toString();
        String description = txt_description.getText().toString();
        String price = txt_price.getText().toString();
        if(!code.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
            ContentValues registro = new ContentValues();
            registro.put("codigo", code);
            registro.put("descripcion", description);
            registro.put("precio", price);
            int count = db.update("articulos", registro, "codigo=" + code, null);
            if(count == 1) {
                Toast.makeText(this.getActivity(), "Articulo modificado", Toast.LENGTH_SHORT).show();
                db.close();
            }
            else{
                Toast.makeText(this.getActivity(), "El articulo no existe", Toast.LENGTH_SHORT).show();
                db.close();
            }
        }
        else{
            Toast.makeText(this.getActivity(), "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    public void cleanForm() {
        txt_code.setText("");
        txt_description.setText("");
        txt_price.setText("");
    }

}