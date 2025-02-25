package com.example.prueba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class NewActivity : AppCompatActivity() {

    lateinit var btnRegisterStudent: Button
    lateinit var btnRegisterEvaluations: Button
    lateinit var btnMakeQueries: Button
    lateinit var btnClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        btnRegisterStudent = findViewById(R.id.btnRegisterStudent)
        btnRegisterEvaluations = findViewById(R.id.btnRegisterEvaluations)
        btnMakeQueries = findViewById(R.id.btnMakeQueries)
        btnClose = findViewById(R.id.btnClose)

        btnRegisterStudent.setOnClickListener {
            val intent = Intent(this, RegisterStudentActivity::class.java)
            startActivity(intent)
        }

        btnRegisterEvaluations.setOnClickListener {
            val intent = Intent(this, RegisterEvaluationsActivity::class.java)
            startActivity(intent)
        }

        btnMakeQueries.setOnClickListener {
            val intent = Intent(this, MakeQueriesActivity::class.java)
            startActivity(intent)
        }

        btnClose.setOnClickListener {
            finish()
        }
    }
}
