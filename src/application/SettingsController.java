package application;

import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController {

	@FXML
	TextField tfLongBreakDuration;
	@FXML
	TextField tfShortBreakDuration;
	@FXML
	TextField tfWorkDuration;
	@FXML
	TextField tfAmountOfShotBreak;
	@FXML
	CheckBox cbContiouseMode;
	
	public void cbCheckContinousMode(){
		Preferences pref = Preferences.userRoot();
		System.out.println("Changed");
		if(cbContiouseMode.isSelected()){
			System.out.println("Set");
			pref.put("continouseMode", "Yes");
		}else{
			System.out.println("No");
			pref.put("continouseMode", "No");
		}
	}
	
	public void goBackToMainMenu(ActionEvent e){
		Parent loader = null;
		try {
			loader = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Scene scene = new Scene(loader);
		scene.getStylesheets().add(getClass().getResource("/style/application.css").toExternalForm());
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.setAlwaysOnTop(true);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize(){
		Preferences pref = Preferences.userRoot();
		tfLongBreakDuration.setPromptText(Integer.toString(pref.getInt("longBreakDuration", 10)));
		tfShortBreakDuration.setPromptText(Integer.toString(pref.getInt("shortBreakDuration", 5)));
		tfWorkDuration.setPromptText(Integer.toString(pref.getInt("workTimeDuration", 25)));
		tfAmountOfShotBreak.setPromptText(Integer.toString(pref.getInt("amountOfCyclesTillLongBreak", 3)));
		
		if(pref.get("continousMode", "Yes").equals("Yes")){
			cbContiouseMode.setSelected(true);
		}else{
			cbContiouseMode.setSelected(false);
		}
	}
	
	public void changeLongBreakTime(ActionEvent e){
		try{
			Preferences pref = Preferences.userRoot();
			pref.putInt("longBreakDuration", Integer.parseInt(tfLongBreakDuration.getText()));
		}catch(NumberFormatException e1){
			System.out.println("That is not a valid number");
		}
	}
	
	public void changeShortBreakTime(ActionEvent e){
		Preferences pref = Preferences.userRoot();
		try{
			pref.putInt("shortBreakDuration", Integer.parseInt(tfShortBreakDuration.getText()));
		}catch(NumberFormatException e1){
			System.out.println("That is not a valid number");
		}
	}
	
	public void changeWorkTime(ActionEvent e){
		try{
		Preferences pref = Preferences.userRoot();
			pref.putInt("workTimeDuration", Integer.parseInt(tfWorkDuration.getText()));
		}catch(NumberFormatException e1){
			System.out.println("That is not a valid number");
		}
		
	}
	public void cahngeAmountOfTimesTillLongBreak(ActionEvent e){
		try{
			Preferences pref = Preferences.userRoot();
			pref.putInt("amountOfCyclesTillLongBreak", Integer.parseInt(tfAmountOfShotBreak.getText()));
		}catch(NumberFormatException e1){
			System.out.println("That is not a valid number");
		}
	}
}
