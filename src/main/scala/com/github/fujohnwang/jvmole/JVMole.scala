package com.github.fujohnwang.jvmole

import commands._
import sbt._
import java.io.File

final class JVMole extends xsbti.AppMain with ProjectInfo with WelcomeCommand with VirtualMachineCommands with JMXCommands {
  val initialLogging = initialGlobalLogging

  def run(configuration: xsbti.AppConfiguration): xsbti.MainResult = {
    MainLoop.runLogged(initialState(configuration))
  }

  def initialState(configuration: xsbti.AppConfiguration): State = {
    val commandDefinitions = listMBeans +: execMBeanMethod +: beanDesc +: setAttr +: listVirtualMachines +: attach +: detach +: welcome +: BasicCommands.allBasicCommands
    val commandsToRun = Seq("welcome", "iflast shell")
    State(configuration, commandDefinitions, Set.empty, None, commandsToRun, State.newHistory, AttributeMap.empty, initialLogging, State.Continue)
  }

  def initialGlobalLogging: GlobalLogging = {
    var logFile = new File("jvmole.log")
    if (!logFile.exists()) {
      if (!logFile.createNewFile()) logFile = File.createTempFile("jvmole", "log")
    }
    GlobalLogging.initial(MainLogging.globalDefault _, logFile)
  }
}