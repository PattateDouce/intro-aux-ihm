package blackjack;

import java.util.Locale;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;

/**
 * Classe principale de l'application
 * @author Guibert Rémy
 */
public class Main extends Application {
	
	/**
	 * Attributs de la classe
	 */
	private Stage primaryStage;
	
	private final DoubleProperty[] offsets = {new SimpleDoubleProperty(-1), new SimpleDoubleProperty(-1)};
	
	private final IntegerProperty[] settings = {new SimpleIntegerProperty(1000), // Solde par défaut
										  new SimpleIntegerProperty(100), // Mise par défaut
										  new SimpleIntegerProperty(1), // Musique d'arrière plan
										  new SimpleIntegerProperty(25), // Volume
										  new SimpleIntegerProperty(0)}; // Langue
	
	private final ObjectProperty<Locale> locale = new SimpleObjectProperty<Locale>(new Locale("fr"));
	
	private final ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<MediaPlayer>();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		primaryStage.setResizable(false);
		primaryStage.getIcons().setAll(new Image(this.getClass().getResourceAsStream("resource/icon.png")));
		primaryStage.setScene(new Scene(new BorderPane()));
		primaryStage.getScene().getStylesheets().setAll(this.getClass().getResource("resource/application.css").toExternalForm());
		
		// Modifie le volume de la musique de fond à chaque fois que le paramètre 4 change de valeur
		this.settings[3].addListener( (obs, oldval, newval) -> this.mediaPlayer.getValue().setVolume(newval.intValue()/100.0) );
		
		Util.loadMusic(this.mediaPlayer, "Memoir of Summer - David Luong.m4a", this.settings[3].getValue());

		Util.loadMainOrGameView(this, primaryStage, this.locale.getValue(), this.offsets, "Main");

		primaryStage.show();
		
		// Enregistre la taille en plus qu'utilise l'OS pour afficher la fenêtre
		this.offsets[0].setValue(this.primaryStage.getWidth() - this.primaryStage.getScene().getWidth());
		this.offsets[1].setValue(this.primaryStage.getHeight() - this.primaryStage.getScene().getHeight());
	}

	public Stage getPrimaryStage() {
		return this.primaryStage;
	}
	
	public DoubleProperty[] getOffsets() {
		return offsets;
	}
	
	public IntegerProperty[] getSettings() {
		return this.settings;
	}
	
	public ObjectProperty<Locale> getLocale() {
		return this.locale;
	}
	
	public ObjectProperty<MediaPlayer> getMediaPlayer() {
		return this.mediaPlayer;
	}
}
