package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

	@FXML
	Button btnStartPomorodo;
	@FXML
	Button btnSeeGraph;
	@FXML
	Button btnSettings;
	
	/**
	 * This class will take you to the home screen of the pomorodo window
	 * @param event
	 */
	public void btnStartPomorodo(ActionEvent event){
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("StartPomorodo.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(loader);
		scene.getStylesheets().add(getClass().getResource("/style/startPomorodo.css").toExternalForm());
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setAlwaysOnTop(true);
		stage.setMinWidth(250.0);
		stage.setMinHeight(200.0);
		stage.setScene(scene);
		stage.show();
	}
	
	public void btnSeeGraph(ActionEvent event){
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("/fxml/Graph.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(loader);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	public void btnSettings(ActionEvent event){
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("/fxml/Settings.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(loader);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
}
