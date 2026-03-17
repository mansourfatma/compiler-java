package fr.ul.miashs.compil.arbre.exemples;

import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.generateur.Generateur;

/**
 * Exemple #8
 */
public class Exemple8 {
    public static void main (String[] args) {
        Prog prog = new Prog();
        //Initialisation de la variable globale i
        Idf i = new Idf("i");
        prog.ajouterUnFils(i);
        //Début de l'arbre
        Fonction main = new Fonction("main");
        prog.ajouterUnFils(main);
        Affectation aff = new Affectation();
        main.ajouterUnFils(aff);
        Const c0 = new Const(0);
        aff.setFilsGauche(i);
        aff.setFilsDroit(c0);
        TantQue tant_que = new TantQue();
        main.ajouterUnFils(tant_que);
        Inferieur inf = new Inferieur();
        tant_que.setCondition(inf);
        Const c6 = new Const(6);
        inf.setFilsGauche(i);
        inf.setFilsDroit(c6);
        Bloc bloc = new Bloc();
        tant_que.setBloc(bloc);
        Ecrire ecr = new Ecrire();
        bloc.ajouterUnFils(ecr);
        ecr.setLeFils(i);
        Affectation aff2 = new Affectation();
        bloc.ajouterUnFils(aff2);
        Plus plus = new Plus();
        aff2.setFilsGauche(i);
        aff2.setFilsDroit(plus);
        Const c1 = new Const(1);
        plus.setFilsGauche(i);
        plus.setFilsDroit(c1);

        //afficher
        TxtAfficheur.afficher(prog);

        Generateur gen = new Generateur();
        System.out.println(gen.generer_programme(prog));
        gen.tds.afficher();
    }
}