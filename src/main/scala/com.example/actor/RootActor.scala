package com.example.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.typed.Cluster
import com.example.actor.UserActor.RemoveUser
import com.example.controller.ChatController

object RootActor {
  trait RootEvent
  case class UserExit() extends RootEvent
  def apply(receivedController: ChatController, port: Int, nickName: String): Behavior[RootEvent] = Behaviors.setup[RootEvent]({ context =>
    val userActor = context.spawn(UserActor(receivedController,port,nickName), "user")
    context.spawn(LoggerActor(userActor), "logger")
    Cluster(context.system)
    Behaviors.receiveMessage{
      case UserExit() =>
        userActor ! RemoveUser()
        Behaviors.empty
    }
  })
}



