package io.getquill.context

import io.getquill.Spec
import io.getquill._

class CompositeContextSpec extends Spec {
  
  val c = CompositeContext(CompositeContext.when(true)(new MirrorContext[MirrorIdiom, SnakeCase]))
  
  import c._
  
  case class Person(personName: String)
  c.run(query[Person])
  
}