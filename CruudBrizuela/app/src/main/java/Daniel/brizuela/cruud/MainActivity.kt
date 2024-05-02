package Daniel.brizuela.cruud

import Modelo.ClaseConexion
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //- Mandar a llamar a todos los elementos de la pantalla
        val txtNomProducto = findViewById<EditText>(R.id.txtNomProducto)
        val txtPrecioProducto = findViewById<EditText>(R.id.txtPrecioProducto)
        val txtCantidadProducto = findViewById<EditText>(R.id.txtCantProducto)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        //- Programar el boton

        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                //- guardar datos
                //1- Creo un objeto de la clase conexion
                val claseConexion = ClaseConexion().CadenaConexion()

                //-2 Creo una variable que tenga PreparedStatement
                val addProductos = claseConexion?.prepareStatement("insert into tbProductos( nombreProducto, precio, cantidad) values(?,?,?)")!!
                addProductos.setString(1, txtNomProducto.text.toString())
                addProductos.setInt(2,txtPrecioProducto.text.toString().toInt())
                addProductos.setInt(3, txtCantidadProducto.text.toString().toInt())
                addProductos.executeUpdate()
            }
        }
    }
}