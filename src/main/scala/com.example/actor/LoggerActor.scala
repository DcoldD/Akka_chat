package com.example.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.scaladsl.Behaviors
import com.example.actor.UserActor.UsersUpdate

object LoggerActor {
  def apply(userActor: ActorRef[UserActor.Event]): Behavior[Receptionist.Listing] = Behaviors.setup({ context =>
    context.system.receptionist ! Receptionist.Subscribe(UserActor.userServiceKey, context.self)
    Behaviors.receiveMessagePartial[Receptionist.Listing] {
      case UserActor.userServiceKey.Listing(listings) =>
        userActor ! UsersUpdate(listings)
        Behaviors.same
    }
  })
}
