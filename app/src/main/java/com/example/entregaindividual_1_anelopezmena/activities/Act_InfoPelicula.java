package com.example.entregaindividual_1_anelopezmena.activities;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_Idioma;
import com.example.entregaindividual_1_anelopezmena.dialogs.Dial_EliminarPelicula;
import com.example.entregaindividual_1_anelopezmena.interfaces.InterfazRV;


/*************************************************************************/
/** --------------------ACTIVITY_INFO_PELICULA ---------------------- **/
/*************************************************************************/
// Se trata de una actividad que permite leer todas particularidades de la película
// seleccionada. La actividad ‘Act_InfoPelícula’ es la que se encargará de mostrar de forma
// ordenada todos los detalles de una película o serie ( Título, año de publicación, género(s),
// tráiler oficial, director(es), actores, etc. )

public class Act_InfoPelicula extends AppCompatActivity implements InterfazRV {

    // Atributos privados
    private String t_titulo, t_caratula ,t_director,t_actores, t_genero, t_trama,t_anyo,t_duracion, t_url, email, pos;
    private TextView tv_titulo, tv_director, tv_actores, tv_genero, tv_trama, tv_anyo, tv_duracion;
    private ImageView iv_caratula;
    private CheckBox cb_visto;
    private RatingBar rb_valoracion;
    private WebView wb_trailer;
    private String idiomaActual;
    private boolean t_visto;
    private float t_valoracion;
    private boolean visto;
    private Gestor_Idioma gestorIdioma;

    //---------------------------------------------------------------------------------
    // 1) Método ON_CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // El idioma por defecto es el ESPAÑOL
        idiomaActual = "es";

        // Crear gestores: IDIOMA
        gestorIdioma = new Gestor_Idioma(this);

        //--------------------------------------------------------------------
        // Gestionar pérdida de información al girar la pantalla:
        // GUARDAR:
        //      * EMAIL
        //      * IDIOMA
        if (savedInstanceState != null){
            this.idiomaActual = savedInstanceState.getString("var_idioma");
            this.email = savedInstanceState.getString("var_email");
            // Settear el idioma recogido
            gestorIdioma.setIdioma(this.idiomaActual);
        }
        // Una vez obtenido el idioma, cargar vista
        setContentView(R.layout.act_info_pelicula);

        //--------------------------------------------------------------------
        // Obtener elementos del layout
        tv_titulo = (TextView) findViewById(R.id.edit_titulo);
        iv_caratula = (ImageView) findViewById(R.id.edit_caratula);
        tv_director = (TextView) findViewById(R.id.info_director);
        tv_actores = (TextView) findViewById(R.id.info_actores);
        tv_trama = (TextView) findViewById(R.id.info_sinopsis);
        tv_genero = (TextView) findViewById(R.id.info_genero);
        tv_anyo = (TextView) findViewById(R.id.info_anyo);
        cb_visto = (CheckBox) findViewById(R.id.edit_visto);
        rb_valoracion = (RatingBar) findViewById(R.id.edit_valoracion);
        tv_duracion = (TextView) findViewById(R.id.info_duracion);
        wb_trailer = (WebView) findViewById(R.id.info_trailer);

        // Recibir los datos lanzados desde el menú principal
        Intent intent = getIntent();
        t_titulo = intent.getExtras().getString("var_titulo");
        t_caratula = intent.getExtras().getString("var_caratula");
        t_director = intent.getExtras().getString("var_director");
        t_actores = intent.getExtras().getString("var_actores");
        t_genero = intent.getExtras().getString("var_genero");
        t_trama = intent.getExtras().getString("var_trama");
        t_anyo = intent.getExtras().getString("var_anyo");
        t_visto = intent.getExtras().getBoolean("var_visto");
        t_valoracion = intent.getExtras().getFloat("var_valoracion");
        t_duracion = intent.getExtras().getString("var_duracion");
        t_url = intent.getExtras().getString("var_trailer");
        email = intent.getExtras().getString("var_email");
        visto = intent.getExtras().getBoolean("var_visto");
        pos = intent.getExtras().getString("var_posicion");
        idiomaActual = intent.getExtras().getString("var_idioma");

        // Settear idioma en la actividad
        gestorIdioma.setIdioma(idiomaActual);

        // Settear datos recogidos con getExtras() en los Views de la actividad InfoPelicula
        tv_titulo.setText(t_titulo);
        iv_caratula.setImageURI(Uri.parse(t_caratula));
        tv_director.setText(t_director);
        tv_actores.setText(t_actores);
        tv_genero.setText(t_genero);
        tv_trama.setText(t_trama);
        tv_anyo.setText(t_anyo);

        if (t_visto) {cb_visto.setChecked(true);}
        else{cb_visto.setChecked(false);}
        // No se puede modificar desde la pantalla de información, solo desde la pantalla de modificar
        cb_visto.setEnabled(false);
        rb_valoracion.setRating(t_valoracion);
        tv_duracion.setText(t_duracion);

