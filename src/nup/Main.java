/*
 * Main.java
 *
 * Created on 9 mars 2005, 23:31
 */

package nup;

import nup.CFenetre;


/**
 *
 * @author Bruno Duyé
 */
public class Main {
   
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.setProperty("user.dir", "/");
        new CFenetre().setVisible(true);
//        CMiseEnPage mise = new CMiseEnPage(false,0f,0f,0f,0f,0f,0f);
//        mise.trouverProduitMin(7.07f, 2.12f, 15);
        
    }
    
}
