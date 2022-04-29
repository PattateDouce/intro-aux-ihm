package blackjack.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import blackjack.Main;
import blackjack.Util;
import blackjack.om.BlackBot;
import blackjack.om.Carte;
import blackjack.om.EtatBlackBot;
import blackjack.om.MainBlackjack;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
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
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Classe contrôleur qui gère la fenêtre d'écran de jeu
 * @author Guibert Rémy
 */
public class GameViewController implements Initializable {
	
	/**
	 * Éléments dans le FXML
	 */
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
	private Button butDrawCard;
	
	@FXML
	private Button butEndTurn;
	
	@FXML
	private MenuBar menuBar;
	
	/**
	 * Attributs de la classe
	 */
	private Main main;
	
	private Stage primaryStage;
	
	private DoubleProperty[] offsets;
	
	private IntegerProperty[] settings;
	
	private ObjectProperty<Locale> locale;
	
	private ResourceBundle resources;
	
	private ObjectProperty<MediaPlayer> mediaPlayer;
	
	private final String[] cardsName = new String[13];
	
	private final String[] colorsName = new String[4];
	
	private final String[] colorsSymbols = {"♠ ", "♥ ", "♣ ", "♦ "};
	
	private String[] playersName;
	
	private int[] playersWallet;
	
	private int[] playersBet;
	
	private ArrayList<Integer> playersIndexTab;
	
	private BlackBot blackbot;
	
	private int currentPlayer;
	
	private boolean firstRound = true;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialise les attributs
		this.resources = resources;
		this.playersName = new String[7];
		this.playersWallet = new int[7];
		this.playersBet = new int[7];
		this.playersIndexTab = new ArrayList<Integer>();
		this.blackbot = new BlackBot(7);
		this.currentPlayer = 0;
		
		// Initialise le nom de cartes et leurs couleurs
		for (int i = 0; i < 13; i++)
			this.cardsName[i] = resources.getString("card." + (i+1));
		for (int i = 0; i < 4; i++)
			this.colorsName[i] = resources.getString("card.color." + (i+1));
		
		// Créer le premier plateau de joueur
		createPlayerVBox(0);
		
