package com.example.entregaindividual_1_anelopezmena.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_DB;
import com.example.entregaindividual_1_anelopezmena.java.Pelicula;


/*******************************************************************/
/** ---------------------- DIAL_SINOPSIS ------------------------ **/
/*******************************************************************/
// Clase JAVA asociada al diálogo que se mostrará cuando el usuario
// pulse una película en la vista horizontal de la lista de todas las
// películas. Muestra una breve descripción de la trama (sinopsis).
// Extiende de la clase 'DialogFragment'

public class Dial_Sinopsis extends DialogFragment {

    // Atributos privados de la clase
    private String titulo;
    private String director;
    private Gestor_DB peliculasDB;

    //---------------------------------------------------------------------------------
    // 1.1) Método constructor
    public Dial_Sinopsis(String titulo, String director) {
        this.titulo=titulo;
        this.director=director;
    }
    //---------------------------------------------------------------------------------
    // 1.2) Método constructor (vacío)
    public Dial_Sinopsis() { }

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE_DIALOG: Método que se ejecuta al crearse el dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (titulo != null && director != null) {
            // Llamar al Gestor_DB para obtener información de la película seleccionada
            peliculasDB = new Gestor_DB(getActivity());
            Pelicula p = peliculasDB.conseguirPelicula(titulo, director);
            // Utilizar la clase Builder, para construir el diálogo más adelante
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Añadir el aspecto al dialog --> (layout) dial_sinopsis.xml
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View aspecto = inflater.inflate(R.layout.dial_sinopsis, null);

            // Recoger views del aspecto creado
            TextView d_titulo = aspecto.findViewById(R.id.dial_1_boton_titulo);
            TextView nombrePeli = aspecto.findViewById(R.id.tituloPeli);
            TextView d_mensaje = aspecto.findViewById(R.id.dial_1_boton_mensaje);
            Button d_OK = aspecto.findViewById(R.id.dial_1_boton_mensaje_boton);

            // Settear texto del dialog, en diferentes idiomas
            d_titulo.setText(getResources().getString(R.string.atencion));

            // Obtener los datos del Gestor_DB
            String trama = p.getTrama().toString();         //trama
            String titulo = p.getTitulo();                  //título

            //Settear datos obtenidos
            nombrePeli.setText(titulo.toUpperCase() + "\n");
            d_mensaje.setText(trama);
            d_OK.setText(getResources().getString(R.string.b_aceptar));

            // ** Cuando aparezca el dialog de la SINOPSIS de la película, el usuario
            // tiene la opción de pulsar el botón de OK
            d_OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Se cierra el dialog con un 'dismiss'
                    dismiss();
                }
            });

            // Settear el aspecto en el builder
            builder.setView(aspecto);

            // Crear el objeto AlertDialog desde el builder y devolverlo
            return builder.create();
        }else{
            // Utilizar la clase Builder, para construir el diálogo más adelante
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Añadir el aspecto al dialog --> (layout) dial_sinopsis.xml
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View aspecto = inflater.inflate(R.layout.dial_sinopsis, null);

            // Recoger views del aspecto creado
            TextView d_titulo = aspecto.findViewById(R.id.dial_1_boton_titulo);
            TextView nombrePeli = aspecto.findViewById(R.id.tituloPeli);
            TextView d_mensaje = aspecto.findViewById(R.id.dial_1_boton_mensaje);
            Button d_OK = aspecto.findViewById(R.id.dial_1_boton_mensaje_boton);

            // Settear texto del dialog, en diferentes idiomas
            d_titulo.setText(getResources().getString(R.string.atencion));
            dismiss();

            // Settear el aspecto en el builder
            builder.setView(aspecto);

            // Crear el objeto AlertDialog desde el builder y devolverlo
            return builder.create();
        }
    }
}
