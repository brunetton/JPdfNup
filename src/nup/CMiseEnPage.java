/*
 * CMiseEnPage.java
 *
 * Created on 17 mars 2005, 20:20
 */

package nup;

import java.awt.Point;

/**
 * Contient la mise en page
 */
public final class CMiseEnPage {
    
    // Dimentions de la page (sans prendre en compte l'état portrait/paysage)
    private float m_L;
    private float m_H;
    private boolean m_portrait;
    // Marges
    public float m_mh;
    public float m_mb;
    public float m_mg;
    public float m_md;
    // Marges minimum entre les étiquettes
    public float m_a;
    public float m_b;
    // Largeur hauteur originale des etiquettes
    public float m_lo;
    public float m_ho;
    // Largeur hauteur finale des etiquettes
    public float m_l;
    public float m_h;
    // Nombre d'étiquettes en hauteur et en largeur
    public float m_u;
    public float m_v;
    // Nombre d'étiquettes total
    private float m_nbre;
    // Marges supplémentaires à ajouter aux marges données
    public float m_dg;
    public float m_dd;
    public float m_dh;
    public float m_db;
    // Echelle des étiquettes
    private float m_t;
    // En cas d'erreur (données non valides)
    public boolean erreur;
    
    
    /**
     * Initialise la mise en page.
     */
    public CMiseEnPage(boolean portrait, float mh, float mb,
            float mg, float md, float a, float b) {
        
        m_H = CConfig.hauteurA4;
        m_L = CConfig.largeurA4;
        initValeurs(portrait, mh, mb, mg, md, a, b);
        
    }
    
    /**
     * Calcule la mise en page.
     */
    public void calculeMEPNbre(float ho, float lo, float n) {
        if (n == 0) {
            erreur = true;
            return;
        }
        
        float L = getFeuilleL();
        float H = getFeuilleH();
        
        m_lo = lo;
        m_ho = ho;
        
        // Répartition des etiquettes :
        m_t = (float) Math.sqrt((double)
        ((L-m_mg-m_md) * (H-m_mh-m_mb)) / (m_lo*m_ho*n)
        );
        
        System.out.print("DEBUG - ");
        
        // Est-ce plus avantageux d'ajouter le reste d'étiquettes en lignes
        // ou une colones ?
        m_u = (L-m_mg-m_md) / (m_t*m_lo);
        m_v = (H-m_mh-m_mb) / (m_t*m_ho);
        
        System.out.print(m_u + ", " + m_v + " : " + n);
        
        float[] uv = trouverUV(m_u, m_v, n, L-m_mg-m_md, H-m_mh-m_mb, m_lo, m_ho);
        
        m_u = uv[0];
        m_v = uv[1];
        
        System.out.println(" -> " + m_u + ", " + m_v);
        
        // Recalcul de t avec les nouvelles valeures de u et v
        
        m_t = min((L-m_mg-m_md) / (m_u*m_lo), (H-m_mh-m_mb) / (m_v*m_ho));
        
        m_l = m_t*m_lo;
        m_h = m_t*m_ho;
        
        float alphaX = (L-m_u*m_l) / (m_u+1);
        float alphaY = (H-m_v*m_h) / (m_v+1);
        
        if ((alphaX < m_mg) || (alphaX < m_md)) {
            alphaX = (L-m_mg-m_md-m_u*m_l) / (m_u+1);
            m_dg = alphaX;
            m_dd = alphaX;
            m_a = alphaX;
        } else {
            m_dg = alphaX - m_mg;
            m_dd = alphaX - m_md;
            m_a = alphaX;
        }
        if ((alphaY < m_mh) || (alphaY < m_mb)) {
            alphaY = (H-m_mh-m_mb-m_v*m_h) / (m_v+1);
            m_dh = alphaY;
            m_db = alphaY;
            m_b = alphaY;
        } else {
            m_dh = alphaY - m_mh;
            m_db = alphaY - m_mb;
            m_b = alphaY;
        }
    }
    
