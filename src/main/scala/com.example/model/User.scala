package com.example.model

import akka.actor.typed.ActorRef
import com.example.actor.UserActor.Event

class User(conNickName: String, conPort: Int, conRefOnActor: ActorRef[Event]){
  private val nickName: String = conNickName
  private val port: Int = conPort
  private val refOnActor:ActorRef[Event] = conRefOnActor

  def getNickName: String = nickName
  def getRefOnActor: ActorRef[Event] =refOnActor
  def canEqual(other: Any): Boolean = other.isInstanceOf[User]
  override def equals(other: Any): Boolean = other match {
    case that: User =>
      (that canEqual this) &&
        nickName == that.nickName &&
        port == that.port &&
        refOnActor == that.refOnActor
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(nickName, port, refOnActor)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"$nickName"
}
