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
 * Classe contrôleur qui gère la fenêtre d'informations d'un joueur
 * @author Guibert Rémy
 */
public class PlayerViewController implements Initializable {
	
	/**
	 * Éléments dans le FXML
	 */
	@FXML
	private TextField playerName;
	
	@FXML
	private Label walletLabel;
	
	@FXML
	private Slider walletSlider;
	
	@FXML
	private Label betLabel;
	
	@FXML
	private Slider betSlider;
	
	@FXML
	private VBox content;

	/**
	 * Attributs de la classe
	 */
	private Stage windowStage;
	
	private boolean isCanceled;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Portefeuille
		this.walletSlider.valueProperty().addListener( (obs, oldval, newval) -> this.walletSlider.setValue(newval.intValue()) );
		Bindings.bindBidirectional(this.walletLabel.textProperty(), this.walletSlider.valueProperty(), new NumberStringConverter());
		
		// Mise
		this.betSlider.maxProperty().bind(this.walletSlider.valueProperty());
		this.betSlider.valueProperty().addListener( (obs, oldval, newval) -> this.betSlider.setValue(newval.intValue()) );
		Bindings.bindBidirectional(this.betLabel.textProperty(), this.betSlider.valueProperty(), new NumberStringConverter());
		
		// Lie la touche Entrée à l'action de valider et la toucher Échap à l'action d'annuler
		this.content.setOnKeyPressed( e -> { if (e.getCode().equals(KeyCode.ENTER))
												actionValidate();
											else if (e.getCode().equals(KeyCode.ESCAPE))
												actionCancel(); } );
	}
	
	/** Récupère le Stage de la fenêtre
	 * Sert plus tard pour fermer la fenêtre
	 * @param pfStage Stage de la fenêtre
	 */
	public void setWindowStage(Stage stage) {
		this.windowStage = stage;
		this.windowStage.setOnCloseRequest( e -> actionCancel() );
	}
	
	/** Permet de définir des valeurs par défaut à l'ouverture
	 * @param nom Le nom du joueur
	 * @param solde Le solde du joueur
	 * @param mise La mise du joueur
	 */
	public void setDefaultValues(String nom, int solde, int mise) {
		this.playerName.setText(nom);
		if (solde > 10000)
			this.walletSlider.setMax(solde);
		this.walletSlider.setValue(solde);
		this.betSlider.setValue(mise);
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
		this.windowStage.close();
	}
	
	/**
	 * Informe que l'utilisateur a annulé et ferme la fenètre
	 */
	@FXML
	private void actionCancel() {
		this.isCanceled = true;
		this.windowStage.close();
	}
	
	/** Indique si l'utilisateur a annulé
	 * @return Vrai si il a annulé, Faux sinon
	 */
	public boolean isCanceled() {
		return this.isCanceled;
	}
	
	/** Renvoit le nom entré
	 * @return Le contenu du TextField
	 */
	public String getNom() {
		return this.playerName.getText();
	}
	
	/** Renvoit le solde entré
	 * @return Le valeur du slider portefeuille
	 */
	public int getPortefeuille() {
		return (int) this.walletSlider.getValue();
	}
	
	/** Renvoit la mise entrée
	 * @return Le valeur du slider mise
	 */
	public int getMise() {
		return (int) this.betSlider.getValue();
	}
}
