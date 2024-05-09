package Daniel.brizuela.cruud

import Adaptor
import Modelo.ClaseConexion
import Modelo.dataClaseProductos
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        fun limpiar (){
            txtNomProducto.setText("")
            txtPrecioProducto.setText("")
            txtCantidadProducto.setText("")
        }


        //////////////////TODO: mostar datos ///////////////////////////
        val RcvProducto = findViewById<RecyclerView>(R.id.RcvProductos)

        //asignar un layout al RecyclerView
        RcvProducto.layoutManager = LinearLayoutManager(this)

        // Funcion para obtener datos
        fun obtenerDatos(): List<dataClaseProductos>{
            val objConexion = ClaseConexion().CadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbproductos")!!

            val productos = mutableListOf<dataClaseProductos>()
            while (resultSet.next()){
                val nombre = resultSet.getString("nombreProducto")
                val producto = dataClaseProductos(nombre)
                productos.add(producto)
            }
            return productos
        }

        //asignar un adaptador

        CoroutineScope(Dispatchers.IO).launch {
            val productosDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val miAdapter = Adaptor(productosDB)
                RcvProducto.adapter = miAdapter
            }
        }

        ///////////////////// TODO: Guardar productos //////////////////////////

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

                val nuevosProductos = obtenerDatos()
                withContext(Dispatchers.Main){
                    (RcvProducto.adapter as? Adaptor)?.actualizarLista(nuevosProductos)
                }
            }
            //limpiar()
        }

    }
}