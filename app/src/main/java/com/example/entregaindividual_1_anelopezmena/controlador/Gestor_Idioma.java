package com.example.entregaindividual_1_anelopezmena.controlador;

import android.content.Context;
import android.content.res.Configuration;
import java.util.Locale;

/*******************************************************************/
/** -------------------- CLASE GESTOR_IDIOMA -------------------- **/
/*******************************************************************/
// Se trata de la clase encargada de gestionar el cambio de idiomas.

public class Gestor_Idioma {

    // Atributos del Gestor_Idioma
    String idiomaApp = "es";            // Variable global - el idioma por defecto será el español
    private Context contexto;           // Contexto de la actividad

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public Gestor_Idioma(Context contexto) {
        this.contexto = contexto;
    }

    //---------------------------------------------------------------------------------
    // 2) Método SET_IDIOMA: Este método se ejecuta cuando hay que cambiar el idioma
    //    de la aplicación.
    public void setIdioma(String idioma){
        //Configurar el idioma
        if (idioma != null) {
            idiomaApp = idioma;
            // Crear un Locale
            Locale nuevaloc = new Locale(idioma);
            Locale.setDefault(nuevaloc);
            // Crear una configuración
            Configuration configuration = contexto.getResources().getConfiguration();
            configuration.setLocale(nuevaloc);
            configuration.setLayoutDirection(nuevaloc);
            Context context = contexto.createConfigurationContext(configuration);
            contexto.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        }
    }

    //---------------------------------------------------------------------------------
    // 3) Método GET_IDIOMA: Devuelve el idioma actual de la aplicación
    public String getIdiomaActual() { return idiomaApp;  }

}
