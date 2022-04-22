package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane(); // Composant central
			
			Button bouton = new Button("coucou");
			bouton.setMaxWidth(Double.MAX_VALUE);
			root.setTop(bouton);
			
			String ficUrl = Main.class.getResource("images/mine.png").toString();
			System.out.println("Image : "+ficUrl);
			Image image = new Image(ficUrl);
			ImageView iView = new ImageView(image);
			root.setCenter(iView);
			
			Scene scene = new Scene(root,400,400); // Scene de l'app
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); // Lie le CSS à la scène
			
			primaryStage.setScene(scene); // Lie la scène à la fenètre
			
			primaryStage.setTitle("Bonjour Java FX"); // Done un titre à la feenètre
			
			primaryStage.show(); // Montre la fenètre
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println( "La version de Java utilisée est : " );
		System.out.println( System.getProperty("java.version"));
		launch(args);
		System.out.println( "fin du programme" );
	}
}
