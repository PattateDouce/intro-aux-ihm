package blackjack;

import java.io.IOException;

import blackjack.view.GameViewController;
import blackjack.view.MainViewController;
import blackjack.view.SettingsViewController;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Guibert Rémy
 * Classe principale de l'application
 */
public class Main extends Application {

	private Stage primaryStage;
	
	private BorderPane rootPane;
	
	private MediaPlayer mediaPlayer;

	private IntegerProperty[] settings = {new SimpleIntegerProperty(1000), // Solde par défaut
										  new SimpleIntegerProperty(100), // Mise par défaut
										  new SimpleIntegerProperty(0), // Volume
										  new SimpleIntegerProperty(0)}; // Musique d'arrière plan
	
	private double offsetWidth;
	
	private double offsetHeight;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/** Charge une musique dans le MediaPlayer
	 * @param path Nom du ficher dans le package resource
	 */
	public void loadMusic() {
		Media media = new Media(this.getClass().getResource("resource/SM64DS Luigi's Casino Theme.m4a").toExternalForm());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
		mediaPlayer.setCycleCount(Integer.MAX_VALUE);
		mediaPlayer.setVolume(this.settings[2].getValue()/100.0);
		this.settings[2].addListener( (obs, oldval, newval) -> mediaPlayer.volumeProperty().setValue(newval.intValue()/100.0) );
	}

	@Override
	public void start(Stage primaryStage) {
		
		loadMusic();
		
		this.primaryStage = primaryStage;
		
		this.rootPane = new BorderPane();
		
		Scene scene = new Scene(rootPane);
		scene.getStylesheets().setAll(Main.class.getResource("resource/application.css").toExternalForm());

		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.getIcons().setAll(new Image(this.getClass().getResourceAsStream("resource/icon.png")));
		
		loadMainView();

		primaryStage.show();
		
		this.offsetWidth = this.primaryStage.getWidth() - this.primaryStage.getScene().getWidth();
		this.offsetHeight = this.primaryStage.getHeight() - this.primaryStage.getScene().getHeight();
	}

	public void loadMainView() {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/MainView.fxml"));
			
			BorderPane vueListe = loader.load();
			
			double oldWidth = this.primaryStage.getScene().getWidth();
			
			if (this.offsetWidth != 0 && this.offsetHeight != 0) {
				this.primaryStage.setWidth(640 + this.offsetWidth);
				this.primaryStage.setHeight(480 + this.offsetHeight);
				this.primaryStage.setX( this.primaryStage.getX() + ((oldWidth-640)/2) );
			}

			this.primaryStage.setTitle("BlackJack");
			
			this.rootPane.setCenter(vueListe);
			
			MainViewController ctrl = loader.getController();
			ctrl.setBj(this);

		} catch (IOException e) {
			System.out.println("Ressource MainView.fxml non disponible");
			System.exit(1);
		}
	}

	public void loadGameView() {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/GameView.fxml"));
			
			BorderPane vueListe = loader.load();
			
			double oldWidth = this.primaryStage.getScene().getWidth();
			
			this.primaryStage.setWidth(960 + this.offsetWidth);
			this.primaryStage.setHeight(540 + this.offsetHeight);
			this.primaryStage.setX( this.primaryStage.getX() - ((960-oldWidth)/2) );
			
			this.rootPane.setCenter(vueListe);

			GameViewController ctrl = loader.getController();
			ctrl.setBj(this);

		} catch (IOException e) {
			System.out.println("Ressource GameView.fxml non disponible");
			System.exit(1);
		}
	}
	
	public void loadSettingsView() {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("view/SettingsView.fxml"));

			BorderPane vueListe = loader.load();
			
			this.primaryStage.setTitle("Paramètres du BlackJack");

			this.rootPane.setCenter(vueListe);
			
			SettingsViewController ctrl = loader.getController();
			ctrl.setBj(this);

		} catch (IOException e) {
			System.out.println("Ressource SettingsView.fxml non disponible");
			System.exit(1);
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public IntegerProperty[] getSettings() {
		return settings;
	}
	
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
}
