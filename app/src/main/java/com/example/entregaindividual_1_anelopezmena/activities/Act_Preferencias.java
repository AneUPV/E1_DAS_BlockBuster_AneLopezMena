package com.example.entregaindividual_1_anelopezmena.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.entregaindividual_1_anelopezmena.R;

/*************************************************************************/
/** -------------------- ACTIVITY_PREFERENCIAS ---------------------- **/
/*************************************************************************/
// Esta última, es la clase java que se corresponde con la creación de la
// pantalla de preferencias del usuario.

public class Act_Preferencias extends AppCompatActivity {

    //---------------------------------------------------------------------------------
    // 1) Método ON_CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Crear la vista
        setContentView(R.layout.activity_act_preferencias);
    }

}