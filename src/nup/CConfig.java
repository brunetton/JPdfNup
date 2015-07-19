/*
 * CConfig.java
 *
 * Created on 17 mars 2005, 21:05
 */

package nup;

/**
 *
 * @author bruno DUYE
 */
public class CConfig {
    static final String suffixeFichierPDF = "-nup";
    static final String cheminHome = System.getenv("HOME");
    static final float largeurA4 = 21;
    static final float hauteurA4 = 29.7f;
//    static final float largeurA4 = 5f;
//    static final float hauteurA4 = 10f;
    // Taux de convertion entre cm et l'unit� de la classe CPdf
    static final float tauxCMtoPDF = 28.344538f;
    
}
