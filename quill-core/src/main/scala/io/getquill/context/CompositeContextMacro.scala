package io.getquill.context

import io.getquill.util.Messages._
import scala.reflect.macros.whitebox.{ Context => MacroContext }

class CompositeContextMacro(val c: MacroContext) {
  import c.universe._

  def run(quoted: Tree): Tree = {
    val elems =
      c.prefix.actualType.member(TermName("elements")).typeSignature.decls.toList
    val calls =
      elems.map { e =>
        cq"""
          _ if($e.cond()) => $e.run($quoted)
        """
      }
    q"""
      () match {
        case ..$calls
        case _ =>
          throw new IllegalStateException("Can't find an enabled context.")
      }
    """
  }

  def apply[T <: Context[_, _]](elements: Tree*)(implicit t: WeakTypeTag[T]): Tree = {
    val vals =
      elements.zipWithIndex.map {
        case (e, i) => q"val ${TermName(s"_$i")} = $e"
      }
    q"""
      import scala.language.experimental.macros
      new $t {
        object elements {
          ..$vals
        }
        override def run[T](quoted: Quoted[T]): RunQuerySingleResult[T] = macro io.getquill.context.CompositeContextMacro.run
      }
    """
  }
}
