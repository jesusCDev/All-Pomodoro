package application;
	
import java.util.Calendar;
import java.util.Date;
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
		Preferences pref = Preferences.userRoot();
		Calendar cal = Calendar.getInstance();
		
		int currentTime = pref.getInt("currentTime", 0);
		pref.putInt(pref.get("CurrentProject", "All Pomorodo"), (pref.getInt(pref.get("CurrentProject", "All Pomorodo"), 0) + currentTime));
		pref.putInt((pref.get("CurrentProject", "All Pomorodo") + " Total"), (pref.getInt(pref.get("CurrentProject", "All Pomorodo"), 0) + currentTime));
		pref.putInt((pref.get("CurrentProject", "All Pomorodo") + " " + cal.get(Calendar.DAY_OF_WEEK)), (pref.getInt(pref.get("CurrentProject", "All Pomorodo"), 0) + currentTime));
		pref.put("CurrentProject", "All Pomorodo");
		pref.putInt("totalMinsWorked", currentTime);
		pref.putInt("currentTime", 0);
		pref.putInt("resumeWhichTimerIsPlaying", 3);
	}
}
