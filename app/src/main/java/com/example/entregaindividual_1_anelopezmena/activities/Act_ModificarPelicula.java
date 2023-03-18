package com.example.entregaindividual_1_anelopezmena.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_DB;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_Idioma;


/******************************************************************************/
/** -------------------- ACTIVITY_MODIFICAR_PELICULA ---------------------- **/
/*****************************************************************************/
// De la misma manera que un usuario puede insertar información en la app,
// también se le permite modificar la información ya existente. Desde la misma
// pantalla de información de la película, seleccionando la opción ‘modificar
// película’ en el toolbar, se mostrará la nueva pantalla de modificación de datos.
// Una vez allí, se podrán aplicar todos los cambios que se consideren necesarios.

public class Act_ModificarPelicula extends AppCompatActivity {

    // Atributos privados
    private String t_titulo, t_caratula, t_director, t_actores,t_genero, t_trama, t_anyo,t_valoracion, t_duracion,t_url, email,visto;
    private EditText et_anyo, et_genero, et_duracion, et_director, et_actores, et_sinopsis, et_trailer;
    private RatingBar rb_valoracion;
    private TextView et_titulo;
    private CheckBox cb_visto;
    private ImageView iv_caratula;
    private Uri caratulaUri;
    private boolean t_visto;
    private String idiomaActual;
    private Gestor_Idioma gestorIdioma;
    private String pos;

    //---------------------------------------------------------------------------------
    // 1) Método ON_CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // El idioma por defecto es el ESPAÑOL
        idiomaActual = "es";
        // Crear gestores: BD e IDIOMA
        gestorIdioma = new Gestor_Idioma(this);
    // ------------------------------------------------------------------
        // Gestionar pérdida de información al girar la pantalla:
        // GUARDAR:
        //      * EMAIL
        //      * IDIOMA
        if (savedInstanceState != null){
            // Obtener idioma y settearlo en el gestor, además del email
            idiomaActual = savedInstanceState.getString("var_idioma");
            email = savedInstanceState.getString("var_email");
            gestorIdioma.setIdioma(idiomaActual);
        }
    // ------------------------------------------------------------------
        // Recoger datos del intent desde la pantalla de Act_InfoPelicula
        Intent i2 = getIntent();
        if ( i2!= null ){
            t_titulo = i2.getExtras().getString("var_titulo");
            t_caratula = i2.getExtras().getString("var_caratula");
            t_director = i2.getExtras().getString("var_director");
            t_actores = i2.getExtras().getString("var_actores");
            t_genero = i2.getExtras().getString("var_genero");
            t_trama = i2.getExtras().getString("var_trama");
            t_anyo = i2.getExtras().getString("var_anyo");
            t_visto = i2.getExtras().getBoolean("var_visto");
            t_valoracion = i2.getExtras().getString("var_valoracion");
            t_duracion = i2.getExtras().getString("var_duracion");
            t_url = i2.getExtras().getString("var_trailer");
            email = i2.getExtras().getString("var_email");
            pos = i2.getExtras().getString("var_posicion");
            idiomaActual = i2.getExtras().getString("var_idioma");

            // Una vez conseguido el idioma, settearlo
            gestorIdioma.setIdioma(idiomaActual);

            // Una vez cargado el idioma, cargar la vista
            setContentView(R.layout.act_modificar_pelicula);

            // Obtener elementos del layout
            et_titulo = findViewById(R.id.edit_titulo);
            iv_caratula = findViewById(R.id.edit_caratula);
            et_anyo = findViewById(R.id.edit_anyo);
            et_genero = findViewById(R.id.edit_genero);
            et_duracion = findViewById(R.id.edit_duracion);
            et_director = findViewById(R.id.edit_director);
            et_actores = findViewById(R.id.edit_actores);
            et_sinopsis = findViewById(R.id.edit_trama);
            et_trailer = findViewById(R.id.edit_trailer);
            rb_valoracion = findViewById(R.id.edit_valoracion);
            cb_visto = findViewById(R.id.edit_visto);

            // Comprobar que la carátula no sea null. Si es así, se le dará
            // una carátula genérica
            if (t_caratula != null){ caratulaUri = Uri.parse(t_caratula);}
            else{
                caratulaUri= Uri.parse("android.resource://com.example.entregaindividual_1_anelopezmena/drawable/vacio");
            }
        }

