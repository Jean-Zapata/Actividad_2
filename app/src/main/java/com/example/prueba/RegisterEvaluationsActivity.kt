package com.example.prueba

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterEvaluationsActivity : AppCompatActivity() {
    private lateinit var etIdEvaluacion: EditText
    private lateinit var etIdAlumno: EditText
    private lateinit var etNota1: EditText
    private lateinit var etNota2: EditText
    private lateinit var tvPromedio: TextView
    private lateinit var tvCondicion: TextView
    private lateinit var btnRegistrar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnModificar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnRetornar: Button
    private lateinit var actvCurso: AutoCompleteTextView
    private lateinit var db: Administrar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_evaluations)

        // Inicializar elementos de la UI
        etIdEvaluacion = findViewById(R.id.etIdEvaluacion)
        etIdAlumno = findViewById(R.id.etIdAlumno)
        etNota1 = findViewById(R.id.etNota1)
        etNota2 = findViewById(R.id.etNota2)
        tvPromedio = findViewById(R.id.tvPromedio)
        tvCondicion = findViewById(R.id.tvCondicion)
        btnRegistrar = findViewById(R.id.btnRegistrarEva)
        btnConsultar = findViewById(R.id.btnConsultarEva)
        btnModificar = findViewById(R.id.btnModificarEva)
        btnEliminar = findViewById(R.id.btnEliminarEva)
        btnRetornar = findViewById(R.id.btnRetornarEva)
        actvCurso = findViewById(R.id.actvCurso)

        db = Administrar(this)

        // Configurar el Spinner de Cursos con datos ficticios
        val cursos = arrayOf("Matemáticas", "Historia", "Ciencias", "Literatura", "Física")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cursos)
        actvCurso.setAdapter(adapter)

        // Configurar botones
        btnRegistrar.setOnClickListener { registrarEvaluacion() }
        btnConsultar.setOnClickListener { consultarEvaluacion() }
        btnModificar.setOnClickListener { modificarEvaluacion() }
        btnEliminar.setOnClickListener { eliminarEvaluacion() }
        btnRetornar.setOnClickListener { finish() }
    }

    private fun registrarEvaluacion() {
        val idAlumno = etIdAlumno.text.toString().toIntOrNull()
        val nota1 = etNota1.text.toString().toFloatOrNull()
        val nota2 = etNota2.text.toString().toFloatOrNull()

        if (idAlumno == null || nota1 == null || nota2 == null) {
            Toast.makeText(this, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val promedio = (nota1 + nota2) / 2
        val condicion = if (promedio >= 10.5) "Aprobado" else "Desaprobado"

        val exito = db.registrarEvaluacion(nota1, nota2, idAlumno)
        if (exito) {
            tvPromedio.text = promedio.toString()
            tvCondicion.text = condicion
            Toast.makeText(this, "Evaluación registrada correctamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al registrar la evaluación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun consultarEvaluacion() {
        val idEvaluacion = etIdEvaluacion.text.toString().toIntOrNull()
        if (idEvaluacion == null) {
            Toast.makeText(this, "Ingrese un ID de Evaluación válido", Toast.LENGTH_SHORT).show()
            return
        }

        val evaluacion = db.consultarEvaluacion(idEvaluacion)
        if (evaluacion != null) {
            etIdAlumno.setText(evaluacion.idAlumno.toString())
            etNota1.setText(evaluacion.nota1.toString())
            etNota2.setText(evaluacion.nota2.toString())
            val promedio = (evaluacion.nota1 + evaluacion.nota2) / 2
            tvPromedio.text = promedio.toString()
            tvCondicion.text = if (promedio >= 10.5) "Aprobado" else "Desaprobado"
            Toast.makeText(this, "Evaluación encontrada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se encontró la evaluación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun modificarEvaluacion() {
        val idEvaluacion = etIdEvaluacion.text.toString().toIntOrNull()
        val idAlumno = etIdAlumno.text.toString().toIntOrNull()
        val nota1 = etNota1.text.toString().toFloatOrNull()
        val nota2 = etNota2.text.toString().toFloatOrNull()

        if (idEvaluacion == null || idAlumno == null || nota1 == null || nota2 == null) {
            Toast.makeText(this, "Complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val promedio = (nota1 + nota2) / 2
        val condicion = if (promedio >= 10.5) "Aprobado" else "Desaprobado"

        val exito = db.modificarEvaluacion(idEvaluacion, nota1, nota2, idAlumno)
        if (exito) {
            tvPromedio.text = promedio.toString()
            tvCondicion.text = condicion
            Toast.makeText(this, "Evaluación modificada correctamente", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "Error al modificar la evaluación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarEvaluacion() {
        val idEvaluacion = etIdEvaluacion.text.toString().toIntOrNull()
        if (idEvaluacion == null) {
            Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = db.eliminarEvaluacion(idEvaluacion)
        if (exito) {
            tvPromedio.text = "0.00"
            tvCondicion.text = "Pendiente"
            Toast.makeText(this, "Evaluación eliminada", Toast.LENGTH_SHORT).show()
            limpiarCampos()
        } else {
            Toast.makeText(this, "No se pudo eliminar la evaluación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        etIdEvaluacion.text.clear()
        etIdAlumno.text.clear()
        etNota1.text.clear()
        etNota2.text.clear()
        actvCurso.setText("")
    }
}
