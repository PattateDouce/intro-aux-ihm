package blackjack.util;

import java.io.IOException;

import blackjack.Main;
import blackjack.view.SettingsViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Util {
	
	/**
	 * Ouvre une nouvelle fenêtre expliquant les règles
	 */
	public static void showRules(Stage stage) {
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.initOwner(stage);
		dialog.setTitle("Règles du BlackJack");
		
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		
		webView.setPrefSize(550, 400);		
		webEngine.load(Main.class.getResource("resource/rules.html").toString());
		
		dialog.getDialogPane().setContent(webView);
		dialog.getDialogPane().setHeader(new ImageView());
		
		dialog.showAndWait();
	}
	
	/**
	 * Ouvre une nouvelle fenêtre avec les crédits
	 */
	public static void showAbout(Stage stage) {
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.initOwner(stage);
		dialog.setTitle("À propos de BlackJack");
		
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		
		webView.setPrefSize(550, 400);
		webEngine.load(Main.class.getResource("resource/about.html").toString());
		
		dialog.getDialogPane().setContent(webView);
		dialog.getDialogPane().setHeader(new ImageView());
		
		dialog.showAndWait();
	}
	
	/**
	 * Ouvre une nouvelle fenêtre donnant accès aux paramètres
	 * @param bj Classe principale
	 * @param primaryStage Stage de la GameView
	 */
	public static void showSettings(Stage pmrStage, Main bj) {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/SettingsView.fxml"));
	
			BorderPane vueSettings = loader.load();
			vueSettings.setBottom(null);
			
			SettingsViewController ctrl = loader.getController();
			ctrl.setBj(bj);
			
			Scene scene = new Scene(vueSettings);
			scene.getStylesheets().setAll(pmrStage.getScene().getStylesheets());
			
			Stage stage = new Stage();
			stage.initOwner(pmrStage);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.getIcons().setAll(pmrStage.getIcons());
			stage.setResizable(false);
			stage.setTitle("Paramètres du BlackJack");
			stage.setScene(scene);
			
			stage.show();
	
		} catch (IOException e) {
			System.out.println("Ressource SettingsView.fxml non disponible");
			System.exit(1);
		}
	}
}
