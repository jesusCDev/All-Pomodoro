package application;
	
import java.util.Calendar;
import java.util.prefs.Preferences;

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
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartPomorodo.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/style/startPomorodo.css").toExternalForm());
			
			Preferences pref = Preferences.userRoot();
			
			primaryStage.setAlwaysOnTop(pref.getBoolean("AlwaysHoverTrueOrFalse", true));
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
		
		String currentProjectPrefString = "currentProject";
		String allPomorodoPrefString = "All Pomorodo";
		String currentTimePrefString = "overAllTimeCountedInCurrentProject";
		String resumeWhichTimerIsPlayingPrefString = "resumeWhichTimerIsPlaying";
		String totalTimeWorkingPrefString = "totalTimeWorking";
		
		String totalKeyWord = " Total";
		String spaceKeyWord = " ";
		
		int currentTime = pref.getInt(currentTimePrefString, 0);

		String projectsName = pref.get(currentProjectPrefString, allPomorodoPrefString);
		String projectsDailyName = (pref.get(currentProjectPrefString, allPomorodoPrefString) + spaceKeyWord + cal.get(Calendar.DAY_OF_WEEK));
		String projectsTotalName = (pref.get(currentProjectPrefString, allPomorodoPrefString) + totalKeyWord);

		System.out.println("Current Tiem: " + currentTime/60);
		
		pref.putInt(projectsName , (pref.getInt(projectsName , 0) + currentTime));
		pref.putInt(projectsDailyName , (pref.getInt(projectsDailyName , 0) + currentTime));
		pref.putInt(projectsTotalName , (pref.getInt(projectsTotalName, 0) + currentTime));
		
		pref.putInt(totalTimeWorkingPrefString, (pref.getInt(totalTimeWorkingPrefString, 0) + currentTime));
		
		//Changes the Current Project to All Pomorodo
		pref.put(currentProjectPrefString, allPomorodoPrefString);
		pref.putInt(currentTimePrefString, 0);
		
		pref.putInt(resumeWhichTimerIsPlayingPrefString, 3);
	}
}
