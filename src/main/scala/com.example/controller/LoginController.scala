package com.example.controller

import com.example.actor.ChatSystem
import com.example.actor.RootActor.UserExit
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Scene
import javafx.scene.control.{Button, TextField}
import javafx.scene.image.Image
import javafx.scene.text.Text
import javafx.stage.{Stage, WindowEvent}

import java.util.Objects


class LoginController {
  @FXML var loginButton: Button = _
  @FXML var nameInput: TextField = _
  @FXML var portInput: TextField = _
  @FXML var warningText: Text = _

  @FXML
  def initialize(): Unit = {
    loginButton.setOnMouseEntered(_ => {
      loginButton.setStyle("-fx-background-color: #643a7e")
    })

    loginButton.setOnMouseExited(_ => {
      loginButton.setStyle("-fx-background-color: #806491")
    })
  }

  @FXML
  def buttonClicked(actionEvent: ActionEvent): Unit = {
    val nickName = nameInput.getText.trim
    val port = portInput.getText.trim
    if (nickName.nonEmpty && port.nonEmpty) {
      loginButton.getScene.getWindow.hide()
      val chatController = new ChatController()
      val system = ChatSystem.startup(chatController, port.toInt, nickName)
      val loader: FXMLLoader = new FXMLLoader(Objects.requireNonNull(getClass.getResource("/FXML/chat_ui.fxml")))
      loader.setController(chatController)
      val stage = new Stage()
      stage.setScene(new Scene(loader.load()))
      stage.setResizable(false)
      stage.getIcons.add(new Image("Pictures/koi.png"))
      stage.show()

      stage.setOnCloseRequest((_: WindowEvent) => {
        system ! UserExit()
        system.terminate()
      })
    } else {
      warningText.setVisible(true)
    }
  }
}