package co.saverin.culverin.engine.aggregator

import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.stage.GraphStage
import scala.collection.immutable.Queue


/**
 * Aggregates on last 'N' elements of the stream.
 * Note that this is a classic (cold) stage, It waits for a pull on it's output to pull on it's input.
 * Note that this stage calculates the aggregation on it's buffer (n * B) on every pull, thus 'length' should be small.
 *
 * @param n num of elements that are buffered
 * @param aggregate aggregation operation to sum-up inputs
 */
class LastNAgg[B](n: Int,aggregate: (Queue[B]) => B) extends GraphStage[FlowShape[B, B]] {

  val in = Inlet[B]("Input")
  val out = Outlet[B]("Output")

  override val shape = FlowShape.of(in, out)

  override def createLogic(attr: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

    var q = Queue[B]()
    var size = 0

    setHandler(in, new InHandler {
      override def onPush(): Unit = {
        val b = grab(in)
        q = if (size >= n)
          q.tail :+ b
        else
          q :+ b
      }
    })

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        push(out, aggregate(q))
        pull(in)
      }
    })
  }
}

/**
 * Hot aggregate on last 'N' elements of the stream.
 * Pull requests are blocked until a change on aggregated value
 * Note that this is a hot aggregate, slow outlet won't slow down event consumption.
 * Also note that the aggregate function is executed on each push on all elements, Do not use for large 'N's.
 */
class HotLastNAgg[B](n: Int, aggregate: (Queue[B]) => B, defaultB: B) extends GraphStage[FlowShape[B, B]] {

  val in = Inlet[B]("Input")
  val out = Outlet[B]("HotOutput")

  override val shape = FlowShape.of(in, out)

  override def createLogic(attr: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

    private var q = Queue[B]()
    private var size = 0
    private var current = defaultB
    private var needsPush = false
    private var changed = false

    override def preStart(): Unit = pull(in)

    setHandler(in, new InHandler {
      override def onPush(): Unit = {
        val b = grab(in)
        q = if (size >= n)
          q.tail :+ b
        else
          q :+ b

        val newCurrent = aggregate(q)

        if (newCurrent != current) {
          if (needsPush) {
            needsPush = false
            changed = false
            push(out, newCurrent)
          } else {
            changed = true
          }
        }
        current = newCurrent

        pull(in)
      }
    })

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        if (changed) {
          push(out, current)
          changed = false
        } else {
          needsPush = true
        }
      }
    })
  }
}



/**
 * Aggregates on last 'N' elements of the stream.
 * Note that this is a classic (cold) stage, It waits for a pull on it's output to pull on it's input.
 * Note that this stage calculates the aggregation on it's buffer (n * B) on every pull, thus 'length' should be small.
 *
 * @param n num of elements that are buffered
 * @param extract how to extract metric from input object
 * @param aggregate aggregation operation to sum-up inputs
 *
 */
class BoundedLastNAgg[A, B](n: Int, extract: A => B, aggregate: (Queue[B]) => B) extends GraphStage[FlowShape[A, B]] {

  val in = Inlet[A]("Input")
  val out = Outlet[B]("Output")

  override val shape = FlowShape.of(in, out)

  override def createLogic(attr: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

    var q = Queue[B]()
    var size = 0

    setHandler(in, new InHandler {
      override def onPush(): Unit = {
        val a = grab(in)
        val b = extract(a)
        q = if (size >= n)
          q.tail :+ b
        else
          q :+ b
      }
    })

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        push(out, aggregate(q))
        pull(in)
      }
    })
  }
}

/**
 * Hot aggregate on last 'N' elements of the stream.
 * Pull requests are blocked until a change on aggregated value
 * Note that this is a hot aggregate, slow outlet won't slow down event consumption.
 * Also note that the aggregate function is executed on each push on all elements, Do not use for large 'N's.
 */
class BoundedHotLastNAgg[A, B](n: Int, extract: A => B, aggregate: (Queue[B]) => B, defaultB: B) extends GraphStage[FlowShape[A, B]] {

  val in = Inlet[A]("Input")
  val out = Outlet[B]("HotOutput")

  override val shape = FlowShape.of(in, out)

  override def createLogic(attr: Attributes): GraphStageLogic = new GraphStageLogic(shape) {

    private var q = Queue[B]()
    private var size = 0
    private var current = defaultB
    private var needsPush = false
    private var changed = false

    override def preStart(): Unit = pull(in)

    setHandler(in, new InHandler {
      override def onPush(): Unit = {
        val a = grab(in)
        val b = extract(a)
        q = if (size >= n)
          q.tail :+ b
        else
          q :+ b

        val newCurrent = aggregate(q)

        if (newCurrent != current) {
          if (needsPush) {
            needsPush = false
            changed = false
            push(out, newCurrent)
          } else {
            changed = true
          }
        }
        current = newCurrent

        pull(in)
      }
    })

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        if (changed) {
          push(out, current)
          changed = false
        } else {
          needsPush = true
        }
      }
    })
  }
}

