package io.getquill.context

import io.getquill.Spec
import io.getquill._
import io.getquill.idiom.Idiom

class CompositeContextSpec extends Spec {
  
  val c = CompositeContext[MirrorContext[Idiom, NamingStrategy]](CompositeContext.when(true)(new MirrorContext[MirrorIdiom, SnakeCase]), CompositeContext.when(true)(new MirrorContext[MirrorIdiom, CamelCase]))
  
  import c._
  
  case class Person(personName: String)
  c.run(query[Person])
  
}