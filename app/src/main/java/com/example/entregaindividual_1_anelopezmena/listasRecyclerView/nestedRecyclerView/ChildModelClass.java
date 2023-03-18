package com.example.entregaindividual_1_anelopezmena.listasRecyclerView.nestedRecyclerView;


/***************************************************************************/
/** ---------------------- CLASE CHILD_MODEL_CLASS ---------------------- **/
/***************************************************************************/
                      /* CHILD = CARATULAS (Elementos child) */
                /* NESTED RECYCLER VIEW <=> RECYCLER VIEW ANIDADO*/

// Será la clase 'CHILD' o clase 'HIJA', que contiene la información de la lista
// que se mostrará dentro de la otra lista (PARENT). En este caso, los elementos 'hijo'
// solo mostrarán la carátula de una película.

public class ChildModelClass {
    // Atributos de la clase (público)

    int image; // Carátula de los elementos 'hijo'

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public ChildModelClass(int image) {
        this.image = image;
    }
}
