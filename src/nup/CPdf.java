/*
 * CPdf.java
 *
 * Created on 18 mars 2005, 11:37
 */

package nup;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFrame;

import nup.CMiseEnPage;

public class CPdf {
    
    private PdfReader m_reader;
    private String m_nomFichierSource;
    private String m_nomFichierResultatRectos;
    private String m_nomFichierResultatVersos;
    
    public CPdf(String nomFichierSource) {
        m_nomFichierSource = nomFichierSource;
    }
    
    public Boolean ouvrirFichierSource() {
        return ouvrirFichierSource(m_nomFichierSource);
    }
    
    public int getNbPagesSource() {
        return m_reader.getNumberOfPages();
    }
    
    public Boolean ouvrirFichierSource(String nomFichier) {
        m_nomFichierSource = nomFichier;
        Boolean erreur = false;
        // Ouverture du fichier source
        try {
            m_reader = new PdfReader(m_nomFichierSource);
        } catch (IOException e) {
            System.out.println("Erreur : impossible d'ouvrir le fichier " + m_nomFichierSource);
            erreur = true;
        }
        return ! erreur;
    }
    
    public float getLargeurCm() {
        return m_reader.getPageSize(1).width() / CConfig.tauxCMtoPDF;
    }
    
    public float getLargeur() {
        return m_reader.getPageSize(1).width();
    }
    
    public float getHauteurCm() {
        return m_reader.getPageSize(1).height() / CConfig.tauxCMtoPDF;
    }
    
