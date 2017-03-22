package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StartPomorodoController {

	@FXML
	Label lbTimer;
	@FXML
	Button btnPlayAndPause;
	
	int playPauseTracker = 1;
	TimeKeeper timer;
	
	public void initialize(){
		timer = new TimeKeeper(lbTimer);
	}
	
	@FXML
	public void playPauseBtn(ActionEvent e){
		System.out.println("Play/Pause");
		timer.playAndPause();
		if(playPauseTracker == 1){
			btnPlayAndPause.setText("Play");
			playPauseTracker *= -1;
		}else{
			btnPlayAndPause.setText("Pause");
			playPauseTracker *= -1;
		}
	}
	
	@FXML
	public void skipBtn(ActionEvent e){
		System.out.println("Skip");
		//SetWindowSize();
		timer.skip();
	}
	
	@FXML
	public void resetBtn(ActionEvent e){
		System.out.println("Reset");
		timer.reset();
	}
	
	/**
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
	**/
	
}
