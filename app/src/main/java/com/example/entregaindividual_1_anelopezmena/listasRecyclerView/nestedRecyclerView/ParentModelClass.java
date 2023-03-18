package com.example.entregaindividual_1_anelopezmena.listasRecyclerView.nestedRecyclerView;

import java.util.List;

/***************************************************************************/
/** ---------------------- CLASE PARENT_MODEL_CLASS ---------------------- **/
/***************************************************************************/
                /* NESTED RECYCLER VIEW <=> RECYCLER VIEW ANIDADO*/
/* PARENT = [Titular + cartelera(=lista de hijos) ] (Elementos de lista) */

// Será la clase 'PADRE' o clase 'PARENT', que contiene la información de la lista
// formada por listas más 'pequeñas' (con elementos 'child').

public class ParentModelClass {

    // Atributos de la clase
    String title;              // Título para indicar categoría/titular (Más visto, recientes...)
    List<ChildModelClass> childModelClassList;     // Lista de elementos 'hijo' (caratulas)

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public ParentModelClass(String title, List<ChildModelClass> childModelClassList) {
        this.title = title;
        this.childModelClassList = childModelClassList;
    }
}