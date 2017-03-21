package application;

import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StartPomorodoController {

	@FXML
	Button btnRest;

	@FXML
	Button btnPlayAndPause;

	@FXML
	Button btnNext;
	
	@FXML
	Label lbTimer;
	TimeKeeper timer;
	
	StartPomorodoController(){
		timer = new TimeKeeper(lbTimer);
	}
	
	public void playPauseBtn(ActionEvent e){
		System.out.println("Play/Pause");
		timer.playAndPause();
	}
	
	public void skipBtn(ActionEvent e){
		System.out.println("Skip");
		SetWindowSize();
		timer.skip();
	}
	
	public void resetBtn(ActionEvent e){
		System.out.println("Reset");
		timer.reset();
	}
	
	public void initialize(){
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
