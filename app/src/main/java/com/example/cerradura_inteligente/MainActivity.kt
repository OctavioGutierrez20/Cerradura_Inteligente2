package com.example.cerradura_inteligente

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etClave = findViewById<EditText>(R.id.etClave)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvCrear = findViewById<TextView>(R.id.tvCrear)

        btnLogin.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val clave = etClave.text.toString()
            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(correo, clave)
                .addOnCompleteListener { tarea ->
                    if (tarea.isSuccessful) {
                        startActivity(Intent(this, RolActivity::class.java))
                    } else {
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        tvCrear.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}
