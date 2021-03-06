package edu.colorado.phet.simsharinganalysis.scripts.utah_november_2011_ii

import java.io.File
import io.Source
import collection.mutable.ArrayBuffer

/**
 * Goal of this analysis:
 * For the Molecule Polarity sim data (from the Utah study last November). I’d like to do two types of analysis:
 * 1)  What % of clickable/moveable options did each group interact with during the “play only” time? (this would include clicking on any tool/tab, and moving a moveable object.)
 * 2)  A histogram (representing all student groups) showing the amount of new moveable objects/tools clicked on over the “play only” time.
 */
case class Arg(key: String, value: String)

//A unique component or user action for counting them
case class UniqueComponent(tab: String, component: String, name: String) {
  override def toString = tab + ": " + component + ( if ( name == "" ) "" else ": " + name )
}

case class Entry(localTime: Long, serverTime: Long, component: String, action: String, args: List[Arg]) {
  def text = args.find(_.key == "text").map(_.value)

  def apply(key: String) = {
    val matched = args.filter(_.key == key)
    if ( matched.length == 1 ) {
      matched(0).value
    } else {
      ""
      //      throw new RuntimeException("No match found or maybe multiple matches found: matched="+matched)
    }
  }
}

case class Log(file: File, machineID: String, sessionID: String, serverTime: Long, entries: List[Entry]) {
  def id = entries(0).args.find(_.key == "id").map(_.value).getOrElse("")

  def project = entries(0).args.find(_.key == "project").map(_.value).getOrElse("")
}

object MoleculePolarityAnalysisAugust2012 {}

case class State(tab: String)

case class Element(start: State, entry: Entry, end: State)

object NewParser {
  def readText(f: File) = {
    val source = Source.fromFile(f)
    val text = source.mkString
    source.close()
    text
  }

  def afterEquals(s: String) = s.split('=').last.trim

  def parseFile(file: File, text: String) = {
    val lines = text.split('\n').map(_.trim)
    val serverStartTime = afterEquals(lines(2)).toLong
    val localStartTime = lines(3).split('\t').apply(0).trim.toLong
    if ( file.getName.contains("urrpk0p2") ) {
      println("sst = " + serverStartTime + ", localstarttime=" + localStartTime)
    }
    Log(file, afterEquals(lines(0)), afterEquals(lines(1)), serverStartTime, lines.slice(3, lines.length).map(s => toEntry(s, serverStartTime, localStartTime)).toList)
  }

  def parseElement(s: String) = {
    val parsed = s.split('=').map(_.trim)
    if ( parsed.length == 2 )
      Arg(parsed(0), parsed(1))
    else
      Arg(parsed(0), "")
  }

  def toEntry(s: String, serverStartTime: Long, localStartTime: Long) = {
    val elements = s.split('\t').map(_.trim)
    val remainingElements = elements.slice(3, elements.length)
    val localTime = elements(0).toLong

    //    to get the time for any event in any log
    //    server time on line N = original server time on line 3 - local time on line 4 + local time on line N
    //    correct because line 4 happens exactly when server time is reported

    val serverTime = serverStartTime - localStartTime + localTime
    Entry(localTime, serverTime, elements(1), elements(2), remainingElements.map(parseElement).toList)
  }

  def minutesToMilliseconds(minutes: Double) = ( minutes * 60000.0 ).toLong

  def getUsedComponents(elements: Seq[Element], filter: Entry => Boolean) = {
    val allowedElements = elements.filter(element => filter(element.entry))
    val textComponents = allowedElements.map(element => {
      val text = element.entry match {
        case e: Entry if e.text.isDefined => e.text.get
        case e: Entry if e.component == "mouse" && e("component") == "jmolViewerNode" => e("component") + ":" + e("currentMolecule")
        case e: Entry if e.component == "buttonNode" => e("actionCommand")
        case e: Entry if e.component == "comboBoxItem" => "someComboItem"
        case e: Entry if e.component == "mouse" && e("atom") != "" && e.action == "startDrag" => "electronegativity-slider-dragged:" + e("atom")
        case e: Entry if e.component == "mouse" && e("atom") != "" && e.action == "endDrag" => "electronegativity-slider-dragged:" + e("atom")
        case _ => ""
      }

      //For the count of rotating molecules, can you make any interaction that is 'moving molecule' just count once…like 'dragged A' counts once, but later 'dragged B' or 'dragged C' doesn't count.
      UniqueComponent(element.start.tab, element.entry.component, text)
    }).toSet.toList

    textComponents.filter(p => p.component != "tab" &&
                               p.component != "system" &&
                               p.component != "menu" &&
                               p.component != "menuItem" &&
                               p.toString != "Three Atoms: checkBox: Molecular Dipole" &&
                               p.toString != "Real Molecules: checkBox: Atom Labels" &&
                               p.toString != "Real Molecules: radioButton: none" &&
                               p.component != "bond" &&
                               !p.toString.contains("jmolViewerNode") &&
                               p.toString != "Two Atoms: checkBox: Bond Dipole" &&
                               p.component != "draggingState" &&
                               p.toString != "Two Atoms: radioButton: none" &&
                               p.toString != "Two Atoms: radioButton: off" &&
                               p.component != "window" &&
                               p.toString != "Three Atoms: mouse" &&
                               p.toString != "Two Atoms: mouse" &&
                               p.toString != "Three Atoms: radioButton: off" &&
                               p.toString != "Real Molecules: buttonNode: Reset All" &&
                               p.toString != "Three Atoms: buttonNode: Reset All" &&
                               p.toString != "Two Atoms: buttonNode: Reset All"

                               //Uncomment this line to make a plot for just one tab
                               && p.toString.startsWith(tabNames(2))
    )
  }

