
package gestclub.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import gestclub.model.EtatMembre;
import gestclub.model.Membre;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaisieMembreController implements Initializable {

	@FXML
	private TextField txtNom;

	@FXML
	private TextField txtPrenom;

	@FXML
	private TextField txtVille;

	@FXML
	private RadioButton radioButProspect;

	@FXML
	private RadioButton radioButMembre;

	@FXML
	private RadioButton radioButAncien;

	@FXML
	private DatePicker datePickInscription;

	@FXML
	private TextArea txtNotes;
	
	@FXML
	private Button butOk;
	
	private Stage dialogStage;
	
	private Membre membre;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@FXML
	private void actionOk() {
		String erreurs = "";
		
		String nom = txtNom.getText();
		if (nom.length() <= 2 || nom.trim() != nom) {
			txtNom.getStyleClass().add("wrong");
			erreurs += "- Veuillez remplir le champ \"Nom\" avec plus de deux charactères et sans espaces au début ou à la fin.\n";
		} else {
			txtNom.getStyleClass().remove("wrong");
		}

		String prenom = txtPrenom.getText(); 
		if (prenom.length() <= 2 || prenom.trim() != prenom) {
			txtPrenom.getStyleClass().add("wrong");
			erreurs += "- Veuillez remplir le champ \"Prénom\" avec plus de deux charactères et sans espaces au début ou à la fin.\n";
		} else {
			if (!prenom.trim().equals("") && prenom.length() > 2) {
				txtPrenom.getStyleClass().remove("wrong");
			}
		}

		EtatMembre etat = null;
		if (radioButProspect.isSelected()) {
			etat = EtatMembre.Prospect;
		} else if (radioButMembre.isSelected()) {
			etat = EtatMembre.Membre;
		} else if (radioButAncien.isSelected()) {
			etat = EtatMembre.Ancien;
		}
		if (etat == null) {
			radioButProspect.getParent().getStyleClass().add("wrong");
			erreurs += "- Veuillez choisir un type de membre.\n";
		} else {
			radioButProspect.getParent().getStyleClass().remove("wrong");
			
			
		}

		String ville = txtVille.getText();

		LocalDate date = datePickInscription.getValue();
		if (date == null) {
			datePickInscription.getStyleClass().add("wrong");
			erreurs += "- Veuillez choisir une date.\n";
		} else {
			datePickInscription.getStyleClass().remove("wrong");
		}

		String notes = txtNotes.getText();
		
		if (erreurs.equals("")) {
			this.membre = new Membre(nom, prenom, etat, ville, date, notes);
			actionAnnuler();
		} else {
			Alert errors = new Alert(AlertType.WARNING);
			errors.setContentText(erreurs);
			errors.initOwner(dialogStage);
			errors.showAndWait();
		}
	}
	
	private void initialiseMembre() {
		txtNom.setText(this.membre.getNom());

		txtPrenom.setText(this.membre.getPrenom());

		txtVille.setText(this.membre.getVille());

		if (this.membre.getEtat().equals(EtatMembre.Prospect)) {
			radioButProspect.setSelected(true);
		} else if (this.membre.getEtat().equals(EtatMembre.Membre)) {
			radioButMembre.setSelected(true);
		} else {
			radioButAncien.setSelected(true);
		}

		datePickInscription.setValue(this.membre.getDateInscription());

		txtNotes.setText(this.membre.getNotes());
	}
	
	@FXML
	private void actionAnnuler() {
		dialogStage.close();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public Membre getMembre() {
		return this.membre;
	}
	
	public void setMembre(Membre pfMembre) {
		this.membre = pfMembre;
		if (this.membre != null)
			initialiseMembre();
	}

	public void setButtonOKText(String pfButtonOKText) {
		this.butOk.setText(pfButtonOKText);
		
	}

}
