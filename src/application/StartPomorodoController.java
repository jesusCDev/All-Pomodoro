package application;

import java.util.prefs.Preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class StartPomorodoController {

	@FXML
	Label lbTimer;
	@FXML
	Button btnPlayAndPause;
	
	int playPauseTracker = 1;
	TimeKeeper timer;
	
	public void initialize(){
		Preferences pref = Preferences.userRoot();
		
		int seconds = (pref.getInt("workTimeDuration", 25) * 60);
		int minLeft = (seconds/60);
		int secondsLeft = (seconds - (minLeft * 60));
		if((seconds%60) == 0){
			lbTimer.setText(minLeft + ":" + "00");
		}else if(seconds == 0){
			lbTimer.setText("00:00");
		}else if(secondsLeft < 10){
			lbTimer.setText(minLeft + ":0" + secondsLeft);
		}else{
			lbTimer.setText(minLeft + ":" + secondsLeft);
		}
		
		timer = new TimeKeeper(lbTimer, pref.get("continouseMode", "Yes"));
	}
	
	@FXML
	public void playPauseBtn(ActionEvent e){
		SetWindowSize();
		System.out.println("Play/Pause");
		if(playPauseTracker == 1 || btnPlayAndPause.getText().equals("Pause")){
			timer.playAndPause(btnPlayAndPause);
			btnPlayAndPause.setText("Play");
			playPauseTracker *= -1;
		}else{
			timer.playAndPause(btnPlayAndPause);
			btnPlayAndPause.setText("Pause");
			playPauseTracker *= -1;
		}
	}
	
	@FXML
	public void skipBtn(ActionEvent e){
		System.out.println("Skip");
		SetWindowSize();
		timer.skip();
	}
	
	@FXML
	public void resetBtn(ActionEvent e){
		System.out.println("Reset");
		timer.reset();
	}
	
	public void SetWindowSize(){
		//Scene scene = ((Node) e.getSource()).getScene();
		Scene scene = btnPlayAndPause.getScene();
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		        System.out.println("Width: " + newSceneWidth);
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		        System.out.println("Height: " + newSceneHeight);
		        if(newSceneHeight.intValue() > 200.0 && newSceneHeight.intValue() < 350){
			        lbTimer.setFont(new Font("Cambria", (double) newSceneHeight - 150));
		        }
		    }
		});
	}
	
}
