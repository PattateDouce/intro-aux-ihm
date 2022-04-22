package version2;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class PlusMoinsController implements Initializable {
	
	@FXML
	private TextField zoneTexte;
	
	private Stage     fenetrePrincipale = null;
	
	@FXML
	private Slider slider;
	
	private IntegerProperty intProp;
	
	public void setFenetrePrincipale(Stage fenetrePrincipale) {
		this.fenetrePrincipale = fenetrePrincipale;
		this.fenetrePrincipale.setOnCloseRequest(event -> {event.consume(); actionQuitter();} );
	}
	
	private void actionAjouter(int valeur) {
		//int nb = Integer.parseInt( this.zoneTexte.getText() );
		//this.zoneTexte.setText(""+(nb+valeur));
		this.intProp.set(this.intProp.get() + valeur);
	}
	@FXML
	private void actionRAZ() {
		//this.zoneTexte.setText("10");
		this.intProp.set(10);
	}
	@FXML
	private void actionPlus() {
		actionAjouter(1);
	}
	@FXML
	private void actionMoins() {
		actionAjouter(-1);
	}
	
	@FXML
	private void actionQuitter() {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setTitle("Fermeture de l'application");
		confirm.setHeaderText("Voulez-vous réellement quitter ?");
		confirm.initOwner(this.fenetrePrincipale);

		ButtonType boutonPlusTard = new ButtonType("Plus tard...");
		confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, boutonPlusTard);
		
		Optional<ButtonType> opt = confirm.showAndWait();

		if (opt.get().equals(ButtonType.NO)) {
			System.out.println("Vous avez cliqué sur le bouton \"Non\"");
		} else if (opt.get().equals(ButtonType.YES)) {
			System.out.println("La dernière valeur du compteur était : " + this.intProp.get());
			fenetrePrincipale.close();
		} else if (opt.get().equals(boutonPlusTard)) {
			System.out.println("Vous avez cliqué sur le bouton \"Plus tard...\"");
		}
	}
	
	@FXML
	private void actionAbout() {
		Alert about = new Alert(AlertType.INFORMATION);
		about.setTitle("À propos");
		about.setHeaderText("Crédits");
		about.setContentText("Plus Moins \nUne superbe application réalisée pendant la semaine IHM.\n\n\n\n");
		about.initOwner(this.fenetrePrincipale);
		
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		
		webView.setPrefSize(400, 300);
		webEngine.load("https://www.iut-blagnac.fr/");
		
		about.getDialogPane().setContent(webView);
		
		about.showAndWait();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void setPropriteCompteur(IntegerProperty intProp) {
		this.intProp = intProp;
		Bindings.bindBidirectional(zoneTexte.textProperty(), this.intProp, new NumberStringConverter());
		slider.valueProperty().bindBidirectional(this.intProp);
	}
	
}
