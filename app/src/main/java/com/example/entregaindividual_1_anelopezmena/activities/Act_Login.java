package com.example.entregaindividual_1_anelopezmena.activities;

import static com.example.entregaindividual_1_anelopezmena.activities.Act_MenuPrincipal.NOTIFICACION_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.entregaindividual_1_anelopezmena.R;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_DB;
import com.example.entregaindividual_1_anelopezmena.controlador.Gestor_Idioma;
import com.example.entregaindividual_1_anelopezmena.dialogs.Dial_CredencialesIncorrectas;

/*************************************************************************/
/** ------------------------- ACTIVITY_LOGIN -------------------------- **/
/*************************************************************************/
// De forma complementaria a la actividad anterior (Act_Registro), la pantalla
// de login, será la forma de permitir el acceso a los usuarios a los servicios
// de ‘BlockBuster!’. Si el usuario introduce correctamente su combinación de
// usuario y contraseña, podrá acceder a la app, mientras que si es incorrecta,
// recibirá una alerta indicando que ha habido un error al acceder a sus sesión.
public class Act_Login extends AppCompatActivity {
    private EditText email, contrasena;
    private Button b_login;
    private Gestor_DB DB;
    private Gestor_Idioma gestorIdioma;
    private String idiomaActual;
    private String e_anterior, e;

    //---------------------------------------------------------------------------------
    // 1) Método ON_CREATE
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Crear el manager de notificaciones
        NotificationManagerCompat NMC = NotificationManagerCompat.from(getApplicationContext());
        // Cuando se entre a la actividad de Login, desaparecerá la notificación
        NMC.cancel(NOTIFICACION_ID);

    // -----------------------------------------------------------------------------------
        // Crear gestor de idioma
        gestorIdioma = new Gestor_Idioma(this);
    // -----------------------------------------------------------------------------------
        // Recibir intent desde la pantalla de registro
        Bundle extras = getIntent().getExtras();
        // Si el usuario se ha registrado ahora, guardar el email para copiarlo
        // en el campo de email del Act_Login
        if (extras != null){
            this.e_anterior = extras.getString("var_email");
            this.idiomaActual = extras.getString("var_idioma");
            // Si recoge la variable boolean 'EXIT0, desde el pending
            // intent de la notificación, se cierra la sisión
            // por completo
        }
    // -----------------------------------------------------------------------------------
        // Gestionar pérdida de información al girar la pantalla:
        // GUARDAR:
        //      * EMAIL
        //      * IDIOMA
        if (savedInstanceState != null){
            // Obtener idioma guardado para volver a settearlo, además del email
            this.idiomaActual = savedInstanceState.getString("var_idioma");
            this.e = savedInstanceState.getString("var_email");
        }
        // Settear idioma recuperado
        gestorIdioma.setIdioma(idiomaActual);

        // Crear la vista
        setContentView(R.layout.act_login_layout);

        // Una vez creada la vista, escribir email en el campo
        email = (EditText) findViewById(R.id.id_usuario_login);
        email.setText(e_anterior);
    }
    // -----------------------------------------------------------------------------------
    // 2) Método COMPROBAR_USUARIO: Aquí se comprobarán las credenciales del
    //    usuario que intenta loguearse. Si son correctas, se le dará paso, y si no,
    //    se le mostrará un dialogo informativo.
    public void comprobarUsuario(View v){
        // Conseguir elementos de la vista
        email = (EditText) findViewById(R.id.id_usuario_login);
        contrasena = (EditText) findViewById(R.id.id_contrasena_login);
        b_login = (Button) findViewById(R.id.id_login);

        // Conseguir texto de los elementos
        String e = email.getText().toString();
        String c = contrasena.getText().toString();

        // Instanciar DB
        DB = new Gestor_DB(this);
        // Llamar al método validador de credenciales
        boolean res = DB.comprobarContraseña(e, c);

        if (res){ // Si se ha encontrado al usuario en la DB

           // Llamar a act_MenuPrincipal y hacer finish
           Intent i =  new Intent(this, Act_MenuPrincipal.class);
           // Pasar email e idioma a la siguiente pantalla
           i.putExtra("var_email", e);
           i.putExtra("var_idioma", this.idiomaActual);
           // Lanzar intent y finish de la activity actual
           startActivity(i);
           finish();
        }
        else{
            // No se ha encontrado al usuario en la DB
            // Crear dialog para informar al usuario
            Dial_CredencialesIncorrectas d = new Dial_CredencialesIncorrectas();
            d.show(getSupportFragmentManager(), "CredencialesIncorrectas");
            //Toast.makeText(this, "Email/Contraseña incorrecta", Toast.LENGTH_LONG).show();

        }
    }

    // -----------------------------------------------------------------------------------
    // 3) Método COMPROBAR_USUARIO: Aquí se comprobarán las credenciales del
    //    usuario que intenta loguearse. Si son correctas, se le dará paso, y si no,
    //    se le mostrará un dialogo informativo.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar datos para evitar la pérdida al girar el móvil, interrupciones, etc.
        outState.putString("var_idioma", this.idiomaActual);
        outState.putString("var_email", e);
    }


}