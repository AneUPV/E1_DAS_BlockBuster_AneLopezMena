package com.example.entregaindividual_1_anelopezmena.fragments.ui.nested;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_DB;
import com.example.entregaindividual_1_anelopezmena.databinding.FragmentNestedDrawerBinding;
import com.example.entregaindividual_1_anelopezmena.listasRecyclerView.nestedRecyclerView.ChildModelClass;
import com.example.entregaindividual_1_anelopezmena.listasRecyclerView.nestedRecyclerView.ParentAdapter;
import com.example.entregaindividual_1_anelopezmena.listasRecyclerView.nestedRecyclerView.ParentModelClass;

import java.util.ArrayList;


/*******************************************************************/
/** ----------------------- FRAG_NESTED ------------------------- **/
/*******************************************************************/
// Este es uno de los 3 Fragmentos que conviven dentro del Navigation Drawer
// que se muestra como menú principal de la aplicación. Este fragment en concreto,
// Muestra la lista vertical con listas horizontales implementadas en las clases
// del paquete 'nestedRecyclerView' del proyecto. Consiste en una pantalla
// en la que se visualizan las carátulas de las películas añadidas a la aplicación.

public class Frag_Nested extends Fragment {

    // Atributos privados de la clase
    private RecyclerView recyclerView;
    private FragmentNestedDrawerBinding binding;
    private ArrayList<ParentModelClass> parentModelClassArrayList;
    private ArrayList<ChildModelClass> childModelClassArrayList;

    // 3 Listas horizontales(child rv) que se muestran dentro de la vertical(parent rv)
    private ArrayList<ChildModelClass> listaFavoritos;
    private ArrayList<ChildModelClass> listaNuevos;
    private ArrayList<ChildModelClass> listaValorados;

    private String email;
    private String usuario;

    // Declarar Adapter del Recycler View 'Padre'
    ParentAdapter parentAdapter;

    //---------------------------------------------------------------------------------
    // 1.1) Método constructor
    public Frag_Nested(String email, String u) {
        this.email = email;
        this.usuario=u;
    }
    //---------------------------------------------------------------------------------
    // 1.2) Método constructor (Vacío)
    public Frag_Nested() {    }

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // ------------- onCreate -------------//
        // 1er evento
        super.onCreate(savedInstanceState);
        childModelClassArrayList = new ArrayList<>();
        parentModelClassArrayList = new ArrayList<>();
        listaFavoritos = new ArrayList<>();
        listaNuevos = new ArrayList<>();
        listaValorados = new ArrayList<>();
        recyclerView = new RecyclerView(getActivity());

        // Instanciar Adapter del Recycler View 'Padre'
        parentAdapter = new ParentAdapter(parentModelClassArrayList, getActivity());

        // Cargar los datos y notificar al adapter
        recyclerView.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();
    }

    //---------------------------------------------------------------------------------
    // 3) Método ON_CREATE_VIEW
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // binding = vista
        binding = FragmentNestedDrawerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener elemento 'recycler view'
        recyclerView = binding.getRoot().findViewById(R.id.rv_parent);
        // Settear el layout de la lista, LINEAR LAYOUT(VERTICAL), en este caso
        LinearLayoutManager vertical = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(vertical);

        // Inicializar las listas
        parentModelClassArrayList = new ArrayList<>();
        listaFavoritos = new ArrayList<>();
        listaNuevos = new ArrayList<>();
        listaValorados = new ArrayList<>();

        // Añadir elementos de muestra
        listaNuevos.add(new ChildModelClass(R.drawable.post_in_time));
        listaNuevos.add(new ChildModelClass(R.drawable.post_mision_imposible_fallout));
        listaNuevos.add(new ChildModelClass(R.drawable.post_matrix_resurrections));
        listaNuevos.add(new ChildModelClass(R.drawable.post_muerte_en_el_nilo));
        listaNuevos.add(new ChildModelClass(R.drawable.post_robin_hood));
        listaNuevos.add(new ChildModelClass(R.drawable.post_judgement_of_paris));

        // RecyclerView 'Padre', que contiene el título y el Recycler View 'Hijo'
        parentModelClassArrayList.add(new ParentModelClass(getResources().getString(R.string.novedades), listaNuevos));


        // Añadir elementos de muestra
        listaValorados.add(new ChildModelClass(R.drawable.post_avatar));
        listaValorados.add(new ChildModelClass(R.drawable.post_caballero_luna));
        listaValorados.add(new ChildModelClass(R.drawable.post_inception));
        listaValorados.add(new ChildModelClass(R.drawable.post_punales_por_la_espalda));
        listaNuevos.add(new ChildModelClass(R.drawable.post_in_time));
        listaNuevos.add(new ChildModelClass(R.drawable.post_mision_imposible_fallout));
        listaNuevos.add(new ChildModelClass(R.drawable.post_matrix_resurrections));

        // RecyclerView 'Padre', que contiene el título y el Recycler View 'Hijo'
        parentModelClassArrayList.add(new ParentModelClass(getResources().getString(R.string.valoradas), listaValorados));


        // Añadir elementos de muestra
        listaFavoritos.add(new ChildModelClass(R.drawable.post_matrix));
        listaFavoritos.add(new ChildModelClass(R.drawable.post_john_wick_parabellum));
        listaFavoritos.add(new ChildModelClass(R.drawable.post_buscando_a_nemo));
        listaFavoritos.add(new ChildModelClass(R.drawable.post_robin_hood));
        listaNuevos.add(new ChildModelClass(R.drawable.post_in_time));
        listaNuevos.add(new ChildModelClass(R.drawable.post_mision_imposible_fallout));
        listaNuevos.add(new ChildModelClass(R.drawable.post_matrix_resurrections));

        // RecyclerView 'Padre', que contiene el título y el Recycler View 'Hijo'
        parentModelClassArrayList.add(new ParentModelClass(getResources().getString(R.string.interes), listaFavoritos));

        parentAdapter = new ParentAdapter(parentModelClassArrayList, getActivity());
        // Cargar datos de las listas
        recyclerView.setAdapter(parentAdapter);
        return root;
    }

    //---------------------------------------------------------------------------------
    // 4) Método ON_CREATE_VIEW
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}