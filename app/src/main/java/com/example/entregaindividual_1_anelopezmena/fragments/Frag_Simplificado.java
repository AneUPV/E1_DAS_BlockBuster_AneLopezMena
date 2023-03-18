package com.example.entregaindividual_1_anelopezmena.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.entregaindividual_1_anelopezmena.R;

/*******************************************************************/
/** -------------------- FRAG_SIMPLIFICADO ---------------------- **/
/*******************************************************************/
// Esta clase JAVA corresponde al fragmento que 'convive' en la vista horizontal
// de la lista de películas, que muestra la versión simplificada de la info de la
// película. En la vista vertical, este fragmento no existe. Extiende de la clase
// Fragment.

public class Frag_Simplificado extends Fragment {

    //---------------------------------------------------------------------------------
    // 1) Método constructor (vacío)
    public Frag_Simplificado() {
    }
    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //---------------------------------------------------------------------------------
    // 3) Método ON_CREATE_VIEW: Se ejectará una vez creada la vista
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_simplificado, container, false);
    }
}