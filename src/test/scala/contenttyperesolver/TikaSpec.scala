package contenttyperesolver

import org.apache.tika.config.TikaConfig
import org.apache.tika.detect.Detector
import org.apache.tika.io.TikaInputStream
import org.apache.tika.metadata.Metadata
import org.apache.tika.mime.MediaType
import org.scalatest.{DiagrammedAssertions, FlatSpecLike, Matchers}

class TikaSpec extends FlatSpecLike with Matchers with DiagrammedAssertions {

  val tika               = new TikaConfig()
  val detector: Detector = tika.getDetector

  behavior of "Tika"

  it should "detect gif" in {
    val mediaType = detector.detect(TikaInputStream.get(getClass.getResourceAsStream("/gif")), new Metadata())
    assert(mediaType === MediaType.image("gif"))
  }

  it should "detect jpg" in {
    val mediaType = detector.detect(TikaInputStream.get(getClass.getResourceAsStream("/jpg")), new Metadata())
    assert(mediaType === MediaType.image("jpeg"))
  }

  it should "detect png" in {
    val mediaType = detector.detect(TikaInputStream.get(getClass.getResourceAsStream("/png")), new Metadata())
    assert(mediaType === MediaType.image("png"))
  }

}
