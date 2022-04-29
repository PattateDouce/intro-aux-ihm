package blackjack;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import blackjack.view.GameViewController;
import blackjack.view.MainViewController;
import blackjack.view.SettingsViewController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Classe de fonctions utilisées par les autres classes
 * @author Guibert Rémy
 */
public class Util {
	
	/** Ouvre une nouvelle fenêtre Alert, utilisée pour afficher les règles ou les crédits
	 * @param primaryStage Stage parent
	 * @param locale Locale du parent
	 * @param title Titre de la nouvelle fenêtre
	 * @param file Fichier HTML à charger
	 */
	public static void dialog(Stage primaryStage, Locale locale, String title, String file) {
		ResourceBundle resources = ResourceBundle.getBundle("blackjack.locales.lang", locale);
		
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.initOwner(primaryStage);
		dialog.setTitle(resources.getString(title));
		
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		
		webView.setPrefSize(550, 400);
		webEngine.load(Main.class.getResource("resource/"+resources.getString(file)+".html").toString());
		
		dialog.getDialogPane().setContent(webView);
		dialog.getDialogPane().setHeader(new ImageView());
		
		dialog.showAndWait();
	}
	
	/** Ouvre une nouvelle fenêtre Alert, utilisée pour demander confirmation
	 * lorsqu'on demande a quitter le jeu, quand une partie a commencée
	 * @param primaryStage Stage parent
	 * @param locale Locale du parent
	 * @param mode Mode retour ou mode fermeture, "about" ou "quit"
	 * @return true sdq
	 */
	public static boolean dialogConfirm(Stage primaryStage, Locale locale, String mode) {
		ResourceBundle resources = ResourceBundle.getBundle("blackjack.locales.lang", locale);
		
		Alert confirm = new Alert(AlertType.WARNING);
		confirm.initOwner(primaryStage);
		confirm.setTitle(resources.getString(mode+".title"));
		confirm.setHeaderText(resources.getString("progression"));
		confirm.setContentText(resources.getString(mode+".content"));
		
		ButtonType butYes = new ButtonType(resources.getString("yes"), ButtonData.YES);
		ButtonType butNo = new ButtonType(resources.getString("no"), ButtonData.NO);
		
		confirm.getButtonTypes().setAll(butYes, butNo);
		
		if (confirm.showAndWait().get().equals(butYes))
			return true;
	
		return false;
	}
	
	/** Charge une musique dans le MediaPlayer
	 * @param mediaPlayer L'ObjectProperty contenant un MediaPlayer
	 * @param resource Le nom du fichier resource
	 * @param volume Le volume de la musique, entre 0 et 100
	 */
	public static void loadMusic(ObjectProperty<MediaPlayer> mediaPlayer, String resource, int volume) {
		Media media = new Media(Main.class.getResource("resource/" + resource).toExternalForm());
		mediaPlayer.setValue(new MediaPlayer(media));
		mediaPlayer.getValue().setCycleCount(Integer.MAX_VALUE);
		mediaPlayer.getValue().setVolume(volume/100.0);
		mediaPlayer.getValue().play();
	}

	/** Charge la vue Settings dans le Stage principal ou dans une nouvelle denêtre, en fonction du paramètre isWindow
	 * @param main L'objet Main
	 * @param primaryStage Stage principal de l'application
	 * @param locale Langue à charger
	 * @param offsets Les pixels que rajoute l'OS pour afficher la fenêtre
	 * @param isWindow true si il faut créer une fenêtre dédiée, false si il faut l'ajouter au primaryStage
	 */
	public static void loadSettingsView(Main main, Stage primaryStage, Locale locale, DoubleProperty[] offsets, boolean isWindow) {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/SettingsView.fxml"));

			ResourceBundle resources = ResourceBundle.getBundle("blackjack.locales.lang", locale);
			
			loader.setResources(resources);
			
			BorderPane borderPane = loader.load();
			
			SettingsViewController ctrl = loader.getController();
			ctrl.initializeObjects(main);
			
			if (isWindow) { // Créer une nouvelle fenêtre
				Button button = (Button) borderPane.getBottom();
				button.setText(resources.getString("button.close"));
				
				Scene scene = new Scene(borderPane);
				scene.getStylesheets().setAll(primaryStage.getScene().getStylesheets());
				
				Stage newStage = new Stage();
				newStage.initOwner(primaryStage);
				newStage.initModality(Modality.WINDOW_MODAL);
				newStage.setResizable(false);
				newStage.getIcons().setAll(primaryStage.getIcons());
				newStage.setTitle(resources.getString("settings.title"));
				newStage.setX(primaryStage.getX() + (primaryStage.getWidth() - (640 + offsets[0].getValue())) / 2);
				newStage.setY(primaryStage.getY() + 3 * (primaryStage.getHeight() - (480 + offsets[1].getValue())) / 4);
				newStage.setScene(scene);
				
				ctrl.setWindowStage(newStage);
				
				newStage.show();
			} else { // Utilise la fenêtre principale
				primaryStage.setTitle(resources.getString("settings.title"));
	
				primaryStage.getScene().setRoot(borderPane);
			}
			
		} catch (IOException e) {
			System.out.println("Problem(s) with SettingsView.fxml");
			System.exit(1);
		}
	}

	/** Charge la vue Main ou Game dans le Stage principal, en fonction du paramètre view
	 * @param main L'objet Main
	 * @param primaryStage Stage principal de l'application
	 * @param locale Langue à charger
	 * @param offsets Les pixels que rajoute l'OS pour afficher la fenêtre
	 * @param view "Main" ou "Game", le premier chargera le contrôleur Main, l'autre chargera le contrôleur Game
	 */
	public static void loadMainOrGameView(Main main, Stage primaryStage, Locale locale, DoubleProperty[] offsets, String view) {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/" + view + "View.fxml"));
			
			loader.setResources(ResourceBundle.getBundle("blackjack.locales.lang", locale));
			
			BorderPane borderPane = loader.load();
			
			primaryStage.setTitle("BlackJack");
			
			double oldWidth = primaryStage.getScene().getWidth();
			
			if (view.equals("Main")) {
				borderPane.getStyleClass().add(locale.getLanguage());
				MainViewController ctrl = loader.getController();
				ctrl.initializeObjects(main);
			} else {
				GameViewController ctrl = loader.getController();
				ctrl.initializeObjects(main);
			}
			
			if (offsets[0].getValue() != -1 && offsets[1].getValue() != -1) {
				primaryStage.setWidth(borderPane.getPrefWidth() + offsets[0].getValue());
				primaryStage.setHeight(borderPane.getPrefHeight() + offsets[1].getValue());
				primaryStage.setX(primaryStage.getX() + (oldWidth - borderPane.getPrefWidth()) / 2);
			}
			
			primaryStage.getScene().setRoot(borderPane);

		} catch (IOException e) {
			System.out.println("Problem(s) with " + view + "View.fxml");
			System.exit(1);
		}
	}
}
