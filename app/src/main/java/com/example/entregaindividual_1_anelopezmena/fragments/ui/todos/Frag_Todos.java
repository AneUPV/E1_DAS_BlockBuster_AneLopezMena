package com.example.entregaindividual_1_anelopezmena.fragments.ui.todos;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.entregaindividual_1_anelopezmena.activities.Act_AñadirPelicula;
import com.example.entregaindividual_1_anelopezmena.activities.Act_InfoPelicula;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_DB;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_Idioma;
import com.example.entregaindividual_1_anelopezmena.dialogs.Dial_Sinopsis;
import com.example.entregaindividual_1_anelopezmena.interfaces.InterfazRV;
import com.example.entregaindividual_1_anelopezmena.java.Pelicula;
import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.listasRecyclerView.AdapterRV_Peliculas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.example.entregaindividual_1_anelopezmena.databinding.FragmentTodosDrawerBinding;


/*******************************************************************/
/** ------------------------- FRAG_TODOS ------------------------ **/
/*******************************************************************/
// Este Fragment contiene la lista completa de películas guardadas en
// la aplicación. Además, está asignado como pantalla predeterminada
// a mostrar cuando el usuario accede a la aplicación, por lo que muestra
// todas las películas en el menú principal. Se corresponde con la sección
// de ‘Todas las películas’ del menú lateral.

public class Frag_Todos extends Fragment implements InterfazRV{

    // Atributos privados
    private ArrayList<Pelicula> listaPelis;
    private ArrayList<Pelicula> listaFiltrada;
    private AdapterRV_Peliculas adaptador;
    private FloatingActionButton b_addPelicula;
    private RecyclerView recyclerPeliculas;
    private String usuario;
    private String email;
    private String idiomaActual;
    private Gestor_DB peliculasDB;
    private Gestor_Idioma gestorIdioma;
    private FragmentTodosDrawerBinding vista;
    private boolean cargados;

    //---------------------------------------------------------------------------------
    // 1) Método constructor (vacío)
    public Frag_Todos(){}

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crear gestores: DB e IDIOMA
        gestorIdioma = new Gestor_Idioma(getActivity());
        peliculasDB= new Gestor_DB(getActivity());

        // Inicializar variable de carga de ejemplos
        cargados = false;
    //------------------------------------------------------------------------------------------
        // Obtener intent lanzado desde el Login hasta la
        // Act_MenuPrincipal.
        Bundle extras = getActivity().getIntent().getExtras();
        // Si tiene datos, recogerlos
        if (extras !=null) {
            email = extras.getString("var_email");
            usuario = extras.getString("var_usuario");
            idiomaActual = extras.getString("var_idioma");
            cargados = extras.getBoolean("var_cargados");
            gestorIdioma.setIdioma(this.idiomaActual);
        }
        // Settear el idioma recibido desde el intent
        this.idiomaActual = gestorIdioma.getIdiomaActual();

