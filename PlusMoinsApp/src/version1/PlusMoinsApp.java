package version1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PlusMoinsApp extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("start");
		
		TextField zoneTexte = new TextField("10");
		zoneTexte.setMaxHeight(Double.MAX_VALUE);
		zoneTexte.setAlignment(Pos.CENTER);
		zoneTexte.setEditable(false);
		zoneTexte.setStyle("-fx-font-size: 28;");
		
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(zoneTexte);
		
		Button buttonPlus = new Button("+");
		buttonPlus.setStyle("-fx-font-size: 18;");
		buttonPlus.setPrefWidth(300);
		Button buttonRAZ = new Button("RAZ");
		buttonRAZ.setStyle("-fx-font-size: 18;");
		buttonRAZ.setPrefWidth(300);
		Button buttonMoins = new Button("-");
		buttonMoins.setStyle("-fx-font-size: 18;");
		buttonMoins.setPrefWidth(300);
		
		GridPane gridPane = new GridPane();
		gridPane.add(buttonPlus, 0, 0);
		gridPane.add(buttonRAZ, 1, 0);
		gridPane.add(buttonMoins, 2, 0);
		borderPane.setBottom(gridPane);
		
		Scene scene = new Scene(borderPane, 320, 200);
		primaryStage.setScene(scene);
		
		primaryStage.setTitle("PlusMoinsApp");
		primaryStage.show();
	}
	
	@Override
	public void init() throws Exception {
		System.out.println("init");
		super.init();
	}
	
	@Override
	public void stop() throws Exception {
		System.out.println("stop");
		super.stop();
	}

}