    private float[] trouverUV(float a, float b, float n, float L, float H,
            float lo, float ho) {
        int u = (int) (a);
        int v = (int) (b);
        int U = arrondiSup(a);
        int V = arrondiSup(b);
        
        int[][] t = {
            {u,(int)(n/u)}, {u,arrondiSup(n/u)},
            {(int)(n/v),v}, {arrondiSup(n/v),v},
            {U,(int)(n/U)}, {U,arrondiSup(n/U)},
            {(int)(n/V),V}, {arrondiSup(n/V),V}
        };
        
        int uMin = -1;
        int vMin = -1;
        float tMin = 0;
        int nMin = 0;
        int produit;
        float taux;
        for (int i=0; i < t.length; i++) {
            produit = t[i][0] * t[i][1];
            if (produit == 0) continue;
            taux = min(L / (t[i][0]*lo), H / (t[i][1]*m_ho));
            if ((produit >= n) && 
                    ((taux > tMin) || ((taux == tMin) && (produit < nMin)))) {
                uMin = t[i][0];
                vMin = t[i][1];
                tMin = taux;
                nMin = produit;
            }
        }
        return new float[] {uMin, vMin};
    }
    
    /**
     * 0.0 -> 0
     * 0.1 -> 1
     * 0.2 -> 1
     * ...
     * 0.9 -> 1
     * 1.0 -> 1
     * 1.1 -> 2
     */
    private int arrondiSup(float n) {
        if (n - (int) n == 0) {
            return (int) n;
        } else {
            return (int) n + 1;
        }
    }
    
    
    /**
     * Calcule la mise en page.
     * Le boolean abAuto désigne si (a,b) est à prendre exactement ou si c'est
     * un minimum.
     * Les booleans tailleX, et tailleY désignent comment calculer la taille des
     * étiquettes : en fonction de la longueur, de la largeur ou bien des deux.
     */
    public void calculeMEPTaille(float ho, float lo, float l, float h,
            boolean abAuto, boolean tailleX, boolean tailleY) {
        
        if ((l == 0) || (h == 0) ||
                ((! tailleX) && (! tailleY)) ||
                (lo == 0) || (ho == 0)
                ) {
            erreur = true;
            return;
        }
        
        
        if ( ! (tailleX && tailleY)) {
            if (tailleX) {
                m_t = l / lo;
                h = m_t * ho;
            } else {
                m_t = h / ho;
                l = m_t * lo;
            }
        }
        
        m_ho = ho;
        m_lo = lo;
        m_l = l;
        m_h = h;
        float alphaX;
        float alphaY;
        
        float L = getFeuilleL();
        float H = getFeuilleH();
        
        if (! abAuto) {
            m_u = (float) (int) ((L - m_mg - m_md + m_a) / (m_l + m_a));
            m_v = (float) (int) ((H - m_mh - m_mb + m_b) / (m_h + m_b));
            alphaX = (L - (m_u-1)*m_a - m_u*m_l) / 2;
            alphaY = (H - (m_v-1)*m_b - m_v*m_h) / 2;
            // Vérid marges d / g
            if (alphaX < m_mg) {
                System.out.println("alpha < mg");
                // Si on déborde sur la marge de gauche
                m_dg = 0;
                m_dd = L - m_mg - m_md - (m_u-1)*m_a - m_u*m_l;
            } else {
                if (alphaX < m_md) {
                    System.out.println("alpha < md");
                    // Si on déborde sur la marge de droite
                    m_dd = 0;
                    m_dg = L - m_mg - m_md - (m_u-1)*m_a - m_u*m_l;
                } else {
                    m_dg = alphaX - m_mg;
                    m_dd = alphaX - m_md;
                }
            }
            // Vérif marges h/b
            if (alphaY < m_mh) {
                System.out.println("alpha < mh");
                // Si on déborde sur la marge du haut
                m_dh = 0;
                m_db = H - m_mh - m_mb - (m_v-1)*m_b - m_v*m_h;
            } else {
                if (alphaY < m_mb) {
                    System.out.println("alpha < mb");
                    // Si on déborde sur la marge du bas
                    m_db = 0;
                    m_dh = H - m_mh - m_mb - (m_v-1)*m_b - m_v*m_h;
                } else {
                    m_dh = alphaY - m_mh;
                    m_db = alphaY - m_mb;
                }
            }
            
        } else {
            // abAuto
            m_u = (float) (int) ((L - m_mg - m_md) / m_l);
            m_v = (float) (int) ((H - m_mh - m_mb) / m_h);
            alphaX = (L - m_u*m_l) / (m_u + 1);
            alphaY = (H - m_v*m_h) / (m_v + 1);
            // Test de débordement de marge
            if ((alphaX < m_mg) || (alphaX < m_md)) {
                alphaX = (L - m_mg - m_md -m_u*m_l) / (m_u + 1);
                m_dg = alphaX;
                m_dd = alphaX;
                m_a = alphaX;
            } else {
                // On ne déborde pas en centrant horizontalement
                m_dg = alphaX - m_mg;
                m_dd = alphaX - m_md;
                m_a = alphaX;
            }
            if ((alphaY < m_mh) || (alphaY < m_mb)) {
                alphaY = (H - m_mh - m_mb - m_v*m_h) / (m_v + 1);
                m_dh = alphaY;
                m_db = alphaY;
                m_b = alphaY;
            } else {
                // On ne déborde pas en centrant verticalement
                m_dh = alphaY - m_mh;
                m_db = alphaY - m_mb;
                m_b = alphaY;
            }
            
        }
    }
    
