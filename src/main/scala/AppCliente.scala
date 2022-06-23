package com.cursosdedesarrollo

import java.sql.{Connection, DriverManager}
import java.text.SimpleDateFormat

object AppCliente  {
  def main(args: Array[String]): Unit = {
    // connect to the database named "mysql" on port 8889 of localhost
    val url = "jdbc:mysql://localhost:3307/test"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"
    var connection: Connection = null

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val dao = new ClienteDAO(connection)


      val meta = connection.getMetaData
      var res = meta.getTables("test", null, "users",null)
      if (res.next()){
        println("Tabla ya creada")
      }else{
        val res=dao.createTable
        println("Tabla creada?: "+res)
      }

      var C1 = new ClienteObjeto()
      println(C1)
      C1 = dao.save(C1)
      var listado =dao.findAll
      println(listado)
      C1 = dao.findByID(C1.id)
      println(C1)
      C1.name = "Pedro"
      C1 = dao.update(C1)
      listado = dao.findAll
      println(listado)
      var borrado=dao.delete(C1.id)
      println("Borrado: "+borrado)
      listado=dao.findAll
      println(listado)

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close


  }

}
