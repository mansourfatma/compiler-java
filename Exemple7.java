package fr.ul.miashs.compil.arbre.exemples;

import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.generateur.*;

/**
 * Exemple #7
 */
public class Exemple7 {
    public static void main(String[] args) {
        Prog prog = new Prog();
        //Variables globales
        Affectation affa = new Affectation(); //a
        Idf a = new Idf("a");
        Const c1 = new Const(1);
        affa.setFilsGauche(a);
        affa.setFilsDroit(c1);
        prog.ajouterUnFils(affa);

        Affectation affb = new Affectation();
        Idf b = new Idf("b");
        Const c2 = new Const(2);
        affb.setFilsGauche(b);
        affb.setFilsDroit(c2);
        prog.ajouterUnFils(affb);

        Idf x = new Idf("x");
        prog.ajouterUnFils(x);

        //Construction de l'arbre
        Fonction main = new Fonction("main");
        prog.ajouterUnFils(main);
        Si si = new Si();
        main.ajouterUnFils(si);
        Superieur sup = new Superieur();
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        si.setCondition(sup);
        si.setBlocAlors(bloc1);
        si.setBlocSinon(bloc2);
        sup.setFilsGauche(a);
        sup.setFilsDroit(b);
        Affectation aff = new Affectation();
        bloc1.ajouterUnFils(aff);
        Const c1000 = new Const(1000);
        aff.setFilsGauche(x);
        aff.setFilsDroit(c1000);
        Affectation aff2 = new Affectation();
        bloc2.ajouterUnFils(aff2);
        Const c2000 = new Const(2000);
        aff2.setFilsGauche(x);
        aff2.setFilsDroit(c2000);

        //afficher
        TxtAfficheur.afficher(prog);

        Generateur gen = new Generateur();
        System.out.println(gen.generer_programme(prog));
        gen.tds.afficher();
    }
}
