package com.cursosdedesarrollo

import java.sql.{Connection, PreparedStatement, Statement}

class OficinaDAO(var connection: Connection) {


  def createTable: Boolean ={
    val createTableSQL="""create table oficinas (
                             id int unsigned auto_increment not null,
                             address varchar(32) not null,
                             tlf varchar(32) not null,
                             createdDate timestamp default now(),
                             status boolean default false,
                             income float,
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
  def save(objeto:OficinaObjeto):OficinaObjeto ={
    // the mysql insert statement// the mysql insert statement

    val query: String = " insert into oficinas (address, tlf, createdDate, status, income)" +
      " values (?, ?, ?, ?, ?)"

    // create the mysql insert preparedstatement
    val preparedStmt: PreparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)

    preparedStmt.setString(1, objeto.address)
    preparedStmt.setString(2, objeto.tlf)
    preparedStmt.setDate(3, objeto.createdDate)
    preparedStmt.setBoolean(4, objeto.status)
    preparedStmt.setFloat(5, objeto.income)

    // execute the preparedstatement

    val res = preparedStmt.executeUpdate()
    val rs = preparedStmt.getGeneratedKeys
    var id = 0
    if (rs.next) id = rs.getInt(1)
    objeto.id=id
    objeto
  }
  def findAll:List[OficinaObjeto] ={
    val selectSQL="select * from oficinas;"
    val statement=connection.prepareStatement(selectSQL);
    val rs=statement.executeQuery()

    var listado= List[OficinaObjeto]()
    // iterate through the java resultset// iterate through the java resultset
    while ( {
      rs.next
    }) {
      val id = rs.getInt("id")
      val address = rs.getString("address")
      val tlf = rs.getString("tlf")
      val createdDate = rs.getDate("createdDate")
      val status = rs.getBoolean("status")
      val income = rs.getFloat("income")
      // print the results
      //System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints)
      listado = listado ::: List(new OficinaObjeto(id,address,tlf,createdDate,status,income))
    }
    listado
  }
  def findByID(id:Int):OficinaObjeto={
    val selectSQL="select * from oficinas where id=?;"
    val statement=connection.prepareStatement(selectSQL);
    statement.setInt(1, id)
    val rs=statement.executeQuery()

    var objeto:OficinaObjeto=null
    // iterate through the java resultset// iterate through the java resultset
    if ( {
      rs.next
    }) {
      val id = rs.getInt("id")
      val address = rs.getString("address")
      val tlf = rs.getString("tlf")
      val createdDate = rs.getDate("createdDate")
      val status = rs.getBoolean("status")
      val income = rs.getFloat("income")
      // print the results
      //System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints)
      objeto = new OficinaObjeto(id,address,tlf,createdDate,status,income)
    }
    objeto
  }
  def update(objeto:OficinaObjeto):OficinaObjeto ={
    val id = objeto.id
    val query: String = " update oficinas set address = ?, tlf = ?, createdDate = ?, status = ?, income = ?" +
      " where id =  ?"

    // create the mysql insert preparedstatement
    val preparedStmt: PreparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)
    preparedStmt.setString(1, objeto.address)
    preparedStmt.setString(2, objeto.tlf)
    preparedStmt.setDate(3, objeto.createdDate)
    preparedStmt.setBoolean(4, objeto.status)
    preparedStmt.setFloat(5, objeto.income)
    preparedStmt.setFloat(6, objeto.id)
    // execute the preparedstatement

    val res = preparedStmt.executeUpdate()
    if (res == null){
      return null
    }
    objeto
  }
  def delete(id:Int):Boolean={

    val query: String = " delete from oficinas  where id=?"
    val preparedStmt: PreparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)
    preparedStmt.setInt(1, id)
    val res = preparedStmt.executeUpdate()
    if (res==null){
      return false
    }
    true
  }
}