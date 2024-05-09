package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun CadenaConexion (): Connection? {
         try {
             val url = "jdbc:oracle:thin:@10.10.2.241:1521:xe"
             val usuario = "system"
             val contrasena = "ITR2024"

             val connection = DriverManager.getConnection(url, usuario, contrasena)
             return connection
              }catch (e:Exception){
                  println("Este es el error: $e")
                return null
              }

    }
}