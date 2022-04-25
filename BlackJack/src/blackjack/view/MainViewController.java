package blackjack.view;

import java.net.URL;
import java.util.ResourceBundle;

import blackjack.Main;
import blackjack.util.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * @author Guibert Rémy
 * Classe contrôleur qui gère la fenètre d'écran titre
 */
public class MainViewController implements Initializable {

	/**
	 * Attributs de la classe
	 */
	private Main bj;
	
	private Stage primaryStage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/** Révupère l'instance du programme principal pour plus tard pouvoir changer de scène
	 * @param pfBj l'objet Blackjack
	 */
	public void setBj(Main pfBj) {
		this.bj = pfBj;
		
		this.primaryStage = this.bj.getPrimaryStage();
	}
	
	/**
	 * Ouvre une nouvelle fenêtre expliquant les règles
	 */
	@FXML
	private void actionRules() {
		Util.showRules(this.primaryStage);
	}
	
	/**
	 * Ouvre une nouvelle fenètre avec les crédits
	 */
	@FXML
	private void actionAbout() {
		Util.showAbout(this.primaryStage);
	}
	
	/**
	 * Lance le jeu
	 */
	@FXML
	private void actionStart() {
		this.bj.loadGameView();
	}
	
	/**
	 * Ouvre les paramètres
	 */
	@FXML
	private void actionSettings() {
		this.bj.loadSettingsView();
	}
	
	/**
	 * Ferme le jeu
	 */
	@FXML
	private void actionQuit() {
		this.primaryStage.close();
	}
}
