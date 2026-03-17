package fr.ul.miashs.compil.generateur;
import fr.ul.miashs.compil.arbre.*;
import fr.ul.miashs.compil.tds.*;

public class Generateur {

    public TDS tds;

    public Generateur() {
        this.tds = new TDS();
    }

    public String generer_programme(Noeud a) {
        generer_data(a);

        Prog p = (Prog) a;
        StringBuilder code = new StringBuilder();

        code.append(".include beta.uasm\n");
        code.append(".include intio.uasm\n");
        code.append(".options tty\n");
        code.append("CMOVE(pile, SP)\n");
        code.append("BR(debut)\n");
        for (Symbole s : tds.getVariablesGlobales()) { //Variables globales
            if (s.getValeur() != null)
                code.append(s.getNom() + ": LONG(" + s.getValeur() + ")\n");
            else
                code.append(s.getNom() + ": LONG(0)\n");
        }
        code.append("debut:\n");
        code.append("CALL(main)\n");
        code.append("HALT()\n");
        for (Noeud f : p.getFils()) {
            if (f.getCat() == Noeud.Categories.FONCTION)
                code.append(generer_fonction(f));
        }
        code.append("pile:\n");
        return code.toString();
    }

    public void generer_data(Noeud a) {
        tds.construire(a);
    }

    public String generer_instruction(Noeud a, String scope) {
        {
            StringBuffer code = new StringBuffer();
            switch (a.getCat()) { //D'après la catégorie du Noeud, faire :
                case AFF:
                    code.append(generer_affectation(a, scope));
                    break;

                case SI:
                    code.append(generer_si(a, scope));
                    break;

                case TQ:
                    code.append(generer_tq(a, scope));
                    break;

                case ECR:
                    code.append(generer_ecrire(a, scope));
                    break;

                case RET:
                    code.append(generer_retour(a, scope));
                    break;
            }
            return code.toString();
        }
    }

    public String generer_ecrire(Noeud a, String scope) {
        StringBuffer code = new StringBuffer();
        Ecrire ecr = (Ecrire) a;
        code.append(generer_expression(ecr.getLeFils(), scope));
        code.append("POP(r0)\n");
        code.append("WRINT()\n");
        return code.toString();
    }

    public String generer_appel(Noeud a, String scope) {
        StringBuilder code = new StringBuilder();
        String nomFonction = a.getLabel().split("/")[2];
        Symbole symFonction = tds.getTable().get(nomFonction);
        int nbParams = 0;
        boolean retourneValeur = false;
        if (symFonction != null) {
            nbParams = symFonction.getNbParam();
            retourneValeur = (symFonction.getType() != TypeSymbole.VOID);
        }
        if (retourneValeur) {
            code.append("ALLOCATE(1)\n");
        }
        for (Noeud fils : a.getFils()) {
            code.append(generer_expression(fils, scope));
        }
        code.append("CALL(").append(nomFonction).append(")\n");
        if (nbParams > 0) {
            code.append("DEALLOCATE(").append(nbParams).append(")\n");
        }
        return code.toString();
    }

    public String generer_fonction(Noeud a) {
        StringBuilder code = new StringBuilder();
        String nomFonction = a.getLabel().split("/")[1];
        Symbole sym = tds.getTable().get(nomFonction);
        code.append(nomFonction + ":\n");
        code.append("PUSH(LP)\n");
        code.append("PUSH(BP)\n");
        code.append("MOVE(SP,BP)\n");
        if (sym != null)
            code.append("ALLOCATE(" + sym.getNbVar() + ")\n");
        for (Noeud f : a.getFils()) {
            if (f.getCat() == Noeud.Categories.BLOC) { //Générer les instructions du bloc
                for (Noeud instruction : f.getFils()) {
                    code.append(generer_instruction(instruction, nomFonction));
                }
            } else if (f.getCat() != Noeud.Categories.IDF) {
                code.append(generer_instruction(f, nomFonction)); //Générer les instructions directes (pas les paramètres)
            }
        }
        code.append("ret_" + nomFonction + ":\n");
        if (sym != null)
            code.append("DEALLOCATE(" + sym.getNbVar() + ")\n");
        code.append("POP(BP)\n");
        code.append("POP(LP)\n");
        code.append("RTN()\n");
        return code.toString();
    }

