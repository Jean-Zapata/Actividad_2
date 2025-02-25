package com.example.prueba

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Administrar(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Modelo de Alumno
    data class Alumno(val id: Int, val nombre: String, val apellido: String, val dni: String)

    // Modelo de Evaluación
    data class Evaluacion(val idEvaluacion: Int, val nota1: Float, val nota2: Float, val idAlumno: Int)

    companion object {
        private const val DATABASE_NAME = "BDPrueba.db"
        private const val DATABASE_VERSION = 1

        // Tabla Alumnos
        const val TABLE_ALUMNOS = "alumnos"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_APELLIDO = "apellido"
        const val COLUMN_DNI = "dni"

        // Tabla Evaluaciones
        const val TABLE_EVALUACIONES = "evaluaciones"
        private const val COLUMN_ID_EVALUACION = "id_eva"
        const val COLUMN_NOTA1 = "nota1"
        const val COLUMN_NOTA2 = "nota2"
        const val COLUMN_ID_ALUMNO = "id_alumno"

        // Tabla Users
        private const val TABLE_USERS = "Users"
        private const val COLUMN_GMAIL = "gmail"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla Alumnos
        db.execSQL(
            "CREATE TABLE $TABLE_ALUMNOS(" +
                    "$COLUMN_ID INTEGER PRIMARY KEY, " +
                    "$COLUMN_NOMBRE TEXT, " +
                    "$COLUMN_APELLIDO TEXT, " +
                    "$COLUMN_DNI TEXT)"
        )

        // Crear tabla Evaluaciones
        db.execSQL(
            "CREATE TABLE $TABLE_EVALUACIONES(" +
                    "$COLUMN_ID_EVALUACION INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NOTA1 REAL, " +
                    "$COLUMN_NOTA2 REAL, " +
                    "$COLUMN_ID_ALUMNO INTEGER, " +
                    "FOREIGN KEY($COLUMN_ID_ALUMNO) REFERENCES $TABLE_ALUMNOS($COLUMN_ID))"
        )

        // Crear tabla Users
        db.execSQL(
            "CREATE TABLE $TABLE_USERS(" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_GMAIL TEXT UNIQUE, " +
                    "$COLUMN_PASSWORD TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALUMNOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVALUACIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Método para registrar un usuario
    fun registrarUsuario(gmail: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GMAIL, gmail)
            put(COLUMN_PASSWORD, password)
        }

        return try {
            db.insertOrThrow(TABLE_USERS, null, values) != -1L
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    // Método para validar usuario y contraseña
    fun validarUsuario(gmail: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_GMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(gmail, password))

        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    // Método para registrar un alumno
    fun registrarAlumno(id: Int, nombre: String, apellido: String, dni: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_APELLIDO, apellido)
            put(COLUMN_DNI, dni)
        }

        return try {
            db.insert(TABLE_ALUMNOS, null, values) != -1L
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    // Método para consultar un alumno
    fun consultarAlumno(id: Int): Alumno? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ALUMNOS WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            val alumno = Alumno(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
            )
            cursor.close()
            db.close()
            alumno
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // Método para modificar un alumno
    fun modificarAlumno(id: Int, nombre: String, apellido: String, dni: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_APELLIDO, apellido)
            put(COLUMN_DNI, dni)
        }

        val filasActualizadas = db.update(TABLE_ALUMNOS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return filasActualizadas > 0
    }

    // Método para eliminar un alumno
    fun eliminarAlumno(id: Int): Boolean {
        val db = this.writableDatabase
        val filasEliminadas = db.delete(TABLE_ALUMNOS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return filasEliminadas > 0
    }

    // Método para registrar una evaluación
    fun registrarEvaluacion(nota1: Float, nota2: Float, idAlumno: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTA1, nota1)
            put(COLUMN_NOTA2, nota2)
            put(COLUMN_ID_ALUMNO, idAlumno)
        }

        val resultado = db.insert(TABLE_EVALUACIONES, null, values)
        db.close()
        return resultado != -1L
    }

    // Método para consultar una evaluación
    fun consultarEvaluacion(idEvaluacion: Int): Evaluacion? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EVALUACIONES WHERE $COLUMN_ID_EVALUACION = ?", arrayOf(idEvaluacion.toString()))

        return if (cursor.moveToFirst()) {
            val evaluacion = Evaluacion(
                cursor.getInt(0),
                cursor.getFloat(1),
                cursor.getFloat(2),
                cursor.getInt(3)
            )
            cursor.close()
            db.close()
            evaluacion
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // Método para modificar una evaluación
    fun modificarEvaluacion(idEvaluacion: Int, nota1: Float, nota2: Float, idAlumno: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTA1, nota1)
            put(COLUMN_NOTA2, nota2)
            put(COLUMN_ID_ALUMNO, idAlumno)
        }

        val resultado = db.update(TABLE_EVALUACIONES, values, "$COLUMN_ID_EVALUACION = ?", arrayOf(idEvaluacion.toString()))
        db.close()
        return resultado > 0
    }

    // Método para eliminar una evaluación
    fun eliminarEvaluacion(idEvaluacion: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete(TABLE_EVALUACIONES, "$COLUMN_ID_EVALUACION = ?", arrayOf(idEvaluacion.toString()))
        db.close()
        return resultado > 0
    }
}