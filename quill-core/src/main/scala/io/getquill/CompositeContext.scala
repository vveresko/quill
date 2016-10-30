package io.getquill

import io.getquill.context.Context
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.{ Context => MacroContext }

trait CompositeContext {
  this: Context[_, _] =>

  override def run[T](quoted: Quoted[T]): RunQuerySingleResult[T] = macro io.getquill.context.CompositeContextMacro.run
  override def run[T](quoted: Quoted[Query[T]]): RunQueryResult[T] = macro io.getquill.context.CompositeContextMacro.run
  override def run(quoted: Quoted[Action[_]]): RunActionResult = macro io.getquill.context.CompositeContextMacro.run
  override def run[T](quoted: Quoted[ActionReturning[_, T]]): RunActionReturningResult[T] = macro io.getquill.context.CompositeContextMacro.run
  override def run(quoted: Quoted[BatchAction[Action[_]]]): RunBatchActionResult = macro io.getquill.context.CompositeContextMacro.run
  override def run[T](quoted: Quoted[BatchAction[ActionReturning[_, T]]]): RunBatchActionReturningResult[T] = macro io.getquill.context.CompositeContextMacro.run
}

object CompositeContext {

  case class Element[T <: Context[_, _]](cond: () => Boolean, ctx: T)

  def when[T <: Context[_, _]](cond: => Boolean)(ctx: T): Element[T] = Element(() => cond, ctx)

  def apply[T <: Context[_, _]](elements: Element[T]*): T = macro context.CompositeContextMacro.apply[T]
}