        // Cargar URL
        wb_trailer.loadUrl(t_url);
        // Configurar navegador para cargar trailer de la película en la activity
        wb_trailer.setWebViewClient(new WebViewClient());
        WebSettings ajustes = wb_trailer.getSettings();
        ajustes.setJavaScriptEnabled(true);

    }
    //--------------------------------------------------------------------
                      /* ----------------- TOOLBAR ---------------- */
    //                 Menú 'toolbar' en la pantalla Act_InfoPelicula

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE_OPTIONS_MENU: Se ejecuta en el momento de crear el toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // le cargar el xml correspondiente al toolbar de 'Act_InfoPelícula (menu_home.xml)
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //---------------------------------------------------------------------------------
    // 3) Método ON_OPTIONS_ITEM_SELECTED:
    // Este método será el encargado de realizar acciones al detectar un click en
    // un elemento del toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Obtener id del elemento pulsado, y actuar en cada caso
        switch (item.getItemId()) {
            //Si se ha pulsado 'Compartir'
            case R.id.compartir:
                // Llamada al método, que hace el INTENT IMPLÍCITO a la aplicación de correo
                recomendarPelicula();
                break;
            // Si se ha pulsado 'Eliminar película'
            case R.id.eliminar:
                // Crear el díalog que advierte al usuario sobre la eliminación de la película
                Dial_EliminarPelicula d = new Dial_EliminarPelicula(this, Integer.parseInt(pos), t_titulo,t_director);
                d.show(getSupportFragmentManager(), "EliminarPelicula");
                break;
            // Si se ha pulsado 'Modificar información'
            case R.id.modificar:
                // Enviar datos INFO_PELICULAS ---> MODIFICAR PELICULAS en un Intent
                Intent i2 = new Intent(Act_InfoPelicula.this, Act_ModificarPelicula.class);
                i2.putExtra("var_titulo", t_titulo);
                i2.putExtra("var_caratula" ,t_caratula);
                i2.putExtra("var_director", t_director);
                i2.putExtra("var_actores", t_actores);
                i2.putExtra("var_genero", t_genero);
                i2.putExtra("var_trama", t_trama);
                i2.putExtra("var_anyo", t_anyo);
                i2.putExtra("var_visto", t_visto);
                i2.putExtra("var_valoracion", String.valueOf(t_valoracion));
                i2.putExtra("var_duracion", t_duracion);
                i2.putExtra("var_trailer", t_url);
                i2.putExtra("var_email", email);
                i2.putExtra("var_posicion", pos);
                i2.putExtra("var_idioma", this.idiomaActual);

                // Lanzar activity
                startActivityIntent2.launch(i2);
                break;
            // Caso por defecto
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------------------------------------
    // 4) Método RECOMENDAR_PELICULA:
    // Este método será el encargado de escribir el contenido del correo electrónico que
    // se enviará desde la aplicación
    private void recomendarPelicula() {
        // Crear Strings dónde guardar el asunto y el cuerpo del mensaje
        String asunto="Este es el asunto";
        String cuerpo="Este es el cuerpo";

        // Según el idioma seleccionado, escribir el mensaje en ese idioma
        switch (idiomaActual){
            //Si el idioma es 'Euskera'(eu)
            case "eu":
                // Definir asunto y cuerpo en euskera
                asunto=" BLOCKBUSTER! - Zergatik ez duzu pelikula hau ikusten?";
                cuerpo=" Pelikula on baten bila zabiltza? Ez ezazu gehiago bilatu, hemen doakizu!\n" +
                        " * '"+t_titulo+"' , "+t_director+"-ren eskutik *\n";
                // Si tiene trailer, que lo incluya en el email
                if (t_url.length()>0) {
                    cuerpo = cuerpo + " --> Trailer ofiziala (Youtube): " + t_url;
                }
                break;
            //Si el idioma es 'Español'(es)
            case "es":
                // Definir asunto y cuerpo en español
                asunto=" BLOCKBUSTER! - ¿Por qué no le echas un vistazo a esta película?";
                cuerpo=" Si estás buscando una peli... ¡No busques más! \n" +
                        "Aquí tienes una buena recomendación\n"+
                        " * '"+t_titulo+"' de "+t_director+" *\n";
                // Si tiene trailer, que lo incluya en el email
                if (t_url.length()>0) {
                    cuerpo = cuerpo + " --> Trailer oficial (Youtube): "+t_url;
                }
                break;
            //Si el idioma es 'Inglés'(en)
            case "en":
                // Definir asunto y cuerpo en inglés
                asunto=" BLOCKBUSTER! - Searching for a GREAT MOVIE?";
                cuerpo=" This is your lucky day! I recommend you to watch this one! \n" +
                        " * '"+t_titulo+"' by "+t_director+" *\n";
                // Si tiene trailer, que lo incluya en el email
                if (t_url.length()>0) {
                    cuerpo = cuerpo + " --> Official trailer (Youtube): "+t_url;
                }
                break;
        }

        // Una vez listo el mensaje, crear el intent a enviar
        Intent i = new Intent(Intent.ACTION_SEND);
        // Añadir el cuerpo y el mensaje en el intent
        i.putExtra(Intent.EXTRA_SUBJECT, asunto);
        i.putExtra(Intent.EXTRA_TEXT,cuerpo);

        // Abrir cliente de mail. Este código solo permitirá enviarlo por EMAIL, no
        // por whatsapp ni por otras plataformas. message/rfc822 --> corresponde al
        // cliente de MAIL
        i.setType("message/rfc822");
        // Lanzar intent, en el que se permitirá elegir la aplicación de mail
        startActivity(Intent.createChooser(i,"elige la app"));
    }

    //---------------------------------------------------------------------------------
    // 5) Método ON_ACTIVITY_RESULT:
    // Este método espera a recibir resultado desde la activity 'Act_ModificarPelícula' ,
    // con el código de resultado 2. Una vez obtenidos los datos, hará finish para volver al
    // menú principal
    ActivityResultLauncher<Intent> startActivityIntent2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    /**    # (CODE:2) #    **/
                    if ( result.getData() != null && result.getResultCode() == 2) {
                        String titulo = result.getData().getStringExtra("var_titulo");
                        String caratula = result.getData().getStringExtra("var_caratula");
                        String director = result.getData().getStringExtra("var_director");
                        String actores = result.getData().getStringExtra("var_actores");
                        String trama = result.getData().getStringExtra("var_trama");
                        String genero = result.getData().getStringExtra("var_genero");
                        String anyo = result.getData().getStringExtra("var_anyo");
                        String visto = result.getData().getStringExtra("var_visto");
                        String valoracion = result.getData().getStringExtra("var_valoracion");
                        String duracion = result.getData().getStringExtra("var_duracion");
                        String trailer = result.getData().getStringExtra("var_trailer");
                        String idiomaActual = result.getData().getStringExtra("var_idioma");

                        // DEVOLVER DATOS A FRAGMENTO_TODOS
                        Intent i4 = new Intent();
                        i4.putExtra("var_titulo", titulo);
                        i4.putExtra("var_caratula" ,caratula);
                        i4.putExtra("var_director", director);
                        i4.putExtra("var_actores", actores);
                        i4.putExtra("var_genero", genero);
                        i4.putExtra("var_trama", trama);
                        i4.putExtra("var_anyo", anyo);
                        i4.putExtra("var_visto", String.valueOf(visto));
                        i4.putExtra("var_valoracion", String.valueOf(valoracion));
                        i4.putExtra("var_duracion", duracion);
                        i4.putExtra("var_trailer", trailer);
                        i4.putExtra("var_email", email);
                        i4.putExtra("var_posicion", pos);
                        i4.putExtra("var_idioma", idiomaActual);
                        setResult(1, i4);

                        // Hacer finish de la Activity para que la otra reciba los datos del Intent
                        // Devolver los datos a la pantalla del menú principal (Frag_Todos) (CODE:1)
                        finish();
                    }
                }
            });
