package fr.ul.miashs.compil.arbre.exemples;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.generateur.Generateur;

/**
 * Exemple #6
 */
public class Exemple6 {
    public static void main(String[] args) {
        Prog prog = new Prog();
        //Déclaration des variables globales
        Affectation affa = new Affectation(); //déclaration de a
        Idf a = new Idf("a");
        Const c100 = new Const(100);
        affa.setFilsGauche(a);
        affa.setFilsDroit(c100);
        prog.ajouterUnFils(affa);

        Affectation affc = new Affectation(); //déclaration de c
        Idf c = new Idf("c");
        Const c170 = new Const(170);
        affc.setFilsGauche(c);
        affc.setFilsDroit(c170);
        prog.ajouterUnFils(affc);

        //Commencement de l'arbre
        Fonction f = new Fonction("f");
        Fonction main = new Fonction("main");
        prog.ajouterUnFils(f);
        prog.ajouterUnFils(main);
        Affectation aff = new Affectation();
        Retour ret = new Retour();
        f.ajouterUnFils(new Idf("a")); // paramètre a
        f.ajouterUnFils(new Idf("b")); // paramètre b
        Bloc blocF = new Bloc();
        f.ajouterUnFils(blocF);
        blocF.ajouterUnFils(aff);
        blocF.ajouterUnFils(ret);
        Idf res = new Idf("res");
        Plus plus = new Plus();
        aff.setFilsGauche(res);
        aff.setFilsDroit(plus);
        Multiplication mult = new Multiplication();
        Division div = new Division();
        plus.setFilsGauche(mult);
        plus.setFilsDroit(div);
        Idf a2 = new Idf("a"); //Paramètre de f !
        Const c2 = new Const(2);
        mult.setFilsGauche(a2);
        mult.setFilsDroit(c2);
        Moins moins = new Moins();
        Const c3 = new Const(3);
        div.setFilsGauche(moins);
        div.setFilsDroit(c3);
        Idf b = new Idf("b"); //Pareil, paramètre de f !!!
        Const c5 = new Const(5);
        moins.setFilsGauche(b);
        moins.setFilsDroit(c5);
        ret.setLeFils(res);
        Ecrire ecr = new Ecrire();
        main.ajouterUnFils(ecr);
        Appel appel = new Appel(f);
        ecr.setLeFils(appel);
        appel.ajouterUnFils(new Idf("a"));
        appel.ajouterUnFils(new Idf("c"));

        //afficher
        TxtAfficheur.afficher(prog);

        Generateur gen = new Generateur();
        System.out.println(gen.generer_programme(prog));
        gen.tds.afficher();
    }
}