    public String generer_retour(Noeud a, String scope) {
        StringBuffer code = new StringBuffer();
        code.append(generer_expression(a.getFils().get(0), scope));
        code.append("POP(r0)\n");
        Symbole f = tds.getTable().get(scope);
        int nbParams = f.getNbParam();
        int offset = 8;
        code.append("PUTFRAME(r0," + offset + ")\n");
        code.append("BR(ret_" + scope + ")\n");
        return code.toString();
    }

    public String generer_affectation(Noeud a, String scope) {
        StringBuilder code = new StringBuilder();
        Noeud gauche = a.getFils().get(0);
        Noeud droit = a.getFils().get(1);
        code.append(generer_expression(droit, scope));
        code.append("POP(r0)\n");
        String nomVar = gauche.getLabel().split("/")[1];
        Symbole s = tds.getSymbole(nomVar, scope);
        if (s != null && s.getCategorie().equals("globale")) {
            code.append("ST(r0," + nomVar + ")\n");
        }
        else if (s != null && s.getCategorie().equals("locale")) {
            int offset = 12 + s.getRang() * 4;
            code.append("PUTFRAME(r0," + offset + ")\n");
        }
        else if (s != null && s.getCategorie().equals("param")) {
            Symbole f = tds.getTable().get(scope);
            int nbParams = f.getNbParam();
            int offset = (nbParams - s.getRang() + 2) * 4;
            code.append("PUTFRAME(r0," + offset + ")\n");
        }
        return code.toString();
    }

    public String generer_expression(Noeud a, String scope) {
        StringBuilder code = new StringBuilder();

        switch (a.getCat()) {

            case CONST:
                code.append("CMOVE(" + a.getLabel().split("/")[1] + ",r0)\n");
                code.append("PUSH(r0)\n");
                break;

            case IDF:
                String nomVar = a.getLabel().split("/")[1];
                Symbole s = tds.getSymbole(nomVar, scope);
                if (s != null && s.getCategorie().equals("globale")) {
                    code.append("LD(" + nomVar + ",r0)\n");
                }
                else if (s != null && s.getCategorie().equals("locale")) {
                    int offset = -(s.getRang() + 1) * 4;
                    code.append("GETFRAME(r0," + offset + ")\n");
                }
                else if (s != null && s.getCategorie().equals("param")) {
                    Symbole f = tds.getTable().get(scope);
                    int nbParams = f.getNbParam();
                    int offset = 12 + s.getRang() * 4;
                    code.append("GETFRAME(r0," + offset + ")\n");
                }
                code.append("PUSH(r0)\n");
                break;

            case PLUS:
                code.append(generer_expression(a.getFils().get(0), scope));
                code.append(generer_expression(a.getFils().get(1), scope));
                code.append("POP(r1)\n");
                code.append("POP(r0)\n");
                code.append("ADD(r0,r1,r2)\n");
                code.append("PUSH(r2)\n");
                break;

            case MOINS:
                code.append(generer_expression(a.getFils().get(0), scope));
                code.append(generer_expression(a.getFils().get(1), scope));
                code.append("POP(r1)\n");
                code.append("POP(r0)\n");
                code.append("SUB(r0,r1,r2)\n");
                code.append("PUSH(r2)\n");
                break;

            case MUL:
                code.append(generer_expression(a.getFils().get(0), scope));
                code.append(generer_expression(a.getFils().get(1), scope));
                code.append("POP(r1)\n");
                code.append("POP(r0)\n");
                code.append("MUL(r0,r1,r2)\n");
                code.append("PUSH(r2)\n");
                break;

            case DIV:
                code.append(generer_expression(a.getFils().get(0), scope));
                code.append(generer_expression(a.getFils().get(1), scope));
                code.append("POP(r1)\n");
                code.append("POP(r0)\n");
                code.append("DIV(r0,r1,r2)\n");
                code.append("PUSH(r2)\n");
                break;

            //Cas 7 : a → lire
            case LIRE:
                code.append("RDINT()\n");
                code.append("PUSH(r0)\n");
                break;

            case APPEL:
                code.append(generer_appel(a, scope));
                break;

            default:
                break;
        }
        return code.toString();
    }

    public String generer_si(Noeud a, String scope) {
        StringBuffer code = new StringBuffer();
        Si si = (Si) a;
        code.append(generer_condition(si.getCondition(), scope)); //Appliquer la méthode sur le 1er fils de a
        code.append("si_" + si.getValeur() + ":\n"); //a.valeur dans ce cas est le numéro du si (1er si rencontré, 2e,etc..)
        code.append("POP(r0)\n");
        code.append("BF(r0,sinon_" + si.getValeur() + ")\n");
        code.append(generer_bloc(si.getBlocAlors(), scope)); //Appliquer la méthode sur le 2e fils de a
        code.append("BR(fsi_" + si.getValeur() + ")\n");
        code.append("sinon_" + si.getValeur() + ":\n");
        code.append(generer_bloc(si.getBlocSinon(), scope)); //Appliquer la méthode sur le 3e fils de a
        code.append("fsi_" + si.getValeur() + ":\n");
        return code.toString();
    }

