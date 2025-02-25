package com.example.prueba

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterStudentActivity : AppCompatActivity() {
    private lateinit var etId: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etDni: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnModificar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnRetornar: Button
    private lateinit var db: Administrar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_student)

        etId = findViewById(R.id.etId)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etDni = findViewById(R.id.etDni)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnConsultar = findViewById(R.id.btnConsultar)
        btnModificar = findViewById(R.id.btnModificar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnRetornar = findViewById(R.id.btnRetornar)

        db = Administrar(this)

        btnRegistrar.setOnClickListener { registrarAlumno() }
        btnConsultar.setOnClickListener { consultarAlumno() }
        btnModificar.setOnClickListener { modificarAlumno() }
        btnEliminar.setOnClickListener { eliminarAlumno() }
        btnRetornar.setOnClickListener { finish() } // Cierra la actividad
    }

    private fun registrarAlumno() {
        val id = etId.text.toString().toIntOrNull()
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val dni = etDni.text.toString().trim()

        if (id == null || nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = db.registrarAlumno(id, nombre, apellido, dni)
        if (exito) {
            Toast.makeText(this, "Alumno registrado correctamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarAlumno() {
        val id = etId.text.toString().toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            return
        }

        val alumno = db.consultarAlumno(id)
        if (alumno != null) {
            etNombre.setText(alumno.nombre)
            etApellido.setText(alumno.apellido)
            etDni.setText(alumno.dni)
            Toast.makeText(this, "Alumno encontrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se encontró el alumno", Toast.LENGTH_SHORT).show()
        }
    }

    private fun modificarAlumno() {
        val id = etId.text.toString().toIntOrNull()
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val dni = etDni.text.toString().trim()

        if (id == null || nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = db.modificarAlumno(id, nombre, apellido, dni)
        if (exito) {
            Toast.makeText(this, "Alumno modificado", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al modificar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarAlumno() {
        val id = etId.text.toString().toIntOrNull()
        if (id == null) {
            Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = db.eliminarAlumno(id)
        if (exito) {
            Toast.makeText(this, "Alumno eliminado", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        etId.text.clear()
        etNombre.text.clear()
        etApellido.text.clear()
        etDni.text.clear()
    }
}
