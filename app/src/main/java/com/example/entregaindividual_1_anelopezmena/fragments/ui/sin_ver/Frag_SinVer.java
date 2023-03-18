package com.example.entregaindividual_1_anelopezmena.fragments.ui.sin_ver;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entregaindividual_1_anelopezmena.listasRecyclerView.Adapter_SinVer;
import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_DB;

import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_Idioma;
import com.example.entregaindividual_1_anelopezmena.databinding.FragmentSlideshowBinding;
import com.example.entregaindividual_1_anelopezmena.dialogs.Dial_EliminarPelicula;
import com.example.entregaindividual_1_anelopezmena.interfaces.InterfazRV;
import com.example.entregaindividual_1_anelopezmena.java.Pelicula;

import java.io.OutputStreamWriter;
import java.util.ArrayList;


/*******************************************************************/
/** ----------------------- FRAG_SIN_VER ------------------------- **/
/*******************************************************************/
// Este fragmento mostrará las películas que el usuario logueado actualmente,
// no haya marcado como vistas. La idea de este Fragment, consiste guardar de
// forma centralizada, las películas que el usuario no ha visto aún y quisiera
// ver en algún momento. Se corresponde con la sección de ‘Películas que no has
// visto’ del menú lateral.
public class Frag_SinVer extends Fragment implements InterfazRV {

    private FragmentSlideshowBinding vista;
    private RecyclerView recyclerSinVer;
    private ArrayList<Pelicula> listaPelis;
    private Adapter_SinVer adaptador;
    private Gestor_DB peliculasDB;
    private Gestor_Idioma gestorIdioma;
    private String usuario;
    private String email;
    //---------------------------------------------------------------------------------
    // 1.1) Método constructor (vacío)
    public Frag_SinVer() {
    }
    //---------------------------------------------------------------------------------
    // 1.2) Método constructor
    public Frag_SinVer(String email, String usuario) {
        this.email = email;
        this.usuario=usuario;
    }

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener desde el fragment, el intent lanzado desde el
        // login y recogido por Act_MenuPrincipal.
        Bundle extras = getActivity().getIntent().getExtras();

        // Instanciar gestores: Idiomas y DB
        peliculasDB = new Gestor_DB(getActivity());
        gestorIdioma = new Gestor_Idioma(getActivity());

