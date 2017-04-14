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
		
		String currentProjectPrefString = "CurrentProject";
		String allPomorodoPrefString = "All Pomorodo";
		String currentTimePrefString = "currentTime";
		
		String totalKeyWord = " Total";
		String spaceKeyWord = " ";
		
		//This saves it for today
		pref.putInt(pref.get(currentProjectPrefString, allPomorodoPrefString), (pref.getInt(pref.get(currentProjectPrefString, allPomorodoPrefString), 0) + currentTime));
		//This saves it for the day of the week
		pref.putInt((pref.get(currentProjectPrefString, allPomorodoPrefString) + spaceKeyWord + cal.get(Calendar.DAY_OF_WEEK)), (pref.getInt(pref.get(currentProjectPrefString, allPomorodoPrefString), 0) + currentTime));
		//This saves it for the overall time
		pref.putInt((pref.get(currentProjectPrefString, allPomorodoPrefString) + totalKeyWord), (pref.getInt(pref.get(currentProjectPrefString, allPomorodoPrefString), 0) + currentTime));
		
		//Changes the Current Project to All Pomorodo
		pref.put(currentProjectPrefString, allPomorodoPrefString);
		pref.putInt(currentTimePrefString, 0);
		
		//TODO This might be deleted
		pref.putInt("totalMinsWorked", currentTime);
		//TODO this will might be deleted
		pref.putInt("resumeWhichTimerIsPlaying", 3);
	}
}
