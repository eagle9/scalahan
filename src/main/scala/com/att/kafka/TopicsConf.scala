package com.att.kafka

import java.util
import java.util.ArrayList
import java.util.List

import scala.io.Source
import java.util.TreeMap
import scala.collection.JavaConverters._

object TopicsConf {
  val PREFIX = "PROD."
  def getGroups(): TreeMap[String,List[String]] = {
    val filename = "conf/m1-topics.txt"
    val topic1Pattern = "T[0-9]{2}17".r
    val topic2Pattern = "U[0-9]{2}22".r
    val map = new TreeMap[String,List[String]]
    for (line <- Source.fromFile(filename).getLines) {
      val match1 = topic1Pattern.findFirstIn(line)
      val match2 = topic2Pattern.findFirstIn(line)
      if (match1.isDefined) {
        val topic = PREFIX + match1.head
        val group = "group" + match1.head
        //println(group,topic)
        val list = new ArrayList[String]()
        list.add(topic)
        map.put(group, list)

      }
      if (match2.isDefined) {
        val group = "groupUxx22"
        val topic = PREFIX + match2.head
        //println(group,topic)
        val list = map.getOrDefault(group, new util.ArrayList[String]())
        list.add(topic)
        map.put(group, list)
      }

    }
    return map;
  }
  def main(args: Array[String]): Unit = {
    val map = getGroups()
    for (kv <- map.asScala) {
      println(kv._1, kv._2)

    }
  }

}
