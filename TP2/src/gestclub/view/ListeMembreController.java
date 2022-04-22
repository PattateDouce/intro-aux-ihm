package gestclub.view;

import java.net.URL;
import java.util.ResourceBundle;

import gestclub.GestClubApp;
import gestclub.model.Membre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ListeMembreController implements Initializable {

	@FXML
	private Button butNew;
	
	@FXML
	private Button butEdit;
	
	@FXML
	private Button butDel;
	
	@FXML
	private ListView<Membre> listview;
	
	private ObservableList<Membre> listeMembres;
	
	private GestClubApp gca;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listeMembres = FXCollections.observableArrayList();
	}
	
	@FXML
	private void actionNew() {
		Membre newMembre = gca.showSaisieMembre();
		
		if (newMembre != null)
			listeMembres.add(newMembre);
		
		updateListeMembres();		
	}
	
	@FXML
	private void actionEdit() {
		System.out.println("edit");
	}
	
	@FXML
	private void actionDel() {
		System.out.println("del");
	}
	
	public void updateListeMembres() {
		listview.setItems(this.listeMembres);
	}
	
	public void setGestClubApp(GestClubApp pfGCA) {
		this.gca = pfGCA;
	}

}
