package eu.timepit.refined.api

trait Min[FTP] { def min: FTP }
object Min {
  def apply[FTP](implicit ev: Min[FTP]): Min[FTP] = ev
  def instance[FTP](m: FTP): Min[FTP] = new Min[FTP] { def min: FTP = m }
}

trait Max[FTP] { def max: FTP }
object Max {
  def apply[FTP](implicit ev: Max[FTP]): Max[FTP] = ev
  def instance[FTP](m: FTP): Max[FTP] = new Max[FTP] { def max: FTP = m }
}
