package com.example.prueba

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MakeQueriesActivity : AppCompatActivity() {

    private lateinit var etBusqueda: EditText
    private lateinit var btnConsultar: Button
    private lateinit var tvID: TextView
    private lateinit var tvNombre: TextView
    private lateinit var tvApellido: TextView
    private lateinit var tvDNI: TextView
    private lateinit var listViewEvaluaciones: ListView

    private lateinit var dbHelper: Administrar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_queries)

        // Inicializar vistas
        etBusqueda = findViewById(R.id.etBusqueda)
        btnConsultar = findViewById(R.id.btnConsultar)
        tvID = findViewById(R.id.tvID)
        tvNombre = findViewById(R.id.tvNombre)
        tvApellido = findViewById(R.id.tvApellido)
        tvDNI = findViewById(R.id.tvDNI)
        listViewEvaluaciones = findViewById(R.id.listViewEvaluaciones)

        // Inicializar el helper de la base de datos
        dbHelper = Administrar(this)

        // Configurar el listener del botón de consulta
        btnConsultar.setOnClickListener {
            val busqueda = etBusqueda.text.toString().trim()
            if (busqueda.isNotEmpty()) {
                consultarDatos(busqueda)
            } else {
                Toast.makeText(this, "Ingrese un ID o nombre", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun consultarDatos(busqueda: String) {
        val db = dbHelper.readableDatabase

        // Consultar el alumno por ID o nombre
        val queryAlumno = """
        SELECT * FROM ${Administrar.TABLE_ALUMNOS}
        WHERE ${Administrar.COLUMN_ID} = ? OR ${Administrar.COLUMN_NOMBRE} LIKE ?
    """.trimIndent()

        val cursorAlumno = db.rawQuery(queryAlumno, arrayOf(busqueda, "%$busqueda%"))

        if (cursorAlumno.moveToFirst()) {
            // Mostrar los datos del alumno
            val id = cursorAlumno.getInt(cursorAlumno.getColumnIndexOrThrow(Administrar.COLUMN_ID))
            val nombre = cursorAlumno.getString(cursorAlumno.getColumnIndexOrThrow(Administrar.COLUMN_NOMBRE))
            val apellido = cursorAlumno.getString(cursorAlumno.getColumnIndexOrThrow(Administrar.COLUMN_APELLIDO))
            val dni = cursorAlumno.getString(cursorAlumno.getColumnIndexOrThrow(Administrar.COLUMN_DNI))

            tvID.text = "ID: $id"
            tvNombre.text = "Nombre: $nombre"
            tvApellido.text = "Apellido: $apellido"
            tvDNI.text = "DNI: $dni"

            // Consultar las evaluaciones del alumno
            val queryEvaluaciones = """
            SELECT * FROM ${Administrar.TABLE_EVALUACIONES}
            WHERE ${Administrar.COLUMN_ID_ALUMNO} = ?
        """.trimIndent()

            val cursorEvaluaciones = db.rawQuery(queryEvaluaciones, arrayOf(id.toString()))

            val evaluaciones = mutableListOf<String>()
            while (cursorEvaluaciones.moveToNext()) {
                val nota1 = cursorEvaluaciones.getFloat(cursorEvaluaciones.getColumnIndexOrThrow(Administrar.COLUMN_NOTA1))
                val nota2 = cursorEvaluaciones.getFloat(cursorEvaluaciones.getColumnIndexOrThrow(Administrar.COLUMN_NOTA2))
                val promedio = (nota1 + nota2) / 2
                val condicion = if (promedio >= 10.5) "Aprobado" else "Desaprobado"

                evaluaciones.add("Nota 1: $nota1, Nota 2: $nota2, Promedio: $promedio, Condición: $condicion")
            }

            // Mostrar las evaluaciones en el ListView
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, evaluaciones)
            listViewEvaluaciones.adapter = adapter

            cursorEvaluaciones.close()
        } else {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
        }

        cursorAlumno.close()
        db.close()
    }
}