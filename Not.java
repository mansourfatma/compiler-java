package fr.ul.miashs.compil.arbre;

import java.util.ArrayList;

public class Not extends Noeud0 {
    //Constructeur
    public Not() {
        setCat(Categories.NOT);
        setFils(new ArrayList<>());
    }

    //Getters + Setters
    public Noeud getLeFils() {
        return getFils().get(0);
    }
    public void setLeFils(Noeud n) {
        getFils().add(0, n);
    }
}