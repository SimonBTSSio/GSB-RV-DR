/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.gsb.rv.dr.technique;

import fr.gsb.rv.dr.entites.Visiteur;
/**
 *
 * @author etudiant
 */
public class Session {
    private static Session session = null ;
    private Visiteur visiteur;
	private Session(Visiteur visiteur) {
		super();
		this.visiteur = visiteur;
	}
    public static void ouvrir(Visiteur visiteur) {
    	if(session == null) {
    		session = new Session(visiteur);
        }
    }
    public static void fermer() {
    	session = null;
    }
    public static Session getSession() {
    	return session;
    }
    public Visiteur getVisiteur() {
    	return visiteur;
    }
    public static boolean estOuvert() {
    	if(session != null) {
    		return true;
    	}
    	return false;
    }
    
}
