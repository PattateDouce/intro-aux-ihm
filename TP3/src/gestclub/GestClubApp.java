package gestclub;

import java.io.IOException;
import gestclub.model.Membre;
import gestclub.view.ListeMembreController;
import gestclub.view.SaisieMembreController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class GestClubApp extends Application {

	private BorderPane rootPane;

	private Stage primaryStage;
	
	private ObservableList<Membre> listeMembres;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.rootPane = new BorderPane();

		Scene scene = new Scene(rootPane);
		scene.getStylesheets().add(GestClubApp.class.getResource("resource/bright.css").toExternalForm());

		primaryStage.setResizable(false);
		primaryStage.setTitle("GestClub App");
		primaryStage.setScene(scene);

		loadListeMembre();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void loadListeMembre() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GestClubApp.class.getResource("view/ListeMembre.fxml"));

			BorderPane vueListe = loader.load();

			this.rootPane.setCenter(vueListe);
			
			ListeMembreController ctrl = loader.getController();
			ctrl.setGestClubApp(this);
			ctrl.setPrimaryStage(this.primaryStage);
			listeMembres = ctrl.getListeMembres();

		} catch (IOException e) {
			System.out.println("Ressource FXML non disponible");
			System.exit(1);
		}
	}

	public Membre showSaisieMembre(Membre pfMembre, String pfTitle, String pfBoutonText) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GestClubApp.class.getResource("view/SaisieMembre.fxml"));

			BorderPane vueSaisie = loader.load();

			Scene scene = new Scene(vueSaisie);
			scene.getStylesheets().setAll(primaryStage.getScene().getStylesheets());

			Stage dialogStage = new Stage();
			dialogStage.setTitle(pfTitle);
			dialogStage.initOwner(this.primaryStage);
			dialogStage.setResizable(false);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setScene(scene);

			SaisieMembreController ctrl = loader.getController();
			ctrl.setDialogStage(dialogStage);
			ctrl.setMembre(pfMembre);
			ctrl.setButtonOKText(pfBoutonText);

			dialogStage.showAndWait();
			
			return ctrl.getMembre();

		} catch (IOException e) {
			System.out.println("Ressource FXML non disponible : SaisieMembres");
			System.exit(1);
		}
		
		return null;
	}
	
	@Override
	public void stop() throws Exception {
		System.out.println("Fin de l'application, voici la liste des membres :");
		for (int i = 0; i < listeMembres.size(); i++) {
			System.out.println(listeMembres.get(i));
		}
	}
}