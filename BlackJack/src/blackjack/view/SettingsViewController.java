package blackjack.view;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import blackjack.Main;
import blackjack.Util;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
 * Classe contrôleur qui gère la fenêtre des paramètres
 * @author Guibert Rémy
 */
public class SettingsViewController implements Initializable {
	
	/**
	 * Éléments dans le FXML
	 */
	@FXML
	private Slider defaultBalanceSlider;
	
	@FXML
	private Label defaultBalanceLabel;
	
	@FXML
	private Slider defaultBetSlider;
	
	@FXML
	private Label defaultBetLabel;
	
	@FXML
	private ChoiceBox<String> music;
	
	@FXML
	private Slider volumeSlider;
	
	@FXML
	private Label volumeLabel;
	
	@FXML
	private ChoiceBox<String> language;
	
	/**
	 * Attributs de la classe
	 */
	private Main main;
	
	private Stage primaryStage;
	
	private Stage windowStage;
	
	private DoubleProperty[] offsets;

	private IntegerProperty[] settings;
	
	private ObjectProperty<Locale> locale;
	
	private ObjectProperty<MediaPlayer> mediaPlayer;
	
	private ResourceBundle resources;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;
	}
	
	/** Récupère l'instance du programme principal pour initialiser les objets
	 * @param main L'objet Main
	 */
	public void initializeObjects(Main main) {
		this.main = main;
		
		this.primaryStage = this.main.getPrimaryStage();
		
		this.offsets = this.main.getOffsets();
		
		this.settings = this.main.getSettings();
		
		this.locale = this.main.getLocale();
		
		this.mediaPlayer = this.main.getMediaPlayer();
		
		// Solde par défaut
		this.defaultBalanceSlider.valueProperty().bindBidirectional(this.settings[0]);
		Bindings.bindBidirectional(this.defaultBalanceLabel.textProperty(), this.settings[0], new NumberStringConverter());
		
		// Mise par défaut
		this.defaultBetSlider.maxProperty().bind(this.settings[0]);
		this.defaultBetSlider.valueProperty().addListener( (obs, oldval, newval) -> { if (newval.intValue() == 0) this.defaultBetSlider.valueProperty().setValue(1); } );
		this.defaultBetSlider.valueProperty().bindBidirectional(this.settings[1]);;
		Bindings.bindBidirectional(this.defaultBetLabel.textProperty(), this.settings[1], new NumberStringConverter());
		
		// Musique de fond
		this.music.getItems().setAll(this.resources.getString("music.original"), this.resources.getString("music.slowed"), this.resources.getString("music.jazz"));
		this.music.getSelectionModel().select(this.settings[2].getValue());
		this.settings[2].bind(this.music.getSelectionModel().selectedIndexProperty());
		this.music.setOnAction( e -> actionMusic(this.settings[2].getValue()) );

		// Volume
		this.volumeSlider.valueProperty().bindBidirectional(this.settings[3]);;
		Bindings.bindBidirectional(this.volumeLabel.textProperty(), this.settings[3], new NumberStringConverter());
		
		// Langue
		this.language.getItems().setAll(this.resources.getString("language.fr"), this.resources.getString("language.en"));
		this.language.getSelectionModel().select(this.settings[4].getValue());
		this.settings[4].bind(this.language.getSelectionModel().selectedIndexProperty());
		this.language.setOnAction( e -> actionLanguage(this.settings[4].getValue()) );
	}
	
	/** Quand les paramètres sont dans une fenêtre séparée,
	 * on enregistre leur Stage pour être en mesure de le fermer
	 * @param stage Le Stage de la fenêtre des paramètres
	 */
	public void setWindowStage(Stage stage) {
		this.windowStage = stage;
	}
	
	/** Change la langue de l'application
	 * @param index Indice la langue
	 */
	private void actionLanguage(int index) {
		if (index == 0) {
			this.locale.setValue(new Locale("fr"));
			
		} else {
			this.locale.setValue(new Locale("en"));
		}

		boolean isWindow = false;
		if (this.windowStage != null) {
			this.windowStage.close();
			isWindow = true;
		}
		Util.loadSettingsView(this.main, this.primaryStage, this.locale.getValue(), this.offsets, isWindow);
	}
	
	/**
	 * Change la musique de fond en fonction de index, fonctionne seulement
	 * si les paramètres ont été ouvert dans une nouvelle fenêtre
	 * @param index Indice de la musique
	 */
	private void actionMusic(int index) {
		if (this.windowStage != null) {
			String audio;
			if (index == 0) {
				audio = "SM64DS Luigi's Casino Theme.m4a";
				
			} else if (index == 1) {
				audio = "SM64DS Luigi's Casino Theme Slowed & Reverb.m4a";
				
			} else {
				audio = "SM64DS Luigi's Casino Theme Jazz Remix.m4a";
			}
			this.mediaPlayer.getValue().stop();
			Util.loadMusic(this.mediaPlayer, audio, this.settings[3].getValue());
		}
	}

	/**
	 * Permet de retourner à l'écran titre, ou de fermer la fenêtre des paramètres
	 */
	@FXML
	private void actionBack() {
		if (this.windowStage != null)
			this.windowStage.close();
		else
			Util.loadMainOrGameView(this.main, primaryStage, this.locale.getValue(), this.offsets, "Main");
	}
}
