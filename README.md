Jun 15 2020
add java class, Hello, then more advanced classes FlexibleMirrorMaker and Uni, with maven dependencies
add deps to sbt.build, sbt package command line works, 
intellij still complains 
close proect and reopen, import starts and kafka client not found error goes away
glad scala and java can be used inside one project!!!

scala core course by han

restructured by shaun with intellij ce sbt

object for singleton, class for blue prints
accompany class
4 ways to create class:
1) new ClassA
2) extends with TraitA
3) apply
4) ???

case class or object

partial function 
  case xxx => ...
  useful in actor

val for immutable, var for mutable
  val x: Int

define a function
def fun(x:Int): Int = {
	2*x
}
no primitive data types, all objects

trait --- java interface

class A extends Base with Trait1
mixin, oc principle

implicit def f(x: Double): Int = {
	x.toInt
}
enrich class functions, oc principle
implicit value, default value, passing value --- must have one
