package fr.ul.miashs.compil.tds;

public class Symbole {
    private String nom;
    private TypeSymbole type;
    private String categorie;  //"locale", "globale", "fonction", "param"
    private Object valeur;
    private int rang;  //pour les variables de fonction et paramètres
    private String scope;  //portée (nom de la fonction ou "global")

    //Attributs spécifiques aux fonctions
    private int nbParam;
    private int nbVar;

    public Symbole(String nom, TypeSymbole type, String categorie) {
        this.nom = nom;
        this.type = type;
        this.categorie = categorie;
        this.rang = -1;
        this.scope = "global";
        this.nbParam = 0;
        this.nbVar = 0;
    }

    //Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeSymbole getType() { return type; }
    public void setType(TypeSymbole type) { this.type = type; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public Object getValeur() { return valeur; }
    public void setValeur(Object valeur) { this.valeur = valeur; }

    public int getRang() { return rang; }
    public void setRang(int rang) { this.rang = rang; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public int getNbParam() { return nbParam; }
    public void setNbParam(int nbParam) { this.nbParam = nbParam; }

    public int getNbVar() { return nbVar; }
    public void setNbVar(int nbVar) { this.nbVar = nbVar; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("nom=").append(nom);
        sb.append(", type=").append(type.toString().toLowerCase());
        sb.append(", cat=").append(categorie);

        if (categorie.equals("fonction")) {
            sb.append(", nb_param=").append(nbParam);
            sb.append(", nb_var=").append(nbVar);
        } else {
            if (valeur != null) {
                sb.append(", val=").append(valeur);
            }
            if (!categorie.equals("globale")) {
                sb.append(", rang=").append(rang);
                sb.append(", scope=").append(scope);
            }
        }
        return sb.toString();
    }
}
