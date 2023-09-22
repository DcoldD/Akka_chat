package com.example.controller

import com.example.actor.UserActor.{PostMessage, PublicMsg}
import com.example.model.User
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, ListView, TextArea, TextField}
import javafx.scene.input.{KeyCode, KeyEvent}

import java.net.URL
import java.util.ResourceBundle


class ChatController extends Initializable{

  @FXML var listOfUsers: ListView[User] = new ListView[User]()
  @FXML var msgInput: TextField = _
  @FXML var msgList: TextArea = _
  @FXML var sendButton: Button = _

  var currentUser: User = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    sendButton.setOnMouseEntered(_ => {
      sendButton.setStyle("-fx-background-color: #358ab5")
    })

    sendButton.setOnMouseExited(_ => {
      sendButton.setStyle("-fx-background-color: #1d79a8")
    })

    sendButton.setOnAction(_ => {
      sendMessage()
    })

    msgInput.setOnKeyPressed((t: KeyEvent) => {
      if (t.getCode.equals(KeyCode.ENTER)) sendMessage()
    })
  }

  def setCurrentUser(currentUser: User): Boolean = {
    this.currentUser = currentUser
    listOfUsers.getItems.add(currentUser)
  }

  private def sendMessage(): Unit = {
    val message = msgInput.getText.trim
    if (message.nonEmpty) {
      if (message.startsWith("@")) {
        val splitMessage = message.split("@")
        val recipient = splitMessage(1)
        val messageText = splitMessage(2)
        var userRecipient: User = null
        listOfUsers.getItems.forEach(friend => if (friend.getNickName == recipient) userRecipient = friend)
        if (userRecipient != null) {
          userRecipient.getRefOnActor ! PostMessage(currentUser.getNickName, userRecipient.getNickName, messageText)
          currentUser.getRefOnActor ! PostMessage(currentUser.getNickName, userRecipient.getNickName, messageText)
        } else {
          msgList.appendText(s"User with $recipient nickname doesn't exist, message not send\n")
        }
      } else {
        currentUser.getRefOnActor ! PublicMsg(currentUser.getNickName, message)
      }
    }
    msgInput.clear()
  }

  def newUser(user: User): Unit = {
    var check: Boolean = true
    listOfUsers.getItems.forEach(friend => if (friend.equals(user)) check = false)
    if (check) {
      listOfUsers.getItems.add(user)
    }
  }

  def setMessage(from: String, recipient: String, message: String): Unit = {
    recipient match {
      case "Group" =>
        msgList.appendText(s"$from: $message\n")
      case _ =>
        msgList.appendText(s"Private message from $from to $recipient: $message\n")
    }
  }

  def exitUser(user: User): Unit = {
    listOfUsers.getItems.remove(user)
  }
}