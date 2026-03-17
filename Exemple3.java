package fr.ul.miashs.compil.arbre.exemples;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.generateur.Generateur;

/**
 * Exemple #3
 */
public class Exemple3 {
    public static void main(String[] args) {
        Prog prog = new Prog();
        //Initialisation des variables globales (a, b, x)
        Affectation affA = new Affectation(); //a
        Idf a = new Idf("a");
        affA.setFilsGauche(a);
        affA.setFilsDroit(new Const(100));
        prog.ajouterUnFils(affA);

        Affectation affB = new Affectation(); //b
        Idf b = new Idf("b");
        affB.setFilsGauche(b);
        affB.setFilsDroit(new Const(170));
        prog.ajouterUnFils(affB);

        Affectation affX = new Affectation(); //x
        Idf x = new Idf("x");
        affX.setFilsGauche(x);
        affX.setFilsDroit(new Const(0));
        prog.ajouterUnFils(affX);
        //Reprise du parcours de l'arbre correctement
        Fonction fonction = new Fonction("main");
        prog.ajouterUnFils(fonction);
        Affectation aff = new Affectation();
        fonction.ajouterUnFils(aff);
        Plus plus = new Plus();
        aff.setFilsGauche(x);
        aff.setFilsDroit(plus);
        Multiplication mult = new Multiplication();
        Division div = new Division();
        plus.setFilsGauche(mult);
        plus.setFilsDroit(div);
        Const deux = new Const(2);
        mult.setFilsGauche(a);
        mult.setFilsDroit(deux);
        Moins moins = new Moins();
        Const trois = new Const(3);
        div.setFilsGauche(moins);
        div.setFilsDroit(trois);
        Const cinq = new Const(5);
        moins.setFilsGauche(b);
        moins.setFilsDroit(cinq);

        TxtAfficheur.afficher(prog);

        Generateur gen = new Generateur();
        System.out.println(gen.generer_programme(prog));
        gen.tds.afficher();
    }
}