    //------------------------------------------------------------------------------------------
        // Gestionar pérdida de información al girar la pantalla:
        // GUARDAR:
        //      * IDIOMA
        //      * EMAIL
        //      * USUARIO
        //      * CARGADOS
        if (savedInstanceState != null){
            this.idiomaActual = savedInstanceState.getString("var_idioma");
            this.email = savedInstanceState.getString("var_email");
            this.usuario = savedInstanceState.getString("var_usuario");
            this.cargados = savedInstanceState.getBoolean("var_cargados");
            gestorIdioma.setIdioma(this.idiomaActual);
        }
        // Una vez conseguidos los datos, actualizar idioma en el gestor
        this.idiomaActual=gestorIdioma.getIdiomaActual();
    }

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE_VIEW
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        // "Inflar" el layout del fragment. El fragment es el elemento 'vista'
        vista = FragmentTodosDrawerBinding.inflate(inflater, container, false);
        View root = vista.getRoot();

        // Conseguir View del Recycler
        recyclerPeliculas = (RecyclerView) vista.getRoot().findViewById(R.id.rv_todos);
        // Recoger orientación
        int orientacion = getResources().getConfiguration().orientation;

        // Si es la orientación es VERTICAL
        if (orientacion == Configuration.ORIENTATION_PORTRAIT) {
            // Obtener textView donde se saluda al usuario, que solo existe en la vista vertical
            TextView saludo = vista.getRoot().findViewById(R.id.saludaUsuario);

            // Si el mail ha llegado
            if (email !=null){
                // Comprobar las preferencias sobre el nombre de usuario
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                // Si el usuario ha indicado un nickname o apodo, actualizarlo en el menú principal
                if (sp.contains("nombre")){
                    usuario = sp.getString("nombre", usuario);
                    peliculasDB.actualizarUsuario(usuario, email);
                }
                // Actualizar nombre en DB y mensaje de la vista
                usuario = peliculasDB.getNombreUsuario(email);
                saludo.setText(getResources().getString(R.string.hola) +" "+usuario+" !");
            }
            saludo.setText(getResources().getString(R.string.hola) +" "+usuario+" !");
            recyclerPeliculas.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        }
        else{
            // Settear la disposición StaggeredGridLayout, con 2 columnas en orientación VERTICAL
            recyclerPeliculas.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        }

        if (!cargados) {
            // Insertar 14 películas con los datos completos en la DB
            peliculasDB.cargarEjemplos();
            cargados=true;
        }
        // Cargar películas de la DB en la app
        listaPelis = peliculasDB.getPeliculas();
        listaFiltrada = listaPelis;

        // Instanciar adaptador y cargar los datos
        this.adaptador = new AdapterRV_Peliculas(getActivity(), listaPelis, this);
        recyclerPeliculas.setAdapter(adaptador);
        return root;
    }

    //---------------------------------------------------------------------------------
    // 3) Método ON_VIEW_CREATED
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


// --------------------------------***### AÑADIR PELÍCULA ###***---------------------------------- //

        // Obtener orientación de la pantalla
        int orientacion = getResources().getConfiguration().orientation;

        if (orientacion == Configuration.ORIENTATION_PORTRAIT) {  // Si es VERTICAL

        // 1.- BOTÓN AÑADIR PELÍCULA (+)  ---> RESULT CODE = 0
            //Obtener view del botón
            b_addPelicula = view.findViewById(R.id.botonAñadir);
            // Añadir listener al Floating Action Button (+)
            b_addPelicula.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Crear un intent, que envíe datos a la Act_AñadirPelicula
                    Intent i = new Intent(getActivity(), Act_AñadirPelicula.class);
                    // Añadir usuario, email e idioma al intent
                    i.putExtra("var_usuario", usuario);
                    i.putExtra("var_email", email);
                    i.putExtra("var_idioma", idiomaActual);
                    // Lanzar intent
                    startActivityIntent.launch(i);
                }
            });
        }
    }
    //---------------------------------------------------------------------------------
    // 4) Método ON_ACTIVITY_RESULT:
    // Este método espera a recibir resultado desde la activity 'Act_AñadirPelicula' ,
    // con el código de resultado 50. Una vez obtenidos los datos, añadirá la película en la DB
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if ( result.getData() != null && result.getResultCode() == 50) {

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

                        // Insertar película en DB
                        Pelicula p = peliculasDB.insertarPelícula(titulo, caratula, director, actores, trama, genero, anyo, Integer.valueOf(visto), Float.parseFloat(valoracion), duracion, trailer);
                        // Notificar del insert al adapter
                        notificarAnadido(p);
                    }
                }
            });

    //---------------------------------------------------------------------------------
    // 5) Método NOTIFICAR_AÑADIDO: Notificar al adapter para actualizar la lista
    private void notificarAnadido(Pelicula p) {
        // Añadir película
        listaPelis.add(p);
        // Notificar al adaptador
        adaptador.notifyItemInserted(listaPelis.size()-1);
    }

    //---------------------------------------------------------------------------------
    // 6) Método NOTIFICAR_ACTUALIZADO: Notificar al adapter para actualizar los
    // datos sobre la lista
    public void notificarActualizado(int pos, Pelicula p) {
        // ACTUALIZAR DATOS
        adaptador.notifyItemChanged(pos, p);
    }

    //---------------------------------------------------------------------------------
    // 7) Método ON_RESUME
    @Override
    public void onResume() {

        // Recoger orientación
        int orientacion = getResources().getConfiguration().orientation;
        // Si es al orientación es VERTICAL
        if (orientacion == Configuration.ORIENTATION_PORTRAIT) {
            // Recoger TextView del saludo al usuario
            TextView saludo = vista.getRoot().findViewById(R.id.saludaUsuario);
            // Si el email se ha mantenido bien, es decir, el dato no se ha perdido
            if (email !=null){
                // Comprobar las preferencias sobre el nombre de usuario
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                // Si el usuario ha indicado un nickname o apodo, actualizarlo en el menú principal
                if (sp.contains("nombre")){
                    usuario = sp.getString("nombre", usuario);
                    peliculasDB.actualizarUsuario(usuario, email);
                }
                // Actualizar mensaje del menú principal
                usuario = peliculasDB.getNombreUsuario(email);
                saludo.setText(getResources().getString(R.string.hola) +" "+usuario+" !");
            }
            saludo.setText(getResources().getString(R.string.hola) +" "+usuario+" !");
        }
        super.onResume();
    }

