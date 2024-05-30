package modelo

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion():Connection?{
        try {
            val url="jdbc:oracle:thin:@10.10.1.105:1521:xe"
            val user="system"
            val password="desarrollo"

            val connection=DriverManager.getConnection(url, user, password)

            return connection
        }catch (e:Exception){
            println("este es el error:$e")
            return null
        }
    }
}