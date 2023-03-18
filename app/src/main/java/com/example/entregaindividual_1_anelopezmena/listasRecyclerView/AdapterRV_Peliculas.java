package com.example.entregaindividual_1_anelopezmena.listasRecyclerView;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.interfaces.InterfazRV;
import com.example.entregaindividual_1_anelopezmena.java.Pelicula;

import java.util.ArrayList;

/* RECYCLER ADAPTER - PELICULAS */
// Será la clase encargada de crear la lista. Para ello, se utiliza la
// clase que extiende a ViewHolder (ViewHolder_peliculas).

public class AdapterRV_Peliculas extends RecyclerView.Adapter<ViewHolder_Peliculas> {
    // --- Atributos privados ---
    private final InterfazRV miListener;
    private ArrayList<Pelicula> peliculas;
    private Context mContext;

//-------------------------------------------------------------------------------------------------
    /* MÉTODO CONSTRUCTOR */
    public AdapterRV_Peliculas(Context mContext, ArrayList<Pelicula> peliculas, InterfazRV miListener) {
        this.mContext = mContext;
        this.peliculas = peliculas;
        this.miListener = miListener;
    }
//-------------------------------------------------------------------------------------------------
    // --- Método onCreateViewHolder ---
    // Infla el layout definido para cada elemento y crea y devuelve una instancia
    // de la clase que extiende a ViewHolder (ViewHolder_peliculas).
    @NonNull
    @Override
    public ViewHolder_Peliculas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutCadaItem = LayoutInflater.from(mContext).inflate(R.layout.item_todos, null);
        ViewHolder_Peliculas vhp = new ViewHolder_Peliculas(layoutCadaItem, miListener);
        return vhp;
    }
    // --------------------------------------------------------------------------------------
    // --- Método onBindViewHolder ---
    // Este método se encarga de asignar los valores a mostrar para una posición concreta a
    // los atributos del ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Peliculas holder, int position) {
     // plantilla.id.setteaRecurso(lista[position])
        holder.titulo.setText(peliculas.get(position).getTitulo());
        Uri miUri = Uri.parse(peliculas.get(position).getCaratula());
        holder.caratula.setImageURI(miUri);//peliculas.get(position).getCaratula());
    }
    // --------------------------------------------------------------------------------------
    // --- Método getItemCount ---
    // Devuelve el número de elementos que se mostrarán en la lista
    @Override
    public int getItemCount() {
        return peliculas.size();
    }

    public void settearListaFiltrada(ArrayList<Pelicula> listaFiltrada){
        this.peliculas = listaFiltrada;
        notifyDataSetChanged();

    }

}