    public String generer_bloc(Noeud a, String scope) {
        StringBuffer code = new StringBuffer();
        Bloc bl = (Bloc) a;
        for (Noeud fils : bl.getFils()) {
            code.append(generer_instruction(fils, scope));
        }
        return code.toString();
    }

    public String generer_condition(Noeud a, String scope) {
        StringBuffer code = new StringBuffer();
        switch (a.getCat()) {
            //Cas 1 : a est >
            case SUP:
                Superieur sup = (Superieur) a;
                code.append(generer_expression(sup.getFilsGauche(), scope));
                code.append(generer_expression(sup.getFilsDroit(), scope));
                code.append("POP(r1)\n");
                code.append("POP(r2)\n");
                code.append("CMPLT(r1,r2,r3)\n");
                code.append("PUSH(r3)\n");
                break;

            //Cas 2 : a est >=
            case SUPE:
                SuperieurEgal supe = (SuperieurEgal) a;
                code.append(generer_expression(supe.getFilsGauche(), scope));
                code.append(generer_expression(supe.getFilsDroit(), scope));
                code.append("POP(r1)\n");
                code.append("POP(r2)\n");
                code.append("CMPLE(r1,r2,r3)\n");
                code.append("PUSH(r3)\n");
                break;

            //Cas 3 : a est
            case INF :
                Inferieur inf = (Inferieur) a;
                code.append(generer_expression(inf.getFilsGauche(), scope));
                code.append(generer_expression(inf.getFilsDroit(), scope));
                code.append("POP(r1)\n");
                code.append("POP(r2)\n");
                code.append("CMPLT(r2,r1,r3)\n");
                code.append("PUSH(r3)\n");
                break;

            //Cas 4 : a est <=
            case INFE :
                InferieurEgal infe = (InferieurEgal) a;
                code.append(generer_expression(infe.getFilsGauche(), scope));
                code.append(generer_expression(infe.getFilsDroit(), scope));
                code.append("POP(r1)\n");
                code.append("POP(r2)\n");
                code.append("CMPLT(r2,r1,r3)\n");
                code.append("PUSH(r3)\n");
                break;

            //Cas 5 : a est =
            case EG :
                Egal eg = (Egal) a;
                code.append(generer_expression(eg.getFilsGauche(), scope));
                code.append(generer_expression(eg.getFilsDroit(), scope));
                code.append("POP(r1)\n");
                code.append("POP(r2)\n");
                code.append("CMPEQ(r2,r1,r3)\n");
                code.append("PUSH(r3)\n");
                break;

            //Cas 6 : a est !=
            case DIF :
                Different diff = (Different) a;
                code.append(generer_expression(diff.getFilsGauche(), scope));
                code.append(generer_expression(diff.getFilsDroit(), scope));
                code.append("POP(r1)\n");
                code.append("POP(r2)\n");
                code.append("CMPEQ(r2,r1,r3)\n");
                code.append("CMOVE(1,r4)\n");
                code.append("XOR(r3,r4,r3)\n");
                code.append("PUSH(r3)\n");
                break;

            //Cas 7 : a est not (!)
            case NOT :
                Not not = (Not) a;
                code.append(generer_expression(not.getLeFils(), scope)); //Récupérer le seul fils de not
                code.append("POP(r1)\n");
                code.append("CMOVE(0,r2)\n");
                code.append("CMPEQ(r1,r2,r2)\n");
                code.append("PUSH(r2)\n");
                break;
        }
        return code.toString();
    }

    public String generer_tq(Noeud a, String scope) {
        StringBuffer code = new StringBuffer();
        TantQue tq = (TantQue) a;
        code.append("tq_" + tq.getValeur() + ":\n");
        code.append(generer_condition(tq.getCondition(), scope));
        code.append("POP(r0)\n");
        code.append("BF(r0,ftq_" + tq.getValeur() + ")\n");
        code.append(generer_bloc(tq.getBloc(), scope));
        code.append("BR(tq_" + tq.getValeur() + ")\n");
        code.append("ftq_" + tq.getValeur() + ":\n");
        return code.toString();
    }
}