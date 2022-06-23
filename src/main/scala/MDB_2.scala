package com.cursosdedesarrollo

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.Completed
import org.mongodb.scala.bson.ObjectId
//import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase, Observer}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Person {
  def apply(name: String, tlf: String, email:String): Person = Person(new ObjectId(), name, tlf, email);
}
//tipo de clase especial similar al javabean

case class Person(_id: ObjectId, name: String, tlf: String, email:String)


object Ejemplo05MongoCodec {
  def main(args: Array[String]): Unit = {

    //val codecRegistry = fromRegistries(fromProviders(classOf[Person]), DEFAULT_CODEC_REGISTRY)
    // Create the client
    val mongoClient: MongoClient = if (args.isEmpty) MongoClient() else MongoClient(args.head)
    // get handle to "mydb" database
    val database: MongoDatabase = mongoClient.getDatabase("mydb")//.withCodecRegistry(codecRegistry)
    // get a handle to the "test" collection
    val collection: MongoCollection[Person] = database.getCollection("test")
    val totalItemsInCollection = for {
      a1 <- collection.insertOne(Person("Charles", "Number 1", "example1@email.com"))
      a2 <- collection.insertOne(Person("George", "Number 2", "example2@email.com"))
      a3 <- collection.insertOne(Person("Juan", "Number 3", "example3@email.com"))
      inCollection <- collection.countDocuments()
    } yield inCollection
    val res = Await.result(totalItemsInCollection.toFuture(),Duration(10, "s"))
    res.foreach(println)
    println(res.head)
    // coge la primera persona
    collection.find().subscribe(new Observer[Person] {
      override def onNext(result: Person): Unit = println("persona:" +result)
      override def onError(e: Throwable): Unit = e.printStackTrace()
      override def onComplete(): Unit = println("Terminado de Listar")
    })
    collection.drop().subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println("Borrado: "+result)
      override def onError(e: Throwable): Unit = e.printStackTrace()
      override def onComplete(): Unit = println("Borrado completado")
      Thread.sleep(1000)
    })

    Thread.sleep(1000)
  }

}
