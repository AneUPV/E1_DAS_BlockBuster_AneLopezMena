package com.example.entregaindividual_1_anelopezmena.listasRecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.interfaces.InterfazRV;

/* VIEW HOLDER */
// Para el Recycler view, es necesario definir una clase que extenda a ViewHolder
// Esta clase será la encargada de asociar los elementos de la clase (en este caso, las películas)
// con los elementos gráficos que se mostrarán en el layout (item.xml). Llama al listener cuando
// se da un evento

public class ViewHolder_SinVer extends RecyclerView.ViewHolder {
    // Atributos
    ImageView caratula;
    TextView titulo;
    CardView cardview;
    RecyclerView rv;

    /* MÉTODO CONSTRUCTOR */
    public ViewHolder_SinVer(@NonNull View itemView, InterfazRV miListener) {
        super(itemView);
        // Conseguir elementos de la vista
        cardview = (CardView) itemView.findViewById(R.id.id_cv);
        caratula = itemView.findViewById(R.id.id_caratula);
        titulo = itemView.findViewById(R.id.id_titulo2);
        rv = itemView.findViewById(R.id.rv_todos);


        // Añadir listener a los elementos de la lista
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (miListener != null){
                    int pos = getAdapterPosition();

                    if (pos != rv.NO_POSITION){
                        // Añadir animación al cardview
                        cardview = (CardView) itemView.findViewById(R.id.id_cv);
                        cardview.startAnimation(AnimationUtils.loadAnimation(titulo.getContext(), R.anim.parpadear));
                        miListener.onitemLongClick(pos);
                    }
                }
                return true;
            }
        });


        // Añadir listener a los elementos de la lista
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (miListener != null){
                    int pos = getAdapterPosition();

                    if (pos != rv.NO_POSITION){
                        miListener.onClick(pos);
                        Log.d("CLICK", "HAS HECHO CLICK");
                    }
                }
            }
        });
    }
}
