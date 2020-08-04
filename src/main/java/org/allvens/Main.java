package org.allvens;
	
import java.util.prefs.Preferences;

import org.allvens.assets.Constants;
import org.allvens.assets.Constants_Prefs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


/**
 * This app is will be used to
 * @author JessuCdev
 *
 */
public class Main extends Application implements Constants{
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource(Constants.FILE_SCREEN_Main));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(Main.class.getResource(Constants.FILE_CSS).toExternalForm());
			Preferences pref = Preferences.userRoot();
			primaryStage.setAlwaysOnTop(pref.getBoolean(Constants_Prefs.SETTINGS_OnTopOfWindow, true));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * These task will run as soon as the app is closed
	 */
	@Override
	public void stop(){
	}
}
