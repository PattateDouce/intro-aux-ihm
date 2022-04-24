package blackjack.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import blackjack.Main;
import blackjack.om.BlackBot;
import blackjack.om.Carte;
import blackjack.om.EtatBlackBot;
import blackjack.om.MainBlackjack;
import blackjack.util.Util;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Guibert Rémy
 * Classe contrôleur qui gère la fenètre d'écran de jeu
 */
public class GameViewController implements Initializable {
	
	/**
	 * Éléments dans le FXML
	 */
//	@FXML
//	private Menu menuThemes;
	
//	@FXML
//	private ToggleGroup theme;
	
	@FXML
	private Label dealerMessageLab;
	
	@FXML
	private Label dealerHandLab;
	
	@FXML
	private TextArea dealerHandTA;
	
	@FXML
	private HBox playersHBox;
	
	@FXML
	private Button butStart;
	
	@FXML
	private Button butGetSelfCard;
	
	@FXML
	private Button butEndSelfTurn;
	
	/**
	 * Attributs de la classe
	 */
	private Main bj;
	
	private Stage primaryStage;
	
	private IntegerProperty[] settings;
	
	private BlackBot bbot;
	
	private final String buttonFirstRound = "Lancer manche";
	
	private final String buttonNextRound = "Nouvelle manche";
	
	private String[] nomJoueurs;
	
	private int[] soldeJoueurs;
	
	private int[] miseJoueurs;
	
	private int currentPlayer;
	
	private ArrayList<Integer> indexJoueursTab;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialise les attributs
		this.bbot = new BlackBot(7);
		this.nomJoueurs = new String[7];
		this.soldeJoueurs = new int[7];
		this.miseJoueurs = new int[7];
		this.currentPlayer = 0;
		this.indexJoueursTab = new ArrayList<Integer>();
		
