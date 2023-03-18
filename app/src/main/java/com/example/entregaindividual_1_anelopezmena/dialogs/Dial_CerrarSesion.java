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
import com.example.entregaindividual_1_anelopezmena.interfaces.Interfaz_Dial;



/*******************************************************************/
/** ------------------- DIAL_CERRAR_SESIÓN ---------------------- **/
/*******************************************************************/
// Clase JAVA asociada al diálogo que se mostrará cuando el usuario
// pulse el botón de cerrar sesión en la aplicación. Extiende de la
// clase 'DialogFragment'

public class Dial_CerrarSesion extends DialogFragment {

    // Atributos privados de la clase
    private Interfaz_Dial listener;

    //---------------------------------------------------------------------------------
    // 1.1) Método constructor
    public Dial_CerrarSesion(Interfaz_Dial listener) {
        // Recibe como parámetro una interfaz, que será la que actúe como listener
        // e implementa sus métodos en el menú principal de la aplicación
        this.listener = listener;
    }
    //---------------------------------------------------------------------------------
    // 1.2) Método constructor (vacío)
    public Dial_CerrarSesion() {}

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE_DIALOG: Método que se ejecuta al crearse el dialog
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Utilizar la clase Builder, para construir el diálogo más adelante
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Añadir el aspecto al dialog --> (layout) dial_2_boton.xml
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View aspecto = inflater.inflate(R.layout.dial_2_boton, null);

        // Recoger views del aspecto creado
        TextView d_titulo = aspecto.findViewById(R.id.dial_1_boton_titulo);
        TextView d_mensaje = aspecto.findViewById(R.id.dial_1_boton_mensaje);
        Button d_OK = aspecto.findViewById(R.id.aceptar);
        Button d_CANCEL = aspecto.findViewById(R.id.cancelar);

        // Settear texto del dialog, en diferentes idiomas
        d_titulo.setText(R.string.dial_cerrar_sesion_t);
        d_mensaje.setText(R.string.dial_cerrar_sesion_m);
        d_OK.setText(R.string.b_aceptar);
        d_CANCEL.setText(R.string.b_cancelar);


        // ** Cuando aparezca el dialog de CERRAR SESIÓN, el usuario
        // ** tendrá 2 opciones posibles:
        //      * 1) ACEPTAR -> Se ejecutan las acciones asociadas al cierre de sesión
        d_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LO QUE HARÁ AL PULSAR "SÍ"
                listener.cerrarSesion();

            }
        });
        //      * 2) CANCELAR -> Se cancela la acción y se cierra el dialog con un 'dismiss'
        d_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LO QUE HARÁ AL PULSAR "NO"
                dismiss();
            }
        });

        // Settear el aspecto en el builder
        builder.setView(aspecto);
        // Al clicar fuera se NO cancela
        builder.setCancelable(false);
        // Crear el objeto AlertDialog desde el builder y devolverlo
        return builder.create();
    }




}
