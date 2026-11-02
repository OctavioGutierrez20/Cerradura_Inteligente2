import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cerradura_inteligente.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CerraduraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cerradura)

        val tvEstado = findViewById<TextView>(R.id.tvEstado)
        val tvUltimo = findViewById<TextView>(R.id.tvUltimo)
        val ref = FirebaseDatabase.getInstance().reference.child("cerradura")

        ref.child("estado").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val abierta = snapshot.getValue(String::class.java) == "abierta"
                tvEstado.text = if (abierta) "ABIERTA" else "CERRADA"
                tvEstado.setTextColor(Color.parseColor(if (abierta) "#17724A" else "#B3382C"))
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CerraduraActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        ref.child("ultimo").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvUltimo.text = snapshot.getValue(String::class.java) ?: "Sin eventos todavía"
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
