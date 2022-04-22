package version2;

import java.io.IOException;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PlusMoinsApp extends Application {
	
	private final IntegerProperty valeur = new SimpleIntegerProperty(20);
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation( PlusMoinsApp.class.getResource("PlusMoins.fxml"));
			BorderPane          borderPane = loader.load(); 
			PlusMoinsController ctrl       = loader.getController();
			
			ctrl.setFenetrePrincipale(primaryStage);
			ctrl.setPropriteCompteur(this.valeur);
			
			Scene scene = new Scene(borderPane);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Plus Moins App v2");
			primaryStage.show();
			
		} catch (IOException e) {
			System.out.println("Ressource FXML non disponible");
			e.printStackTrace(); // TODO à supprimer en prod
			System.exit(1);
		} 
	}	

}
