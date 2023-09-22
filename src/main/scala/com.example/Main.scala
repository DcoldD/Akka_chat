package com.example

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.image.Image
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class Main extends Application{
  override def start(stage: Stage): Unit = {
    val loader: Parent = FXMLLoader.load(getClass.getResource("/FXML/login_ui.fxml"))
    stage.setScene(new Scene(loader))
    stage.setResizable(false)
    stage.getIcons.add(new Image("Pictures/koi.png"))
    stage.show()
  }
}