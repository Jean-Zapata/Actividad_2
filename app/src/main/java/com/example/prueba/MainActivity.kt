package com.example.prueba

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var singUp: TextView
    private lateinit var logIn: TextView
    private lateinit var singUpLayout: LinearLayout
    private lateinit var logInLayout: LinearLayout
    private lateinit var singIn: Button
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var registerEmailField: EditText
    private lateinit var registerPasswordField: EditText
    private lateinit var registerConfirmPasswordField: EditText

    private lateinit var dbHelper: Administrar

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = Administrar(this)

        // Referencias a los elementos de la UI
        singUp = findViewById(R.id.singUp)
        logIn = findViewById(R.id.logIn)
        singUpLayout = findViewById(R.id.singUpLayout)
        logInLayout = findViewById(R.id.logInLayout)
        singIn = findViewById(R.id.singIn)

        // Campos del formulario de login
        emailField = findViewById(R.id.eMail)
        passwordField = findViewById(R.id.passwords)

        // Campos del formulario de registro
        registerEmailField = findViewById(R.id.eMails)
        registerPasswordField = findViewById(R.id.passwordss)
        registerConfirmPasswordField = findViewById(R.id.passwords01)

        // Configurar los listeners para cambiar entre Login y Sign Up
        singUp.setOnClickListener {
            singUp.background = resources.getDrawable(R.drawable.switch_trcks, null)
            singUp.setTextColor(resources.getColor(R.color.textColor, null))
            logIn.background = null
            singUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            logIn.setTextColor(resources.getColor(R.color.pinkColor, null))
            singIn.text = getString(R.string.sign_up)
        }

        logIn.setOnClickListener {
            singUp.background = null
            singUp.setTextColor(resources.getColor(R.color.pinkColor, null))
            logIn.background = resources.getDrawable(R.drawable.switch_trcks, null)
            singUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            logIn.setTextColor(resources.getColor(R.color.textColor, null))
            singIn.text = getString(R.string.log_in)
        }

        // Configurar el botón principal que maneja tanto login como registro
        singIn.setOnClickListener {
            if (logInLayout.visibility == View.VISIBLE) {
                // Proceso de login
                handleLogin()
            } else {
                // Proceso de registro
                handleSignUp()
            }
        }
    }

    private fun handleLogin() {
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.validarUsuario(email, password)) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            // Navegar a la siguiente actividad
            val intent = Intent(this, NewActivity::class.java)
            startActivity(intent)
            // Opcional: finish() para cerrar esta actividad
        } else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSignUp() {
        val email = registerEmailField.text.toString().trim()
        val password = registerPasswordField.text.toString().trim()
        val confirmPassword = registerConfirmPasswordField.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.registrarUsuario(email, password)) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            // Cambiar a la vista de login después de un registro exitoso
            logIn.performClick()
            // Limpiar los campos
            registerEmailField.text.clear()
            registerPasswordField.text.clear()
            registerConfirmPasswordField.text.clear()
        } else {
            Toast.makeText(this, "Error: el usuario ya existe", Toast.LENGTH_SHORT).show()
        }
    }
}