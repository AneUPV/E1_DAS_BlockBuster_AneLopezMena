package com.example.entregaindividual_1_anelopezmena.controlador;

import com.example.entregaindividual_1_anelopezmena.java.Pelicula;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import java.util.ArrayList;
import android.util.Log;

/*******************************************************************/
/** ---------------------- CLASE GESTOR_DB ---------------------- **/
/*******************************************************************/
// Se trata de la clase encargada de gestionar las operaciones y
// consultas que la aplicación hace contra la base de datos.

public class Gestor_DB extends SQLiteOpenHelper {

    // Atributos del Gestor_DB
    public static final String nombre_DB = "PeliculasDB"; // Indicar nombre de la base de datos
    private SQLiteDatabase MyDB;                          // Declaración de la base de datos

    //---------------------------------------------------------------------------------
    // 1) Método constructor
    public Gestor_DB(Context context) {
        super(context, nombre_DB, null, 1);
    }

    //---------------------------------------------------------------------------------
    // 2) Método ON_CREATE: Este método se ejecuta cuando hay que crear
    //    la DB. En este caso, se ejecutará para crear las tablas de 'películas',
    //    'guardar' y 'usuarios'
    @Override
    public void onCreate(SQLiteDatabase MyDB) {

        // Acceder a DB en modo LECTURA/ESCRITURA para crear tablas


        // TABLA 1: Crear tabla de usuarios --> PK: email
        MyDB.execSQL("create Table usuarios(" +
                "email TEXT primary key ," +
                "nombre TEXT NOT NULL," +
                "contraseña TEXT NOT NULL)");

        // TABLA 2: Crear tabla de peliculas --> PK: titulo, director
        MyDB.execSQL("create Table peliculas(" +
                "titulo TEXT ," +
                "caratula TEXT," +
                "director TEXT," +
                "actores TEXT, " +
                "genero TEXT ," +
                "trama TEXT," +
                "anyo TEXT," +
                "valoracion REAL," +
                "duracion TEXT," +
                "trailer TEXT," +
                "PRIMARY KEY(titulo, director))");

        // TABLA 3: Crear tabla de peliculas guardadas --> PK: email titulo, director
        // Todas las filas de esta tabla se corresponden con las películas que cada
        // usuario ha guardado como 'vistas'
        MyDB.execSQL("create Table guardar (" +
                "email TEXT," +
                "titulo TEXT," +
                "director TEXT," +
                "PRIMARY KEY(email, titulo, director)," +
                "FOREIGN KEY(email) REFERENCES usuarios(email)," +
                "FOREIGN KEY (titulo,director) REFERENCES peliculas(titulo, director))");
        // Cerrar DB
        //MyDB.close();
    }

    //---------------------------------------------------------------------------------
    // 3) Método ON_UPGRADE: Se ejecuta cuando la version de la DB que queremos usar y la que existe,
    //    no coinciden
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Las tablas se eliminarán si ya existen
        MyDB.execSQL("drop Table if exists usuarios");
        MyDB.execSQL("drop Table if exists peliculas");
        MyDB.execSQL("drop Table if exists guardar");

