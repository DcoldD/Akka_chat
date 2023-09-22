package com.example.actor

import akka.actor.typed.ActorSystem
import com.example.actor.RootActor.RootEvent
import com.example.controller.ChatController
import com.typesafe.config.ConfigFactory

object ChatSystem {
  def startup(receivedController: ChatController, port: Int, nickName: String): ActorSystem[RootEvent] = {
    val config = ConfigFactory
      .parseString(
        s"""
       akka.remote.artery.canonical.port=$port
       """)
      .withFallback(ConfigFactory.load())
    ActorSystem(RootActor(receivedController, port, nickName), "ClusterSystem", config)
  }
}
