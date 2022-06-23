import org.mongodb.scala.{BulkWriteResult, Completed, Document, MongoClient, MongoCollection, MongoDatabase, Observable, Observer, SingleObservable, Subscription}
//filtros como equal
import org.mongodb.scala.model.Filters._
// tipos de ordenación
import org.mongodb.scala.model.Sorts._
//tipos de proyecciones (seleccionando campos)
import org.mongodb.scala.model.Projections._
//tipos de agregación como "filter"
import org.mongodb.scala.model.Aggregates._
// tipos de cambio en campo
import org.mongodb.scala.model.Updates._
// Cambiando Modelos
import org.mongodb.scala.model._
// Cambiando resultados
import org.mongodb.scala.result._

object OficinasMDB{

  def main(args: Array[String]): Unit = {

    // Conectando a la BBDD
    val client: MongoClient = MongoClient("mongodb://localhost:27017/test")

    //Obteniendo el listado de BBDD
    val databases=client.listDatabaseNames()
    println("Databases:")
    databases.subscribe(new Observer[String] {
      override def onNext(result: String): Unit = println("Database Name:" + result)

      override def onError(e: Throwable): Unit = e.printStackTrace()

      override def onComplete(): Unit = println("Completado")
    })
    // Ejemplos de https://mongodb.github.io/mongo-scala-driver/2.9/getting-started/installation-guide/
    //Seleccionando una BBDD
    val database: MongoDatabase = client.getDatabase("mydb")

    var collection: MongoCollection[Document] =
      database.getCollection("test")


    // Creando u Obteniendo la BBDD
    var collection2: MongoCollection[Document]= database.getCollection("mycoll")
    // insertando un documento
    val document: Document = Document("x" -> 1)
    val insertObservable: Observable[Completed] = collection2.insertOne(document)

    insertObservable.subscribe(new Observer[Completed] {

      override def onSubscribe(subscription: Subscription): Unit = subscription.request(1)

      override def onNext(result: Completed): Unit = println(s"onNext $result")

      override def onError(e: Throwable): Unit = println("Failed")

      override def onComplete(): Unit = println("Completed")
    })

    // insertando otro documento
    val doc: Document = Document(
      "name" -> "Oficinas",
      "type" -> "database",
      "count" -> 1,
      "active" -> true,
      "duration" -> 2.1,
      "info" -> Document(
        "address" -> "Example Street",
        "tlf" -> "Example Number",
        "createdDate" -> 01/01/2001,
        "sratus" -> true,
        "income" -> 10000)
    )

    collection2.insertOne(doc).subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = {
        println("Next Insert: "+ result)
      }

      override def onError(e: Throwable): Unit = e.printStackTrace()

      override def onComplete(): Unit = {
        println("Insert Complete")
        println("Documento insertado " + doc)
      }
    })

    println("Listado de Documentos")
    var listadoDocumentos:List[Document] = List[Document]()
    // Cogiendo todos los documentos
    collection2.find.subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = {
        println(result)
        listadoDocumentos = listadoDocumentos ::: List(result)
      }

      override def onError(e: Throwable): Unit = e.printStackTrace()

      override def onComplete(): Unit = {
        println("Listado de Documentos Completado")
        //println(listadoDocumentos)
        listadoDocumentos.foreach(println)
      }
    })
    // Cogiendo un documento
    collection.find.first().subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println(result)

      override def onError(e: Throwable): Unit = e.printStackTrace()

      override def onComplete(): Unit = println("Find Completado")
    })


    collection.find().first().subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println("Documento: "+result)

      override def onError(e: Throwable): Unit = e.printStackTrace()

      override def onComplete(): Unit = println("Find Terminado")
    })

    // Query Filters
    // now use a query to get 1 document out
    collection.find(equal("i", 71)).first().subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println("Documento: "+result)
      override def onError(e: Throwable): Unit = e.printStackTrace()

      override def onComplete(): Unit = println("Find Completado")
    })

    // Filtrado
    collection.find(gt("i", 50)).subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println("Documento: "+result)
      override def onError(e: Throwable): Unit = e.printStackTrace()
      override def onComplete(): Unit = println("Listado Impreso")
    })
    // Filtrado por rango
    collection.find(and(gt("i", 50), lte("i", 100))).subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println("Documento: "+result)
      override def onError(e: Throwable): Unit = e.printStackTrace()
      override def onComplete(): Unit = println("Listado Impreso")
    })

    // Ordenado
    collection.find(exists("i")).sort(descending("i")).first().subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println("Documento: "+result)
      override def onError(e: Throwable): Unit = e.printStackTrace()
      override def onComplete(): Unit = println("Listado Impreso")
    })

    // Proyección
    collection.find().projection(excludeId()).first().subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println("Documento: "+result)
      override def onError(e: Throwable): Unit = e.printStackTrace()
      override def onComplete(): Unit = println("Listado Impreso")
    })

    //Aggregación
    collection.aggregate(Seq(
      filter(gt("i", 0)),
      project(Document("""{ITimes10: {$multiply: ["$i", 10]}}"""))
    )).subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = println("Documento: "+result)
      override def onError(e: Throwable): Unit = e.printStackTrace()
      override def onComplete(): Unit = println("Listado Impreso")
    })

    // Update One


    // Delete One
    collection.deleteOne(equal("i", 110)).subscribe(new Observer[DeleteResult] {
      override def onNext(result: DeleteResult): Unit = println("Resultado Delete:" +result)

      override def onError(e: Throwable): Unit = e.printStackTrace()

      override def onComplete(): Unit = println("Documento Cambiado")
    })

    // Delete Many
    collection.deleteMany(gte("i", 100)).subscribe(new Observer[DeleteResult] {
      override def onNext(result: DeleteResult): Unit = println("Resultado Delete:" +result)

      override def onError(e: Throwable): Unit = e.printStackTrace()

      override def onComplete(): Unit = println("Documento Cambiado")
    })

    // Al ser asíncrono hay que esperar
    Thread.sleep(1000)
  }
}