// ------------------------------------------------------------------------------------------------
    // Métodos de la interfaz, a implementar en la clase.
    // En este caso, 'onitemLongClick' y 'onClick' no serán necesarios
    @Override
    public void onitemLongClick(int pos) {}

    @Override
    public void onClick(int pos) {}

    //---------------------------------------------------------------------------------
    // 6) Método ELIMINAR_CONFIRMADO:
    // Este método, que es llamado por un listener creará un intent que 
    // volverá a menú principal para notificar de que se ha eliminado la
    // película que se había clicado.
    @Override
    public void eliminarConfirmado(int pos) {
        // Crea un intent donde cargar los datos
        Intent i5 = new Intent();
        i5.putExtra("var_titulo", t_titulo);
        i5.putExtra("var_director", t_director);
        i5.putExtra("var_posicion", pos);
        i5.putExtra("var_idioma", this.idiomaActual);
        /**    # (CODE:404) #    **/
        setResult(404, i5);
        // Hacer finish de la Activity para que la otra reciba los datos del Intent
        // Devolver los datos a la pantalla del menú principal (Frag_Todos) (CODE:404)
        finish();
    }
    // Método de la interfaz, a implementar en la clase.
    // En este caso, 'notificarActualizado' no será necesario
    @Override
    public void notificarActualizado() {}

    //---------------------------------------------------------------------------------
    // 7) Método ON_SAVE_INSTANCE_STATE: Este método se ejecuta antes del destroy() de la
    //    aplicación, por lo que es aquí dónde se deben guardar los datos que se quieran
    //    mantener. Por ejemplo,para evitar la pérdida de datos al girar el móvil, interrupciones,
    //    etc.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar datos en el objeto outState para evitar la
        // pérdida al girar el móvil, interrupciones, etc.
        outState.putString("var_idioma", this.idiomaActual);
        outState.putString("var_email", this.email);
    }

}