    public float getZoom() {
        return m_t;
    }
    
    /**
     * Renvoie la marge effective (somme de la marge utilisateur et dy)
     */
    public float getVraieMargeH() {
        return m_mh + m_dh;
    }
    
    /**
     * Renvoie la marge effective (somme de la marge utilisateur et dx)
     */
    public float getVraieMargeB() {
        return m_mb + m_db;
    }
    
    /**
     * Renvoie la marge effective (somme de la marge utilisateur et dx)
     */
    public float getVraieMargeD() {
        return m_md + m_dd;
    }
    
    /**
     * Renvoie la marge effective (somme de la marge utilisateur et dx)
     */
    public float getVraieMargeG() {
        return m_mg + m_dg;
    }
    
    /**
     * Renvoie la hauteur de la feuille
     */
    public float getFeuilleH() {
        if (m_portrait) {
            return m_H;
        } else {
            return m_L;
        }
    }
    
    /**
     * Renvoie la largeur de la feuille
     */
    public float getFeuilleL() {
        if (m_portrait) {
            return m_L;
        } else {
            return m_H;
        }
    }
    
    /**
     * Renvoie le nombre d'étiquettes total
     */
    public float getNb() {
        return m_v * m_u;
    }
    
    /**
     * Renvoie le nombre d'étiquettes en hauteur
     */
    public float getNbH() {
        return m_v;
    }
    
    /**
     * Renvoie le nombre d'étiquettes en largueur
     */
    public float getNbL() {
        return m_u;
    }
    
    /**
     * Fixe les donnees membres de la classe.
     */
    private void initValeurs(boolean portrait, float mh, float mb,
            float mg, float md, float a, float b) {
        
        m_mh = mh;
        m_mb = mb;
        m_mg = mg;
        m_md = md;
        m_a = a;
        m_b = b;
        m_portrait = portrait;
        erreur = false;
    }
    
    
    public void MAJ(boolean portrait, float mh, float mb,
            float mg, float md, float a, float b) {
        
        initValeurs(portrait, mh, mb, mg, md, a, b);
        
    }
    
    
    public String toString() {
        String s="Mise en page :";
        s += " Feuille : (" + getFeuilleL() + ", " + getFeuilleH()  + ")" + "\n"
                + " Marges : " + m_mh + " " + m_mb + " " + m_md + " " + m_mg + "\n";
        s += " a : " + m_a + "\n";
        s += " b : " + m_b + "\n";
        s += " u : " + m_u + "\n";
        s += " v : " + m_v + "\n";
        s += " d : " + m_dh + ", " + m_db + ", " + m_dd + ", " + m_dg + "\n";
        s += " Echelle : " + m_t + "\n";
        return s;
    }
    
    private float min(float x, float y) {
        if (x < y)
            return x;
        else
            return y;
    }
    
    
}
