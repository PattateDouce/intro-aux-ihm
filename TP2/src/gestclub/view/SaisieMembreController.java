
package gestclub.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import gestclub.model.EtatMembre;
import gestclub.model.Membre;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	
	private Stage dialogStage;
	
	private Membre Membre;
 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private void actionOk() {
		boolean valide = true;
		
		String nom = txtNom.getText();
		if (nom.length() <= 2 || nom.trim() != nom) {
			txtNom.getStyleClass().add("wrong");
			valide = false;
			System.out.println("Veuillez remplir le champ Nom avec plus de deux charactères et sans espaces au début ou à la fin.");
		} else {
			txtNom.getStyleClass().remove("wrong");
		}

		String prenom = txtPrenom.getText(); 
		if (prenom.length() <= 2 || prenom.trim() != prenom) {
			txtPrenom.getStyleClass().add("wrong");
			valide = false;
			System.out.println("Veuillez remplir le champ Prénom avec plus de deux charactères et sans espaces au début ou à la fin.");
		} else {
			if (!prenom.trim().equals("") && prenom.length() > 2) {
				txtPrenom.getStyleClass().remove("wrong");
			}
		}

		EtatMembre etat = null;
		if (!radioButProspect.isSelected() && !radioButMembre.isSelected() && !radioButAncien.isSelected()) {
			radioButProspect.getParent().getStyleClass().add("wrong");
			valide = false;
			System.out.println("Veuillez choisir un type de membre.");
		} else {
			radioButProspect.getParent().getStyleClass().remove("wrong");
			
			if (radioButProspect.isSelected()) {
				etat = EtatMembre.Prospect;
			} else if (radioButMembre.isSelected()) {
				etat = EtatMembre.Membre;
			} else {
				etat = EtatMembre.Ancien;
			}
		}

		String ville = txtVille.getText();

		LocalDate date = datePickInscription.getValue();
		if (date == null) {
			datePickInscription.getStyleClass().add("wrong");
			valide = false;
			System.out.println("Veuillez choisir une date.");
		} else {
			datePickInscription.getStyleClass().remove("wrong");
		}

		String notes = txtNotes.getText();
		
		if (valide) {
			Membre = new Membre(nom, prenom, etat, ville, date, notes);
			actionAnnuler();
		} else {
			System.out.println("Veuillez remplir le(s) champ(s) entouré(s) en rouge.");
		}
	}
	
	@FXML
	private void actionAnnuler() {
		dialogStage.close();
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public Membre getMembre() {
		return Membre;
	}

}
