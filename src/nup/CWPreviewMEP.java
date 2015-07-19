/*
 * CWPreviewMEP.java
 *
 * Created on 18 mars 2005, 11:48
 */

package nup;

import com.lowagie.text.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

import nup.CMiseEnPage;

public class CWPreviewMEP extends JComponent {
    
    private CMiseEnPage m_miseEnPage;
    // Décalage
    private Dimension m_decalage;
    private float m_zoom;
    
    int m_longeurWidget;
    int m_hauteurWidget;
    
    /**
     * @deprecated Pour en faire un bean.
     */
    public CWPreviewMEP() {
        this(null);
    }
    
    public CWPreviewMEP(CMiseEnPage miseEnPage) {
        m_miseEnPage = miseEnPage;
        setPreferredSize(new Dimension(200,170));
    }
    
    protected void paintComponent(java.awt.Graphics g) {
        
        m_longeurWidget = getWidth() - 1;
        m_hauteurWidget = getHeight() - 1;
        
        // Pour l'affichage du bean ds netbeans ...
        if (m_miseEnPage == null) {
            g.setColor(new Color(.3f,.7f,0));
            g.drawRect(1, 1, m_longeurWidget - 1, m_hauteurWidget - 1);
            return;
        }
        
        // Calcul du décalage et du facteur de zoom
        {
            int longeur;
            int hauteur;
            m_zoom = Math.min(m_longeurWidget / m_miseEnPage.getFeuilleL(),
                    m_hauteurWidget / m_miseEnPage.getFeuilleH());
            longeur = (int) (m_zoom * m_miseEnPage.getFeuilleL()) - 1;
            hauteur = (int) (m_zoom * m_miseEnPage.getFeuilleH()) - 1;
            m_decalage = new Dimension((int) (m_longeurWidget - longeur) / 2,
                    (int) (m_hauteurWidget - hauteur) / 2);
        }
        
        // Dessine la marge utilisateur
        g.setColor(new Color(236f/256f, 233f/255f, 184f/255f));
        g.drawRect((int) (m_decalage.getWidth() + m_miseEnPage.m_mg * m_zoom),
                (int) (m_decalage.getHeight() + m_miseEnPage.m_mh * m_zoom),
                (int) ((m_miseEnPage.getFeuilleL() - m_miseEnPage.m_mg - m_miseEnPage.m_md) * m_zoom),
                (int) ((m_miseEnPage.getFeuilleH() - m_miseEnPage.m_mh - m_miseEnPage.m_mb) * m_zoom));

        // Dessine la feuille
        g.setColor(new Color(.3f,.7f,0));
        g.drawRect((int) m_decalage.getWidth(), (int) m_decalage.getHeight(),
                (int) (m_miseEnPage.getFeuilleL() * m_zoom),
                (int) (m_miseEnPage.getFeuilleH() * m_zoom));
        
        if (m_miseEnPage.erreur) {
            return;
        }
        
        // Dessine les étiquettes
        g.setColor(new Color(0,0,0));
        int largeur = (int) (m_miseEnPage.m_l * m_zoom) - 1;
        int hauteur = (int) (m_miseEnPage.m_h * m_zoom) - 1;
        m_decalage.setSize(m_decalage.getWidth() + 1, m_decalage.getHeight() + 1);
        
        for (int i=0; i < m_miseEnPage.getNbL(); i++) {
            for (int j=0; j < m_miseEnPage.getNbH(); j++) {
                dessinerEtiquette(g, i, j, largeur, hauteur);
            }
        }
        
        g.dispose();
    }
    
    
    /**
     * Dessine l'étiquette (i,j) avec le zoom f
     */
    private void dessinerEtiquette(Graphics g, int i, int j, int largeur, int hauteur) {
        int x = m_decalage.width + (int) (m_zoom*(
                m_miseEnPage.getVraieMargeG() + i*(m_miseEnPage.m_l + m_miseEnPage.m_a)));
        int y = m_decalage.height + (int) (m_zoom*(
                m_miseEnPage.getVraieMargeH() + j*(m_miseEnPage.m_h + m_miseEnPage.m_b)));
        
        g.drawRect(x, y, largeur, hauteur);
    }
    
}