        // Cerrar DB
        MyDB.close();

    }

    //---------------------------------------------------------------------------------
    // +++++++++++++++++++++++++++++ TABLA DE USUARIOS +++++++++++++++++++++++++++++

    // 4) Método AÑADIR_USUARIO: Añade un usuario a la tabla 'usuarios' de la DB
    //   * Si devuelve -1, ha habido un error --> FALSE
    //   * Si devuelve != -1, ha ido bien     --> TRUE
    public Boolean anadirUsuario(String pUsuario, String pEmail, String pContrasena) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Crea un 'ContentValue' para guardar en él los datos a insertar en la DB
        ContentValues cv = new ContentValues();

        // Añadir datos al CV
        cv.put("email", pEmail);
        cv.put("nombre", pUsuario);
        cv.put("contraseña", pContrasena);

        // Hacer consulta y recibir resultado
        long resultado = MyDB.insert("usuarios", null, cv);
        // Cerrar DB
        MyDB.close();
        // Si devuelve FALSE, significa que ha habido un error
        return resultado != -1;

    }

    //---------------------------------------------------------------------------------
    // 5) Método COMPROBAR_USUARIO: Comprueba que el usuario que se intenta loggear exista en la DB
    //    * Si existe    --> TRUE
    //    * Si no existe --> FALSE
    public Boolean comprobarUsuario(String pEmail) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();
        // Realizar consulta y recoger resultados en un cursor
        Cursor cursor = MyDB.rawQuery("Select * from usuarios where email = ?", new String[]{pEmail});

        // Si existen coincidencias, devuelve true
        boolean res = cursor.getCount() > 0;

        // Cerrar cursor
        cursor.close();
        // Cerrar DBz
        MyDB.close();
        return res;
    }

    //---------------------------------------------------------------------------------
    // 6) Método COMPROBAR_CONTRASEÑA: Comprueba que el usuario introduce bien sus credenciales
    //    * Contraseña & email OK    --> TRUE
    //    * Contraseña & email ERROR --> FALSE
    public boolean comprobarContraseña(String pUsuario, String pContra) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Realizar consulta y recoger resultados en un cursor
        Cursor cursor = MyDB.rawQuery("Select * from usuarios where email = ? and contraseña = ?", new String[]{pUsuario, pContra});
        // Si existen coincidencias, devuelve true
        boolean res = cursor.getCount() > 0;

        // Cerrar cursor
        cursor.close();
        // Cerrar DB
        MyDB.close();
        return res;
    }

    //---------------------------------------------------------------------------------
    // 7) Método GET_NOMBRE_USUARIO: Dado el email asociado a una cuenta, devuelve el nombre del
    //    usuario al que corresponde dicha cuenta
    public String getNombreUsuario(String pEmail) {
        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();
        // Realizar consulta y recoger resultados en un cursor
        Cursor cursor = MyDB.rawQuery("Select nombre from usuarios where email = ?", new String[]{pEmail});
        // Mover cursor para extraer datos del resultado
        cursor.moveToNext();
        String n = cursor.getString(0);
        // Cerrar cursor
        cursor.close();
        // Cerrar DB
        MyDB.close();
        // Devolver el nombre de usuario
        return n;
    }

    //---------------------------------------------------------------------------------
    // +++++++++++++++++++++++++++++ TABLA DE PELÍCULAS +++++++++++++++++++++++++++++

    //---------------------------------------------------------------------------------
    // 8) Método INSERTAR_PELICULA: Añade un registro nuevo a la tabla 'pelicula' de la
    //    DB. Tras insertar los datos nuevos, devuelve un objeto que se corresponde con
    //    la película nueva
    public Pelicula insertarPelícula(String titulo, String caratula, String dir, String act, String genero, String trama, String anyo, int visto, float val, String dur, String url) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Encapsular datos en ContentValues para insertar en la DB
        ContentValues cv = new ContentValues();

        // Añadir datos al CV
        cv.put("titulo", titulo);
        cv.put("caratula", caratula);
        cv.put("director", dir);
        cv.put("actores", act);
        cv.put("genero", genero);
        cv.put("trama", trama);
        cv.put("anyo", anyo);
        cv.put("valoracion", val);
        cv.put("duracion", dur);

        // Si el vídeo no ha sido proporcionado, cargar una imagen por defecto
        if (url.length()<=0){
            url = "https://cdn-icons-png.flaticon.com/512/6890/6890696.png";
        }

        cv.put("trailer", url);

        // Añadir registro a la base de datos
        MyDB.insert("peliculas", null, cv);

        // Cerrar DB
        MyDB.close();

        // Instanciar elemento recién añadido para devolver
        Pelicula p = new Pelicula(titulo, caratula, dir, act, genero, trama, anyo, visto, val, dur, url);
        return p;
    }

    //---------------------------------------------------------------------------------
    // 9) Método GET_PELICULAS: Obtiene la lista completa de películas, todas las
    //    películas insertadas en la base de datos
    public ArrayList<Pelicula> getPeliculas() {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Obtener lista actualizada de TODAS las películas en el cursor
        Cursor cursorPelis = MyDB.rawQuery("Select * from peliculas", null);

        // Crear lista nueva para 'volcar' resultado de la consulta
        ArrayList<Pelicula> listaDeResultados = new ArrayList<Pelicula>();

        // Si ha habido resultados
        if (cursorPelis != null && cursorPelis.getCount() > 0) {
            // Vaciar la lista de resultados
            listaDeResultados = new ArrayList<Pelicula>();
            // Poner el cursor al inicio de los resultados
            if (cursorPelis.moveToFirst()) {
                do {
                    // Transladar datos recogidos por el cursor
                    // al ArrayList<Pelicula>
                    listaDeResultados.add(new Pelicula(
                            cursorPelis.getString(0),     // titulo
                            cursorPelis.getString(1),     // caratula
                            cursorPelis.getString(2),     // director
                            cursorPelis.getString(3),     // actores
                            cursorPelis.getString(4),     // genero
                            cursorPelis.getString(5),     // trama
                            cursorPelis.getString(6),     // anyo
                            0,                              // visto
                            cursorPelis.getFloat(7),      // valoracion
                            cursorPelis.getString(8),     // duracion
                            cursorPelis.getString(9)));  // trailer
                } while (cursorPelis.moveToNext());
                // mover el cursor al próximo resultado
            }
            // cerrar cursor
            cursorPelis.close();
        } else {
            // Lanzar mensaje de error al LOG
            Log.e("LOG", "LA CONSULTA NO TIENE RESULTADOS");
        }
        // Cerrar DB
        MyDB.close();
        // Devolver arraylist de películas
        return listaDeResultados;
    }

    //---------------------------------------------------------------------------------
    // 10) Método BORRAR_PELÍCULA: Proporcionando el titulo y director(es), elimina el
    //    registro de la película en la DB. En este caso, también debe eliminarse la
    //    película de la lista de películas sin ver si estaba incluida en el conjunto
    public void eliminarPelicula(String pTitulo, String pDirector) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();
        // Ejecutar query para eliminar registro correspondiente
        MyDB.execSQL("DELETE from peliculas where titulo =? and director=?", new String[]{pTitulo, pDirector});

        // Cerrar DB
        MyDB.close();
    }

    //---------------------------------------------------------------------------------
    // 11) Método CARGAR_EJEMPLOS: Este método contiene 14 ejemplos completos de películas
    //    con sus datos completos y listos para añadir a la aplicación
    public void cargarEjemplos() {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Ejemplo 1: Avatar 2
        insertarPelícula("Avatar: El sentido del agua", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_avatar", "James Cameron",
                "Sam Worthington, Zoe Saldana, Sigourney Weaver, Kate Winslet, Stephen Lang, Cliff Curtis, Joel David Moore, Giovanni Ribisi, Edie Falco, CCH Pounder, Jemaine " +
                    "Clement, Brendan Cowell, Jamie Flatters, Britain Dalton, Trinity Jo-Li Bliss, Jack Champion, Bailey Bass, Filip Geljo, Duane Wichman-Evans, Dileep Rao, Matt Gerald, " +
                    "Keston John, Alicia Vela-Bailey, Sean Anthony Moran, Andrew Arrabito, Johnny Alexander", "Ciencia ficción. Aventuras. Fantástico. Acción | Familia. Extraterrestres. Secuela. 3-D",
                "Más de una década después de los acontecimientos de 'Avatar', los Na'vi Jake Sully, Neytiri y sus hijos viven en paz en los bosques de Pandora hasta que regresan los hombres del cielo." +
                     "Entonces comienzan los problemas que persiguen sin descanso a la familia Sully, que decide hacer un gran sacrificio para mantener a su pueblo a salvo y seguir ellos con vida.", "2022", 1, (float) 5,
                  "192 min", "https://www.youtube.com/watch?v=FSyWAxUg3Go");

        // Ejemplo 2: Buscando a Dory
        insertarPelícula("Buscando a Dory", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_buscando_a_nemo", "Andrew Stanton, Victoria Strouse",
                "Animación", "Animación. Aventuras. Comedia | Aventuras marinas. Secuela. Pixar. 3-D. Cine familiar",
                "Un año después de los acontecimientos narrados en \"Buscando a Nemo\", Dory vive apaciblemente con Marlin " +
                      "y su hijo Nemo. Pero durante un viaje para ver cómo las mantarrayas migran de vuelta a casa, los problemas de " +
                      "memoria de Dory parecen desaparecer durante un segundo: recuerda que tiene una familia e inmediatamente decide " +
                      "emprender viaje para reencontrarse con sus padres, a los que perdió hace años.", "2016", 1, (float) 4.6,
                  "103 min", "https://www.youtube.com/watch?v=5Z_tMWAFTyg");

        // Ejemplo 3: Caballero Luna
        insertarPelícula("Caballero Luna", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_caballero_luna", "Jeremy Slater (Creador), Mohamed Diab, Justin Benson, Aaron Moorhead",
                "Oscar Isaac, Ethan Hawke, Gaspard Ulliel, May Calamawy, Lucy Thackeray, Fernanda Andrade, Díana Bermudez, Ann Akin, Rey Lucas, David Ganly, Saffron Hocking, Shaun Scott, Karim El Hakim, Sofia Danu, Antonia Salib, Khalid Abdalla, Alexander Cobb, Loic Mabanza. Voz: F. Murray Abraham, Seba Mubarak", "Serie de TV. Fantástico. Acción. Aventuras | Thriller psicológico. Antiguo Egipto. Superhéroes. Marvel Comics. Cómic. MCU. Miniserie de TV ",
                "Serie (2022). 6 episodios. Un trabajador de un museo que lucha contra un trastorno de identidad disociativo, recibe los poderes de un dios egipcio de la luna. Pronto descubre que estos poderes pueden ser tanto una bendición como una maldición.",
                "2022", 1, (float) 3.8, "50 min", "https://www.youtube.com/watch?v=el7smmUNg4U");

        // Ejemplo 4: Glass Onion
        insertarPelícula("Puñales por la espalda: El misterio de Glass Onion", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_glass_onion", "Rian Johnson",
                "Daniel Craig, Edward Norton, Janelle Monáe, Kathryn Hahn, Leslie Odom Jr., Jessica Henwick, Madelyn Cline, Dave Bautista, Kate Hudson, Ethan Hawke, Noah Segan, Jackie Hoffman, Dallas Roberts, Hugh Grant. Cameo: Stephen Sondheim, Natasha Lyonne, Kareem Abdul-Jabbar, Angela Lansbury", "Intriga. Comedia | Crimen. Secuela",
                "Cuando el multimillonario Miles Bron (Edward Norton) invita a algunos de sus allegados a una escapada a su isla griega privada, pronto queda claro que no todo es perfecto en el paraíso. Y cuando alguien aparece muerto, ¿quién mejor que Benoit Blanc para desentrañar todas las capas del misterio? ", "2022", 1, (float) 4.5,
                "139 min", "https://www.youtube.com/watch?v=tFXrdgtNBD4");

        // Ejemplo 5: In Time
        insertarPelícula("In Time", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_in_time", "Andrew Niccol",
                "Justin Timberlake, Amanda Seyfried, Vincent Kartheiser, Cillian Murphy, Johnny Galecki, Olivia Wilde, Alex Pettyfer, Matt Bomer, Rachel Roberts, Yaya DaCosta, Emma Fitzpatrick, Shyloh Oostwald, Will Harris, Michael William Freeman, Jesse Lee Soffer, Aaron Perilo, Bella Heathcote", "Ciencia ficción. Thriller. Acción | Thriller futurista. Distopía. Crimen. Robos & Atracos",
                "Ambientada en una sociedad futura. El hallazgo de una fórmula contra el envejecimiento trae consigo no sólo superpoblación, sino también la transformación del tiempo en moneda de cambio que permite sufragar tanto lujos como necesidades. Los ricos pueden vivir para siempre, pero los demás tendrán que negociar cada minuto de vida, y los pobres mueren jóvenes. Tras conseguir, por " +
                     "casualidad, una inmensa cantidad de tiempo, Will (Timberlake), un joven obrero, será perseguido por unos policías corruptos, \"los guardianes del tiempo\". En su huida, toma como rehén a una joven de familia adinerada (Seyfried).", "2011", 1, (float) 2.5,
                 "109 min", "https://www.youtube.com/watch?v=2wfkvB4k3fg");

        // Ejemplo 6: Inception (Origen)
        insertarPelícula("Origen ", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_inception", "Christopher Nolan", "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page, Ken Watanabe, Marion Cotillard, Tom Hardy, Cillian Murphy, Tom Berenger, Michael Caine, Dileep Rao, Lukas Haas, Pete Postlethwaite, Talulah Riley, Miranda Nolan",
                "Ciencia ficción. Thriller. Intriga. Acción | Thriller futurista. Robos & Atracos",
                "Dom Cobb (DiCaprio) es un experto en el arte de apropiarse, durante el sueño, de los secretos del subconsciente ajeno. La extraña habilidad de Cobb le ha convertido en un hombre muy cotizado en el mundo del espionaje, pero también lo ha condenado a ser un fugitivo y, por consiguiente, a renunciar a llevar una vida normal. Su única oportunidad para cambiar de vida será hacer exactamente lo contrario de lo que ha hecho siempre: la incepción, que consiste en implantar una idea en el subconsciente en lugar de sustraerla. Sin embargo, su plan se complica debido a la intervención de alguien que parece predecir cada uno de sus movimientos, alguien a quien sólo Cobb podrá descubrir.",
                "2012", 1, (float) 4.3, "148 min", "https://www.youtube.com/watch?v=RV9L7ui9Cn8");

        //Ejemplo 7: John Wick: Parabellum
        insertarPelícula("John Wick: Capítulo 3 - Parabellum  ", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_john_wick_parabellum", "Chad Stahelski", "Keanu Reeves, Halle Berry, Ian McShane, Anjelica Huston, Mark Dacascos, Laurence Fishburne, Lance Reddick, Asia Kate Dillon, Jason Mantzoukas, Yayan Ruhian, Cecep Arif Rahman, Robin Taylor, Tobias Segal, Saïd Taghmaoui, Jerome Flynn, Randall Duk Kim, Margaret Daly, Susan Blommaert, Boban Marjanovic, Unity Phelan, Andrea Sooch, Sergio Delavicci, Tiler Peck, Baily Jones, India Bradley, Olivia MacKinnon, Sarah Villwock, Eliza Blutt, Harrison Coll, Maxim Beloserkovsky, Charles Askegard, Stefaniya Makarova, Jeff G. Waxman, Aïssam Bouali, Mustapha Adidou, Alexey Golousenko",
                "Acción. Thriller | Crimen. Secuela ",
                "John Wick (Keanu Reeves) regresa a la acción, solo que esta vez con una recompensa de 14 millones de dólares sobre su cabeza y con un ejército de mercenarios intentando darle caza. Tras asesinar a uno de los miembros del gremio de asesinos al que pertenecía, Wick es expulsado de la organización, pasando a convertirse en el centro de atención de multitud de asesinos a sueldo que esperan detrás de cada esquina para tratar de deshacerse de él.",
                "2019", 1, (float) 3.2, "130 min", "https://www.youtube.com/watch?v=6Dx4NvMHuic");

        //Ejemplo 8: Matrix 1
        insertarPelícula("Matrix ", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_matrix", "Lilly Wachowski, Lana Wachowski", "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Joe Pantoliano, Hugo Weaving," +
                        "Marcus Chong, Gloria Foster, Matt Doran, Belinda McClory, Julian Arahanga, Anthony Ray Parker, Paul Goddard, Robert Taylor, Marc Aden Gray",
                "Ciencia ficción. Fantástico. Acción. Thriller | Thriller futurista. Mundo virtual. Cyberpunk. Distopía. Internet / Informática. Artes marciales. Película de culto",
                "Thomas Anderson es un brillante programador de una respetable compañía de software. Pero fuera del trabajo es Neo, un hacker que un día recibe una misteriosa visita.",
                "1999", 1, (float) 5.0, "178 min", "https://www.youtube.com/watch?v=Pl_H2Lmjn6k");

        // Ejemplo 10: Matrix Resurrections
        insertarPelícula("Matrix Resurrections", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_matrix_resurrections", "Lana Wachowski",
                "Keanu Reeves, Carrie-Anne Moss, Neil Patrick Harris, Jada Pinkett Smith, Yahya Abdul-Mateen II, Jessica Henwick, Priyanka Chopra, Ellen Hollman, Jonathan Groff, Brian J. Smith, Max Riemelt, Lambert Wilson, Andrew Caldwell, Erendira Ibarra, Toby Onwumere, Christopher S. Reid, Andrew Koponen, Thomas Dalby, James D. Weston II, John Lobato, William W. Barbour, Cabran E. Chamberlain, Christina Ricci",
                "Ciencia ficción. Acción. Drama | Cyberpunk. Mundo virtual. Secuela","Neo vive una vida normal y corriente en San Francisco mientras su terapeuta le prescribe pastillas azules. Hasta que Morfeo le ofrece la pastilla roja y vuelve a abrir su mente al mundo de Matrix.", "2021", 1, (float) 4.5,
                "148 min", "https://www.youtube.com/watch?v=GF3aGovu8To");

        // Ejemplo 11: Misión Imposible: FallOut
        insertarPelícula("Misión imposible: Fallout ", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_mision_imposible_fallout", "Christopher McQuarrie",
                "Tom Cruise, Rebecca Ferguson, Henry Cavill, Simon Pegg, Ving Rhames, Vanessa Kirby, Michelle Monaghan, Alec Baldwin, Angela Bassett, Sian Brooke, Sean Harris, Wes Bentley, Frederick Schmidt, Liang Yang, Kristoffer Joner", "Thriller. Acción | Espionaje. Secuela",
                "Sexta entrega de la saga. En esta ocasión presenta a Ethan Hunt (Tom Cruise) y su equipo IMF (Alec Baldwin, Simon Pegg, Ving Rhames), con algunos aliados conocidos (Rebecca Ferguson, Michelle Monaghan), en una lucha contrarreloj después de que una misión salga mal.", "", 1, (float) 2.2,
                "147 min", "https://www.youtube.com/watch?v=pXsYxWccEps");

        // Ejemplo 12: Muerte en el Nilo
        insertarPelícula("Muerte en el Nilo ", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_muerte_en_el_nilo", "Kenneth Branagh",
                "Kenneth Branagh, Gal Gadot, Letitia Wright, Armie Hammer, Annette Bening, Ali Fazal, Sophie Okonedo, Tom Bateman, Emma Mackey, Dawn French, Rose Leslie, Jennifer Saunders, Russell Brand, Nikkita Chadha",
                "Intriga. Thriller | Crimen. Años 30","Basada en la novela de Agatha Christie, publicada en 1937. \"Muerte en el Nilo\" es un thriller de misterio dirigido por Kenneth Branagh sobre el caos emocional y las consecuencias letales " +
                        "que provocan los amores obsesivos. Las vacaciones egipcias del detective belga Hércules Poirot, a bordo de un glamuroso barco de vapor, se ven alteradas por la búsqueda de un asesino cuando la idílica luna de miel de una pareja perfecta se ve truncada de la forma más trágica.", "2022", 1, (float) 5,
                "127 min", "https://www.youtube.com/watch?v=2HQPNRtMbJ0");

        // Ejemplo 13: Puñales por la espalda
        insertarPelícula("Puñales por la espalda", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_punales_por_la_espalda", "Rian Johnson", "Daniel Craig, Ana de Armas, Chris Evans, Jamie Lee Curtis, Toni Collette, Don Johnson, Michael Shannon, Christopher Plummer, Lakeith Stanfield, Katherine Langford, Jaeden Martell, Riki Lindhome, Edi Patterson, Raúl Castillo, Frank Oz, M. Emmet Walsh",
                "Intriga | Crimen. Comedia negra",
                "Cuando el renombrado novelista de misterio Harlan Thrombey (Christopher Plummer) es encontrado muerto en su mansión, justo después de la celebración familiar de su 85 cumpleaños, el inquisitivo y cortés detective Benoit Blanc (Daniel Craig) es misteriosamente reclutado para investigar el asunto. Se moverá entre una red de pistas falsas y mentiras interesadas para tratar de descubrir la verdad tras la muerte del escritor.",
                "2019", 1, (float) 5.0, "130 min", "https://www.youtube.com/watch?v=dzt1BPkm97I");

        // Ejemplo 14: Robin Hood
        insertarPelícula("Robin Hood", "android.resource://com.example.entregaindividual_1_anelopezmena/drawable/post_robin_hood", "Ridley Scott",
                "Russell Crowe, Cate Blanchett, Oscar Isaac, Mark Strong, Max von Sydow, William Hurt, Kevin Durand, Danny Huston, Matthew Macfadyen, Léa Seydoux, Eileen Atkins, Mark Addy, Scott Grimes, Jonathan Zaccaï, Douglas Hodge, Arthur Darvill, Alan Doyle, Simon McBurney, Bronson Webb, Robert Pugh, Gerard McSorley, Velibor Topic, Denis Ménochet, Luke Evans, Mark Lewis Jones",
                "Aventuras. Acción | Siglo XIII. Edad Media. Capa y espada. Cine épico",
                "Inglaterra, siglo XIII. Robin Longstride (Russell Crowe), un magnífico arquero que ha luchado en las Cruzadas al servicio del rey Ricardo Corazón de León (Danny Huston), vuelve de Tierra Santa luchando contra los franceses y saqueando poblados. Cuando Ricardo muere alcanzado por una flecha, Robin se traslada a Nottingham para cumplir una promesa que hizo a Sir Robert " +
                      "Loxley (Douglas Hodge) antes de morir: llevar su espada a su padre, Sir Walter Loxley (Max Von Sydow). Allí conoce a Lady Marion (Cate Blanchett), la viuda de Loxley. Mientras tanto, en Inglaterra, reina Juan Sin Tierra (Oscar Isaac), un rey sin carácter e incapaz de hacer frente tanto a las rebeliones internas como a las amenazas externas urdidas por el pérfido Godfrey " +
                      "(Mark Strong). El objetivo de Robin y sus hombres será impedir una sangrienta guerra civil y devolver la gloria a su país.", "2010", 1, (float) 3.5,
                "131 min", "https://www.youtube.com/watch?v=xc3Ggjd5wmI");
        // Cerrar DB
        MyDB.close();
    }

    //---------------------------------------------------------------------------------
    // 12) Método ACTUALIZAR_PELICULA: Actualiza el registro de la película en la DB con los
    //     datos que el usuario introduce desde la pantalla de modificar la información de la
    //     película.
    public void actualizarPelicula(String t_titulo, String caratula, String t_director, String actores, String genero, String trama, String anyo, String val, String dur, String trailer) {
        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();
        // Ejecutar query de actualización de datos (UPDATE)
        MyDB.execSQL("UPDATE peliculas SET caratula=?, actores=? , genero=?, trama=?, anyo=?, valoracion=?, duracion=?, trailer=? WHERE titulo=? AND director=?", new String[]{caratula, actores, genero, trama, anyo, String.valueOf(val), dur, trailer, t_titulo, t_director});
        // Cerrar DB
        MyDB.close();
    }

    //---------------------------------------------------------------------------------
    // 13) Método CONSEGUIR_PELICULA: Pasando el título y el director de la película,
    //     retorna la instancia de la película con todos los datos. Devuelve 'Null' si
    //     no hay resultados
    public Pelicula conseguirPelicula(String titulo, String director) {
        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();
        // Declarar película que se devolverá como resultado + inicializar a null.
        Pelicula p = null;
        // Realizar consulta y recoger resultados en cursor
        Cursor cursorPelis = MyDB.rawQuery("Select * from peliculas where titulo = ? and director=?", new String[]{titulo, director});
        // Si ha habido resultados
        if (cursorPelis != null && cursorPelis.getCount() > 0) {
            // Poner el cursor al inicio de los resultados
            if (cursorPelis.moveToFirst()) {
                p = new Pelicula(
                        cursorPelis.getString(0),     // titulo
                        cursorPelis.getString(1),     // caratula
                        cursorPelis.getString(2),     // director
                        cursorPelis.getString(3),     // actores
                        cursorPelis.getString(4),     // genero
                        cursorPelis.getString(5),     // trama
                        cursorPelis.getString(6),     // anyo
                        0,                              // visto
                        cursorPelis.getFloat(7),      // valoracion
                        cursorPelis.getString(8),     // duracion
                        cursorPelis.getString(9));  // trailer
            }
            // cerrar cursor
            cursorPelis.close();

        } else {
            // Si NO ha habido resultados
            Log.d("LOG", "La consulta no tiene resultados");
        }
        // Cerrar DB
        MyDB.close();
        // Devolver película con datos o 'null', según el caso
        return p;
    }

    //---------------------------------------------------------------------------------
    // 14) Método GET_PELICULAS_NUEVAS: Obtiene la lista con todas las películas
    //     del año indicado como parámetro de entrada
    public ArrayList<Pelicula> getPeliculasNuevas() {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Obtener lista actualizada de TODAS las películas
        Cursor cursorPelis = MyDB.rawQuery("Select * from peliculas where anyo=?", new String[]{"2023"});
        // Crear nueva lista para 'volcar' los resultados
        ArrayList<Pelicula> listaDeResultados = new ArrayList<Pelicula>();

        // Si ha habido resultados
        if (cursorPelis != null && cursorPelis.getCount() > 0) {
            // Vaciar la lista
            listaDeResultados = new ArrayList<Pelicula>();
            // Poner el cursor al inicio de los resultados
            if (cursorPelis.moveToFirst()) {
                do {
                    // Transladar datos recogidos por el cursor
                    // al ArrayList<Pelicula>
                    listaDeResultados.add(new Pelicula(
                            cursorPelis.getString(0),     // titulo
                            cursorPelis.getString(1),     // caratula
                            cursorPelis.getString(2),     // director
                            cursorPelis.getString(3),     // actores
                            cursorPelis.getString(4),     // genero
                            cursorPelis.getString(5),     // trama
                            cursorPelis.getString(6),     // anyo
                            0,                              // visto
                            cursorPelis.getFloat(7),      // valoracion
                            cursorPelis.getString(8),     // duracion
                            cursorPelis.getString(9)));  // trailer
                } while (cursorPelis.moveToNext());
                // mover el cursor a la próxima
            }
            // cerrar cursor
            cursorPelis.close();
        }
        // Si NO ha habido resultados
        else {
            Log.d("PRUEBA", "La consulta no ha tenido resultados");
        }
        // Cerrar DB
        MyDB.close();
        // Devolver resultados en ArrayList
        return listaDeResultados;
    }


    //---------------------------------------------------------------------------------
    // +++++++++++++++++++++++++++++ TABLA DE GUARDAR +++++++++++++++++++++++++++++
    //---------------------------------------------------------------------------------
    // 15) Método GET_VISTO: Dado el email y los identificadores de la película (titulo
    //    y director(es)), devuelve si el usuario ha marcado la película como vista o no.
    public boolean getVisto(String pEmail, String pTitulo, String pDirector) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Realizar consulta y recoger resultados en un cursor
        Cursor cursor = MyDB.rawQuery("Select * from guardar where email = ? and titulo=? and director=?", new String[]{pEmail, pTitulo, pDirector});
        // Si existen coincidencias, devuelve true
        boolean res = cursor.getCount() > 0;

        // Cerrar cursor
        cursor.close();
        // Cerrar DB
        MyDB.close();
        return res;
    }

    //---------------------------------------------------------------------------------
    // 16) Método AÑADIR_VISTO: Cuando el usuario indique que ya ha visto una película,
    //     se llama a este método para que inserte el registro en la tabal 'guardar' de la DB
    public void anadirVisto(String email, String t_titulo, String t_director) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Encapsular datos para insertar en ContentValues
        ContentValues cv = new ContentValues();

        // Añadir datos al CV
        cv.put("email", email);
        cv.put("titulo", t_titulo);
        cv.put("director", t_director);

        // Hacer consulta de INSERT en la DB
        MyDB.insert("guardar", null, cv);

        // Cerrar DB
        MyDB.close();
    }

    //---------------------------------------------------------------------------------
    // 17) Método GET_PELICULAS_SIN_VER: Devuelve la lista de todas las películas de la DB
    //     y que además, el usuario NO haya visto. Es decir, se devuelven todas las películas,
    //     menos las vistas por el usuario.
    public ArrayList<Pelicula> getPeliculasSinVer(String pEmail) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        // Obtener lista actualizada de TODAS las películas sin ver por el usuario (email)
        Cursor cursorPelis = MyDB.rawQuery("SELECT * FROM peliculas WHERE titulo NOT IN (SELECT titulo FROM guardar WHERE email=?);", new String[]{pEmail});

        // Crear lista para recoger los resultados
        ArrayList<Pelicula> listaDeResultados = new ArrayList<Pelicula>();

        // Si ha habido resultados
        if (cursorPelis != null && cursorPelis.getCount() > 0) {
            // Vaciar lista
            listaDeResultados = new ArrayList<Pelicula>();
            // Poner el cursor al inicio de los resultados
            if (cursorPelis.moveToFirst()) {
                do {
                    // Transladar datos recogidos por el cursor al ArrayList<Pelicula>
                    listaDeResultados.add(new Pelicula(
                            cursorPelis.getString(0),     // titulo
                            cursorPelis.getString(1),     // carátula
                            cursorPelis.getString(2),     // director
                            cursorPelis.getString(3),     // actores
                            cursorPelis.getString(4),     // genero
                            cursorPelis.getString(5),     // trama
                            cursorPelis.getString(6),     // anyo
                            0,                              // visto
                            cursorPelis.getFloat(7),      // valoracion
                            cursorPelis.getString(8),     // duracion
                            cursorPelis.getString(9)));  // trailer
                } while (cursorPelis.moveToNext());
                // mover el cursor a la próxima
            }
            // cerrar cursor
            cursorPelis.close();
        } else {
            // Si NO ha habido resultados
            Log.d("LOG", "La consulta no tiene resultados");
        }
        // Cerrar DB
        MyDB.close();
        return listaDeResultados;
    }

    //---------------------------------------------------------------------------------
    // 18) Método INSERTAR_PELICULA_VISTA: Cuando el usuario modifica una película y la indica
    //     como 'VISTA', se inserta un nuevo registro en la tabla 'guardar' de la DB
    public void insertarPeliculaVista(String pEmail, String pTitulo, String pDirector) {

        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();

        //Crear CV donde encapsular los datos a insertar
        ContentValues cv = new ContentValues();

        // Añadir datos al CV
        cv.put("email", pEmail);
        cv.put("titulo", pTitulo);
        cv.put("director", pDirector);

        // Insertar datos y cerrar DB
        MyDB.insert("guardar", null, cv);
        MyDB.close();
    }

    //---------------------------------------------------------------------------------
    // 19) Método ELIMINAR_PELICULA_VISTA: Cuando el usuario modifica una película y la indica
    //     como 'NO VISTA', se elimina el registro en la tabla 'guardar' de la DB
    public void eliminarPeliculaVista(String pEmail, String pTitulo, String pDirector) {
        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();
        // Ejecutar query para eliminar registro correspondiente
        MyDB.execSQL("DELETE from guardar where email=? and titulo =? and director=?", new String[]{pEmail, pTitulo, pDirector});
        // Cerrar DB
        MyDB.close();
    }

    //---------------------------------------------------------------------------------
    // 20) Método ACTUALIZAR_USUARIO: Actualiza el nombre de usuario relativo a la cuenta de email
    //     que ha iniciado sesión, según el nombre indicado en las preferencias.
    public void actualizarUsuario(String nombreNuevo, String email) {
        // Acceder a DB en modo LECTURA/ESCRITURA
        MyDB = getWritableDatabase();
        // Ejecutar query de actualización de datos (UPDATE)
        MyDB.execSQL("UPDATE usuarios SET nombre=? WHERE email=?", new String[]{nombreNuevo, email});
        // Cerrar DB
        MyDB.close();
    }




}
