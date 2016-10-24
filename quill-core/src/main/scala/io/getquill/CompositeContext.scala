package io.getquill

import io.getquill.context.Context
import scala.language.experimental.macros

object CompositeContext {

  case class Element[T <: Context[_, _]](cond: () => Boolean, ctx: T)

  def when[T <: Context[_, _]](cond: => Boolean)(ctx: T): Element[T] = Element(() => cond, ctx)

  def apply[T <: Context[_, _]](elements: Element[T]*): T = macro context.CompositeContextMacro.apply[T]
}
