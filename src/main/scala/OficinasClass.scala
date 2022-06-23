package com.cursosdedesarrollo

import java.sql.Date

class OficinaObjeto(
                     var id:Int = 0,
                     var address:String = "Example Address",
                     var tlf:String = "Example Number",
                     var createdDate: Date = null,
                     var status: Boolean = true,
                     var income: Float = 0
                   ) {
  override def toString = s"Oficina(ID: $id, Adress: $address, Tlf: $tlf, Status: $status, Yearly Income: $income)"
}