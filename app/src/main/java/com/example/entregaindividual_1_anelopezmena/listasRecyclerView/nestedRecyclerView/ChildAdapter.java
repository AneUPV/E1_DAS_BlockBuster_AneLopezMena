package com.example.entregaindividual_1_anelopezmena.listasRecyclerView.nestedRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entregaindividual_1_anelopezmena.R;

import java.util.List;

/***********************************************************************/
/** ---------------------- CLASE CHILD_ADAPTER ---------------------- **/
/***********************************************************************/
                 /* CHILD = CARATULAS (Elementos child) */
            /* NESTED RECYCLER VIEW <=> RECYCLER VIEW ANIDADO */
// Será la clase encargada de crear la lista HORIZONTAL de carátulas que utiliza
// la lista vertical mostrada en la actividad TOP_Peliculas. Para ello, se utiliza la
// clase que extiende a ViewHolder (ChildAdapter.ViewHolder, clase incluida en el
// mismo .java).

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    // Atributos privados
    // Crear una lista de elementos 'hijo' (que serán las carátulas), y el contexto
    private List<ChildModelClass> childModelClassList;
    private Context contexto;

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public ChildAdapter(List<ChildModelClass> childModelClassList, Context contexto) {
        this.childModelClassList = childModelClassList;
        this.contexto = contexto;
    }

    //-------------------------------------------------------------------------------------------------
    // 2) Método ON_CREATE_VIEW_HOLDER: Infla el layout definido para cada elemento
    // y tras ello, crea y devuelve una instancia de la clase que extiende a ViewHolder.
    @NonNull
    @Override
    public ChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Se le indica el layout definido para los elementos 'hijo', y se devuelve la instancia
        View cadaElem = LayoutInflater.from(contexto).inflate(R.layout.nested_child_rv_layout, null, false);
        ViewHolder vh = new ViewHolder(cadaElem);
        return vh;
    }

    //-------------------------------------------------------------------------------------------------
    // 3) Método ON_BIND_VIEW_HOLDER: Este método se encarga de asignar los valores a mostrar
    // para una posición concreta a los atributos del ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ChildAdapter.ViewHolder holder, int position) {
        holder.iv_child_image.setImageResource(childModelClassList.get(position).image);
    }

    //-------------------------------------------------------------------------------------------------
    // 4) Método GET_ITEM_COUNT: Devuelve el número de elementos que se mostrarán en la lista
    @Override
    public int getItemCount() {
        return childModelClassList.size();
    }


/**********************************************************************************/
/** ---------------------- CLASE CHILD_ADAPTER.VIEWHOLDER ---------------------- **/
/**********************************************************************************/
                                /* VIEW HOLDER */
                /* NESTED RECYCLER VIEW <=> RECYCLER VIEW ANIDADO*/
// En este caso, el view Holder está alojado en la propia clase ChildAdapter, pero
// la implementación es la misma que para el Recycler View simple. Para el Recycler
// view, es necesario definir una clase que extenda a ViewHolder. Esta clase será la
// encargada de asociar los elementos de la clase (en este caso, las carátulas 'child')
// con los elementos gráficos que se mostrarán en el layout (nested_child_rv_layout.xml).
// Extiende a la clase 'RecyclerView.ViewHolder'.

public class ViewHolder extends RecyclerView.ViewHolder {
   // Atributos privados
    private ImageView iv_child_image;
    private RecyclerView rv;

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        // Obtener elemento del layout mediante id
        iv_child_image = itemView.findViewById(R.id.iv_child_item);
        rv = itemView.findViewById(R.id.rv_child);

    }
}
}
