package RecyclerViewHelper

import Manuel.Ortega.crudmanuel2_a.R
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modelo.ClaseConexion
import modelo.dataClassProductos

class Adaptador(private var Datos: List<dataClassProductos>) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarLista(nuevaLista:List<dataClassProductos>){
        Datos=nuevaLista
        notifyDataSetChanged()
    }

    fun eliminarRegistro(nombreProducto:String,position: Int){

        //quitar el elementpo de la lista
        val listaDatos = Datos .toMutableList()
        listaDatos.removeAt(position)

        //quitar de la base de datos
        GlobalScope.launch(Dispatchers.IO) {

            //crear un objeto e la clase conexion
            val objConexion=ClaseConexion().cadenaConexion()

            val deleteProducto = objConexion?.prepareStatement("delete tbproductos where NombreProducto=?")!!
            deleteProducto.setString( 1,nombreProducto)
            deleteProducto.executeUpdate()

            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Datos=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.NombreProducto

        val item =Datos[position]


        holder.imgBorrar.setOnClickListener {
            //craeamos una alaerta

            //invocamos  el contexto
            val context = holder.itemView.context

            //CREO LA ALERTA

            val builder = AlertDialog.Builder(context)

            //le ponemos titulo a la alerta

            builder.setTitle("¿estas seguro?")

            //ponerle mendsaje a la alerta

            builder.setMessage("Deseas en verdad eliminar el registro")

            //agrgamos los botones

            builder.setPositiveButton("si"){dialog,wich ->
                eliminarRegistro(item.NombreProducto,position)
            }

            builder.setNegativeButton("no"){dialog,wich ->

            }

            //cramos la alerta
            val alertDialog=builder.create()

            //mostramos la alerta

            alertDialog.show()

        }

    }
}
