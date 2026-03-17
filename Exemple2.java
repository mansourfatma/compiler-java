/*
 *  License and Copyright:
 *
 *  This file is part of arbre  project.
 *
 * MIT License:
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * Copyright 2025 by IDMC, Université de Lorraine (azim)
 * All right reserved
 *
 */

package fr.ul.miashs.compil.arbre.exemples;

import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.generateur.Generateur;

/**
 * Exemple #2
 */
public class Exemple2 {
    public static void main(String[] args) {
        //on crée les noeuds
        Prog prog = new Prog();
        //on ajoute les variables globales
        Affectation affi = new Affectation(); //i
        affi.setFilsGauche(new Idf("i"));
        affi.setFilsDroit(new Const(10));
        prog.ajouterUnFils(affi);

        Affectation affj = new Affectation(); //j
        affj.setFilsGauche(new Idf("j"));
        affj.setFilsDroit(new Const(20));
        prog.ajouterUnFils(affj);

        prog.ajouterUnFils(new Idf("k")); //k
        prog.ajouterUnFils(new Idf("l")); //l

        Fonction main = new Fonction("main");
        prog.ajouterUnFils(main);
        //afficher
        TxtAfficheur.afficher(prog);

        Generateur gen = new Generateur();
        System.out.println(gen.generer_programme(prog));
        gen.tds.afficher();
    }
}
