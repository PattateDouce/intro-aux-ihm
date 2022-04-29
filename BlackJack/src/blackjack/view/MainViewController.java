package blackjack.view;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import blackjack.Main;
import blackjack.Util;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Classe contrôleur qui gère la fenêtre d'écran titre
 * @author Guibert Rémy
 */
public class MainViewController implements Initializable {

	/**
	 * Attributs de la classe
	 */
	private Main main;
	
	private Stage primaryStage;
	
	private DoubleProperty[] offsets;
	
	private IntegerProperty[] settings;
	
	private Locale locale;
	
	private ObjectProperty<MediaPlayer> mediaPlayer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.locale = resources.getLocale();
	}

	/** Récupère l'instance du programme principal pour initialiser les objets
	 * @param main L'objet Main
	 */
	public void initializeObjects(Main main) {
		this.main = main;
		
		this.primaryStage = this.main.getPrimaryStage();
		this.primaryStage.setOnCloseRequest( e -> {e.consume(); actionQuit();} );
		
		this.offsets = this.main.getOffsets();
		
		this.settings = this.main.getSettings();
		
		this.mediaPlayer = this.main.getMediaPlayer();
	}
	
	/**
	 * Ouvre une nouvelle fenêtre expliquant les règles
	 */
	@FXML
	private void actionRules() {
		Util.dialog(this.primaryStage, this.locale, "rules.title", "rules.html");
	}
	
	/**
	 * Ouvre une nouvelle fenêtre avec les crédits
	 */
	@FXML
	private void actionAbout() {
		Util.dialog(this.primaryStage, this.locale, "about.title", "about.html");
	}
	
	/**
	 * Lance le jeu
	 */
	@FXML
	private void actionStart() {
		this.mediaPlayer.getValue().stop();
		String audio;
		if (this.settings[2].getValue() == 0) {
			audio = "SM64DS Luigi's Casino Theme.m4a";
			
		} else if (this.settings[2].getValue() == 1) {
			audio = "SM64DS Luigi's Casino Theme Slowed & Reverb.m4a";
			
		} else {
			audio = "SM64DS Luigi's Casino Theme Jazz Remix.m4a";
		}
		Util.loadMusic(this.mediaPlayer, audio, this.settings[3].getValue());
		Util.loadMainOrGameView(this.main, primaryStage, this.locale, this.offsets, "Game");
	}
	
	/**
	 * Ouvre les paramètres
	 */
	@FXML
	private void actionSettings() {
		Util.loadSettingsView(this.main, this.primaryStage, this.locale, this.offsets, false);
	}
	
	/**
	 * Ferme le jeu
	 */
	@FXML
	private void actionQuit() {
		this.primaryStage.close();
	}
}
