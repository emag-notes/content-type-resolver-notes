package contenttyperesolver

import java.io.InputStream
import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentType, ContentTypes}
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, StreamConverters}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import org.apache.tika.config.TikaConfig
import org.apache.tika.detect.Detector
import org.apache.tika.io.TikaInputStream
import org.apache.tika.metadata.Metadata
import org.apache.tika.mime.MediaType
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}

class StreamWithTikaSpec extends BaseSpec {

  val tika: TikaConfig   = new TikaConfig()
  val detector: Detector = tika.getDetector

  implicit val system: ActorSystem             = ActorSystem()
  implicit val ec: ExecutionContext            = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val fileName    = "tika.png"
  val destination = Paths.get(s"target/${fileName}_meta.txt")

  val wsClient = StandaloneAhcWSClient()

  case class ImageWithMeta(contentType: String, fileName: String, in: InputStream)

  behavior of "Akka Stream with Tika"

  it should "detect content-type with response body" in {
    wsClient
      .url(s"http://tika.apache.org/$fileName")
      .stream()
      .flatMap { resp =>
        val ct: ContentType = resp
          .header("Content-Type")
          .flatMap(str => ContentType.parse(str).toOption)
          .getOrElse {
            val in        = resp.bodyAsBytes.iterator.asInputStream
            val mediaType = detector.detect(TikaInputStream.get(in), new Metadata())
            println(s"????? $mediaType")
            ContentType.parse(mediaType.toString).getOrElse(ContentTypes.NoContentType)
          }
        resp.bodyAsSource.runForeach(_ => println(s"###### $ct"))
      }
  }

  it should "detect content-type with Flow" in {
    val byteStringToInputStream: Flow[ByteString, InputStream, NotUsed] =
      Flow[ByteString].map(bs => bs.iterator.asInputStream)

    val inputStreamToImageWithMeta: Flow[InputStream, ImageWithMeta, NotUsed] = Flow[InputStream].map { in =>
      val mediaType: MediaType = detector.detect(TikaInputStream.get(in), new Metadata())
      ImageWithMeta(mediaType.toString, fileName, in)
    }

    val serialize: Flow[ImageWithMeta, ByteString, NotUsed] = Flow[ImageWithMeta].map(im => ByteString(im.toString))

    val flow: Flow[ByteString, ByteString, NotUsed] =
      byteStringToInputStream
        .via(inputStreamToImageWithMeta)
        .via(serialize)

    val sink: Sink[ByteString, Future[IOResult]] = FileIO.toPath(destination)

    wsClient
      .url(s"http://tika.apache.org/$fileName")
      .stream()
      .flatMap { resp =>
        resp.bodyAsSource.via(flow).toMat(sink)(Keep.right).run()
      }
  }

  it should "detect content-type with StreamConverters" in {
    val sink = StreamConverters.asInputStream().mapMaterializedValue { in =>
      val mediaType: MediaType = detector.detect(TikaInputStream.get(in), new Metadata())
      Future { println(ImageWithMeta(mediaType.toString, fileName, in)) }
    }

    wsClient
      .url(s"http://tika.apache.org/$fileName")
      .stream()
      .flatMap { resp =>
        resp.bodyAsSource.toMat(sink)(Keep.right).run()
      }
  }

}
