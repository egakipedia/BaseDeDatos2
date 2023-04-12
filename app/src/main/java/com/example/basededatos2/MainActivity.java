package com.example.basededatos2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Base de Datos
    private String NombreBaseDeDatos = "administracion";

    private EditText txt_codigo, txt_descripcion, txt_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_codigo = (EditText) findViewById(R.id.txt_codigo);
        txt_descripcion = (EditText) findViewById(R.id.txt_descripcion);
        txt_precio = (EditText) findViewById(R.id.txt_precio);
    }

    // Método para dar de alta a los productos
    public void Registrar (View view){
        // Instanciamos la clase de la base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, NombreBaseDeDatos, null, 1);
        // Abrimos la bas de datos en modo lectura y escritura
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = txt_codigo.getText().toString();
        String descripcion = txt_descripcion.getText().toString();
        String precio = txt_precio.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty() ){
            ContentValues registro = new ContentValues();

            registro.put("codigo", codigo);
            registro.put("descripcion",descripcion );
            registro.put("precio", precio);

            // Insertamos dentro de la tabla "articulos" los registros que hemos creado
            BaseDeDatos.insert("articulos", null, registro);

            // Cerramos la base de datos
            BaseDeDatos.close();

            txt_codigo.setText("");
            txt_descripcion.setText("");
            txt_precio.setText("");

            Toast.makeText(this, "El producto se ha grabado correctamente", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Métido para buscar artículos
    public void Buscar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, NombreBaseDeDatos, null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = txt_codigo.getText().toString();

        if(!codigo.isEmpty()){
            Cursor fila = BaseDeDatos.rawQuery("select descripcion, precio from articulos where codigo="+ codigo, null);

            if(fila.moveToFirst()){
                txt_descripcion.setText(fila.getString(0));
                txt_precio.setText(fila.getString(1));
                BaseDeDatos.close();
            }else{
                txt_descripcion.setText("");
                txt_precio.setText("");
                Toast.makeText(this, "El artículo no existe", Toast.LENGTH_SHORT).show();
                BaseDeDatos.close();
            }

        }else{
            Toast.makeText(this, "Debes introducir el código del producto", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para elimiar producto
    public void Eliminar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, NombreBaseDeDatos, null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = txt_codigo.getText().toString();

        if(!codigo.isEmpty()){
            int cantidad = BaseDeDatos.delete("articulos", "codigo=" + codigo, null);
            BaseDeDatos.close();

            if(cantidad == 1){
                txt_codigo.setText("");
                txt_descripcion.setText("");
                txt_precio.setText("");
                Toast.makeText(this, "El artículo se ha borrado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El artículo no existe", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Debes introducir el código del artículo", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para modificar un artículo
    public void Modificar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, NombreBaseDeDatos, null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = txt_codigo.getText().toString();
        String descripcion = txt_descripcion.getText().toString();
        String precio = txt_precio.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            ContentValues registro = new ContentValues();

            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            int cantidad = BaseDeDatos.update("articulos", registro, "codigo=" + codigo, null);
            BaseDeDatos.close();

            if(cantidad == 1){
                Toast.makeText(this, "El artículo se ha modificado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El artículo no existe", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    // Limpiar datos
    public void Limpiar(View view){
        txt_codigo.setText("");
        txt_descripcion.setText("");
        txt_precio.setText("");
    }
}