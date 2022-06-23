package com.cursosdedesarrollo

class ClienteObjeto(
                        var id:Int = 0,
                        var name:String = "John Doe",
                        var tlf:String = "Example Number",
                        var email: String ="example@email.com"
                      ) {
  override def toString = s"Cliente(ID: $id, Name: $name, Tlf: $tlf, Email: $email)"
}