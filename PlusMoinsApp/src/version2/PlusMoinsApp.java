package version2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PlusMoinsApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PlusMoinsApp.class.getResource("PlusMoins.fxml"));
		
		BorderPane borderPane = loader.load();
		
		Scene scene = new Scene(borderPane);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("PlusMoinsApp V2");
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
