import Daniel.brizuela.cruud.R
import Modelo.ClaseConexion
import Modelo.dataClaseProductos
import RecyclerViewHelper.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Adaptor(private var Datos: List<dataClaseProductos>) : RecyclerView.Adapter<ViewHolder>() {
    fun actualizarLista(nuevaLista: List<dataClaseProductos>){
        Datos = nuevaLista
        notifyDataSetChanged()
    }
    fun eliminarRegistros(nombreProductos: String, posicion: Int){

        //2- quitar el elemento de la lista
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        //quitar de la base de datos
        GlobalScope.launch (Dispatchers.IO){
            //1- Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().CadenaConexion()

            val delProducto = objConexion?.prepareStatement("delete tbProductos where nombreProducto = ?")!!
            delProducto.setString(1,nombreProductos)
            delProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_carta, parent, false)
        return ViewHolder(vista)
    }
    override fun getItemCount() = Datos.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreProducto

        val item = Datos[position]
        holder.imgBorrar.setOnClickListener{
            eliminarRegistros(item.nombreProducto, position)
        }
    }
}
