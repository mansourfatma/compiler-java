package fr.ul.miashs.compil.arbre.exemples;

import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.generateur.Generateur;

/**
 * Exemple #9
 */
public class Exemple9 {
    public static void main (String[] args) {
        Prog prog = new Prog();
        Fonction main = new Fonction("main");
        Fonction f = new Fonction("f");
        Si si = new Si();
        InferieurEgal infe = new InferieurEgal();
        Bloc bloc = new Bloc();
        Idf a = new Idf("a");
        Const zero = new Const(0);
        Retour ret = new Retour();
        Retour ret2 = new Retour();
        Plus plus = new Plus();
        Idf a2 = new Idf("a");
        Appel appel = new Appel(f);
        Moins moins = new Moins();
        Idf a3 = new Idf("a");
        Const un = new Const(1);
        Ecrire ecrire = new Ecrire();
        Appel appel2 = new Appel(f);
        Const six = new Const(6);

        prog.ajouterUnFils(f); //Construction du programme
        prog.ajouterUnFils(main);
        f.ajouterUnFils(a);        //paramètre a
        f.ajouterUnFils(si);
        f.ajouterUnFils(ret2);
        si.setCondition(infe); //Si : inféreeurEgal + bloc
        si.setBlocAlors(bloc);
        infe.setFilsGauche(a); //Condition : a <= 0
        infe.setFilsDroit(zero);
        bloc.ajouterUnFils(ret); //Bloc alors : retour 0
        ret.setLeFils(zero);
        ret2.setLeFils(plus); //Retour : a + f(a - 1)
        plus.setFilsGauche(a2);
        plus.setFilsDroit(appel);
        appel.ajouterUnFils(moins); //Appel f(a - 1)
        moins.setFilsGauche(a3);
        moins.setFilsDroit(un);
        main.ajouterUnFils(ecrire); //Fonction main : ecrire(f(6))
        ecrire.setLeFils(appel2);
        appel2.ajouterUnFils(six);

        //afficher
        TxtAfficheur.afficher(prog);

        Generateur gen = new Generateur();
        System.out.println(gen.generer_programme(prog));
        gen.tds.afficher();
    }
}