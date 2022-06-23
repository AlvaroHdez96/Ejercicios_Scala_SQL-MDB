package com.cursosdedesarrollo

import java.sql.{Connection, PreparedStatement, Statement}

class ClienteDAO(var connection: Connection) {


  def createTable: Boolean ={
    val createTableSQL="""create table users (
                             id int unsigned auto_increment not null,
                             name varchar(32) not null,
                             tlf varchar(32) not null,
                             email varchar(32) not null,
                             primary key (id)
                         );"""
    val statement=connection.prepareStatement(createTableSQL);
    try{
      statement.execute()
    } catch {
      case e: Exception => {
        false
      }
    }
  }

  def save(objeto: ClienteObjeto): ClienteObjeto = {
    // the mysql insert statement// the mysql insert statement

    val query: String = " insert into users (name, tlf, email)" +
      " values (?, ?, ?)"

    // create the mysql insert preparedstatement
    val preparedStmt: PreparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

    preparedStmt.setString(1, objeto.name)
    preparedStmt.setString(2, objeto.tlf)
    preparedStmt.setString(3, objeto.email)

    // execute the preparedstatement

    val res = preparedStmt.executeUpdate()
    val rs = preparedStmt.getGeneratedKeys
    var id = 0
    if (rs.next) id = rs.getInt(1)
    objeto.id = id
    objeto
  }
  def findAll:List[ClienteObjeto] ={
    val selectSQL="select * from users;"
    val statement=connection.prepareStatement(selectSQL);
    val rs=statement.executeQuery()

    var listado= List[ClienteObjeto]()
    // iterate through the java resultset// iterate through the java resultset
    while ( {
      rs.next
    }) {
      val id = rs.getInt("id")
      val name = rs.getString("name")
      val tlf = rs.getString("tlf")
      val email = rs.getString("email")
      // print the results
      //System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints)
      listado = listado ::: List(new ClienteObjeto(id,name,tlf,email))
    }
    listado
  }
  def findByID(id:Int):ClienteObjeto={
    val selectSQL="select * from users where id=?;"
    val statement=connection.prepareStatement(selectSQL);
    statement.setInt(1, id)
    val rs=statement.executeQuery()

    var objeto:ClienteObjeto=null
    // iterate through the java resultset// iterate through the java resultset
    if ( {
      rs.next
    }) {
      val id = rs.getInt("id")
      val name = rs.getString("name")
      val tlf = rs.getString("tlf")
      val email = rs.getString("email")
      // print the results
      //System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints)
      objeto = new ClienteObjeto(id,name,tlf,email)
    }
    objeto
  }
  def update(objeto:ClienteObjeto):ClienteObjeto ={
    val id = objeto.id
    val query: String = " update users set name = ?, tlf = ?, email = ?" +
      " where id =  ?"

    // create the mysql insert preparedstatement
    val preparedStmt: PreparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)
    preparedStmt.setString(1, objeto.name)
    preparedStmt.setString(2, objeto.tlf)
    preparedStmt.setString(3, objeto.email)
    preparedStmt.setFloat(4, objeto.id)
    // execute the preparedstatement

    val res = preparedStmt.executeUpdate()
    if (res == null){
      return null
    }
    objeto
  }
  def delete(id:Int):Boolean={

    val query: String = " delete from users  where id=?"
    val preparedStmt: PreparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)
    preparedStmt.setInt(1, id)
    val res = preparedStmt.executeUpdate()
    if (res==null){
      return false
    }
    true
  }

}