        // Settear los valores recibidos de INFO_PELICULAS a los Views de MODIFICAR_PELICULA
        et_titulo.setText(t_titulo);
        iv_caratula.setImageURI(caratulaUri);
        et_director.setText(t_director);
        et_actores.setText(t_actores);
        et_genero.setText(t_genero);
        et_sinopsis.setText(t_trama);
        et_anyo.setText(t_anyo);
        cb_visto.setChecked(t_visto);
        rb_valoracion.setRating(Float.parseFloat(t_valoracion));
        et_duracion.setText(t_duracion);
        et_trailer.setText(t_url);

    }

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE_VIEW
    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    // ------***### LISTENERS DE LOS BOTONES ###***------ //
    // --> Boton de GUARDAR
    //---------------------------------------------------------------------------------
    // 3) Método ACTUALIZAR_DATOS
    public void actualizarDatos(View v){
        // Instanciar un gestor DB
        Gestor_DB peliculaDB = new Gestor_DB(this);

        // Obtener datos de los campos de texto
        String actores = et_actores.getText().toString();
        String genero = et_genero.getText().toString();
        String trama = et_sinopsis.getText().toString();
        String anyo = et_anyo.getText().toString();
        String val = String.valueOf(rb_valoracion.getRating());
        String dur = et_duracion.getText().toString();
        String trailer = et_trailer.getText().toString();
        boolean heVisto = cb_visto.isChecked();

        // Comprobar estado de la película: Vista o no vista
        if (heVisto){ // Si ya la ha visto
            // Actualizar base de datos
            peliculaDB.anadirVisto(email, t_titulo, t_director);
        } // Si no la ha visto
        else{
            // Actualizar base de datos
            peliculaDB.eliminarPeliculaVista(email, t_titulo, t_director);
        }
        // Con los datos nuevos, actualizar la película en la base de datos
        peliculaDB.actualizarPelicula( t_titulo,  t_caratula,  t_director,  actores,  genero,  trama,  anyo, val, dur,trailer);

        // DEVOLVER DATOS A ACT_INFO_PELICULAS  ( Act_InfoPeliculas <-- Act_ModificarPeliculas )
        // Crear el intent para devolver los datos
        Intent i3 = new Intent();
        i3.putExtra("var_titulo", t_titulo);
        i3.putExtra("var_caratula" ,t_caratula);
        i3.putExtra("var_director", t_director);
        i3.putExtra("var_actores", actores);
        i3.putExtra("var_genero", genero);
        i3.putExtra("var_trama", trama);
        i3.putExtra("var_anyo", anyo);
        i3.putExtra("var_visto", heVisto);
        i3.putExtra("var_valoracion", val);
        i3.putExtra("var_duracion", dur);
        i3.putExtra("var_trailer", trailer);
        i3.putExtra("var_email", email);
        i3.putExtra("var_posicion", pos);
        // Settear code 2 al intent i3
        setResult(2, i3);

        finish();
    }

    //---------------------------------------------------------------------------------
    // 4) Método ON_SAVE_INSTANCE_STATE: Este método se ejecuta antes del destroy() de la
    //    aplicación, por lo que es aquí dónde se deben guardar los datos que se quieran
    //    mantener. Por ejemplo,para evitar la pérdida de datos al girar el móvil, interrupciones, etc.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar datos para evitar la pérdida al girar el móvil, interrupciones, etc.
        outState.putString("var_idioma", this.idiomaActual);
        outState.putString("var_email", this.email);
  }

}