package fr.ul.miashs.compil.tds;

import fr.ul.miashs.compil.arbre.*;
import java.util.*;

public class TDS {
    private Map<String, Symbole> table;
    private String scopeCourante;
    private Map<String, Integer> compteurRang;
    private Map<String, Integer> compteurRangParam;
    private Map<String, Integer> compteurRangLocale;

    public TDS() {
        this.table = new HashMap<>();
        this.scopeCourante = "global";
        this.compteurRang = new HashMap<>();
        this.compteurRangParam = new HashMap<>();
        this.compteurRangLocale = new HashMap<>();
    }

    public void construire(Noeud arbre) {
        parcourirArbre(arbre);
    }

    private void parcourirArbre(Noeud noeud) { //On parcourt l'arbre récursivement
        if (noeud == null) return;

        switch (noeud.getCat()) {
            case PROG:
                traiterProgramme(noeud);
                break;

            case FONCTION:
                traiterFonction(noeud);
                break;

            case BLOC:
                traiterBloc(noeud);
                break;

            case AFF:
                traiterAffectation(noeud);
                break;

            case SI:
                traiterSi(noeud);
                break;

            case TQ:
                traiterTantQue(noeud);
                break;

            case ECR:
                traiterEcrire(noeud);
                break;

            case RET:
                traiterRetour(noeud);
                break;

            case IDF:
                traiterVariable(noeud);
                break;

            case LIRE:
                traiterLire(noeud);
                break;

            case APPEL:
                traiterAppel(noeud);
                break;

            case CONST:
                break;

            case PLUS:
            case MOINS:
            case MUL:
            case DIV:
                traiterOperateur(noeud);
                break;

            case SUP:
            case INF:
            case SUPE:
            case INFE:
            case EG:
            case DIF:
                traiterComparaison(noeud);
                break;

            default:
                if (!noeud.estFeuille()) {
                    for (Noeud fils : noeud.getFils()) {
                        parcourirArbre(fils);
                    }
                }
                break;
        }
    }