    /**
     * Génère le document
     */
    public void generer(CMiseEnPage miseEnPage, String nomFichierDest, boolean portrait) {
        Document resultat;
        PdfWriter writer;
        PdfContentByte cb1;
        PdfImportedPage original;
        float tauxCmToPixel = CConfig.tauxCMtoPDF;
        
        Rectangle A4Size = new Rectangle(CConfig.largeurA4 * CConfig.tauxCMtoPDF,
                CConfig.hauteurA4 * CConfig.tauxCMtoPDF);
        if (portrait) {
            resultat = new Document(A4Size);
        } else {
            resultat = new Document(A4Size.rotate());
        }
        
        writer = null;
        try {
            writer = PdfWriter.getInstance(resultat, new FileOutputStream(nomFichierDest));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
        
        resultat.open();
        cb1 = writer.getDirectContent();
        try {
            resultat.newPage();
            original = writer.getImportedPage(m_reader, 1);
            
            float taux = miseEnPage.getZoom();
            for (int i=0; i < miseEnPage.getNbL(); i++) {
                for (int j=0; j < miseEnPage.getNbH(); j++) {
                    cb1.addTemplate(original, taux, 0, 0, taux,
                            (miseEnPage.getVraieMargeG() + i*(miseEnPage.m_l + miseEnPage.m_a))
                            * CConfig.tauxCMtoPDF,
                            (miseEnPage.getVraieMargeB() + j*(miseEnPage.m_h + miseEnPage.m_b))
                            * CConfig.tauxCMtoPDF);
                }
            }
            
            resultat.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-3);
        }
        
        
        
        
        
        
        
//        writer = null;
//
//        System.out.println("Génération");
//
//        Rectangle A4Size = new Rectangle(CConfig.largeurA4 * CConfig.tauxCMtoPDF,
//                CConfig.hauteurA4 * CConfig.tauxCMtoPDF);
//        if (portrait) {
//            resultat = new Document(A4Size);
//        } else {
//            resultat = new Document(A4Size.rotate());
//        }
//
//        try {
//            writer = PdfWriter.getInstance(resultat, new FileOutputStream(nomFichierDest));
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(-2);
//        }
//
//        resultat.open();
//        cb1 = writer.getDirectContent();
//        original = writer.getImportedPage(m_reader, 1);
//        try {
//            resultat.newPage();
//                cb1.addTemplate(original, miseEnPage.getZoom(), 0, 0, miseEnPage.getZoom(),
//                        5 * tauxCmToPixel,
//                        1 * tauxCmToPixel);
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultat.close();
//            System.exit(-3);
//        }
//        resultat.close();
//
//
        
        
//            for (int i = 1; i <= m_ordrePages.getNbFeuilles(); i++) {
//                System.out.println("-  " + Integer.toString(i));
//                resultat.newPage();
//                // petite page 1
//                System.out.println("feuille : " + m_ordrePages.getFeuilleRecto(i));
//                feuille = m_ordrePages.getFeuilleRecto(i);
//                if (feuille.getPage1() != 0) {
//                    page = writer.getImportedPage(m_reader, m_ordrePages.getFeuilleRecto(i).getPage1());
//                    cb1.addTemplate(page, miseEnPage.getZoom(), 0, 0, miseEnPage.getZoom(),
//                            miseEnPage.getBG1().getx() * tauxCmToPixels,
//                            miseEnPage.getBG1().gety() * tauxCmToPixels);
//                }
//                // petite page 2
//                if (feuille.getPage2() != 0) {
//                    page = writer.getImportedPage(m_reader, m_ordrePages.getFeuilleRecto(i).getPage2());
//                    cb1.addTemplate(page, miseEnPage.getZoom(), 0, 0, miseEnPage.getZoom(),
//                            miseEnPage.getBG2().getx() * tauxCmToPixels,
//                            miseEnPage.getBG2().gety() * tauxCmToPixels);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultat.close();
//            System.exit(-3);
//        }
//        resultat.close();
//
//
//        System.out.println("- Versos");
//
//        resultat = new Document((m_reader.getPageSize(1)).rotate());
//        writer = null;
//        try {
//            writer = PdfWriter.getInstance(resultat, new FileOutputStream(fichierVersos));
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(-2);
//        }
//
//        resultat.open();
//        cb1 = writer.getDirectContent();
//        try {
//            for (int i = 1; i <= m_ordrePages.getNbFeuilles(); i++) {
//                System.out.println("-  " + Integer.toString(i));
//                resultat.newPage();
//                // petite page 1
//                System.out.println("feuille : " + m_ordrePages.getFeuilleVerso(i));
//                feuille = m_ordrePages.getFeuilleVerso(i);
//                if (feuille.getPage1() != 0) {
//                    page = writer.getImportedPage(m_reader, m_ordrePages.getFeuilleVerso(i).getPage1());
//                    cb1.addTemplate(page, miseEnPage.getZoom(), 0, 0, miseEnPage.getZoom(),
//                            miseEnPage.getBG1().getx() * tauxCmToPixels,
//                            miseEnPage.getBG1().gety() * tauxCmToPixels);
//                }
//                // petite page 2
//                if (feuille.getPage2() != 0) {
//                    page = writer.getImportedPage(m_reader, m_ordrePages.getFeuilleVerso(i).getPage2());
//                    cb1.addTemplate(page, miseEnPage.getZoom(), 0, 0, miseEnPage.getZoom(),
//                            miseEnPage.getBG2().getx() * tauxCmToPixels,
//                            miseEnPage.getBG2().gety() * tauxCmToPixels);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultat.close();
//            System.exit(-3);
//        }
//        resultat.close();
//
//        JOptionPane.showMessageDialog(null, "Fichier écrit.", "Fini", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void tests() {
        int pagenumber = 1;
        
        // step 1: creation of a document-object
        Document resultat = new Document((m_reader.getPageSize(1)).rotate());
        //   step 2: we create a writer that listens to the document
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(resultat, new FileOutputStream("tests.pdf"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
        //   step 3: we open the document
        resultat.open();
        PdfContentByte cb1 = writer.getDirectContent();
        PdfImportedPage page;
        System.out.println(resultat.getPageSize().width());
        try {
            resultat.newPage();
            page = writer.getImportedPage(m_reader, 1);
            float facteur = .1f;
            cb1.addTemplate(page, facteur, 0, 0, facteur,
                    1 * CConfig.tauxCMtoPDF, 5 * CConfig.tauxCMtoPDF);
            
            resultat.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-3);
        }
        
        
        
//        int rotation;
//        int i = 0;
//        // step 4: we add content
//        while (i < pagenumber - 1) {
//            i++;
//            document1.setPageSize(reader.getPageSizeWithRotation(i));
//            document1.newPage();
////                page = writer1.getImportedPage(reader, i);
//            rotation = reader.getPageRotation(i);
//            if (rotation == 90 || rotation == 270) {
////                    cb1.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).height());
//            } else {
////                    cb1.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
//            }
//        }
//        while (i < n) {
//            i++;
//            document2.setPageSize(reader.getPageSizeWithRotation(i));
//            document2.newPage();
////                page = writer2.getImportedPage(reader, i);
//            rotation = reader.getPageRotation(i);
//            if (rotation == 90 || rotation == 270) {
////                    cb2.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).height());
//            } else {
////                    cb2.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
//            }
//            System.out.println("Processed page " + i);
//        }
//        // step 5: we close the document
//        document1.close();
//        document2.close();
    }
    
}