        // Si el intent tenía datos en los extras
        if (extras !=null) {
            // recoger esos datos en los atributos de la clase
            email = extras.getString("var_email");
            usuario = peliculasDB.getNombreUsuario(email);
        }
    }
    //---------------------------------------------------------------------------------
    // 3) Método ON_CREATE_VIEW
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // "Inflar" el layout del fragment. El fragment es el elemento 'vista'
        vista = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = vista.getRoot();

        // Conseguir View del Recycler
        recyclerSinVer = (RecyclerView) vista.getRoot().findViewById(R.id.rv_sinVer);

        // Conseguir View del Recycler
        Button b_exportar = (Button) vista.getRoot().findViewById(R.id.exportar);

        // Añadir LISTENER al botón de 'EXPORTAR LISTA'
        b_exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llamar al método que genera la lista en .txt
                exportarListaSinVer();
                // Notificar éxito mediante un Toast
                Toast.makeText(getActivity(), getResources().getString(R.string.exportadoExito), Toast.LENGTH_LONG).show();
            }
        });
        //Settear layout de la lista
        recyclerSinVer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

        // Cargar películas de la DB en la app
        listaPelis = peliculasDB.getPeliculasSinVer(email);

        // Instanciar adaptador y cargar los datos
        this.adaptador = new Adapter_SinVer(getActivity(), listaPelis, this);
        recyclerSinVer.setAdapter(adaptador);
        return root;
    }

    //---------------------------------------------------------------------------------
    // 4) Método EXPORTAR_LISTA_SIN_VER: Este método es el encargado de escribir la
    //    lista de películas sin ver por cada usuario que la solicite. Según el idioma,
    //    generará un texto diferente (traducido). Se guardará en la memoria interna
    //    del dispositivo [Desde ANDROID: data>data>proyecto>files]
    private void exportarListaSinVer() {
        // Crear variables para guardar datos (titulo, director, año, valoración e idioma)
        String t, dir, anyo, val;
        String idioma = gestorIdioma.getIdiomaActual();

        // Generación del fichero
        try{
            // Dar un nombre al fichero
            String nombreFichero = "peliculas_sin_ver.txt";
            if (email != null) {
                nombreFichero = email + "_peliculas_sin_ver.txt";
            }
            // Crear objeto 'OutputStreamWriter' para escribir en el interior del fichero
            OutputStreamWriter fichero = new OutputStreamWriter(getActivity().openFileOutput(nombreFichero, Context.MODE_PRIVATE));

            //Según el idioma, el texto será diferente
            switch (idioma) {
                // Si es ESPAÑOL
                case "es":
                    fichero.write("#---------<< LISTA DE PELICULAS PARA VER >>-------------#\n");
                    fichero.write("#-------------------------------------------------------#\n");
                    fichero.write("\n");
                    break;
                // Si es EUSKERA
                case "eu":
                    fichero.write("#---------<< ORAINDIK IKUSI GABEKO PELIKULAK >>-------------#\n");
                    fichero.write("#-----------------------------------------------------------#\n");
                    fichero.write("#            -----------------------------                  #\n");
                    break;
                // Si es INGLÉS
                case "en":
                    fichero.write("#------------------<< TO SEE LIST >>--------------------#\n");
                    fichero.write("#-------------------------------------------------------#\n");
                    fichero.write("\n");
                    break;
                default:
                    break;
            }
            int i=1;
            // Iterar lista de películas sin ver por el usuario
            for( Pelicula p: listaPelis){
                // Obtener datos por cada película
                t = p.getTitulo();
                dir = p.getDirector();
                anyo = p.getAnyo();
                val = String.valueOf(p.getValoracion());

                //Según el idioma, el texto será diferente
                switch (idioma) {
                    // Si es ESPAÑOL
                    case "es":
                        fichero.write("____\n");
                        fichero.write(i+") TITULO: "+t+"\n");
                        fichero.write("    DIRECTOR(ES): "+dir+"\n");
                        fichero.write("    AÑO DE PUBLICACIÓN: "+anyo+"\n");
                        fichero.write("    VALORACIÓN OBTENIDA: "+val+"/5 \n");
                        break;
                    // Si es EUSKERA
                    case "eu":
                        fichero.write("____\n");
                        fichero.write(i+") TITULUA: "+t+"\n");
                        fichero.write("    ZUZENDARIA(K): "+dir+"\n");
                        fichero.write("    ARGITARAPEN-URTEA: "+anyo+"\n");
                        fichero.write("    LORTUTAKO BALORAZIOA: "+val+"/5 \n");
                        break;
                    // Si es INGLES
                    case "en":
                        fichero.write("____\n");
                        fichero.write(i+") TITTLE: "+t+"\n");
                        fichero.write("    DIRECTOR(S): "+dir+"\n");
                        fichero.write("    YEAR OF RELEASE: "+anyo+"\n");
                        fichero.write("    RATING OF THE FILM: "+val+"/5 \n");
                        break;
                }
                // Escribir separador
                fichero.write("#-------------------------------------------------------#\n");
                fichero.write("#------------------- BLOCKBUSTER! ----------------------#\n");
                i++;
            }
            // Cerrar el fichero al acabar de escribir
            fichero.close();
        }
        catch(Exception e){
            // Notificar del error en el LOG
            Log.e("EXPORTAR", "Error escribiendo el fichero de texto");
        }
    }

    //---------------------------------------------------------------------------------
    // 5) Método ON_DESTROY_VIEW
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vista = null;
    }
    //---------------------------------------------------------------------------------
    // 5) Método ON_ITEM_LONG_CLICK: Se ejecutará cada vez que el usuario mantenga pulsado
    // un item de la lista durante unos segundos. En este caso, se eliminará la película
    // si el usuario confirma la acción mediante el díalogo de alerta que se lanza
    @Override
    public void onitemLongClick(int pos) {

        // Obtener identificadores de la película --> Titulo y directores
        String t = listaPelis.get(pos).getTitulo();
        String d = listaPelis.get(pos).getDirector();
        // Llamar al diálogo (Dial_EliminarPelicula)
        Dial_EliminarPelicula d2 = new Dial_EliminarPelicula(this, pos, t, d);
        d2.show(getActivity().getSupportFragmentManager(), "EliminarVisto");

    }
    // Método de la Interfaz - No es necesario en esta clase
    @Override
    public void onClick(int pos) {}

    //---------------------------------------------------------------------------------
    // 6) Método ELIMINAR_CONFIRMADO: Se ejecutará cuando el usuario confirme la
    // acción de eliminar la película desde el dialog. Se eliminará el registro de
    // la película y se notificará al adaptador para que actualice la lista
    @Override
    public void eliminarConfirmado(int pos) {
        // Obtener elemento de la lista
        Pelicula p = listaPelis.get(pos);
        // Añadirla como vista en la DB
        peliculasDB.insertarPeliculaVista(email, p.getTitulo(), p.getDirector());
        //Eliminar elemento de la lista
        listaPelis.remove(pos);
        //Notificar al adapter
        adaptador.notifyItemRemoved(pos);

    }
    // Método de la Interfaz - No es necesario en esta clase
    @Override
    public void notificarActualizado() {}

}