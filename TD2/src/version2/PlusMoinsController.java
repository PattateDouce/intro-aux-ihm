package version2;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlusMoinsController implements Initializable {

	@FXML
	private TextField zoneTexte;
	
	private Stage primaryStage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Initialisation");
	}
	
	@FXML
	private void actionPlus() {
		actionAddInt(1);
	}
	
	@FXML
	private void actionMoins() {
		actionAddInt(-1);
	}
	
	private void actionAddInt(int pfEntier) {
		String oldNum = zoneTexte.getText();
		
		int num = Integer.parseInt(oldNum) + pfEntier;

		String newNum = String.valueOf(num);
		
		zoneTexte.setText(newNum);
	}
	
	@FXML
	private void actionReset() {
		zoneTexte.setText("10");
	}
	
	private void actionQuitter() {
		System.out.println("Dernière valeur : " + zoneTexte.getText());
	}
	
	public void setPrimaryStage(Stage pfStage) {
		primaryStage = pfStage;
		primaryStage.setOnCloseRequest( e -> actionQuitter() );
	}

}
