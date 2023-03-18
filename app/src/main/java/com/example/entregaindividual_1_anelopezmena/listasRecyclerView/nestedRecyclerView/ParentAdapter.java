package com.example.entregaindividual_1_anelopezmena.listasRecyclerView.nestedRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entregaindividual_1_anelopezmena.R;

import java.util.List;

/*******************************************************************************/
/** ---------------------------- CLASE PARENT_ADAPTER ----------------------- **/
/*******************************************************************************/
            /* NESTED RECYCLER VIEW <=> RECYCLER VIEW ANIDADO*/
/* PARENT = [Titular + cartelera(=lista de hijos) ] (Elementos de lista) */

// Será la clase encargada de crear la lista VERTICAL de listas HORIZONTALES mostrada
// en la actividad TOP_Peliculas. Para ello, se utiliza la clase que extiende a
// ViewHolder (ParentAdapter.ViewHolder, clase incluida en el mismo .java).

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {

    // Crear una lista de elementos 'padre[ Titular+ cartelera]' y el contexto
    private List<ParentModelClass> parentModelClassList;
    private Context contexto;

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public ParentAdapter(List<ParentModelClass> parentModelClassList, Context contexto) {
        this.contexto = contexto;
        this.parentModelClassList = parentModelClassList;
    }

    //-------------------------------------------------------------------------------------------------
    // 2) Método ON_CREATE_VIEW_HOLDER
    // Infla el layout definido para cada elemento y crea y devuelve una instancia
    // de la clase que extiende a ViewHolder.
    @NonNull
    @Override
    public ParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Se le indica el layout definido para los elementos 'padre', y se devuelve la instancia
        View cadaElem = LayoutInflater.from(contexto).inflate(R.layout.nested_parent_rv_layout, null, false);
        return new ViewHolder(cadaElem);
    }

    //-------------------------------------------------------------------------------------------------
    // 3) Método ON_BIND_VIEW_HOLDER
    // Este método se encarga de asignar los valores a mostrar para una posición concreta a
    // los atributos del ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ParentAdapter.ViewHolder holder, int position) {
        // Obtener título
        holder.tv_parent_title.setText(parentModelClassList.get(position).title);

        // Conseguir adaptador para elemento 'hijo'
        ChildAdapter childAdapter;
        childAdapter = new ChildAdapter(parentModelClassList.get(position).childModelClassList, contexto);
        holder.rv_child.setLayoutManager(new LinearLayoutManager(contexto, LinearLayoutManager.HORIZONTAL, false));
        holder.rv_child.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();
    }

    //-------------------------------------------------------------------------------------------------
    // 4) Método GET_ITEM_COUNT: Devuelve el número de elementos que se mostrarán en la lista
    @Override
    public int getItemCount() {
        return parentModelClassList.size();
    }

    //################################# CLASE VIEWHOLDER #################################
                                        /* VIEW HOLDER */
    // Para el Recycler view, es necesario definir una clase que extenda a ViewHolder
    // Esta clase será la encargada de asociar los elementos de la clase (en este caso, los elementos [titulo+lista])
    // con los elementos gráficos que se mostrarán en el layout (nested_parent_rv_layout.xml)

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Atributos privados
        private TextView tv_parent_title;
        private RecyclerView rv_child;

        //---------------------------------------------------------------------------------
        // 1) Método constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Conseguir elementos de la vista
            rv_child = itemView.findViewById(R.id.rv_child);
            tv_parent_title = itemView.findViewById(R.id.tv_parent_title);

        }
    }
}