// -------------------------------- INFO + MODIFICAR PELICULA --------------------------------- //
    //==================================================================================
    // Métodos onClick de la interfaz 'InterfazRV' implementados


    // Método de la Interfaz - No es necesario en esta clase
    @Override
    public void onitemLongClick(int pos) { }

    //---------------------------------------------------------------------------------
    // 8) Método ON_CLICK: ---> RESULT CODE = 1
    // Este método envía todos los datos a la activity Act_infoPelicula
    // para visualizar los datos sobre la película que se haya seleccionado
    @Override
    public void onClick(int position) {
        // Obtener película desde posición clicada
        Pelicula p;
        p = peliculasDB.conseguirPelicula(listaPelis.get(position).getTitulo(), listaPelis.get(position).getDirector());
        // Según la orientación
        int orientacion = getResources().getConfiguration().orientation;
        //   * Si la orientación es VERTICAL
        if (orientacion == Configuration.ORIENTATION_PORTRAIT) {
            // Enviar los datos a Act_InfoPelicula desde un Intent
            // Crear intent hacia Act_InfoPelicula
            Intent i1 = new Intent(getActivity(), Act_InfoPelicula.class);
            // Pasar datos: FRAGMENTO_TODOS ---> ACT_INFO_PELICULA
            i1.putExtra("var_email", email);
            i1.putExtra("var_titulo", p.getTitulo());
            i1.putExtra("var_caratula", p.getCaratula());
            i1.putExtra("var_director", p.getDirector());
            i1.putExtra("var_actores", p.getActores());
            i1.putExtra("var_genero", p.getGenero());
            i1.putExtra("var_trama", p.getTrama());
            i1.putExtra("var_anyo", p.getAnyo());
            i1.putExtra("var_valoracion", p.getValoracion());
            i1.putExtra("var_duracion", p.getDuracion());
            i1.putExtra("var_trailer", p.getTrailer());
            i1.putExtra("var_posicion", String.valueOf(position));
            i1.putExtra("var_idioma", this.idiomaActual);

            // Obtener si el usuario ya ha visto la película
            boolean v = peliculasDB.getVisto(email, p.getTitulo(), p.getDirector());
            i1.putExtra("var_visto", v);

            // Lanzar intent
            startActivityIntent2.launch(i1);
        }else{
                // Si la orientación es HORIZONTAL
                // Obtener los elementos exclusivos del fragment que aparece en la orientación horizontal
                TextView tv_titulo_s = getActivity().findViewById(R.id.s_titulo);
                TextView tv_director_s = getActivity().findViewById(R.id.s_director);
                ImageView iv_caratula_s = getActivity().findViewById(R.id.s_caratula);
                ImageView info = getActivity().findViewById(R.id.s_info);

                // Settear los valores
                tv_titulo_s.setText(p.getTitulo());
                tv_director_s.setText(p.getDirector());
                iv_caratula_s.setImageURI(Uri.parse(p.getCaratula()));

                // Listener sobre el botón de información, que mostrará el dialogo de la sinopsis
                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Llamar al dialog (Dial_Sinopsis)
                        Dial_Sinopsis d = new Dial_Sinopsis(p.getTitulo(), p.getDirector());
                        d.show(getActivity().getSupportFragmentManager(), "MostrarInfo");
                    }
                });
        }
    }
    //---------------------------------------------------------------------------------
    // 9) Método ELIMINAR_CONFIRMADO: Se ejecutará cuando el usuario confirme la
    // acción de eliminar la película desde el dialog. Se eliminará el registro de
    // la película y se notificará al adaptador para que actualice la lista
    @Override
    public void eliminarConfirmado(int pos) {
        listaPelis.remove(pos);
        adaptador.notifyDataSetChanged();
    }

    //---------------------------------------------------------------------------------
    // Método de la Interfaz - No es necesario en esta clase
    @Override
    public void notificarActualizado() {}

    //---------------------------------------------------------------------------------
    // 10) Método ON_ACTIVITY_RESULT:
    // Este método espera a recibir resultado desde la activity 'Act_InfoPelicula' ,
    // con el código de resultado 1. Una vez obtenidos los datos, añadirá o actualizará
    // la película en la DB
    ActivityResultLauncher<Intent> startActivityIntent2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                // Le ha pulsado a 'MODIFICAR'
                public void onActivityResult(ActivityResult result) {
                    if ( result.getData() != null && result.getResultCode() == 1) {
                        // Obtener datos
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

                        // Obtener posición para notificar al adapter
                        String pos = result.getData().getStringExtra("var_posicion");
                        // Obtener equivalente al valor de visto en int
                        int v=0;
                        if (Boolean.valueOf(visto)){ v=1;}

                        // Crear la peLícula
                        Pelicula p = new Pelicula(titulo, caratula, director, actores, trama, genero, anyo, v, Float.parseFloat(valoracion), duracion, trailer);

                        if (peliculasDB.conseguirPelicula(titulo, director)==null) {
                            // Si la pelicula es nueva (ES NULL), añadirla a la lista y notificar al adapter
                            notificarAnadido(p);
                        }
                        else{ // La película se ha modificado, notificar al adapter del cambio
                            notificarActualizado(Integer.parseInt(pos), p);
                        }
                    }
                    // Le ha pulsado a 'ELIMINAR'
                    if ( result.getData() != null && result.getResultCode() == 404) {
                        // Obtener identificadores de la película a eliminar
                        String t = result.getData().getExtras().getString("var_titulo");
                        String d = result.getData().getExtras().getString("var_director");
                        int pos = result.getData().getExtras().getInt("var_posicion");
                        // Eliminar película de la DB
                        peliculasDB.eliminarPelicula(t, d);
                        // Llamar a confirmar eliminado
                        eliminarConfirmado(pos);
                    }
                }
            });

    //---------------------------------------------------------------------------------
    // 11) Método ON_SAVE_INSTANCE_STATE: Este método se ejecuta antes del destroy() de la
    //    aplicación, por lo que es aquí dónde se deben guardar los datos que se quieran
    //    mantener. Por ejemplo,para evitar la pérdida de datos al girar el móvil, interrupciones,
    //    etc.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar datos para evitar la pérdida al girar el móvil, interrupciones, etc.
        outState.putString("var_idioma", this.idiomaActual);
        outState.putString("var_email", this.email);
        outState.putString("var_usuario", this.usuario);
        outState.putBoolean("var_cargados", cargados);

    }

    //---------------------------------------------------------------------------------
    // 12) Método ON_DESTROY_VIEW
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vista = null;
    }

}