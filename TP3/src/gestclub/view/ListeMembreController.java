package gestclub.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import gestclub.GestClubApp;
import gestclub.model.Membre;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ListeMembreController implements Initializable {

	private Stage primaryStage;

	@FXML
	private Button butNew;
	
	@FXML
	private Button butEdit;
	
	@FXML
	private Button butDel;
	
	@FXML
	private ListView<Membre> listview;
	
	@FXML
	private MenuItem miEdit;
	
	@FXML
	private MenuItem miDel;
	
	private ObservableList<Membre> listeMembres;
	
	private ObjectProperty<ObservableList<Membre>> objProp;
	
	private GestClubApp gca;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listeMembres = FXCollections.observableArrayList();
		objProp = new SimpleObjectProperty<ObservableList<Membre>>();
		objProp.setValue(listeMembres);
		listview.itemsProperty().bindBidirectional(this.objProp);
		listview.setOnMouseClicked( e -> actionClique(e) );
	}
	
	@FXML
	private void actionNew() {
		Membre newMembre = gca.showSaisieMembre(null, "Création d'un membre", "Créer");
		
		if (newMembre != null)
			listeMembres.add(newMembre);
		}
	
	@FXML
	private void actionEdit() {
		int index = this.listview.getFocusModel().getFocusedIndex();
		
		Membre oldMembre = listeMembres.get(index);
		Membre editedMembre = gca.showSaisieMembre(oldMembre, "Modification du membre", "Appliquer");
		
		if (editedMembre != null)
			listeMembres.set(index, editedMembre);
		
		actionVerifSelect();
	}
	
	@FXML
	private void actionDel() {
		int index = this.listview.getFocusModel().getFocusedIndex();
		Membre membre = listeMembres.get(index);
		
		Alert warn = new Alert(AlertType.CONFIRMATION);
		warn.setTitle("Suppression d'un membre");
		warn.setHeaderText("Voulez-vous vraiment supprimmer " + membre.getNom() + " " + membre.getPrenom() + " ?");
		warn.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		warn.initOwner(this.primaryStage);

		Optional<ButtonType> choix = warn.showAndWait();
		if (choix.get().equals(ButtonType.YES)) {
			listeMembres.remove(index);
			actionVerifSelect();
		}
		
	}
	
	@FXML
	private void actionVerifSelect() {
		int index = this.listview.getFocusModel().getFocusedIndex();
		
		if (index != -1) {
			butEdit.setDisable(false);
			butDel.setDisable(false);
			miEdit.setDisable(false);
			miDel.setDisable(false);
		} else {
			butEdit.setDisable(true);
			butDel.setDisable(true);
			miEdit.setDisable(true);
			miDel.setDisable(true);
		}
	}
	
	@FXML
	private void actionQuitter() {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setTitle("Fermeture de l'application");
		confirm.setHeaderText("Voulez-vous réellement quitter ?");
		confirm.initOwner(this.primaryStage);

		confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		
		Optional<ButtonType> opt = confirm.showAndWait();

		if (opt.get().equals(ButtonType.YES))
			primaryStage.close();
	}
	
	@FXML
	private void actionAbout() {
		Alert about = new Alert(AlertType.INFORMATION);
		about.setTitle("À propos de GestClub");
		about.setHeaderText("Crédits");
		about.setContentText("Plus Moins \nUne superbe application réalisée pendant la semaine IHM.\n\n\n\n");
		about.initOwner(this.primaryStage);
		
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		
		webView.setPrefSize(400, 150);		
		webEngine.load(GestClubApp.class.getResource("resource/about.html").toString());
		
		about.getDialogPane().setContent(webView);
		
		about.showAndWait();
	}
	
	@FXML
	private void actionSave() {
		try {
			Alert save = new Alert(AlertType.CONFIRMATION);
			save.setTitle("Sauvegarde");
			save.setHeaderText("Veuillez choisir le nom du fichier dans lequel sauvegarder");
			TextField tfSave = new TextField("membres");
			save.getDialogPane().setContent(tfSave);
			ButtonType butSave = new ButtonType("Sauvegarder", ButtonData.YES);
			save.getButtonTypes().set(0, butSave);
			save.initOwner(this.primaryStage);

			Optional<ButtonType> opt = save.showAndWait();
			
			if (opt.get().equals(butSave)) {

				ObjectOutputStream flux = new ObjectOutputStream( new FileOutputStream( tfSave.getText() + ".sav" ) );
				
				for (int i = 0; i < this.listeMembres.size(); i++) {
					flux.writeObject(this.listeMembres.get(i));
				}
				
				flux.close();
				
				System.out.println("Sauvegarde OK.");
				
				Alert saveSuccess = new Alert(AlertType.INFORMATION);
				saveSuccess.setTitle("Sauvegarde terminée");
				saveSuccess.setHeaderText("Sauvegardé avec succès dans \"" + tfSave.getText() + ".sav\".");
				saveSuccess.initOwner(this.primaryStage);
				saveSuccess.showAndWait();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Impossible de créer le fichier ");
		} catch (IOException e) {
			System.out.println("Erreur d'écriture");
		}
	}
	
	@FXML
	private void actionRestore() {
		try {
			
			Alert save = new Alert(AlertType.CONFIRMATION);
			save.setTitle("Restauration");
			save.setHeaderText("Veuillez donner le nom du fichier à ouvrir");
			TextField tfSave = new TextField("membres");
			save.getDialogPane().setContent(tfSave);
			save.initOwner(this.primaryStage);
			ButtonType butRest = new ButtonType("Charger", ButtonData.YES);
			save.getButtonTypes().set(0, butRest);

			Optional<ButtonType> opt = save.showAndWait();
			
			if (opt.get().equals(butRest)) {

				ObjectInputStream flux = new ObjectInputStream( new FileInputStream( tfSave.getText() + ".sav" ));
	
				this.listeMembres.clear();
				try {
					for (Membre membre = (Membre) flux.readObject(); membre != null; membre = (Membre) flux.readObject()) {
						this.listeMembres.add(membre);
					}
				} catch (IOException e) {
					System.out.println("Restauration terminée.");
				}
	
				flux.close();
				
				Alert about = new Alert(AlertType.INFORMATION);
				about.setTitle("Restauration terminée");
				about.setHeaderText("Membres restaurés avec succès depuis \"" + tfSave.getText() + ".sav\".");
				about.initOwner(this.primaryStage);
				about.showAndWait();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Impossible de trouver le fichier.");
		} catch (IOException e) {
			System.out.println("Erreur de lecture.");
		} catch (ClassNotFoundException e) {
			System.out.println("Le contenu du fichier n'est pas bon.");
		}
	}
	
	@FXML
	private void actionThemeBright() {
		primaryStage.getScene().getStylesheets().setAll(GestClubApp.class.getResource("resource/bright.css").toExternalForm());
	}
	
	@FXML
	private void actionThemeRed() {
		primaryStage.getScene().getStylesheets().setAll(GestClubApp.class.getResource("resource/flatred.css").toExternalForm());
	}
	
	@FXML
	private void actionThemeBee() {
		primaryStage.getScene().getStylesheets().setAll(GestClubApp.class.getResource("resource/flatbee.css").toExternalForm());
	}
	
	@FXML
	private void actionThemeSky() {
		primaryStage.getScene().getStylesheets().setAll(GestClubApp.class.getResource("resource/sky.css").toExternalForm());
	}

	private void actionClique(MouseEvent mouseEvent) {
		if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
			actionVerifSelect();
			if (mouseEvent.getClickCount() == 2) {
				int index = this.listview.getFocusModel().getFocusedIndex();
				if (index != -1) {
					actionEdit();
				}
			}
		}
	}
	
	public void setGestClubApp(GestClubApp pfGCA) {
		this.gca = pfGCA;
	}

	public ObservableList<Membre> getListeMembres() {
		return this.listeMembres;
		
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest( e -> {e.consume(); actionQuitter();} );
	}

}
