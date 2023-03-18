package com.example.entregaindividual_1_anelopezmena.interfaces;

/*******************************************************************/
/** ------------------------ INTERFAZ_RV------------------------- **/
/*******************************************************************/
// Esta interfaz contiene las cabeceras de los métodos que implementarán otras
// clases y que ejecutarán los listeners.

public interface InterfazRV {
    void onitemLongClick(int pos);      // Implementado en: 'Act_InfoPelicula', 'Frag_Todos' y 'Frag_SinVer'
    void onClick(int pos);              // Implementado en: 'Act_InfoPelicula', 'Frag_Todos' y 'Frag_SinVer'
    void eliminarConfirmado(int pos);   // Implementado en: 'Act_InfoPelicula', 'Frag_Todos' y 'Frag_SinVer'
    void notificarActualizado();        // Implementado en: 'Act_InfoPelicula', 'Frag_Todos' y 'Frag_SinVer'

}