		// Créer le premier plateau de joueur
		createPlayerVBox(0);
	}
	
	/** Récupère des objets importants
	 * @param pfBj l'objet Blackjack
	 */
	public void setBj(Main pfBj) {
		this.bj = pfBj;
		
		this.primaryStage = this.bj.getPrimaryStage();
		this.primaryStage.setOnCloseRequest( e -> {e.consume(); actionQuit();} );
		
		this.settings = this.bj.getSettings();
//		RadioMenuItem radio = (RadioMenuItem) this.menuThemes.getItems().get(this.settings[2].getValue());
//		radio.setSelected(true);
	}
	
	/** Méthode créant une VBox Joueur vide
	 * @param indexPlayerVBox Indice de la VBox à créer
	 */
	private void createPlayerVBox(int indexPlayerVBox) {
		Label newPlayerLabel = new Label();
		newPlayerLabel.setMinHeight(60);
		newPlayerLabel.setMaxWidth(Double.MAX_VALUE);
		
		Button newPlayerButton = new Button("Ajouter joueur");
		newPlayerButton.getStyleClass().add("buttonGreenLittle");
		newPlayerButton.setPrefWidth(125);
		newPlayerButton.setOnAction( e -> actionMiser(e, indexPlayerVBox, indexPlayerVBox) );
		
		TextArea newPlayerTextArea = new TextArea();
		newPlayerTextArea.setVisible(false);
		newPlayerTextArea.setEditable(false);
		newPlayerTextArea.setWrapText(true);
		
		VBox newPlayerVBox = new VBox(newPlayerLabel, newPlayerButton, newPlayerTextArea);
		newPlayerVBox.getStyleClass().add("playerVBox");
		newPlayerVBox.setMinWidth(134);
		newPlayerVBox.setMaxWidth(160);

		VBox.setMargin(newPlayerLabel, new Insets(45, 0, 0, 0));
		VBox.setMargin(newPlayerTextArea, new Insets(3));
		
		this.playersHBox.getChildren().add(newPlayerVBox);
		
		if (indexPlayerVBox == 0) // La plus à gauche à une marge à gauche de 4px
			HBox.setMargin(getPlayerVBox(indexPlayerVBox), new Insets(0, 0, 0, 4));
		else if (indexPlayerVBox == 6) // La plus à droite à une marge à droite de 4px
			HBox.setMargin(getPlayerVBox(indexPlayerVBox), new Insets(0, 4, 0, 2));
		else
			HBox.setMargin(getPlayerVBox(indexPlayerVBox), new Insets(0, 0, 0, 2));
	}
	
	/**
	 * Action liée à un RadioMenuItem permettant d'ouvrir la fenètre des règles
	 */
	@FXML
	private void actionRules() {
		Util.showRules(this.primaryStage);
	}
	
	/**
	 * Ouvre une nouvelle fenètre avec les crédits
	 */
	@FXML
	private void actionAbout() {
		Util.showAbout(this.primaryStage);
	}
	
	/**
	 * Revient à l'écran d'accueil, sauf si un joueur a misé.
	 */
	@FXML
	private void actionGoBackToMain() {
		if (isPlaying()) {
			Alert confirm = new Alert(AlertType.WARNING);
			confirm.initOwner(this.primaryStage);
			confirm.setTitle("Retour à l'écran titre");
			confirm.setHeaderText("Toute progression sera perdu");
			confirm.setContentText("Voulez-vous vraiment revenir à l'écran titre ?");
			confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
			
			Optional<ButtonType> choix = confirm.showAndWait();
	
			if (choix.get().equals(ButtonType.YES))
				this.bj.loadMainView();
		} else
			this.bj.loadMainView();
	}
	
	/**
	 * Quitte le jeu, sauf si un joueur a misé.
	 */
	@FXML
	private void actionQuit() {
		if (isPlaying()) {
			Alert confirm = new Alert(AlertType.WARNING);
			confirm.initOwner(this.primaryStage);
			confirm.setTitle("Fermeture du jeu");
			confirm.setHeaderText("Toute progression sera perdu");
			confirm.setContentText("Voulez-vous vraiment quitter ?");
			confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
			
			Optional<ButtonType> choix = confirm.showAndWait();
	
			if (choix.get().equals(ButtonType.YES))
				this.primaryStage.close();
		} else
			this.primaryStage.close();
	}
	
	/** Appellé par les boutons de création ou de modification de joueur
	 * Permet de créer ou modifier un joueur, ou seulement sa mise si une partie à été lancée
	 * @param event L'évènement déclancheur, permet de modifier le texte du bouton 
	 * @param indexPlayerTab Indice dans les tableau d'informations des joueurs
	 * @param indexPlayerVBox Indice dans la liste de VBox
	 */
	private void actionMiser(ActionEvent event, int indexPlayerTab, int indexPlayerVBox) {
		if (this.butStart.getText().equals(this.buttonFirstRound) || this.soldeJoueurs[indexPlayerTab] != 0) {
			try {
				Button playerBut = (Button) event.getSource();
				
				Stage stagePlayer = new Stage();
				stagePlayer.setResizable(false);
				stagePlayer.initOwner(this.primaryStage);
				stagePlayer.initModality(Modality.APPLICATION_MODAL);
				stagePlayer.getIcons().setAll(this.primaryStage.getIcons());
				
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/JoueurView.fxml"));
			
				BorderPane borderPane = loader.load();
				
				JoueurViewController ctrl = loader.getController();
				ctrl.setPrimaryStage(stagePlayer);
				
				// Donne les valeurs déjà enregistrées ou non à afficher
				if (this.nomJoueurs[indexPlayerTab]== null) {
					ctrl.setDefaultValues("Joueur " + (indexPlayerTab+1), this.settings[0].getValue(), this.settings[1].getValue());
					stagePlayer.setTitle("Création d'un joueur");
				} else if (this.butStart.getText().equals(this.buttonNextRound) && this.miseJoueurs[indexPlayerTab] == 0) {
					ctrl.setDefaultValues(this.nomJoueurs[indexPlayerTab], this.soldeJoueurs[indexPlayerTab], this.settings[1].getValue());
				} else {
					ctrl.setDefaultValues(this.nomJoueurs[indexPlayerTab], this.soldeJoueurs[indexPlayerTab], this.miseJoueurs[indexPlayerTab]);
					stagePlayer.setTitle("Modification de " + this.nomJoueurs[indexPlayerTab]);
				}
				
				// Si la phase de création de joueur ets passer alors on ne peut plus changer que la mise
				if (this.butStart.getText().equals(this.buttonNextRound)) {
					ctrl.modeMise();
					stagePlayer.setTitle("Choix de la mise de " + this.nomJoueurs[indexPlayerTab]);
				}
				
				Scene scene = new Scene(borderPane);
				scene.getStylesheets().setAll(this.primaryStage.getScene().getStylesheets());
				
				stagePlayer.setScene(scene);
				stagePlayer.showAndWait();
				
				// On ne fait rien si l'utilisateur annule
				if (!ctrl.isCanceled()) {
					
					// Enregistre les données entrées
					if (ctrl.getNom().trim().equals(""))
						this.nomJoueurs[indexPlayerTab] = "Joueur " + (indexPlayerTab+1);
					else
						this.nomJoueurs[indexPlayerTab] = ctrl.getNom().trim();
					this.soldeJoueurs[indexPlayerTab] = ctrl.getPortefeuille();
					this.miseJoueurs[indexPlayerTab] = ctrl.getMise();
					
					// On met à jour les infos du joueur
					Label playerLab;
					if (this.butStart.getText().equals(this.buttonFirstRound))
						playerLab = getPlayerLabel(indexPlayerTab);
					else
						playerLab = getPlayerLabel(indexPlayerVBox);
					playerLab.setText(
								this.nomJoueurs[indexPlayerTab] + "\n" +
								"Portefeuille : " + (this.soldeJoueurs[indexPlayerTab]-this.miseJoueurs[indexPlayerTab]) + "\n" +
								"Mise : " + this.miseJoueurs[indexPlayerTab]);
					VBox.setMargin(playerLab, new Insets(71, 0, 0, 0));
					
					// Réinitialise l'ArrayList des joueurs (pour qu'ils soit dans l'ordre des indices)
					this.indexJoueursTab.clear();
					for (int i = 0; i < 7; i ++) {
						if (this.miseJoueurs[i] > 0) {
							this.indexJoueursTab.add(i);
						}
					}
					
					// Si il y a des joueurs avec des mises on active le bouton de manche et on change le texte de la bulle
					if (this.indexJoueursTab.size() > 0) {
						this.butStart.setDisable(false);
						if (this.butStart.getText().equals(this.buttonFirstRound))
							this.dealerMessageLab.setText("Vous pouvez lancer une manche (" + this.indexJoueursTab.size() + " joueur(s))");
						else
							this.dealerMessageLab.setText("Vous pouvez lancer une nouvelle manche (" + this.indexJoueursTab.size() + " joueur(s))");
					} else {
						if (this.butStart.getText().equals(this.buttonFirstRound))
							this.dealerMessageLab.setText("Au moins un joueur doit miser pour lancer une manche");
						else
							this.dealerMessageLab.setText("Au moins un joueur doit miser pour lancer une nouvelle manche");
					}
					
					if (this.butStart.getText().equals(this.buttonFirstRound)) {
						playerBut.setText("Modifier");
					}
						
					// Si on a cliquer sur le dernier plateau de la liste et qu'il
					// n'y a pas déjà 7 plateaux, alors on en crée un nouveau
					int nbVBox = this.playersHBox.getChildren().size();
					if (indexPlayerTab == nbVBox-1 && nbVBox < 7 && this.butStart.getText().equals(this.buttonFirstRound)) {
						createPlayerVBox(indexPlayerTab+1);
					}
				}
			} catch (IOException e) {
				System.out.println("Ressource ChoixMiseView.fxml non disponible");
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			Alert dialog = new Alert(AlertType.INFORMATION);
			dialog.initOwner(this.primaryStage);
			dialog.setTitle("Portefeuille vide");
			dialog.setHeaderText(this.nomJoueurs[indexPlayerTab] + " n'a plus d'argent, il ne peut plus miser.");
			dialog.setContentText("Il sera retiré des joueurs au démarrage de la prochaine manche.");
			dialog.showAndWait();
		}
	}

	/**
	 * Lance la première manche et les suivantes, s'occuppe aussi de distribuer les gains
	 */
	@FXML
	private void actionStart() {
		if (this.bbot.getEtat() == EtatBlackBot.MISE) { // Lançage de partie
			
			// Mise et met à jour le solde
			for (int i = 0; i < this.indexJoueursTab.size(); i++) {
				int indexPlayerTab = this.indexJoueursTab.get(i);
				int tempMise = this.miseJoueurs[indexPlayerTab];
				bbot.miser(indexPlayerTab, tempMise);
				this.soldeJoueurs[indexPlayerTab] -= tempMise;
			}
			
			this.bbot.distribuer();
			
			this.butStart.setDisable(true);
			this.butGetSelfCard.setDisable(false);
			this.butEndSelfTurn.setDisable(false);
			this.dealerHandLab.setVisible(true);
			this.dealerHandTA.setVisible(true);
			this.dealerHandTA.setDisable(false);
			this.dealerHandTA.setText(cartesMain(bbot.getMainBanque()));
			
			// Détermine la première VBox contenant un joueur
			int indexPrem = 0;
			for (int i = 0; i < 7; i++) {
				if (nomJoueurs[i] != null) {
					indexPrem = i;
					i += 7; // met fin à la boucle
				}
			}
			
			// Supprime les VBox qui n'ont pas de mise
			int indexDel = 0;
			for (int i  = indexPrem; this.playersHBox.getChildren().size() != this.indexJoueursTab.size(); i ++) {
				if (!this.indexJoueursTab.contains(i)) {
					this.soldeJoueurs[i] = 0; // pour savoir quand il n'y a plus personne qui peut jouer
					this.nomJoueurs[i] = null;
					playersHBox.getChildren().remove(indexDel);
				} else {
					indexDel ++;
				}
			}
			
			for (int i = 0; i < this.indexJoueursTab.size(); i++) {
				// Met à jour les plateaux des joueurs (contenu)
				getPlayerVBox(i).getChildren().remove(1);
				
				// Met à jour les plateaux des joueurs (position et affichage)
				VBox.setMargin(getPlayerLabel(i), new Insets(0));
				getPlayerTextArea(i).setVisible(true);
				
				// Affiche les cartes des joueurs
				getPlayerTextArea(i).setText(cartesMain(bbot.getMainJoueurs(this.indexJoueursTab.get(i))));
			}
			
			this.dealerMessageLab.setText(this.nomJoueurs[this.indexJoueursTab.get(0)] + " à toi de jouer");
			
			// Met en avant le joueur qui doit jouer
			getPlayerVBox(0).getStyleClass().add("currentplayer");
			
		} else { // Récupération des gains
			
			int tempSom = 0;
			for (int i = 0; i < soldeJoueurs.length; i++) {
				tempSom += soldeJoueurs[i];
			}
			
			if (tempSom == 0) { // Si plus aucun joueur ne peut jouer
				Alert gameFinished = new Alert(AlertType.CONFIRMATION);
				gameFinished.initOwner(this.primaryStage);
				gameFinished.setTitle("Fin de partie");
				gameFinished.setHeaderText("Plus aucun joueur n'est en capacité de jouer");
				gameFinished.setContentText("Voulez vous réinitialiser le plateau ?");
				
				ButtonType butClose = new ButtonType("Fermer le jeu", ButtonData.CANCEL_CLOSE);
				
				gameFinished.getButtonTypes().set(0, ButtonType.YES);
				gameFinished.getButtonTypes().set(1, butClose);
				
				Optional<ButtonType> choix = gameFinished.showAndWait();
				
				if (choix.get().equals(ButtonType.YES))
					this.bj.loadGameView();
				else
					this.primaryStage.close();
			} else {
				for (int i = 0; i < this.indexJoueursTab.size(); i++) {
					int indexPlayerTab = this.indexJoueursTab.get(i);
					
					this.miseJoueurs[indexPlayerTab] = 0;
					VBox.setMargin(getPlayerLabel(i), new Insets(71, 0, 0, 0));
					
					Button playerBut = new Button("Modifier mise");
					playerBut.setPrefWidth(125);
					playerBut.getStyleClass().add("buttonGreenLittle");
					
					getPlayerVBox(i).getChildren().add(1, playerBut);
					getPlayerVBox(i).getChildren().get(2).setVisible(false);
					
					int ind = i;
					playerBut.setOnAction( e -> actionMiser(e, indexPlayerTab, ind) );
					
					getPlayerLabel(i).setText(
								this.nomJoueurs[indexPlayerTab] + "\n" +
								"Portefeuille : " + this.soldeJoueurs[indexPlayerTab] + "\n" +
								"Mise : " + this.miseJoueurs[indexPlayerTab]);
					
					getPlayerVBox(i).setDisable(false);
				}
				this.butStart.setDisable(true);
				this.butStart.setText(this.buttonNextRound);
				this.dealerMessageLab.setText("Au moins un joueur doit miser pour lancer une nouvelle manche");
				this.dealerHandLab.setVisible(false);
				this.dealerHandTA.setVisible(false);
				this.indexJoueursTab.clear();
				bbot.relancerPartie();
			}
		}
	}
	
	/**
	 * Lié au bouton de demande de carte.
	 * Fait tirer un carte au joueur actuel et met à jour sa main.
	 * Si le joueur perd alors on passe au joueur suivant si il y en a, sinon, le croupier joue.
	 */
	@FXML
	private void actionDrawACard() {
		int indexPlayerTab = this.indexJoueursTab.get(this.currentPlayer);
		
		this.bbot.tirer(indexPlayerTab);
		getPlayerTextArea(this.currentPlayer).setText(cartesMain(bbot.getMainJoueurs(indexPlayerTab)));
		
		if (this.bbot.getFinJoueurs(indexPlayerTab)) {
			// Retire la mise en avant
			getPlayerVBox(this.currentPlayer).getStyleClass().remove("currentplayer");
			// Si le jouer dépasse 21 alors son TextArea est désactivé
			getPlayerVBox(this.currentPlayer).setDisable(true);
			checkFin();
		}
	}
	
	/**
	 * Lié au bouton de fin de tour.
	 * Met fait au tour du joueur actuel.
	 * On passe au joueur suivant si il y en a, sinon, le croupier joue.
	 */
	@FXML
	private void actionEndSelfTurn() {
		this.bbot.terminer(this.indexJoueursTab.get(this.currentPlayer));
		// Retire la mise en avant
		getPlayerVBox(this.currentPlayer).getStyleClass().remove("currentplayer");
		checkFin();
	}
	
	/**
	 * Vérifie si tous les joueurs on joué, si c'est le cas alors on fait jouer le croupier
	 * Sinon on passe au joueur suivant
	 */
	private void checkFin() {
		if (this.indexJoueursTab.size()-1 == this.currentPlayer) {
			tourCroupier();
		} else {
			this.currentPlayer ++;
			this.dealerMessageLab.setText(this.nomJoueurs[this.indexJoueursTab.get(this.currentPlayer)] + " à toi de jouer");
			// Met en avant le joueur qui doit jouer
			getPlayerVBox(this.currentPlayer).getStyleClass().add("currentplayer");
		}
	}
	
	/**
	 * Fait jouer le croupier, et afficher les gains de chaque joueur
	 */
	private void tourCroupier() {
		this.butGetSelfCard.setDisable(true);
		this.butEndSelfTurn.setDisable(true);
		this.dealerHandTA.setText(cartesMain(bbot.getMainBanque()));
		if (bbot.getMainBanque().isPerdante())
			this.dealerHandTA.setDisable(true);
		
		for (int i = 0; i < this.indexJoueursTab.size(); i++) {
			int indexPlayerTab = this.indexJoueursTab.get(i);
			getPlayerLabel(i).setText(
						this.nomJoueurs[indexPlayerTab] + "\n" +
						"Portefeuille : " + this.soldeJoueurs[indexPlayerTab] + "\n" +
						"Gain : " + this.bbot.getGainJoueurs(indexPlayerTab));
			this.soldeJoueurs[indexPlayerTab] += this.bbot.getGainJoueurs(indexPlayerTab);
		}
		
		this.dealerMessageLab.setText("Manche terminée, vous pouvez récupérer vos gains");
		this.butStart.setText("Récupérer gains");
		this.butStart.setDisable(false);
		this.currentPlayer = 0;
	}
	
	/** Méthode permettant de récupérer le Label d'un joueur en fonction de son indice
	 * @param index Indice du joueur
	 * @return L'élement Label
	 */
	private Label getPlayerLabel(int indexPlayer) {
		return (Label) getPlayerVBox(indexPlayer).getChildren().get(0);
	}
	
	/** Méthode permettant de récupérer la TextArea d'un joueur en fonction de son indice
	 * @param index Indice du joueur
	 * @return L'élement TextArea
	 */
	private TextArea getPlayerTextArea(int indexPlayer) {
		// En mode mise il y a un bouton en position 1, autrement il n'y en a pas
		if (this.bbot.getEtat() == EtatBlackBot.MISE)
			return (TextArea) getPlayerVBox(indexPlayer).getChildren().get(2);
		else
			return (TextArea) getPlayerVBox(indexPlayer).getChildren().get(1);
	}
	
	/** Méthode permettant de récupérer la VBox d'un joueur en fonction de son indice
	 * @param index Indice du joueur
	 * @return L'élement VBox
	 */
	private VBox getPlayerVBox(int indexPlayer) {
		return (VBox) this.playersHBox.getChildren().get(indexPlayer);
	}
	
	/** Renvoie les cartes et le score d'un joueur sous une meilleur forme
	 * @param pfMainJoueur Main du joueur
	 * @return Le texte des cartes du joueur
	 */
	private String cartesMain(MainBlackjack pfMainJoueur) {
		List<Carte> mainJoueur = pfMainJoueur.getCartes();
		String cartesJoueur = "";
		
		for (int j = 0; j < mainJoueur.size(); j++) {
			if (mainJoueur.get(j).getValeur() == 0) {
				cartesJoueur += "A";
			} else if (mainJoueur.get(j).getValeur() == 10) {
				if (mainJoueur.get(j).getHauteur() == 9) {
					cartesJoueur += "10";
				} else if (mainJoueur.get(j).getHauteur() == 10) {
					cartesJoueur += "J";
				} else if (mainJoueur.get(j).getHauteur() == 11) {
					cartesJoueur += "Q";
				} else
					cartesJoueur += "K";
			} else {
				cartesJoueur += mainJoueur.get(j).getValeur();
			}
			
			if (mainJoueur.get(j).getCouleur() == 0)
				cartesJoueur += "♠ ";
			else if (mainJoueur.get(j).getCouleur() == 1)
				cartesJoueur += "♥ ";
			else if (mainJoueur.get(j).getCouleur() == 2)
				cartesJoueur += "♣ ";
			else
				cartesJoueur += "♦ ";
			
			cartesJoueur += mainJoueur.get(j).getNomComptet() + "\n";
		}
		
		return cartesJoueur + "\n" + pfMainJoueur.getScore() + " points";
	}
	
	private boolean isPlaying() {
		int sum = 0;
		
		for (int i = 0; i < soldeJoueurs.length; i++) {
			sum += soldeJoueurs[i];
		}
		
		if (sum > 0)
			return true;
		
		return false;
	}
}