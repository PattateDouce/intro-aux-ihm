package blackjack.util;

import blackjack.Main;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Util {
	
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
	 * Ouvre une nouvelle fenètre avec les crédits
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
}
