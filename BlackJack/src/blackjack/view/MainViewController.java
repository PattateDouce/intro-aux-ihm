package blackjack.view;

import java.net.URL;
import java.util.ResourceBundle;

import blackjack.Main;
import blackjack.util.Util;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * @author Guibert Rémy
 * Classe contrôleur qui gère la fenètre d'écran titre
 */
public class MainViewController implements Initializable {

	/**
	 * Éléments dans le FXML
	 */
//	@FXML
//	private Menu menuThemes;
	
//	@FXML
//	private ToggleGroup theme;
	
	/**
	 * Attributs de la classe
	 */
	private Main bj;
	
	private Stage primaryStage;
	
//	private IntegerProperty[] settings;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/** Révupère l'instance du programme principal pour plus tard pouvoir changer de scène
	 * @param pfBj l'objet Blackjack
	 */
	public void setBj(Main pfBj) {
		this.bj = pfBj;
		
		this.primaryStage = this.bj.getPrimaryStage();
		
//		this.settings = this.bj.getSettings();
//		RadioMenuItem radio = (RadioMenuItem) this.menuThemes.getItems().get(this.settings[2].getValue());
//		radio.setSelected(true);
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
	
	/**
	 * Action liée à un RadioMenuItem permettant d'ouvrir la fenètre des règles
	 */
	@FXML
	private void actionRules() {
		Util.showRules(this.primaryStage);
	}
}
