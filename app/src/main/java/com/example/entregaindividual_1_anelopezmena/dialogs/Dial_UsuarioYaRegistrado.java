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

/*******************************************************************/
/** ----------------- DIAL_USUARIO_YA_REGISTRADO ---------------- **/
/*******************************************************************/
// Clase JAVA asociada al diálogo que se mostrará cuando un usuario ya
// registrado (email ya vinculado), trata de volver a registrarse con la
// misma dirección.
public class Dial_UsuarioYaRegistrado extends DialogFragment {

    //---------------------------------------------------------------------------------
    // 1) Método ON_CREATE_DIALOG: Método que se ejecuta al crearse el dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Utilizar la clase Builder, para construir el diálogo más adelante
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Añadir el aspecto al dialog --> (layout) dial_1_boton.xml
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View aspecto = inflater.inflate(R.layout.dial_1_boton, null);

        // Recoger views del aspecto creado
        TextView d_titulo = aspecto.findViewById(R.id.dial_1_boton_titulo);
        TextView d_mensaje = aspecto.findViewById(R.id.dial_1_boton_mensaje);
        Button d_OK = aspecto.findViewById(R.id.dial_1_boton_mensaje_boton);

        // Settear texto del dialog, en diferentes idiomas
        d_titulo.setText(R.string.d_usuarioYaRegistrado_t);
        d_mensaje.setText(R.string.d_usuarioYaRegistrado_m);
        d_OK.setText(R.string.d_usuarioYaRegistrado_b);

        // ** Cuando aparezca el dialog de que el USUARIO YA EXISTE, el usuario
        // tiene la opción de pulsar el botón de OK para cerrar el diálogo
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
    }





}
