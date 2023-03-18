package com.example.entregaindividual_1_anelopezmena.activities;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_DB;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_Idioma;
import com.example.entregaindividual_1_anelopezmena.dialogs.Dial_CerrarSesion;
import com.example.entregaindividual_1_anelopezmena.interfaces.Interfaz_Dial;
import com.example.entregaindividual_1_anelopezmena.java.Pelicula;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import com.example.entregaindividual_1_anelopezmena.databinding.ActivityMainBinding;


/*************************************************************************/
/** -------------------- ACTIVITY_MENU_PRINCIPAL ---------------------- **/
/*************************************************************************/
// Se trata de la clase que dará la bienvenida al usuario que se haya identificado
// correctamente en la pantalla anterior de login. Esta actividad comprende un toolbar
// superior, un botón de acción (Floating action button) y el menú lateral (llamado
// ‘Navigation Drawer’) que darán  acceso al resto de funcionalidades de la aplicación,
// así como visualizar la lista de todas las películas, añadir registros a la base de datos,
// modificar los ya existentes, etc.
public class Act_MenuPrincipal extends AppCompatActivity implements Interfaz_Dial {

    // Atributos privados
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private ArrayList<Pelicula> listaPelis = new ArrayList<Pelicula>();
    private Gestor_DB miDB;
    private Gestor_Idioma gestorIdioma;
    private String u, email;
    private String idiomaActual;
    private TextView tuEmail, tuUsuario;

    //-- **## NOTIFICACIONES ##**--//
    // Indicar ID de canal para las notificaciones ( Android O o superior)
    private final static String CHANNEL_ID = "SESION";

    // Asignar un ID a la notificación
    public final static int NOTIFICACION_ID = 10;

    // Para las notificaciones, crear los Pending intent
    private PendingIntent pendingIntent;
    private PendingIntent siPendingIntent;

    //---------------------------------------------------------------------------------
    // 1) Método ON_CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // El idioma por defecto es el ESPAÑOL
        idiomaActual = "es";

        // Crear gestores: DB e IDIOMA
        miDB = new Gestor_DB(this);
        gestorIdioma = new Gestor_Idioma(this);

