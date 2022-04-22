package version2;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PlusMoinsApp extends Application {

	private PlusMoinsController ctrl;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation( PlusMoinsApp.class.getResource("PlusMoins.fxml"));
			
			BorderPane borderPane = loader.load();  // peut lever IOEXception
			
			ctrl = loader.getController();
			ctrl.setPrimaryStage(primaryStage);
			
			Scene scene = new Scene(borderPane);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Plus Moins App v2");
			primaryStage.show();
			
		} catch (IOException e) {
			System.out.println("Ressource FXML non disponible");
			e.printStackTrace();
			System.exit(1);
		} 
		
		
	}

}
