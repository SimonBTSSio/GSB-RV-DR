/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.gsb.rv.dr;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Menu;
import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.util.Pair;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import java.util.Optional;
import fr.gsb.rv.dr.technique.ConnexionBD;
import fr.gsb.rv.dr.technique.Session;
import fr.gsb.rv.dr.technique.ConnexionException;
import fr.gsb.rv.dr.entites.Visiteur;
import fr.gsb.rv.dr.modeles.*;
import java.sql.*;

/**
 *
 * @author etudiant
 */

class VueConnexion extends Dialog {
    public VueConnexion(){
        super();
        // Création de la boite de dialogue     
        
        super.setTitle("Authentification");
        super.setHeaderText("Saisir vos données de connexion");
        //Création des buttons (seConnecter et Annuler)
        ButtonType loginButtonType = new ButtonType("Se connecter", ButtonData.OK_DONE);
        super.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        //champ de saisie 
        BorderPane champSaisie = new BorderPane();
        champSaisie.setPadding(new Insets(10, 150, 10, 10));
        TextField login  = new TextField();
        login.setPromptText("login");
        PasswordField mdp = new PasswordField();
        mdp.setPromptText("mdp");
        champSaisie.getChildren().add(new Label("My Label"));
        login.setPadding(new Insets(5, 5, 5, 5));
        champSaisie.setTop(login);
        mdp.setPadding(new Insets(5, 5, 5, 5));
        champSaisie.setCenter(mdp);
        super.getDialogPane().setContent(champSaisie);
    
        Platform.runLater(() -> login.requestFocus());

        super.setResultConverter(dialogButton -> {
        if (dialogButton == loginButtonType) {
            return new Pair<>(login.getText(), mdp.getText());
        }
        return null;
        });

        
       }
}
public class JavaFXApplication1 extends Application {
    
   
    @Override
    public void start(Stage primaryStage)  {
        
        MenuBar barreMenus = new MenuBar();
        //FICHIER
        Menu menuFichier = new Menu("Fichier");
        MenuItem itemSeConnecter = new MenuItem( "Se connecter" );
        menuFichier.getItems().add( itemSeConnecter );
        MenuItem itemSeDeconnecter = new MenuItem( "Se déconnecter" );
        SeparatorMenuItem sep1= new SeparatorMenuItem();
        menuFichier.getItems().add( sep1 );
        MenuItem itemQuitter = new MenuItem( "Quitter     Ctrl+x" );
        menuFichier.getItems().add( itemQuitter );
        barreMenus.getMenus().add( menuFichier );
        //Rapports 
        Menu menuRapports = new Menu("Rapports");
        MenuItem itemConsulter = new MenuItem( "Consulter" );
        menuRapports.getItems().add( itemConsulter );            
        barreMenus.getMenus().add( menuRapports );
        //Praticiens 
        Menu menuPraticiens = new Menu("Praticiens");
        MenuItem itemHesitants = new MenuItem( "Hésitants" );
        menuPraticiens.getItems().add( itemHesitants );
        barreMenus.getMenus().add( menuPraticiens );
        menuRapports.setDisable(true);
        menuPraticiens.setDisable(true);
        menuRapports.setOnAction(new EventHandler<ActionEvent>() { 
  
        @Override 
        public void handle(ActionEvent actionEvent) { 
            System.out.println("[Rapports] " + Session.getSession().getVisiteur().getNom() + " " + Session.getSession().getVisiteur().getPrenom()) ;           
        } 
        });

        menuPraticiens.setOnAction(new EventHandler<ActionEvent>() { 
  
        @Override 
        public void handle(ActionEvent actionEvent) { 
            System.out.println("[Praticiens] " + Session.getSession().getVisiteur().getNom() + " " + Session.getSession().getVisiteur().getPrenom()) ;           
        } 
        });
        
        itemSeConnecter.setOnAction(new EventHandler<ActionEvent>() { 
  
        @Override 
        public void handle(ActionEvent actionEvent) {            
            VueConnexion vue = new VueConnexion();
            Optional<Pair<String, String>> result = vue.showAndWait();

            result.ifPresent (pair -> {
                try {
                    Visiteur vis = ModeleGsbRv.seConnecter( pair.getKey() , pair.getValue() );
                    if(vis != null){
                        Session.ouvrir(vis);
                        menuFichier.getItems().remove( itemSeConnecter );
                        menuFichier.getItems().remove( sep1 );
                        menuFichier.getItems().remove( itemQuitter );
                        menuFichier.getItems().add( itemSeDeconnecter );
                        menuFichier.getItems().add( sep1 );
                        menuFichier.getItems().add( itemQuitter );
                        menuRapports.setDisable(false);
                        menuPraticiens.setDisable(false);
                        primaryStage.setTitle(Session.getSession().getVisiteur().getNom() + " " + Session.getSession().getVisiteur().getPrenom());
                        System.out.print("marche");
                    }
                    System.out.print("marche pas");
                       
                }
                catch( Exception e ){
                    System.out.println(e);
                }
            });
        } 
        });
        
        itemSeDeconnecter.setOnAction(new EventHandler<ActionEvent>() { 
  
        @Override 
        public void handle(ActionEvent actionEvent) {
            menuFichier.getItems().remove( itemSeDeconnecter );
            menuFichier.getItems().remove( sep1 );
            menuFichier.getItems().remove( itemQuitter );
            menuFichier.getItems().add( itemSeConnecter  );
            menuFichier.getItems().add( sep1 );
            menuFichier.getItems().add( itemQuitter );
            menuRapports.setDisable(true);
            menuPraticiens.setDisable(true);
            Session.fermer();
            primaryStage.setTitle("GSB-RV-DR");
            
        } 
        });
        
        itemQuitter.setOnAction(new EventHandler<ActionEvent>() { 
  
        @Override 
        public void handle(ActionEvent actionEvent) {
    
    Alert alertQuitter = new Alert(Alert.AlertType.CONFIRMATION);
           alertQuitter.setTitle("quitter");
           alertQuitter.setHeaderText("Demande de confirmation");
           alertQuitter.setContentText("quitter");
           ButtonType btnOui= new ButtonType("oui");
           ButtonType btnNon= new ButtonType("non");
           alertQuitter.getButtonTypes().setAll(btnOui,btnNon);
           Optional<ButtonType> reponse = alertQuitter.showAndWait();
            if(reponse.get() == btnOui){
                Platform.exit();
            }

        } 
        });
        final KeyCombination keyCombinationShiftC = new KeyCodeCombination(
        KeyCode.X, KeyCombination.CONTROL_DOWN);

        

        BorderPane root = new BorderPane();
        root.setTop(barreMenus);
        Scene scene = new Scene(root, 900, 500);
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (keyCombinationShiftC.match(event)) {
                Alert alertQuitter = new Alert(Alert.AlertType.CONFIRMATION);
           alertQuitter.setTitle("quitter");
           alertQuitter.setHeaderText("Demande de confirmation");
           alertQuitter.setContentText("quitter");
           ButtonType btnOui= new ButtonType("oui");
           ButtonType btnNon= new ButtonType("non");
           alertQuitter.getButtonTypes().setAll(btnOui,btnNon);
           Optional<ButtonType> reponse = alertQuitter.showAndWait();
            if(reponse.get() == btnOui){
                Platform.exit();
            }
            }
        }
        });
        primaryStage.setTitle("GSB-RV-DR");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ConnexionException{
        System.out.println("");      
        launch(args);
    }
}
