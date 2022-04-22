package blackjack.view;

import java.net.URL;
import java.util.ResourceBundle;

import blackjack.Blackjack;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * @author Guibert Rémy
 * Classe contrôleur qui gère la fenètre d'écran titre
 */
public class MainViewController implements Initializable {

	/**
	 * Éléments dans le FXML
	 */
	@FXML
	private Menu menuThemes;
	
	@FXML
	private ToggleGroup theme;
	
	/**
	 * Attributs de la classe
	 */
	private Blackjack bj;
	
	private Stage primaryStage;
	
	private IntegerProperty[] settings;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/** Révupère l'instance du programme principal pour plus tard pouvoir changer de scène
	 * @param pfBj l'objet Blackjack
	 */
	public void setBj(Blackjack pfBj) {
		this.bj = pfBj;
		
		this.primaryStage = this.bj.getPrimaryStage();
		
		this.settings = this.bj.getSettings();
		RadioMenuItem radio = (RadioMenuItem) this.menuThemes.getItems().get(this.settings[2].getValue());
		radio.setSelected(true);
	}
	
	/**
	 * Ouvre une nouvelle fenètre avec les crédits
	 */
	@FXML
	private void actionAbout() {
		Alert about = new Alert(AlertType.INFORMATION);
		about.setTitle("À propos de BlackJack");
		about.setHeaderText("Crédits");
		about.initOwner(this.primaryStage);
		
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		
		webView.setPrefSize(550, 300);		
		webEngine.load(Blackjack.class.getResource("resource/about.html").toString());
		
		about.getDialogPane().setContent(webView);
		
		about.showAndWait();
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
	 * Action liée aux RadioMenuItem permettant de charger le thème choisi
	 */
	@FXML
	private void actionThemes() {
		RadioMenuItem rad = (RadioMenuItem) this.theme.getSelectedToggle();
		switch(rad.getText()) {
			case("Clair"):
				this.primaryStage.getScene().getStylesheets().setAll(Blackjack.class.getResource("resource/bright.css").toExternalForm());
				this.settings[2].setValue(0);
				return;
			case("Sombre"):
				this.primaryStage.getScene().getStylesheets().setAll(Blackjack.class.getResource("resource/dark.css").toExternalForm());
				this.settings[2].setValue(1);
				return;
			case("Bee"):
				this.primaryStage.getScene().getStylesheets().setAll(Blackjack.class.getResource("resource/flatbee.css").toExternalForm());
				this.settings[2].setValue(2);
				return;
			default:
				return;
		}
	}
}
