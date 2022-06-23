package com.cursosdedesarrollo

import java.sql.{Connection, DriverManager}
import java.text.SimpleDateFormat

object AppOficinas  {
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
      val dao = new OficinaDAO(connection)


      val meta = connection.getMetaData
      var res = meta.getTables("test", null, "oficinas",null)
      if (res.next()){
        println("Tabla ya creada")
      }else{
        val res=dao.createTable
        println("Tabla creada?: "+res)
      }

      var O1 = new OficinaObjeto()
      println(O1)
      O1 = dao.save(O1)
      var listado =dao.findAll
      println(listado)
      O1 = dao.findByID(O1.id)
      println(O1)
      O1.income = 10000
      O1 = dao.update(O1)
      listado = dao.findAll
      println(listado)
      var borrado=dao.delete(O1.id)
      println("Borrado: "+borrado)
      listado=dao.findAll
      println(listado)

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close
  }

}
