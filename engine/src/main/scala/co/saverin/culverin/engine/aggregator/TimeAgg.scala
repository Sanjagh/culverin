package co.saverin.culverin.engine.aggregator

import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.stage.GraphStage
import scala.collection.immutable.Queue
import scala.concurrent.duration._

/**
 * Aggregates on last 'N' elements of the stream.
 * Note that this is a Hot stage, slow pulls on outlet won't slow down event consumption
 * Note that this stage calculates the aggregation on it's buffer (n * B) on every pull, thus 'timeSpan' should be small (relatvie to events per time unit).
 *
 * @param n num of elements that are buffered
 * @param aggregate aggregation operation to sum-up inputs
 */
class TimeAgg[T](timeSpan: FiniteDuration, aggregate: (Queue[T]) => T) extends GraphStage[FlowShape[T, T]] {
  private val span: Long = timeSpan.toMillis

  val in = Inlet[T]("Input")
  val out = Outlet[T]("Output")

  override val shape = FlowShape.of(in, out)

  override def createLogic(attr: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

    private var q = Queue[T]()
    private var timeQ = Queue[Long]()

    private def cleanup() {}

    setHandler(in, new InHandler {
      override def onPush(): Unit = {
        cleanup()
        val b = grab(in)
        timeQ :+ System.currentTimeMillis()
        q :+ b

        pull(in)
      }
    })

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        cleanup()
        push(out, aggregate(q))

      }
    })
  }
}
