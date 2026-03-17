package fr.ul.miashs.compil.arbre.exemples;

import fr.ul.miashs.compil.generateur.Generateur;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;

/**
 * Exemple #4
 */
public class Exemple4 {
    public static void main (String[] args){
        Prog prog = new Prog();
        Fonction main = new Fonction("main");
        //Déclaration des variables globales
        Idf x = new Idf("x");
        prog.ajouterUnFils(new Idf("x"));

        Affectation affectation = new Affectation();
        Plus plus = new Plus();
        Multiplication mul = new Multiplication();
        Division div = new Division();
        Lire lire1 = new Lire();
        Lire lire2 = new Lire();
        Moins moins = new Moins();
        Const c2 = new Const(2);
        Const c3 = new Const(3);
        Const c5 = new Const(5);
        Ecrire ecrire = new Ecrire();
        prog.ajouterUnFils(main);
        main.ajouterUnFils(affectation);
        affectation.setFilsGauche(x);
        affectation.setFilsDroit(plus);
        plus.setFilsGauche(mul);
        plus.setFilsDroit(div);
        mul.setFilsGauche(lire1);
        mul.setFilsDroit(c2);
        div.setFilsGauche(moins);
        div.setFilsDroit(c3);
        moins.setFilsGauche(lire2);
        moins.setFilsDroit(c5);
        main.ajouterUnFils(ecrire);
        ecrire.setLeFils(x);

        //afficher
        TxtAfficheur.afficher(prog);

        Generateur gen = new Generateur();
        System.out.println(gen.generer_programme(prog));
        gen.tds.afficher();
    }
}