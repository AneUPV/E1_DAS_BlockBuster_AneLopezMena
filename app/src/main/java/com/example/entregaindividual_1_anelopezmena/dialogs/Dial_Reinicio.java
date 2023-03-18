package com.example.entregaindividual_1_anelopezmena.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.example.entregaindividual_1_anelopezmena.activities.Act_Registro;
import com.example.entregaindividual_1_anelopezmena.R;



/*******************************************************************/
/** ------------------- DIAL_REINICIO ---------------------- **/
/*******************************************************************/
// Clase JAVA asociada al diálogo que se mostrará cuando el usuario
// cambie el idioma en las preferencias. El motivo de este dialog es
// avisar al usuario de que la apilcación necesita reiniciarse para
// traducirse al completo al idioma elegido'

public class Dial_Reinicio extends DialogFragment {

    // Atributo privado de la clase
    private Context contexto;

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public Dial_Reinicio(Context contexto) {
        this.contexto = contexto;
    }
    //---------------------------------------------------------------------------------
    // 1.1) Método constructor (vacío)
    public Dial_Reinicio() {  }

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
        d_titulo.setText(R.string.reinicio_t);
        d_mensaje.setText(R.string.pregunta_reiniciar);
        d_OK.setText(R.string.b_reiniciar_ahora);
        d_CANCEL.setText(R.string.b_cancelar_reinicio);

        // ** Cuando aparezca el dialog de REINICIAR APLICACIÓN, el usuario
        // ** tendrá 2 opciones posibles:
        //      * 1) ACEPTAR -> Se ejecutan las acciones asociadas al reinicio
        d_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LO QUE HARÁ AL PULSAR "SÍ"
                // Termina la vida de la aplicación
                Intent intent = new Intent(contexto, Act_Registro.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
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
        /// Al clicar fuera se NO cancela
        builder.setCancelable(false);
        // Crear el objeto AlertDialog desde el builder y devolverlo
        return builder.create();
    }

}
