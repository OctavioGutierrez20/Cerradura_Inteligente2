package com.example.cerradura_inteligente

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val auth = FirebaseAuth.getInstance()
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etClave = findViewById<EditText>(R.id.etClave)
        val etConfirmar = findViewById<EditText>(R.id.etConfirmar)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val clave = etClave.text.toString()
            val confirmar = etConfirmar.text.toString()

            if (nombre.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (clave.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (clave != confirmar) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(correo, clave)
                .addOnCompleteListener { tarea ->
                    if (tarea.isSuccessful) {
                        val cambios = UserProfileChangeRequest.Builder()
                            .setDisplayName(nombre)
                            .build()
                        tarea.result.user?.updateProfile(cambios)?.addOnCompleteListener {
                            Toast.makeText(this, "Cuenta creada. Inicia sesión", Toast.LENGTH_SHORT).show()
                            auth.signOut()
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "No se pudo crear la cuenta: ${tarea.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