        // Recibir email y usuario desde login
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Obtener email y nombre del usuario logueado
            idiomaActual = extras.getString("var_idioma");
            email = extras.getString("var_email");
        }
        // Obtener nombre de usuario a partir del email
        u = miDB.getNombreUsuario(email);

    //---------------------------------------------------------------------------------
        // Gestionar pérdida de información al girar la pantalla:
        // GUARDAR:
        //      * EMAIL
        //      * USUARIO
        //      * IDIOMA
        if (savedInstanceState != null){
            idiomaActual = savedInstanceState.getString("var_idioma");
            email = savedInstanceState.getString("var_email");
            u = savedInstanceState.getString("var_usuario");
        }
        // Settear el idioma guardado
        gestorIdioma.setIdioma(idiomaActual);

        // Crear la vista
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    //---------------------------------------------------------------------------------

        //-- **## NOTIFICACIONES ##**--//
        // Pedir permiso al usuario para enviar notificaciones al iniciar sesión
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Pedir permisos
            ActivityCompat.requestPermissions(this,  new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 20);
        }
    //---------------------------------------------------------------------------------

        // Crear Drawer Layout que mostrará los fragments en su interior
        drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        // Indicar a la AppBar los identificadores de los menús diferentes, para que se
        // consideren como destinos de primer nivel --> // XML de los fragments
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.frag_simplificado)
                .setOpenableLayout(drawer)
                .build();
        setSupportActionBar(binding.appBarMain.toolbar);


        // Obtener elemento Header del navigation Drawer
        View header = navigationView.getHeaderView(0);

        // Obtener TextViews de la barra lateral
        tuEmail = header.findViewById(R.id.tuEmail);
        tuUsuario = header.findViewById(R.id.tuUsuario);
        // Settear nombre de usuario y email en la barra lateral
        tuEmail.setText(email);
        tuUsuario.setText(u);

        // Comprobar las preferencias sobre el nombre de usuario
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // Si el usuario ha indicado un nickname o apodo, actualizarlo en el menú principal
        if (sp.contains("nombre")){
            // Obtener el nuevo nombre y actualizarlo en la DB
            u = sp.getString("nombre", u);
            miDB.actualizarUsuario(u, email);
        }
        // Settear en la vista el nombre actualizado
        u = miDB.getNombreUsuario(email);
        tuUsuario.setText(u);

    //---------------------------------------------------------------------------------
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    //---------------------------------------------------------------------------------
    // 2) Método ON_RESUME
    @Override
    protected void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // Si el usuario ha indicado un nickname o apodo, actualizarlo en el menú principal
        if (sp.contains("nombre")){
            // obtener el nombre nuevo y actualizarlo en la DB
            u = sp.getString("nombre", u);
            miDB.actualizarUsuario(u, email);
        }
        // Settear en la vista el nombre actualizado
        u = miDB.getNombreUsuario(email);
        tuUsuario.setText(u);

        super.onResume();
    }

    //---------------------------------------------------------------------------------
    // 3) Método ON_CREATE_OPTIONS_MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menu y añade elementos a la Action Bar si está presente
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //---------------------------------------------------------------------------------
    // 4) Método ON_OPTIONS_ITEM_SELECTED
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Obtener id del elemento seleccionado y ejecutar una
        // acción según cuál sea ese ID
        switch (item.getItemId()) {
            // Si se ha pulsado 'CERRAR SESIÓN'
            case R.id.cerrarSesion:
                // Mostrar diálogo de Cierre de Sesión
                Dial_CerrarSesion d = new Dial_CerrarSesion(this);
                d.show(getSupportFragmentManager(), "CerrarSesion");
            break;
            // Si se ha pulsado 'Cambiar idioma de app' --> 'EUSKERA'
            case R.id.idioma_euskera:
                // Settear idioma como "eu"
                gestorIdioma.setIdioma("eu");
                idiomaActual="eu";
                // Notificar del cambio para actualizar la vista
                actualizarIdiomaApp();
                break;
            // Si se ha pulsado 'Cambiar idioma de app' --> 'ESPAÑOL'
            case R.id.idioma_español:
                // Settear idioma como "es"
                gestorIdioma.setIdioma("es");
                idiomaActual="es";
                // Notificar del cambio para actualizar la vista
                actualizarIdiomaApp();
                break;
            // Si se ha pulsado 'Cambiar idioma de app' --> 'INGLÉS'
            case R.id.idioma_ingles:
                // Settear idioma como "en"
                gestorIdioma.setIdioma("en");
                idiomaActual="en";
                // Notificar del cambio para actualizar la vista
                actualizarIdiomaApp();
                break;
                // Si se ha pulsado  'Ajustes'
            case R.id.preferencias:
                // Lanzar un intent a la actividad de preferencias (Act_Preferencias)
                Intent i = new Intent(this, Act_Preferencias.class);
                // Enviar datos a la activity
                i.putExtra("var_idioma", idiomaActual);
                i.putExtra("var_email", idiomaActual);
                i.putExtra("var_usuario", u);
                startActivity(i);
                break;
            // Opción por defecto
            default:
                break;
        }
            return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------------------------------------
    // 5) Método ACTUALIZAR_IDIOMA_APP: Este método recoge el intent, y
    //    con los datos actualizados, se lanza un intent hacia sí mismo
    private void actualizarIdiomaApp() {
        // Recoger intent
        Intent i = getIntent();
        // Introducir los datos en el intent
        i.putExtra("var_email", email);
        i.putExtra("var_usuario", u);
        i.putExtra("var_idioma", idiomaActual);
        i.putExtra("var_cargados", true);
        // finish y lanzar Intent
        finish();
        startActivity(i);
    }

    //---------------------------------------------------------------------------------
    // 6) Método ON_SUPPORT_NAVIGATE_UP
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)|| super.onSupportNavigateUp();
    }

    //-- **## NOTIFICACIONES ##**--//
    //---------------------------------------------------------------------------------
    // 7) Método LANZAR_NOTIFICACIÓN
    private void lanzarNotificacion() {

        // Crear un Notification Manager
        NotificationManager NM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // -----------****#### CREAR UN CANAL PARA LAS NOTIFICACIONES ####****-------------- //
        // Si la versión de ANDROID es mayor que OREO, se creará un canal
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            // Indicar nombre del canal --> "Películas"
            CharSequence nombre = "Peliculas";
            // Pasar el ID, nombre y la prioridad en el momento de crearlo
            NotificationChannel NC = new NotificationChannel(CHANNEL_ID, nombre, NotificationManager.IMPORTANCE_DEFAULT);
            // Crear canal
            NM.createNotificationChannel(NC);
            // Habilitar las luces
            NC.enableLights(true);
            // Dar una descripción al canal
            NC.setDescription("Notificación de cierre de sesión");
        }

        // Instanciar PendingIntents
        crearPendingIntent();
        crearSiPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        // Settear icono a la notificación
        builder.setSmallIcon(R.drawable.claqueta);
        // Settear título de la notificación
        builder.setContentTitle(getResources().getString(R.string.titulo_noti));
        // Settear texto de la notificación
        builder.setContentText((getResources().getString(R.string.mensaje_noti)));
        // Poner luces de notificación
        builder.setLights(Color.BLUE, 1000, 1000);

        // A partir de Android v13, hay que solicitar los permisos si no han sido concedidos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Pedir permisos
            ActivityCompat.requestPermissions(this,  new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 20);
        }

        //Indicar al builder que la notificación tendrá PendingIntent
        builder.setContentIntent(pendingIntent);
        // Añadirle los botones a la notificación - SI
        builder.addAction(R.drawable.claqueta, getResources().getString(R.string.entrar_noti), siPendingIntent);

        // Lanzar notificación
        NM.notify(NOTIFICACION_ID, builder.build());

    }

    //-- **## NOTIFICACIONES ##**--//
    //---------------------------------------------------------------------------------
    // 8) Método CREAR_PENDING_INTENT: Este método crea el PendingIntent que debe
    //    llevar al usuario a la pantalla de Login
    //    * PENDING INTENT 1 -  CERRAR SESIÓN --> LOGIN
    private void crearPendingIntent() {
        // Crear intent
        Intent i = new Intent(this, Act_Login.class);
        //TaskStackBuilder gestiona el estado de la pila (si el usuario pulsa back)
        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(Act_Login.class);
        // Indicar el intent
        TSB.addNextIntent(i);
        pendingIntent = TSB.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE);
        finish();
        finishAndRemoveTask();
    }

    //-- **## NOTIFICACIONES ##**--//
    //---------------------------------------------------------------------------------
    // 9) Método CREAR_PENDING_INTENT: Este método crea el PendingIntent que debe
    //    llevar al usuario a la pantalla de Login si se pulsa el botón SÍ
    //    * PENDING INTENT 2 -   CERRAR SESIÓN --> LOGIN
    private void crearSiPendingIntent() {
        // Crear intent
        Intent i = new Intent(this, Act_Login.class);
        //TaskStackBuilder gestiona el estado de la pila (si el usuario pulsa back)
        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(Act_Login.class);
        // Indicar el intent
        TSB.addNextIntent(i);
        siPendingIntent = TSB.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE| PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //---------------------------------------------------------------------------------
    // 10) Método CERRAR_SESIÓN: Este método gestionará todo el cierre de sesión
    // y provocará que la aplicación se cierre por completo con un intent
    @Override
    public void cerrarSesion() {
        Intent i = new Intent(this, Act_Registro.class);
        // FLAG_ACTIVITY_CLEAR_TOP --> Destruye todas las actividades
        // que estén por encima en la pila y entonces se comporta como "singleTop"
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Añadir valor "EXIT" como 'chivato': Cuando la pantalla de registro
        // reciba el valor EXIT en True, hará finish y la aplicación se cerrará por completo
        i.putExtra("EXIT", true);
        // Llamar a lanzarNotificacion() antes de salir
        lanzarNotificacion();
        // Lanzar intent y hacer finish
        startActivity(i);
        finish();

    }

    //---------------------------------------------------------------------------------
    // 11) Método ON_SAVE_INSTANCE_STATE: Este método se ejecuta antes del destroy() de la
    //    aplicación, por lo que es aquí dónde se deben guardar los datos que se quieran
    //    mantener. Por ejemplo,para evitar la pérdida de datos al girar el móvil, interrupciones, etc.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar datos para evitar la pérdida al girar el móvil, interrupciones, etc.
        outState.putString("var_idioma", this.idiomaActual);
        outState.putString("var_email", this.email);
        outState.putString("var_usuario", this.u);
    }

    //---------------------------------------------------------------------------------
    // 12) Método ON_BACK_PRESSED: Método que se ejecuta al pulsar
    // el botón back del móvil
    @Override
    public void onBackPressed() {
        // Si el menú lateral estaba abierto, lo cerrará
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

}

