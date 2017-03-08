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
		Parent imageViewScreenParent = null;
		try {
			imageViewScreenParent = FXMLLoader.load(getClass().getResource("/fxml/StartPomorodo.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene imageViewScene = new Scene(imageViewScreenParent);
		//imageViewScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage imageViewStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		imageViewStage.setScene(imageViewScene);
		imageViewStage.show();
	}
	
	public void btnSeeGraph(ActionEvent event){
		Parent imageViewScreenParent = null;
		try {
			imageViewScreenParent = FXMLLoader.load(getClass().getResource("/fxml/Graph.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene imageViewScene = new Scene(imageViewScreenParent);
		//imageViewScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage imageViewStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		imageViewStage.setScene(imageViewScene);
		imageViewStage.show();
	}
	
	public void btnSettings(ActionEvent event){
		Parent imageViewScreenParent = null;
		try {
			imageViewScreenParent = FXMLLoader.load(getClass().getResource("/fxml/Settings.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scene imageViewScene = new Scene(imageViewScreenParent);
		//imageViewScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage imageViewStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		imageViewStage.setScene(imageViewScene);
		imageViewStage.show();
	}
}
