package eu.timepit.refined.api

trait InhabitantsOf[FTP] {

  def all: Stream[FTP]

}

object InhabitantsOf {

  def apply[FTP](implicit ev: InhabitantsOf[FTP]): InhabitantsOf[FTP] = ev

}