    private void traiterProgramme(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    private void traiterFonction(Noeud noeud) {
        if (!(noeud instanceof Fonction)) return;
        Fonction fonction = (Fonction) noeud;
        String nomFonction = fonction.getLabel().split("/")[1];
        compteurRangParam.put(nomFonction, 0);
        compteurRangLocale.put(nomFonction, 0);
        //Détecter si la fonction a un retour
        boolean aUnRetour = false;
        for (Noeud fils : fonction.getFils()) {
            if (fils.getCat() == Noeud.Categories.RET) {
                aUnRetour = true;
                break;
            }
        }
        TypeSymbole type = aUnRetour ? TypeSymbole.INT : TypeSymbole.VOID;
        Symbole symbole = new Symbole(nomFonction, TypeSymbole.INT, "fonction"); //Crée le symbole de la fonction
        symbole.setNbVar(0); //sera mis à jour après
        table.put(nomFonction, symbole); // ajouter dès maintenant
        compteurRang.put(nomFonction, 0); //Initialise le compteur
        String ancienneScope = scopeCourante; //Permet de changer de scope
        scopeCourante = nomFonction;
        int nbParam = 0;
        if (!fonction.estFeuille()) { //On parcourt les fils
            List<Noeud> fils = fonction.getFils();
            int indexBloc = -1;
            for (int i = 0; i < fils.size(); i++) {
                if (fils.get(i).getCat() != Noeud.Categories.IDF) {
                    indexBloc = i;
                    break;
                }
            }
            if (indexBloc > 0) { //Identifier et traiter les param
                for (int i = 0; i < indexBloc; i++) {
                    if (fils.get(i).getCat() == Noeud.Categories.IDF) {
                        traiterParametre(fils.get(i), nomFonction);
                        nbParam++;
                    }
                }
            }
            for (int i = 0; i < fils.size(); i++) { //Traiter le corps de la fonction
                Noeud f = fils.get(i);
                if (f.getCat() != Noeud.Categories.IDF || i >= indexBloc) {
                    parcourirArbre(f);
                }
            }
        }
        int nbVarTotal = 0; //Compter le nombre total de variables locales uniquement
        for (Symbole s : table.values()) {
            if (s.getScope().equals(nomFonction) && s.getCategorie().equals("locale")) {
                nbVarTotal++;
            }
        }
        symbole.setNbParam(nbParam); //Maj des infos sur la fonction
        symbole.setNbVar(nbVarTotal);
        scopeCourante = ancienneScope; //Restaurer l'ancienne scope
        symbole.setNbVar(nbVarTotal);
    }

    private void traiterBloc(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    private void traiterSi(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    private void traiterTantQue(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    private void traiterEcrire(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    private void traiterRetour(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    private void traiterParametre(Noeud noeud, String nomFonction) {
        String nomParam = noeud.getLabel().split("/")[1];
        String cle = nomFonction + "." + nomParam + ".param";

        if (!table.containsKey(cle)) {
            Symbole symbole = new Symbole(nomParam, TypeSymbole.INT, "param");
            symbole.setScope(nomFonction);
            int rang = compteurRangParam.get(nomFonction);
            symbole.setRang(rang);
            compteurRangParam.put(nomFonction, rang + 1);
            table.put(cle, symbole);
        }
    }

    private void traiterVariable(Noeud noeud) {
        String nomVar = noeud.getLabel().split("/")[1];

        if (scopeCourante.equals("global")) {
            //Variable globale
            if (!table.containsKey(nomVar)) {
                Symbole symbole = new Symbole(nomVar, TypeSymbole.INT, "globale");
                symbole.setScope("global");
                table.put(nomVar, symbole);
            }
        } else {
            //Variable locale - vérifier qu'elle n'existe pas déjà comme paramètre, locale ou globale
            String cleParam = scopeCourante + "." + nomVar + ".param";
            String cleLocale = scopeCourante + "." + nomVar + ".locale";
            //Si elle n'existe ni comme param, ni comme locale, ni comme globale, la créer
            if (!table.containsKey(nomVar) && !table.containsKey(cleParam) && !table.containsKey(cleLocale)) {
                Symbole symbole = new Symbole(nomVar, TypeSymbole.INT, "locale");
                symbole.setScope(scopeCourante);
                int rang = compteurRangLocale.get(scopeCourante);
                symbole.setRang(rang);
                compteurRangLocale.put(scopeCourante, rang + 1);
                table.put(cleLocale, symbole);
            }
        }
    }

    private void traiterAffectation(Noeud noeud) {
        if (noeud instanceof Affectation) {
            Affectation aff = (Affectation) noeud;
            parcourirArbre(aff.getFilsDroit()); //Parcourir d'abord l'expression à droite pour détecter les variables
            String nomVar = aff.getFilsGauche().getLabel().split("/")[1]; //Ensuite traiter la variable à gauche
            if (scopeCourante.equals("global")) {
                if (!table.containsKey(nomVar)) {
                    traiterVariable(aff.getFilsGauche());
                }
                Noeud droite = aff.getFilsDroit(); //Essayer d'évaluer la valeur pour les variables globales
                if (droite.getCat() == Noeud.Categories.CONST) {
                    Symbole symbole = table.get(nomVar);
                    if (symbole != null) {
                        try {
                            symbole.setValeur(Integer.parseInt(droite.getLabel().split("/")[1]));
                        } catch (NumberFormatException e) {
                            symbole.setValeur(droite.getLabel().split("/")[1]);
                        }
                    }
                }
            } else {
                String cleParam = scopeCourante + "." + nomVar + ".param";
                String cleLocale = scopeCourante + "." + nomVar + ".locale";
                if (!table.containsKey(nomVar) && !table.containsKey(cleParam) && !table.containsKey(cleLocale)) {
                    traiterVariable(aff.getFilsGauche());
                }
            }
        }
    }

    private void traiterLire(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                if (fils.getCat() == Noeud.Categories.IDF) {
                    String nomVar = fils.getLabel().split("/")[1];

                    if (scopeCourante.equals("global")) {
                        if (!table.containsKey(nomVar)) {
                            traiterVariable(fils);
                        }
                    } else {
                        String cleParam = scopeCourante + "." + nomVar + ".param";
                        String cleLocale = scopeCourante + "." + nomVar + ".locale";

                        if (!table.containsKey(cleParam) && !table.containsKey(cleLocale)) {
                            traiterVariable(fils);
                        }
                    }
                }
            }
        }
    }

    private void traiterAppel(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    private void traiterOperateur(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    private void traiterComparaison(Noeud noeud) {
        if (!noeud.estFeuille()) {
            for (Noeud fils : noeud.getFils()) {
                parcourirArbre(fils);
            }
        }
    }

    public Symbole getSymbole(String nom, String scope) {
        //Chercher d'abord comme paramètre
        String cleParam = scope + "." + nom + ".param";
        Symbole symbole = table.get(cleParam);

        //Sinon comme locale
        if (symbole == null) {
            String cleLocale = scope + "." + nom + ".locale";
            symbole = table.get(cleLocale);
        }

        //Sinon comme globale
        if (symbole == null && !scope.equals("global")) {
            symbole = table.get(nom);
        }

        return symbole;
    }

    public boolean existe(String nom, String scope) {
        return getSymbole(nom, scope) != null;
    }

    public List<Symbole> getVariablesGlobales() {
        List<Symbole> globales = new ArrayList<>();
        for (Symbole s : table.values()) {
            if (s.getCategorie().equals("globale")) {
                globales.add(s);
            }
        }
        return globales;
    }

    public List<Symbole> getFonctions() {
        List<Symbole> fonctions = new ArrayList<>();
        for (Symbole s : table.values()) {
            if (s.getCategorie().equals("fonction")) {
                fonctions.add(s);
            }
        }
        return fonctions;
    }

    public List<Symbole> getSymbolesFonction(String nomFonction) {
        List<Symbole> symboles = new ArrayList<>();
        for (Symbole s : table.values()) {
            if (s.getScope().equals(nomFonction) &&
                    (s.getCategorie().equals("param") || s.getCategorie().equals("locale"))) {
                symboles.add(s);
            }
        }
        //Trier par rang
        symboles.sort(Comparator.comparingInt(Symbole::getRang));
        return symboles;
    }

    public int getAdresseRelative(String nomVar, String nomFonction) {
        Symbole symbole = getSymbole(nomVar, nomFonction);
        if (symbole != null) {
            if (symbole.getCategorie().equals("param")) {
                Symbole symFonction = table.get(nomFonction);
                int nbParams = (symFonction != null) ? symFonction.getNbParam() : 0;
                return (nbParams - symbole.getRang() + 2) * 4;  //+2 pour LP et BP sauvegardés
            } else {
                return -(symbole.getRang() + 1) * 4;  //* 4 pour convertir en octets
            }
        }
        return 0;
    }

    public void afficher() {
        System.out.println("=== Table Des Symboles ===");
        //Afficher les fonctions
        System.out.println("\nFonctions:");
        for (Symbole symbole : table.values()) {
            if (symbole.getCategorie().equals("fonction")) {
                System.out.println("? " + symbole);
            }
        }
        //Afficher les variables globales
        System.out.println("\nVariables globales:");
        for (Symbole symbole : table.values()) {
            if (symbole.getCategorie().equals("globale")) {
                System.out.println("? " + symbole);
            }
        }
        //Afficher les variables locales et paramètres par fonction
        Set<String> fonctions = new HashSet<>();
        for (Symbole symbole : table.values()) {
            if (symbole.getCategorie().equals("fonction")) {
                fonctions.add(symbole.getNom());
            }
        }

        for (String fonction : fonctions) {
            System.out.println("\nVariables locales:");
            List<Symbole> symbolesFonction = new ArrayList<>(); // Collecter tous les symboles de la fonction
            for (Symbole symbole : table.values()) {
                if (symbole.getScope().equals(fonction) &&
                        (symbole.getCategorie().equals("param") || symbole.getCategorie().equals("locale"))) {
                    symbolesFonction.add(symbole);
                }
            }
            //Trier par rang
            symbolesFonction.sort(Comparator.comparingInt(Symbole::getRang));
            for (Symbole s : symbolesFonction) {
                System.out.println("? " + s);
            }
        }
    }

    public Map<String, Symbole> getTable() {
        return table;
    }
}