  val tabNames = "Two Atoms" :: "Three Atoms" :: "Real Molecules" :: Nil

  def process(state: State, entry: Entry) = {
    if ( entry.component == "tab" && entry.action == "pressed" )
      State(entry.text.get)
    else
      state
  }

  def getStates(log: Log) = {
    val states = new ArrayBuffer[Element]
    var startState = State(tabNames(0))
    for ( e <- log.entries ) {
      val newState = process(startState, e)
      states += Element(startState, e, newState)
      startState = newState
    }
    states.toList
  }

  def main(args: Array[String]) {
    val file = new File("C:\\Users\\Sam\\Desktop\\phet\\studies\\molecule-polarity")
    val logs = file.listFiles.map(file => (file, readText(file))).map(tuple => parseFile(tuple._1, tuple._2)).filter(_.project == "molecule-polarity")
    //.filter(_.id == "20")
    //    logs.foreach(println)
    //    logs.map(_.id).foreach(println)
    //    logs.map(_.entries(0)).foreach(println)

    val group2 = logs.filter(_.id == "2")
    //assert(group2.length == 1)
    //For the recording that is linked with audio #2, the play time was from (min:sec) 0:00-9:30 from the start.
    //    val startPlayTime = group2.apply(0).serverTime
    //    println("file for for group 2 = "+group2.apply(0).file)
    //    println(startPlayTime)

    //5 seconds before activity from group 10
    val startPlayTime = 1320867917969L

    //Time on the server
    //    val elapsedPlayTime: Long = minutesToMilliseconds(9.5)
    //    val endPlayTime: Long = startPlayTime + elapsedPlayTime

    //right before everyone starts changing to tab 1 for the activity
    val elapsed = 609600L
    println("elapsed minutes = " + elapsed / 1000.0 / 60.0)
    val endPlayTime: Long = 1320867957969L + 609600L

    //    println(endPlayTime)

    //Todo could flat map this probably
    val allComponents = new ArrayBuffer[UniqueComponent]
    for ( log <- logs ) {
      val used = getUsedComponents(getStates(log), e => true)
      allComponents ++= used
    }

    println("[==============All Components for selected tabs===============]")
    val componentSet = allComponents.toSet
    componentSet.map(_.toString).toList.sorted.foreach(println)

    println()
    println("Total items possible: " + componentSet.size)
    println()

    println("group\tnumber missed during play only time\tcomponents missed during\"play only\" time")
    for ( log <- logs.sortBy(_.id) ) {
      val elements = getStates(log)
      val entriesUsedInPlayTime = getUsedComponents(elements, e => e.serverTime >= startPlayTime && e.serverTime <= endPlayTime)
      val entriesUsedAnyTime = getUsedComponents(elements, e => true)
      //      println(log.id + "\t" + formatter.format(entriesUsedInPlayTime.length.toDouble / componentSet.size.toDouble * 100.0) + "%")
      val missedComponents = allComponents.distinct -- entriesUsedInPlayTime.distinct
      println(log.id + "\t" + missedComponents.length + "\t" + missedComponents.mkString(", "))
    }
    println()

    //Print line plots.  How many controls each team used as a function of time
    println("[========================Line plots of how many controls each team used as a function of time===============]")
    val columns = startPlayTime to endPlayTime by 5000

    //    println("start time = " + startPlayTime)
    //    println("end time = " + endPlayTime)
    //    println("first few columns = " + columns.take(3))

    //    def timeToServerTime(seconds: Long) = {
    //      //      new Date(seconds*1000 + startPlayTime)
    //      seconds * 1000L + startPlayTime
    //    }

    print("time(millis)\t" + columns.mkString("\t") + "\n")
    for ( log <- logs.sortBy(_.id) ) {

      print(log.id + "\t" + columns.map(endTimeForHistogram => {
        val elements = getStates(log)
        val entriesUsedInPlayTime = getUsedComponents(elements, e =>
          e.serverTime >= startPlayTime &&
          e.serverTime <= endTimeForHistogram)
        entriesUsedInPlayTime.length.toDouble / componentSet.size.toDouble * 100.0
      }).mkString("\t"))
      println()
    }

    println("[========================For each computer, what actions were not used for each tab===============]")
    //EM: For each computer, can you list out what actions were not used, for each tab?
    println("Session\tActions Not Used")
    for ( log <- logs.sortBy(_.id) ) {
      val unusedComponents = componentSet -- getUsedComponents(getStates(log), e => e.serverTime >= startPlayTime && e.serverTime <= endPlayTime)
      println(log.id + "\t" + unusedComponents.toList.sortBy(_.toString).mkString(", "))
    }

    //tabulate the number of mouse clicks the group with "id = 23" made during the 10 minutes of sim use we've specified previously.
    //Since the focus of the Polarity paper is on the amount of features students use (the representations and tools students interacted with) I want to make sure the reader doesn't confuse the number of features used with the number of actual clicks. To do that, I'd like to give the number of clicks an example student group made, and compare that with the number of features used.
    println("tabulate the number of events")
    for ( log <- logs.sortBy(_.id) ) {
      val entries = log.entries.filter(e => e.component != "window" && e.component != "system" && e.serverTime >= startPlayTime && e.serverTime <= endPlayTime)
      println(log.id + "\t" + entries.length + "\t" + entries.map(entry => "\"" + entry.component + " " + entry.action + "\"").mkString("\t"))
    }
  }
}
