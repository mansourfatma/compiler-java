package fr.ul.miashs.compil.arbre.exemples;

import fr.ul.miashs.compil.generateur.Generateur;
import fr.ul.miashs.compil.tds.*;
import fr.ul.miashs.compil.arbre.*;

/**
 * Exemple #5
 */
public class Exemple5 {
    public static void main(String[] args) {
        Prog prog = new Prog();
        Fonction main = new Fonction("main");
        //variables globales
        Affectation affa = new Affectation(); //a
        Idf a = new Idf("a");
        Const c100 = new Const(100);
        affa.setFilsGauche(a);
        affa.setFilsDroit(c100);
        prog.ajouterUnFils(affa);

        Affectation affb = new Affectation();
        Idf b = new Idf("b");
        Const c170 = new Const(170);
        affb.setFilsGauche(b);
        affb.setFilsDroit(c170);
        prog.ajouterUnFils(affb);

        Ecrire ecrire = new Ecrire();
        Plus plus = new Plus();
        Multiplication mul = new Multiplication();
        Division div = new Division();
        Moins moins = new Moins();

        Const c2 = new Const(2);
        Const c5 = new Const(5);
        Const c3 = new Const(3);

        prog.ajouterUnFils(main);
        main.ajouterUnFils(ecrire);
        ecrire.setLeFils(plus);
        plus.setFilsGauche(mul);
        plus.setFilsDroit(div);
        mul.setFilsGauche(a);
        mul.setFilsDroit(c2);
        div.setFilsGauche(moins);
        div.setFilsDroit(c3);
        moins.setFilsGauche(b);
        moins.setFilsDroit(c5);

        //afficher
        TxtAfficheur.afficher(prog);

        Generateur gen = new Generateur();
        System.out.println(gen.generer_programme(prog));
        gen.tds.afficher();
    }
}