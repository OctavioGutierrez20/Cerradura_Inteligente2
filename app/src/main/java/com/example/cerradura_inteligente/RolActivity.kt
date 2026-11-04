package com.example.cerradura_inteligente

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RolActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rol)

        findViewById<Button>(R.id.btnPropietario).setOnClickListener {
            startActivity(Intent(this, PanelActivity::class.java))
        }
        findViewById<Button>(R.id.btnCerradura).setOnClickListener {
            startActivity(Intent(this, CerraduraActivity::class.java))
        }
        findViewById<Button>(R.id.btnSalir).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }
}