		// Barre de menus
		Label lab = new Label(resources.getString("settings"));
		lab.setOnMouseClicked( e -> Util.loadSettingsView(this.main, this.primaryStage, this.locale.getValue(), this.offsets, true) );
		menuBar.getMenus().add(0, new Menu("", lab));
	}
	
	/** Récupère l'instance du programme principal pour initialiser les objets
	 * @param main L'objet Main
	 */
	public void initializeObjects(Main main) {
		this.main = main;
		
		this.primaryStage = this.main.getPrimaryStage();
		this.primaryStage.setOnCloseRequest( e -> {e.consume(); actionQuit();} );
		
		this.offsets = this.main.getOffsets();
		
		this.settings = this.main.getSettings();
		
		this.locale = this.main.getLocale();
		
		this.mediaPlayer = this.main.getMediaPlayer();
	}
	
	/** Méthode créant une VBox Joueur vide
	 * @param indexPlayerVBox Indice de la VBox à créer
	 */
	private void createPlayerVBox(int indexPlayerVBox) {
		Label newPlayerLab = new Label();
		newPlayerLab.setMinHeight(60);
		newPlayerLab.setMaxWidth(Double.MAX_VALUE);
		
		Button newPlayerBut = new Button(this.resources.getString("button.create"));
		newPlayerBut.getStyleClass().add("buttonGreenLittle");
		newPlayerBut.setPrefWidth(125);
		newPlayerBut.setOnAction( e -> actionMiser(e, indexPlayerVBox, indexPlayerVBox) );
		
		TextArea newPlayerTA = new TextArea();
		newPlayerTA.setVisible(false);
		newPlayerTA.setEditable(false);
		newPlayerTA.setWrapText(true);
		
		VBox newPlayerVBox = new VBox(newPlayerLab, newPlayerBut, newPlayerTA);
		newPlayerVBox.getStyleClass().add("playerVBox");
		newPlayerVBox.setMinWidth(134);
		newPlayerVBox.setMaxWidth(160);

		VBox.setMargin(newPlayerLab, new Insets(45, 0, 0, 0));
		VBox.setMargin(newPlayerTA, new Insets(3));
		
		this.playersHBox.getChildren().add(newPlayerVBox);
		
		int left = 2;
		int right = 0;
		if (indexPlayerVBox == 0) // La plus à gauche à une marge à gauche de 4px
			left = 4;
		else if (indexPlayerVBox == 6) // La plus à droite à une marge à droite de 4px
			right = 4;
		
		HBox.setMargin(getPlayerVBox(indexPlayerVBox), new Insets(0, right, 0, left));
	}
	
	/**
	 * Ouvre une nouvelle fenètre avec les règles
	 */
	@FXML
	private void actionRules() {
		Util.dialog(this.primaryStage, this.locale.getValue(), "rules.title", "rules.html");
	}
	
	/**
	 * Ouvre une nouvelle fenètre avec les crédits
	 */
	@FXML
	private void actionAbout() {
		Util.dialog(this.primaryStage, this.locale.getValue(), "about.title", "about.html");
	}
	
	/**
	 * Revient à l'écran d'accueil, sauf si un joueur existe
	 */
	@FXML
	private void actionBack() {
		if (isPlaying()) {
			if (!Util.dialogConfirm(this.primaryStage, this.locale.getValue(), "back"))
				return;
		}
		this.mediaPlayer.getValue().stop();
		Util.loadMusic(this.mediaPlayer, "Memoir of Summer - David Luong.m4a", this.settings[3].getValue());
		Util.loadMainOrGameView(this.main, primaryStage, this.locale.getValue(), this.offsets, "Main");
	}
	
	/**
	 * Quitte le jeu, sauf si un joueur existe
	 */
	@FXML
	private void actionQuit() {
		if (isPlaying()) {
			if (!Util.dialogConfirm(this.primaryStage, this.locale.getValue(), "quit"))
				return;
		}
		this.primaryStage.close();
	}
	
	/** Appellé par les boutons de création ou de modification de joueur
	 * Permet de créer ou modifier un joueur, ou seulement sa mise si une partie à était lancée
	 * @param event L'évènement déclancheur, permet de modifier le texte du bouton 
	 * @param playerIndexTab Indice dans les tableaux d'informations des joueurs
	 * @param playerIndexVBox Indice dans la liste de VBox
	 */
	private void actionMiser(ActionEvent event, int playerIndexTab, int playerIndexVBox) {
		if (firstRound || this.playersWallet[playerIndexTab] != 0) {
			try {
				Button playerBut = (Button) event.getSource();
				
				Stage stagePlayer = new Stage();
				stagePlayer.setResizable(false);
				stagePlayer.initOwner(this.primaryStage);
				stagePlayer.initModality(Modality.APPLICATION_MODAL);
				stagePlayer.getIcons().setAll(this.primaryStage.getIcons());
				stagePlayer.setX(primaryStage.getX() + (primaryStage.getWidth() - (320 + offsets[0].getValue())) / 2);
				stagePlayer.setY(primaryStage.getY() + (primaryStage.getHeight() - (257 + offsets[1].getValue())) / 2);
				
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/PlayerView.fxml"));
				loader.setResources(this.resources);
			
				BorderPane borderPane = loader.load();
				
				PlayerViewController ctrl = loader.getController();
				ctrl.setWindowStage(stagePlayer);
				
				// Donne les valeurs déjà enregistrées ou non à afficher
				if (this.playersName[playerIndexTab] == null) {
					ctrl.setDefaultValues(this.resources.getString("player") + (playerIndexTab+1), this.settings[0].getValue(), this.settings[1].getValue());
					stagePlayer.setTitle(this.resources.getString("title.creating"));
				} else {
					if (this.playersBet[playerIndexTab] == 0)
						ctrl.setDefaultValues(this.playersName[playerIndexTab], this.playersWallet[playerIndexTab], this.settings[1].getValue());
					else
						ctrl.setDefaultValues(this.playersName[playerIndexTab], this.playersWallet[playerIndexTab], this.playersBet[playerIndexTab]);
					stagePlayer.setTitle(this.resources.getString("title.modifying") + this.playersName[playerIndexTab]);
				}
				
				// Si la phase de création de joueur est passer alors on ne peut plus changer que la mise
				if (!firstRound) {
					ctrl.modeMise();
					stagePlayer.setTitle(this.resources.getString("title.changebet") + this.playersName[playerIndexTab]);
				}
				
				Scene scene = new Scene(borderPane);
				scene.getStylesheets().setAll(this.primaryStage.getScene().getStylesheets());
				
				stagePlayer.setScene(scene);
				stagePlayer.showAndWait();
				
				// On ne fait rien si l'utilisateur annule
				if (!ctrl.isCanceled()) {
					
					// Enregistre les données entrées
					if (ctrl.getNom().trim().equals(""))
						this.playersName[playerIndexTab] = this.resources.getString("player") + (playerIndexTab+1);
					else
						this.playersName[playerIndexTab] = ctrl.getNom().trim();
					this.playersWallet[playerIndexTab] = ctrl.getPortefeuille();
					this.playersBet[playerIndexTab] = ctrl.getMise();
					
					// On met à jour les infos du joueur
					Label playerLab = getPlayerLabel(playerIndexVBox);
					playerLab.setText(
							this.playersName[playerIndexTab] + "\n" +
							this.resources.getString("wallet") + " : " + (this.playersWallet[playerIndexTab]-this.playersBet[playerIndexTab]) + "\n" +
							this.resources.getString("bet") + " : " + this.playersBet[playerIndexTab]);
					VBox.setMargin(playerLab, new Insets(71, 0, 0, 0));
					
					updatePlayersIndexTab();
					
					if (this.playersIndexTab.size() > 0) {
						this.butStart.setDisable(false);
						this.dealerMessageLab.setText(this.resources.getString("message.before") + "\n(" + this.playersIndexTab.size() + " " + this.resources.getString("message.players") + ")");
					} else {
						this.butStart.setDisable(true);
						this.dealerMessageLab.setText(this.resources.getString("message.default"));
					}
					
					if (firstRound) {
						playerBut.setText(this.resources.getString("button.edit"));
					}
						
					// Si on a cliqué sur le dernier plateau de la liste et qu'il n'y a pas déjà 7 plateaux
					// et qu'on n'a pas encore lancer la première manche, alors on créer un nouveau plateau
					int nbVBox = this.playersHBox.getChildren().size();
					if (playerIndexTab == nbVBox-1 && nbVBox < 7 && firstRound) {
						createPlayerVBox(playerIndexTab+1);
					}
				}
			} catch (IOException e) {
				System.out.println("Problem(s) with PlayerView.fxml");
				System.exit(1);
			}
		} else {
			Alert dialog = new Alert(AlertType.INFORMATION);
			dialog.initOwner(this.primaryStage);
			dialog.setTitle(this.resources.getString("emptywallet.title"));
			dialog.setHeaderText(this.playersName[playerIndexTab] + " " + this.resources.getString("emptywallet.header"));
			dialog.setContentText(this.resources.getString("emptywallet.content"));
			dialog.showAndWait();
		}
	}

	/**
	 * Lance la première manche et les suivantes, s'occuppe aussi de distribuer les gains
	 */
	@FXML
	private void actionStart() {
		if (this.blackbot.getEtat() == EtatBlackBot.MISE) {
			
			// Fait miser les joueurs et met à jour leurs soldes
			for (int i = 0; i < this.playersIndexTab.size(); i++) {
				int indexPlayerTab = this.playersIndexTab.get(i);
				int mise = this.playersBet[indexPlayerTab];
				blackbot.miser(indexPlayerTab, mise);
				this.playersWallet[indexPlayerTab] -= mise;
			}
			
			this.blackbot.distribuer();
			
			this.butStart.setDisable(true);
			this.butDrawCard.setDisable(false);
			this.butEndTurn.setDisable(false);
			this.dealerHandLab.setVisible(true);
			this.dealerHandTA.setText(handCards(blackbot.getMainBanque()));
			this.dealerHandTA.setDisable(false);
			this.dealerHandTA.setVisible(true);
			
			// Détermine la première VBox contenant un joueur
			int indexPrem = 0;
			for (int i = 0; i < 7; i++) {
				if (playersName[i] != null) {
					indexPrem = i;
					i += 7; // met fin à la boucle
				}
			}
			
			// Supprime les VBox qui n'ont pas de mise
			int indexDel = 0;
			for (int i  = indexPrem; this.playersHBox.getChildren().size() != this.playersIndexTab.size(); i ++) {
				if (!this.playersIndexTab.contains(i)) {
					// Pour savoir quand il n'y a plus personne qui peut jouer
					this.playersWallet[i] = 0;
					this.playersName[i] = null;
					playersHBox.getChildren().remove(indexDel);
				} else {
					indexDel ++;
				}
			}
			
			for (int i = 0; i < this.playersIndexTab.size(); i++) {
				// Supprime le bouton
				getPlayerVBox(i).getChildren().remove(1);
				
				// Met à jour les plateaux des joueurs (position et affichage)
				VBox.setMargin(getPlayerLabel(i), new Insets(0));
				getPlayerTextArea(i).setVisible(true);
				
				// Affiche les cartes des joueurs
				getPlayerTextArea(i).setText(handCards(blackbot.getMainJoueurs(this.playersIndexTab.get(i))));
			}
			
			this.dealerMessageLab.setText(this.playersName[this.playersIndexTab.get(0)] + " " + this.resources.getString("message.turn"));
			
			// Met en avant le joueur qui doit jouer
			getPlayerVBox(0).getStyleClass().add("currentplayer");
			
			this.firstRound = false;
			
		} else { // Récupération des gains
			
			int sum = 0;
			for (int i = 0; i < 7; i++) {
				sum += playersWallet[i];
			}
			
			if (sum == 0) { // Si plus aucun joueur ne peut jouer
				Alert gameOver = new Alert(AlertType.CONFIRMATION);
				gameOver.initOwner(this.primaryStage);
				gameOver.setTitle(this.resources.getString("endgame.title"));
				gameOver.setHeaderText(this.resources.getString("endgame.header"));
				gameOver.setContentText(this.resources.getString("endgame.content"));
				
				ButtonType butClose = new ButtonType(this.resources.getString("endgame.button.close"), ButtonData.CANCEL_CLOSE);
				ButtonType butYes = new ButtonType(this.resources.getString("yes"), ButtonData.YES);
				
				gameOver.getButtonTypes().set(0, butYes);
				gameOver.getButtonTypes().set(1, butClose);
				
				Optional<ButtonType> choix = gameOver.showAndWait();
				
				if (choix.get().equals(butYes))
					Util.loadMainOrGameView(this.main, primaryStage, this.locale.getValue(), this.offsets, "Game");
				else
					this.primaryStage.close();
			} else {
				for (int i = 0; i < this.playersIndexTab.size(); i++) {
					int indexPlayerTab = this.playersIndexTab.get(i);
					
					if (this.playersWallet[indexPlayerTab] > this.settings[1].getValue())
						this.playersBet[indexPlayerTab] = this.settings[1].getValue();
					else
						this.playersBet[indexPlayerTab] = this.playersWallet[indexPlayerTab];
					VBox.setMargin(getPlayerLabel(i), new Insets(71, 0, 0, 0));
					
					Button playerBut = new Button(this.resources.getString("button.changebet"));
					playerBut.setPrefWidth(125);
					playerBut.getStyleClass().add("buttonGreenLittle");
					int ind = i;
					playerBut.setOnAction( e -> actionMiser(e, indexPlayerTab, ind) );
					
					getPlayerVBox(i).getChildren().add(1, playerBut);
					getPlayerVBox(i).getChildren().get(2).setVisible(false);
					
					getPlayerLabel(i).setText(
								this.playersName[indexPlayerTab] + "\n" +
								this.resources.getString("wallet") + " : " + (this.playersWallet[indexPlayerTab]-this.playersBet[indexPlayerTab]) + "\n" +
								this.resources.getString("bet") + " : " + this.playersBet[indexPlayerTab]);
					
					getPlayerVBox(i).setDisable(false);
				}
				updatePlayersIndexTab();
				this.butStart.setText(this.resources.getString("button.next"));
				this.dealerMessageLab.setText(this.resources.getString("message.before") + "\n(" + this.playersIndexTab.size() + " " +this.resources.getString("message.players") + ")");
				this.dealerHandLab.setVisible(false);
				this.dealerHandTA.setVisible(false);
				blackbot.relancerPartie();
			}
		}
	}
	
	/**
	 * Fait tirer un carte au joueur qui joue et met à jour sa main.
	 * Si le joueur perd alors on passe au joueur suivant si il y en a, sinon, le croupier joue.
	 */
	@FXML
	private void actionDrawACard() {
		int indexPlayerTab = this.playersIndexTab.get(this.currentPlayer);
		
		this.blackbot.tirer(indexPlayerTab);
		getPlayerTextArea(this.currentPlayer).setText(handCards(blackbot.getMainJoueurs(indexPlayerTab)));
		
		// Si le joueur brûle on retire la mise en avant et désactive son TextArea
		if (this.blackbot.getFinJoueurs(indexPlayerTab)) {
			getPlayerVBox(this.currentPlayer).getStyleClass().remove("currentplayer");
			getPlayerVBox(this.currentPlayer).setDisable(true);
			checkFin();
		}
	}
	
	/**
	 * Met fin au tour du joueur qui joue
	 * On passe au joueur suivant si il y en a, sinon, le croupier joue.
	 */
	@FXML
	private void actionEndSelfTurn() {
		this.blackbot.terminer(this.playersIndexTab.get(this.currentPlayer));
		// Retire la mise en avant
		getPlayerVBox(this.currentPlayer).getStyleClass().remove("currentplayer");
		checkFin();
	}
	
	/**
	 * Vérifie si tous les joueurs on joué, si c'est le cas alors on fait jouer le croupier
	 * Sinon on passe au joueur suivant
	 */
	private void checkFin() {
		if (this.playersIndexTab.size()-1 == this.currentPlayer) {
			tourCroupier();
		} else {
			this.currentPlayer ++;
			this.dealerMessageLab.setText(this.playersName[this.playersIndexTab.get(this.currentPlayer)] + " " + this.resources.getString("message.turn"));
			// Met en avant le joueur qui doit jouer
			getPlayerVBox(this.currentPlayer).getStyleClass().add("currentplayer");
		}
	}
	
	/**
	 * Fait jouer le croupier, et afficher les gains de chaque joueur
	 */
	private void tourCroupier() {
		this.butDrawCard.setDisable(true);
		this.butEndTurn.setDisable(true);
		this.dealerHandTA.setText(handCards(blackbot.getMainBanque()));
		if (blackbot.getMainBanque().isPerdante())
			this.dealerHandTA.setDisable(true);
		
		for (int i = 0; i < this.playersIndexTab.size(); i++) {
			int indexPlayerTab = this.playersIndexTab.get(i);
			getPlayerLabel(i).setText(
						this.playersName[indexPlayerTab] + "\n" +
						this.resources.getString("wallet") + " : " + this.playersWallet[indexPlayerTab] + "\n" +
						this.resources.getString("earnings") + " : " + this.blackbot.getGainJoueurs(indexPlayerTab));
			this.playersWallet[indexPlayerTab] += this.blackbot.getGainJoueurs(indexPlayerTab);
		}
		
		this.dealerMessageLab.setText(this.resources.getString("message.end"));
		this.butStart.setText(this.resources.getString("button.earnings"));
		this.butStart.setDisable(false);
		this.currentPlayer = 0;
	}
	
	/** Méthode permettant de récupérer la VBox d'un joueur en fonction de son indice
	 * @param index Indice du joueur
	 * @return L'élement VBox correspondant
	 */
	private VBox getPlayerVBox(int indexPlayer) {
		return (VBox) this.playersHBox.getChildren().get(indexPlayer);
	}
	
	/** Méthode permettant de récupérer le Label d'un joueur en fonction de son indice
	 * @param index Indice du joueur
	 * @return L'élement Label correspondant
	 */
	private Label getPlayerLabel(int indexPlayer) {
		return (Label) getPlayerVBox(indexPlayer).getChildren().get(0);
	}
	
	/** Méthode permettant de récupérer la TextArea d'un joueur en fonction de son indice
	 * @param index Indice du joueur
	 * @return L'élement TextArea correspondant
	 */
	private TextArea getPlayerTextArea(int indexPlayer) {
		// En mode mise il y a un bouton en position 1, autrement il n'y en a pas
		if (this.blackbot.getEtat() == EtatBlackBot.MISE)
			return (TextArea) getPlayerVBox(indexPlayer).getChildren().get(2);
		else
			return (TextArea) getPlayerVBox(indexPlayer).getChildren().get(1);
	}
	
	/** Détermine si l'utilisateur à créer un joueur
	 * @return Vrai si au moins un joueur à un nom
	 */
	private boolean isPlaying() {
		for (int i = 0; i < 7; i++) {
			if (playersName[i] != null)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Met à jour l'ArrayList des joueurs
	 */
	private void updatePlayersIndexTab() {
		this.playersIndexTab.clear();
		for (int i = 0; i < 7; i ++) {
			if (this.playersBet[i] > 0) {
				this.playersIndexTab.add(i);
			}
		}
	}
	
	/** Renvoie les cartes et le score d'un joueur
	 * @param pfMainJoueur Main du joueur
	 * @return Le texte des cartes du joueur
	 */
	private String handCards(MainBlackjack pfMainJoueur) {
		List<Carte> playerHand = pfMainJoueur.getCartes();
		String playerCards = "";

		for (int j = 0; j < playerHand.size(); j++) {
			
			if (playerHand.get(j).getHauteur() == 0 || playerHand.get(j).getHauteur() > 9) {
				playerCards += this.cardsName[playerHand.get(j).getHauteur()].charAt(0);
			} else {
				playerCards += playerHand.get(j).getValeur();
			}
			
			playerCards += this.colorsSymbols[playerHand.get(j).getCouleur()] +
							this.cardsName[playerHand.get(j).getHauteur()] +
							resources.getString("card.between") +
							this.colorsName[playerHand.get(j).getCouleur()] + "\n";
		}
		
		return playerCards + "\n" + pfMainJoueur.getScore() + " points";
	}
}
