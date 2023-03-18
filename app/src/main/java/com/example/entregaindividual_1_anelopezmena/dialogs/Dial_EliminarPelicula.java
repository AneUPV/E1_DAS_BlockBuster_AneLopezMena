package com.example.entregaindividual_1_anelopezmena.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.interfaces.InterfazRV;
import com.example.entregaindividual_1_anelopezmena.java.Pelicula;



/*******************************************************************/
/** ------------------ DIAL_ELIMINAR_PELICULA ------------------- **/
/*******************************************************************/
// Clase JAVA asociada al diálogo que se mostrará cuando el usuario
// vaya a borrar una película de una lista. Como es una opción que no
// se puede deshacer directamente, se le mostrará el diálogo como alerta
// antes de realizar la acción.

public class Dial_EliminarPelicula extends DialogFragment {

    // Atributos
    private InterfazRV listener;
    private String director;
    private String titulo;
    private Pelicula miPeli;
    int pos;

    //---------------------------------------------------------------------------------
    // 1.1) Método constructor
    public Dial_EliminarPelicula(InterfazRV listener, int pos, String titulo, String director) {
        // Recibe como parámetro una interfaz, que será la que actúe como listener
        // e implementa sus métodos en otra clase
        this.listener = listener;
        this.pos=pos;
        this.titulo = titulo;
        this.director=director;
    }
    //---------------------------------------------------------------------------------
    // 1.2) Método constructor
    public Dial_EliminarPelicula(){}

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
        d_titulo.setText(R.string.eliminar);
        d_mensaje.setText(R.string.pregunta_eliminar);
        d_OK.setText(R.string.b_aceptar);
        d_CANCEL.setText(R.string.b_cancelar);

        // ** Cuando aparezca el dialog de ELIMINAR PELÍCULA, el usuario
        // ** tiene 2 opciones:
        //      * 1) ACEPTAR -> Se ejecutan las acciones asociadas a la eliminación
        d_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // LO QUE HARÁ AL PULSAR "SÍ"
                if ( listener != null ) {
                    // El listener ejecuta el método 'eliminarConfirmado' implementado en otra clase
                    listener.eliminarConfirmado(pos);
                    // Toast informativo
                    Toast.makeText(getActivity(), getResources().getString(R.string.vista), Toast.LENGTH_SHORT).show();
                }
                dismiss();
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

        // Crear el objeto AlertDialog desde el builder y devolverlo
        return builder.create();
    }

}
