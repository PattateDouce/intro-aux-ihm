package blackjack.view;

import java.net.URL;
import java.util.ResourceBundle;

import blackjack.Main;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
//import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Guibert Rémy
 * Classe contrôleur qui gère la fenètre des paramètres
 */
public class SettingsViewController implements Initializable {
	
	/**
	 * Éléments dans le FXML
	 */
	@FXML
	private HBox defaultBalance;
	
	@FXML
	private HBox defaultBet;
	
	@FXML
	private HBox volume;

//	@FXML
//	private ToggleGroup theme;
	
	@FXML
	private ToggleGroup music;
	
	/**
	 * Attributs de la classe
	 */
	private Main bj;
	
//	private Stage primaryStage;
	
	private IntegerProperty[] settings;
	
	private MediaPlayer mediaPlayer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	/** Révupère l'instance du programme principal pour plus tard pouvoir changer de scène
	 * @param pfBj l'objet Blackjack
	 */
	public void setBj(Main pfBj) {
		this.bj = pfBj;
		
//		this.primaryStage = this.bj.getPrimaryStage();
		
		this.settings = this.bj.getSettings();
		// Solde par défaut
		Slider defaultBalanceSlider = (Slider) this.defaultBalance.getChildren().get(0);
		Label defaultBalanceLabel = (Label) this.defaultBalance.getChildren().get(1);
		Bindings.bindBidirectional(defaultBalanceLabel.textProperty(), this.settings[0], new NumberStringConverter());
		defaultBalanceSlider.valueProperty().bindBidirectional(this.settings[0]);
		
		// Mise par défaut
		Slider defaultBetSlider = (Slider) this.defaultBet.getChildren().get(0);
		Label defaultBetLabel = (Label) this.defaultBet.getChildren().get(1);
		defaultBetSlider.maxProperty().bind(defaultBalanceSlider.valueProperty());
		defaultBetSlider.valueProperty().addListener( (obs, oldval, newval) -> { if (newval.intValue() == 0) defaultBetSlider.valueProperty().setValue(1); } );
		Bindings.bindBidirectional(defaultBetLabel.textProperty(), this.settings[1], new NumberStringConverter());
		defaultBetSlider.valueProperty().bindBidirectional(this.settings[1]);
		
		// Thème
//		RadioMenuItem radioTheme = (RadioMenuItem) this.theme.getToggles().get(this.settings[2].getValue());
//		radioTheme.setSelected(true);
		
		// Volume
		Slider volumeSlider = (Slider) this.volume.getChildren().get(0);
		Label volumeLabel = (Label) this.volume.getChildren().get(1);
		Bindings.bindBidirectional(volumeLabel.textProperty(), this.settings[2], new NumberStringConverter());
		volumeSlider.valueProperty().bindBidirectional(this.settings[2]);
		
		// Musique de fond
		RadioMenuItem radioMusic = (RadioMenuItem) this.music.getToggles().get(this.settings[3].getValue());
		radioMusic.setSelected(true);
		
		this.mediaPlayer = this.bj.getMediaPlayer();
	}
	
	/**
	 * Action liée aux RadioMenuItem permettant de charger la musique de fond choisie
	 */
	@FXML
	private void actionMusic() {
		this.mediaPlayer.stop();
		RadioMenuItem rad = (RadioMenuItem) this.music.getSelectedToggle();
		switch(rad.getText()) {
			case("Originale"):
				loadMusic("SM64DS Luigi's Casino Theme.m4a");
				this.settings[3].setValue(0);
				return;
			case("Ralentie"):
				loadMusic("SM64DS Luigi's Casino Theme Slowed & Reverb.m4a");
				this.settings[3].setValue(1);
				return;
			case("Jazz"):
				loadMusic("SM64DS Luigi’s Casino Theme Jazz Remix.m4a");
				this.settings[3].setValue(2);
				return;
		}
	}
	
	/** Charge une musique dans le MediaPlayer
	 * @param path Nom du ficher dans le package resource
	 */
	public void loadMusic(String path) {
		Media media = new Media(Main.class.getResource("resource/"+path).toExternalForm());
		this.mediaPlayer = new MediaPlayer(media);
		this.mediaPlayer.play();
		this.mediaPlayer.setCycleCount(Integer.MAX_VALUE);
		DoubleProperty doubleProp = new SimpleDoubleProperty(0.5);
		this.mediaPlayer.volumeProperty().bind(doubleProp);
		this.settings[2].addListener( (obs, oldval, newval) -> doubleProp.setValue(newval.intValue()/100.0) );
	}
	
	/**
	 * Permet de retourner à l'écran d'accueil
	 */
	@FXML
	private void actionBack() {
		this.bj.loadMainView();
	}
}
