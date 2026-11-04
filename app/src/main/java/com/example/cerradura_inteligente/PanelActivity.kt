package com.example.cerradura_inteligente

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PanelActivity : AppCompatActivity() {

    private val db = FirebaseDatabase.getInstance().reference
    private val historial = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)

        val tvEstado = findViewById<TextView>(R.id.tvEstado)
        val btnAbrir = findViewById<Button>(R.id.btnAbrir)
        val btnCerrar = findViewById<Button>(R.id.btnCerrar)
        val lvHistorial = findViewById<ListView>(R.id.lvHistorial)

        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, historial)
        lvHistorial.adapter = adaptador

        btnAbrir.setOnClickListener { cambiarEstado("abierta") }
        btnCerrar.setOnClickListener { cambiarEstado("cerrada") }

        // Escucha el estado de la cerradura en tiempo real
        db.child("cerradura").child("estado").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val estado = snapshot.getValue(String::class.java) ?: "cerrada"
                tvEstado.text = if (estado == "abierta") "Abierta" else "Cerrada"
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PanelActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Escucha los últimos 20 registros del historial
        db.child("historial").limitToLast(20).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historial.clear()
                for (item in snapshot.children) {
                    val accion = item.child("accion").getValue(String::class.java) ?: ""
                    val usuario = item.child("usuario").getValue(String::class.java) ?: ""
                    val fecha = item.child("fecha").getValue(String::class.java) ?: ""
                    historial.add(0, "$accion · $usuario · $fecha") // el más nuevo arriba
                }
                adaptador.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun cambiarEstado(nuevo: String) {
        val usuario = FirebaseAuth.getInstance().currentUser?.displayName
            ?.takeIf { it.isNotBlank() } ?: "desconocido"
        val fecha = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date())
        val accion = if (nuevo == "abierta") "Abierta" else "Cerrada"

        db.child("cerradura").child("estado").setValue(nuevo)
        db.child("cerradura").child("ultimo").setValue("$accion por $usuario · $fecha")
        db.child("historial").push().setValue(
            mapOf("accion" to accion, "usuario" to usuario, "fecha" to fecha)
        )
    }
}
