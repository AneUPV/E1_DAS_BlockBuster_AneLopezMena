package com.example.entregaindividual_1_anelopezmena.java;

import android.net.Uri;

/*******************************************************************/
/** ---------------------- CLASE PELÍCULA ---------------------- **/
/*******************************************************************/
// Clase que representa los elementos de la base de datos (Películas).
// En este caso, cada película almacenará sus datos y se accederá a ellos
// por medio de getters. De la misma manera, se modificarán esos datos
// por medio de setters.

public class Pelicula {
    // Atributos privados de la clase Película
    private String titulo;
    private String caratula;
    private String director;
    private String actores;
    private String genero;
    private String trama;
    private String anyo;
    private int visto;
    private float valoracion;
    private String duracion;
    private String trailer;

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public Pelicula(String titulo, String pCaratula, String director, String actores, String genero, String trama,
                    String anyo, int visto, float valoracion, String duracion, String trailer) {

        this.titulo = titulo;
        // Si la carátula no se indica, introducir una imagen por defecto
        if (pCaratula == null){
            this.caratula=Uri.parse("android.resource://com.example.entregaindividual_1_anelopezmena/drawable/vacio").toString();}
        else{
            this.caratula = pCaratula;
        }
        this.director = director;
        this.actores = actores;
        this.genero = genero;
        this.trama = trama;
        this.anyo = anyo;
        this.visto = visto;
        this.valoracion= valoracion;
        this.duracion = duracion;
        this.trailer = trailer;
    }

    //---------------------------------------------------------------------------------
    // 2) Métodos getter de la clase Película
    public String getTitulo() {return titulo;}
    public String getCaratula() {return caratula;}
    public String getDirector() {return director;}
    public String getActores() {return actores;}
    public String getGenero() {return genero;}
    public String getTrama() {return trama;}
    public String getAnyo() {return anyo;}
    public int getVisto() {return visto;}
    public float getValoracion() {return valoracion;}
    public String getDuracion() {return duracion;}
    public String getTrailer() {
        return trailer;
    }

    //---------------------------------------------------------------------------------
    // 3) Métodos setter de la clase Película
    public void setTitulo(String titulo) {this.titulo = titulo;}
    public void setCaratula(String caratula) {this.caratula = caratula;}
    public void setDirector(String director) {this.director = director;}
    public void setActores(String actores) {this.actores = actores;}
    public void setGenero(String genero) {this.genero = genero; }
    public void setTrama(String trama) {this.trama = trama;}
    public void setAnyo(String anyo) {this.anyo = anyo;}
    public void setVisto(int visto) {this.visto = visto;}
    public void setValoracion(float valoracion) {this.valoracion = valoracion;}
    public void setDuracion(String duracion) {this.duracion = duracion;}
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

}
