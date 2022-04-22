package blackjack.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Guibert Rémy
 * Classe contrôleur qui gère la fenètre d'informations d'un joueur
 */
public class ChoixMiseController implements Initializable {
	
	/**
	 * Éléments dans le FXML
	 */
	@FXML
	private TextField nomJoueurTextField;
	
	@FXML
	private Label portefeuilleLabel;
	
	@FXML
	private Slider portefeuilleSlider;
	
	@FXML
	private Label miseLabel;
	
	@FXML
	private Slider miseSlider;
	
	@FXML
	private VBox content;

	/**
	 * Attributs de la classe
	 */
	private Stage primaryStage;
	
	private boolean isCanceled;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Portefeuille
		this.portefeuilleSlider.valueProperty().addListener( (obs, oldval, newval) -> this.portefeuilleSlider.setValue(newval.intValue()) );
		Bindings.bindBidirectional(this.portefeuilleLabel.textProperty(), this.portefeuilleSlider.valueProperty(), new NumberStringConverter());
		
		// Mise
		this.miseSlider.maxProperty().bind(this.portefeuilleSlider.valueProperty());
		this.miseSlider.valueProperty().addListener( (obs, oldval, newval) -> this.miseSlider.setValue(newval.intValue()) );
		Bindings.bindBidirectional(this.miseLabel.textProperty(), this.miseSlider.valueProperty(), new NumberStringConverter());
		
		// Lie la touche Entrée à l'action de valider et la toucher Échap à l'action d'annuler
		this.content.setOnKeyPressed( e -> { if (e.getCode().equals(KeyCode.ENTER))
												actionValidate();
											else if (e.getCode().equals(KeyCode.ESCAPE))
												actionCancel(); } );
	}
	
	/** Récupère le primaryStage du controlleur parent
	 * @param pfStage
	 */
	public void setPrimaryStage(Stage pfStage) {
		this.primaryStage = pfStage;
		this.primaryStage.setOnCloseRequest( e -> actionCancel() );
	}
	
	/** Permet de définir des valeurs par défaut à l'ouverture
	 * @param nom Le nom du joueur
	 * @param solde Le solde du joueur
	 * @param mise La mise du joueur
	 */
	public void setDefaultValues(String nom, int solde, int mise) {
		this.nomJoueurTextField.setText(nom);
		if (solde > 10000)
			this.portefeuilleSlider.setMax(solde);
		this.portefeuilleSlider.setValue(solde);
		this.miseSlider.setValue(mise);
	}
	
	/**
	 * Mode dans lequel seul la mise peut être modifiée
	 */
	public void modeMise() {
		for (int i = 0; i < 5; i++) {
			this.content.getChildren().remove(0);
		}
		this.content.setPrefHeight(VBox.USE_COMPUTED_SIZE);
	}
	
	/**
	 * Informe que l'utilisateur n'a pas annuler et ferme la fenètre
	 */
	@FXML
	private void actionValidate() {
		this.isCanceled = false;
		this.primaryStage.close();
	}
	
	/**
	 * Informe que l'utilisateur a annuler et ferme la fenètre
	 */
	@FXML
	private void actionCancel() {
		this.isCanceled = true;
		this.primaryStage.close();
	}
	
	/** Indique si l'utilisateur a annuler
	 * @return Vrai si il a annulé, Faux sinon
	 */
	public boolean isCanceled() {
		return this.isCanceled;
	}
	
	/** Renvoit le nom entré
	 * @return Le contenu du TextField
	 */
	public String getNom() {
		return this.nomJoueurTextField.getText();
	}
	
	/** Renvoit le solde entré
	 * @return Le valeur du slider portefeuille
	 */
	public int getPortefeuille() {
		return (int) this.portefeuilleSlider.getValue();
	}
	
	/** Renvoit la mise entrée
	 * @return Le valeur du slider mise
	 */
	public int getMise() {
		return (int) this.miseSlider.getValue();
	}
}
