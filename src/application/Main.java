package application;
	
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


/**
 * This app is will be used to
 * @author JessuCdev
 *
 */
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartPomorodo.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style/startPomorodo.css").toExternalForm());
			primaryStage.setAlwaysOnTop(true);
			primaryStage.setMinWidth(250.0);
			primaryStage.setMinHeight(200.0);
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
		Preferences prefs = Preferences.userRoot();
		int currentTime = prefs.getInt("currentTime", 0);
		prefs.putInt("currentTime", 0);
		prefs.putInt("totalMinsWorked", currentTime);
	}
}
