package com.example.entregaindividual_1_anelopezmena.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_DB;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_Idioma;
import com.example.entregaindividual_1_anelopezmena.dialogs.Dial_FaltaTituloDirector;
import com.example.entregaindividual_1_anelopezmena.dialogs.Dial_PeliculaYaExiste;
import com.example.entregaindividual_1_anelopezmena.java.Pelicula;

/*************************************************************************/
/** --------------------ACTIVITY_AÑADIR_PELICULA ---------------------- **/
/*************************************************************************/
// Para acceder a la actividad de añadir una película, se debe pulsar el Action
// Floatting Button de la parte inferior derecha del menú principal de la app (+)
// Pulsando sobre el botón, se muestra la actividad desde la que añadir una película
// al registro.
//
// En esta nueva pantalla, el usuario podrá indicar todos los detalles
// de la película (titulo, año, director, actores...) y guardarlos fácilmente pulsando
// el botón rojo de 'GUARDAR'. Esta clase extiende a 'AppCompatActivity'


public class Act_AñadirPelicula extends AppCompatActivity {

    // Atributos privados
    private EditText et_titulo,et_anyo, et_genero, et_duracion, et_director, et_actores, et_sinopsis, et_trailer;
    private RatingBar rb_valoracion;
    private CheckBox cb_visto;
    private ImageView iv_caratula;
    private String visto = "0";
    private Uri caratulaUri;
    private Gestor_DB peliculasDB;
    private Gestor_Idioma gestorIdioma;
    private String idiomaActual, email;

    //---------------------------------------------------------------------------------
    // 1) Método ON_CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se indica el idioma por defecto, que será el ESPAÑOL
        idiomaActual = "es";

        // Crear gestores: Base de datos e IDIOMA
        peliculasDB = new Gestor_DB(this);
        gestorIdioma = new Gestor_Idioma(this);

        //---------------------------------------------------------------------------------
        // Recoger intent lanzado desde el menú principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Obtener email y nombre del usuario loggeado
            idiomaActual = extras.getString("var_idioma");
            email = extras.getString("var_email");
        }
        //---------------------------------------------------------------------------------
        //  Gestionar pérdida de información al girar la pantalla:
        //  --> GUARDAR:
        //        * EMAIL
        //        * IDIOMA
        if (savedInstanceState != null){
            idiomaActual = savedInstanceState.getString("var_idioma");
            email = savedInstanceState.getString("var_email");
            // Settear idioma actual en la aplicación
            gestorIdioma.setIdioma(idiomaActual);
        }
        //---------------------------------------------------------------------------------
        // Inflar la vista con el layout (act_anadir_pelicula_layout.xml)
        setContentView(R.layout.act_anadir_pelicula_layout);

        // Tras ello, obtener elementos del layout
        et_titulo = findViewById(R.id.add_titulo);
        iv_caratula = findViewById(R.id.add_caratula);
        et_anyo = findViewById(R.id.add_anyo);
        et_genero = findViewById(R.id.add_genero);
        et_duracion = findViewById(R.id.add_duracion);
        et_director = findViewById(R.id.add_director);
        et_actores = findViewById(R.id.add_actores);
        et_sinopsis = findViewById(R.id.add_trama);
        et_trailer = findViewById(R.id.add_url);
        rb_valoracion = findViewById(R.id.add_valoracion);
        cb_visto = findViewById(R.id.add_visto);

        // Imagen genérica, para las películas sin carátula
        caratulaUri =  Uri.parse("android.resource://com.example.entregaindividual_1_anelopezmena/drawable/vacio");
    }

    //---------------------------------------------------------------------------------
    // 2) Método VOLVER: Se ejecuta al pulsar el pequeño botón negro con el icono de
    // la fecha (<--). Hace 'finish()' de la activity
    public void volver(View v) {
        finish();
    }

    //---------------------------------------------------------------------------------
    // 3) Método ON_CLICK: Se ejecuta al pulsar el botón ROJO 'GUARDAR'.
    // Este método gestionará la recogida de datos de los Views de la Activity, y el
    // envío de los datos actualizados, bien a la activity anterior, como a la base de datos
    public void onClick(View v){

        // Obtener texto de los elementos del layout
        String tit = et_titulo.getText().toString();
        String anyo = et_anyo.getText().toString();
        String genero = et_genero.getText().toString();
        String dur = et_duracion.getText().toString();
        String dir =  et_director.getText().toString();
        String act = et_actores.getText().toString();
        String trama = et_sinopsis.getText().toString();
        float val2 = rb_valoracion.getRating();
        String val = String.valueOf(val2);

        // Obtener valor del checkbox de 'visto'
        if ( cb_visto.isChecked() ){ visto = "1";}
        else{ visto="0";}

        String url = et_trailer.getText().toString();
        String c = caratulaUri.toString();

        // Si el campo de 'título' y/o 'director' NO está(n) vacíos,
        // continuar con la operación de añadir el registro en la DB
        if ( !tit.equals("") && !dir.equals("")){
            // Llamar al GestorDB y comprobar que la película que se quiere añadir NO EXISTE
            peliculasDB = new Gestor_DB(this);
            Pelicula p = peliculasDB.conseguirPelicula(tit, dir);
            // Si la película NO está registrada en la DB
            if ( p == null ) {
                // Devolver los datos a la pantalla del menú principal (CODE:50)
                Intent i = new Intent();
                i.putExtra("var_titulo", tit);
                i.putExtra("var_caratula", c);
                i.putExtra("var_director", dir);
                i.putExtra("var_actores", act);
                i.putExtra("var_genero", genero);
                i.putExtra("var_trama", trama);
                i.putExtra("var_anyo", anyo);
                i.putExtra("var_visto", visto);
                i.putExtra("var_valoracion", val);
                i.putExtra("var_duracion", dur);
                i.putExtra("var_trailer", url);

                /**    # (CODE:50) #    **/
                // Settear resultado para atrapar el intent en la otra actividad
                setResult(50, i);

                // Hacer finish de la Activity para que la otra reciba los datos del Intent
                finish();
            }
            else{
                // Si la película SÍ está registrada en la DB, mostrar un dialog que
                // informe al usuario de la situación, y no insertar la película
                Dial_PeliculaYaExiste d_existe = new Dial_PeliculaYaExiste();
                d_existe.show(getSupportFragmentManager(), "PeliculaYaExiste");
            }
        }
        else{
            // Si el campo de 'título' y/o 'director' está(n) vacíos, lanzar
            // un dialog que avise al usuario del problema
            Dial_FaltaTituloDirector d = new Dial_FaltaTituloDirector();
            d.show(getSupportFragmentManager(), "FaltanDatos");
        }
    }

    //---------------------------------------------------------------------------------
    // 4) Método ON_SAVE_INSTANCE_STATE: Este método se ejecuta antes del destroy() de la
    //    aplicación, por lo que es aquí dónde se deben guardar los datos que se quieran
    //    mantener. Por ejemplo,para evitar la pérdida de datos al girar el móvil, interrupciones, etc
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar datos en el objeto outState para evitar la
        // pérdida al girar el móvil, interrupciones, etc.
        outState.putString("var_idioma", this.idiomaActual);
        outState.putString("var_email", this.email);
    }
}