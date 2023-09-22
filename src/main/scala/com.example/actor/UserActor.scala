package com.example.actor

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.example.actor.RootActor.RootEvent
import com.example.controller.ChatController
import com.example.model.User
import javafx.application.Platform

object UserActor {
  var currentUser: User = _
  private var userSet: Set[ActorRef[UserActor.Event]] = _
  val userServiceKey: ServiceKey[Event] = ServiceKey[UserActor.Event]("User")

  trait Event extends RootEvent

  case class UsersUpdate(newUsers: Set[ActorRef[UserActor.Event]]) extends Event

  case class PostMessage(sender: String, recipient: String = "Group", message: String) extends Event

  case class PublicMsg(sender: String, message: String) extends Event

  private case class NewUser(user: User) extends Event

  case class RemoveUser() extends Event

  private case class DeleteUser(user: User) extends Event

  def apply(receivedController: ChatController, port: Int, nickName: String): Behavior[Event] = {
    Behaviors.setup { context =>
      currentUser = new User(nickName, port, context.self)
      receivedController.setCurrentUser(currentUser)
      context.system.receptionist ! Receptionist.Register(userServiceKey, context.self)
      Behaviors.receiveMessage {
        case UsersUpdate(newUsers) =>
          userSet = newUsers
          userSet.foreach(actor => if (actor != context.self) actor ! NewUser(new User(nickName, port, context.self)))
          Behaviors.same
        case NewUser(user) =>
          Platform.runLater(() =>receivedController.newUser(user))
          Behaviors.same
        case PostMessage(from, recipient, message) =>
          receivedController.setMessage(from, recipient, message)
          Behaviors.same
        case PublicMsg(from, message) =>
          userSet.foreach(actor => actor ! PostMessage(sender = from, message = message))
          Behaviors.same
        case RemoveUser() =>
          userSet.foreach(actor => actor ! DeleteUser(currentUser))
          Behaviors.same
        case DeleteUser(user) =>
          Platform.runLater(()=>receivedController.exitUser(user))
          Behaviors.same
      }
    }
  }
}

