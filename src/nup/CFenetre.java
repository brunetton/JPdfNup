/*
 * CFenetre.java
 *
 * Created on 18 mars 2005, 11:46
 */

package nup;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


/**
 *
 * @author  bruno DUYE
 */
public class CFenetre extends javax.swing.JFrame {
    
    private CMiseEnPage m_miseEnPage;
    private CPdf m_pdf;
    private String m_nomFichierSource;            // Sans chemin
    private String m_nomFichierDest;            // Sans chemin
    private String m_cheminFichierSource;
    private String m_cheminFichierDest;
    private String m_repertoireCourant;           // Deriner répertoire selectionné
    private Boolean m_fichierSourceOk = false;
    private Boolean m_fichierDestOk = false;
    // Indique si on est dans le mode taille ou nombre
    private Boolean m_taille = true;
    private float m_hauteurFichierSource;
    private float m_largueurFichierSource;
    
    /** Creates new form CFenetre */
    public CFenetre() {
        m_miseEnPage = new CMiseEnPage(false, 1f,1f,2f,2f, 2f, 2f);
        m_widget = new CWPreviewMEP(m_miseEnPage);
        initComponents();
        initSpinner(jSpinnerMb);
        initSpinner(jSpinnerMh);
        initSpinner(jSpinnerMg);
        initSpinner(jSpinnerMd);
        initSpinner(jSpinnerDx);
        initSpinner(jSpinnerDy);
        initSpinner(jSpinnerTailleX);
        initSpinner(jSpinnerTailleY);
        initSpinnerEntier(jSpinnerNbPages);
        initSpinnerEntier(jSpinnerNbre);
        m_pdf = new CPdf(m_nomFichierSource);
        
        MAJMiseEnPage();
        
        // DEBUG
//        m_pdf.ouvrirFichierSource("2pages.pdf");
//        m_largueurFichierSource = m_pdf.getLargeurCm();
//        m_hauteurFichierSource = m_pdf.getHauteurCm();
        
//        m_pdf.generer(m_miseEnPage, "etiquettes.pdf", jCheckPortrait.isSelected());
        
        
//        m_fichierSourceOk = true;
//        m_fichierDestOk = true;
//        MAJBouttonGenerer();
//        m_nomFichierSource = "5pages.pdf";
//        m_cheminFichierSource = "/home/bruno/tmp/livret/";
//        m_nomFichierDestCommun = "res";
//        m_cheminFichierDest = m_cheminFichierSource;
        
//        m_pdf.ouvrirFichierSource(m_cheminFichierSource + m_nomFichierSource);
//        m_ordrePages.MAJ(m_pdf.getNbPagesSource());
        
    }
    
    private void generer() {
        System.out.println(m_cheminFichierDest + m_nomFichierDest + CConfig.suffixeFichierPDF + ".pdf");
        m_pdf.generer(m_miseEnPage, m_cheminFichierDest + m_nomFichierDest + CConfig.suffixeFichierPDF + ".pdf",
                jCheckPortrait.isSelected());
        JOptionPane message = new JOptionPane();
        message.showMessageDialog(null, "Fichier écrit.", "Fini", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void initSpinnerEntier(JSpinner s) {
        s.setModel(new SpinnerNumberModel(
                new Float(1), new Float(0), new Float(999), new Float(1f)));
    }
    
    private void initSpinner(JSpinner s) {
        s.setModel(new SpinnerNumberModel(
                new Float(1), new Float(0), new Float(999), new Float(.5f)));
    }
    
    
    /**
     * Met à jour la mise en page
     */
    private void MAJMiseEnPage() {
        if ( ! m_fichierSourceOk ) return;
        m_miseEnPage.MAJ(jCheckPortrait.isSelected(),
                (Float) jSpinnerMh.getValue(), (Float) jSpinnerMb.getValue(),
                (Float) jSpinnerMg.getValue(), (Float) jSpinnerMd.getValue(),
                (Float) jSpinnerDx.getValue(), (Float) jSpinnerDy.getValue());
        if (m_taille) {
            m_miseEnPage.calculeMEPTaille(m_hauteurFichierSource, m_largueurFichierSource,
                    (Float) jSpinnerTailleX.getValue(), (Float) jSpinnerTailleY.getValue(),
                    jCheckABAuto.isSelected(), jCheckTaillex.isSelected(), jCheckTailley.isSelected());
        } else {
            m_miseEnPage.calculeMEPNbre(m_hauteurFichierSource, m_largueurFichierSource,
                    (Float) jSpinnerNbre.getValue());
        }
        m_widget.repaint();
        afficherVrai(labelNbre_vrai, m_miseEnPage.getNb());
        afficherVrai(labelTaillex_vrai, m_miseEnPage.m_l);
        afficherVrai(labelTailley_vrai, m_miseEnPage.m_h);
        afficherVrai(labeldx_vrai, m_miseEnPage.m_a);
        afficherVrai(labeldy_vrai, m_miseEnPage.m_b);
        afficherVrai(lmd, m_miseEnPage.getVraieMargeD());
        afficherVrai(lmg, m_miseEnPage.getVraieMargeG());
        afficherVrai(lmh, m_miseEnPage.getVraieMargeH());
        afficherVrai(lmb, m_miseEnPage.getVraieMargeB());
        
        System.out.println(m_miseEnPage.toString());
    }
    
    private void afficherVrai(JLabel label, float valeur) {
        if (m_miseEnPage.erreur) {
            label.setText("");
        } else {
            label.setText(affichFloat(valeur));
        }
    }
    
    /**
     * Mets à jour l'état du boutton générer
     */
    private void MAJBouttonGenerer() {
        if (m_fichierDestOk && m_fichierSourceOk) {
            bouttonGenerer.setEnabled(true);
        } else {
            bouttonGenerer.setEnabled(false);
        }
    }
    
    private String affichFloat(float f) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(f);
    }
    
    private String enleverExtension(String s) {
        return s.split("\\.")[0];
    }
    
    private void selectionFichierSource() {
        JFileChooser fileChooser;
        if (m_repertoireCourant == "") {
            fileChooser = new javax.swing.JFileChooser(CConfig.cheminHome);
        } else {
            fileChooser = new JFileChooser(m_repertoireCourant);
        }
        fileChooser.setDialogTitle("Fichier source ...");
        int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fichier = fileChooser.getSelectedFile();
            m_repertoireCourant = fichier.getParent() + "/";
            String nomFichierSource = fichier.getAbsolutePath();
            if (m_pdf.ouvrirFichierSource(nomFichierSource) == true) {
                m_nomFichierSource = fichier.getName();
                m_cheminFichierSource = m_repertoireCourant;
                jLabelFSource.setText("Taille : (" + affichFloat(m_pdf.getLargeurCm()) +
                        "; " + affichFloat(m_pdf.getHauteurCm()) + ")");
                nomFSource.setText(nomFichierSource);
//                m_ordrePages.MAJ(m_pdf.getNbPagesSource());
//                jLabelFDest.setText("Nombre de pages : " + m_ordrePages.getNbFeuilles());
                m_fichierSourceOk = true;
                m_fichierDestOk = true;
                MAJBouttonGenerer();
                m_cheminFichierDest = m_repertoireCourant;
                m_nomFichierDest =  enleverExtension(m_nomFichierSource);
                nomFDestRectos.setText(m_cheminFichierDest + m_nomFichierDest + CConfig.suffixeFichierPDF + ".pdf");
                jLabelFDest.setText("");
                m_largueurFichierSource = m_pdf.getLargeurCm();
                m_hauteurFichierSource = m_pdf.getHauteurCm();
                jSpinnerTailleX.setValue((float) (int) m_largueurFichierSource/2);
                MAJMiseEnPage();
            } else {
                // Erreur ouverture
                jLabelFSource.setText("Erreur de chargment !");
                m_fichierSourceOk = false;
                MAJBouttonGenerer();
            }
        }
    }
    
    private void selectionFichierDest() {
        JFileChooser fileChooser;
        if (m_repertoireCourant == "") {
            fileChooser = new JFileChooser(CConfig.cheminHome);
        } else {
            fileChooser = new JFileChooser(m_repertoireCourant);
        }
        fileChooser.setDialogTitle("Fichier destination ...");
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fichierDest = fileChooser.getSelectedFile();
            m_repertoireCourant = fichierDest.getParent() + "/";
            m_cheminFichierDest = m_repertoireCourant;
            m_nomFichierDest = fichierDest.getName();
            nomFDestRectos.setText(m_cheminFichierDest + m_nomFichierDest + CConfig.suffixeFichierPDF + ".pdf");
            if ( ! m_fichierSourceOk) {
                jLabelFDest.setText("");
            }
            m_fichierDestOk = true;
            MAJBouttonGenerer();
        } else {
            // User annule
            m_fichierDestOk = false;
            MAJBouttonGenerer();
        }
        
    }
    
    private void activerGroupe1() {
        if (m_taille) {
            // On est déjà dans ce mode
            return;
        }
        setEtatGroupe1(true);
        setEtatGroupe2(false);
        m_taille = true;
    }
    
    private void activerGroupe2() {
        if ( ! m_taille) {
            // On est déjà dans ce mode
            return;
        }
        setEtatGroupe1(false);
        setEtatGroupe2(true);
        m_taille = false;
    }
    
    private void setEtatGroupe1(Boolean b) {
        JComponent[] groupe1 = {jCheckTaillex, jCheckTailley, labelTaillex_vrai,
                labelTailley_vrai, jSpinnerTailleX, jSpinnerTailleY,
                labeldx, labeldy, labeldx_vrai, labeldy_vrai, jSpinnerDx,
                jSpinnerDy, jCheckABAuto
        };
        for (JComponent i : groupe1) {
            i.setEnabled(b);
        }
    }
    
    private void setEtatGroupe2(Boolean b) {
        JComponent[] groupe2 = {labelNbre, labelNbre_vrai, jSpinnerNbre};
        for (JComponent i : groupe2) {
            i.setEnabled(b);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        panelFSource = new javax.swing.JPanel();
        nomFSource = new javax.swing.JTextField();
        bouttonParcourirSource = new javax.swing.JButton();
        jLabelFSource = new javax.swing.JLabel();
        panelFDest = new javax.swing.JPanel();
        nomFDestRectos = new javax.swing.JTextField();
        bouttonParcourirSource1 = new javax.swing.JButton();
        jLabelFDest = new javax.swing.JLabel();
        bas = new javax.swing.JPanel();
        bouttonGenerer = new javax.swing.JButton();
        panelOptions = new javax.swing.JPanel();
        jSpinnerNbPages = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerTailleY = new javax.swing.JSpinner();
        labelTailley_vrai = new javax.swing.JLabel();
        labelNbre = new javax.swing.JLabel();
        jSpinnerNbre = new javax.swing.JSpinner();
        labelTaillex_vrai = new javax.swing.JLabel();
        jSpinnerTailleX = new javax.swing.JSpinner();
        jRadioNombre = new javax.swing.JRadioButton();
        jRadioTaille = new javax.swing.JRadioButton();
        jCheckPortrait = new javax.swing.JCheckBox();
        jCheckTaillex = new javax.swing.JCheckBox();
        jCheckTailley = new javax.swing.JCheckBox();
        labelNbre_vrai = new javax.swing.JLabel();
        panelMarges = new javax.swing.JPanel();
        m_widget = new CWPreviewMEP(m_miseEnPage);
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerMh = new javax.swing.JSpinner();
        jSpinnerMd = new javax.swing.JSpinner();
        jSpinnerMg = new javax.swing.JSpinner();
        jSpinnerMb = new javax.swing.JSpinner();
        lmh = new javax.swing.JLabel();
        lmb = new javax.swing.JLabel();
        lmg = new javax.swing.JLabel();
        labeldy = new javax.swing.JLabel();
        jSpinnerDy = new javax.swing.JSpinner();
        labeldy_vrai = new javax.swing.JLabel();
        labeldx = new javax.swing.JLabel();
        jSpinnerDx = new javax.swing.JSpinner();
        labeldx_vrai = new javax.swing.JLabel();
        jCheckABAuto = new javax.swing.JCheckBox();
        lmd = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JPdfNup 0.1");
        getAccessibleContext().setAccessibleName("JPDFNup 0.1");
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelFSource.setLayout(new java.awt.GridBagLayout());

        panelFSource.setBorder(new javax.swing.border.TitledBorder("Fichier source"));
        panelFSource.setMinimumSize(new java.awt.Dimension(500, 80));
        panelFSource.setPreferredSize(new java.awt.Dimension(500, 80));
        nomFSource.setMinimumSize(new java.awt.Dimension(200, 19));
        nomFSource.setPreferredSize(new java.awt.Dimension(400, 19));
        panelFSource.add(nomFSource, new java.awt.GridBagConstraints());

        bouttonParcourirSource.setText("...");
        bouttonParcourirSource.setMaximumSize(new java.awt.Dimension(43, 19));
        bouttonParcourirSource.setMinimumSize(new java.awt.Dimension(43, 19));
        bouttonParcourirSource.setPreferredSize(new java.awt.Dimension(43, 19));
        bouttonParcourirSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouttonParcourirSourceActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        panelFSource.add(bouttonParcourirSource, gridBagConstraints);

        jLabelFSource.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabelFSource.setText("Fichier non charg\u00e9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        panelFSource.add(jLabelFSource, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(panelFSource, gridBagConstraints);

        panelFDest.setLayout(new java.awt.GridBagLayout());

        panelFDest.setBorder(new javax.swing.border.TitledBorder("Fichiers destination"));
        panelFDest.setMinimumSize(new java.awt.Dimension(500, 100));
        panelFDest.setPreferredSize(new java.awt.Dimension(500, 120));
        nomFDestRectos.setFocusable(false);
        nomFDestRectos.setMinimumSize(new java.awt.Dimension(200, 19));
        nomFDestRectos.setPreferredSize(new java.awt.Dimension(400, 19));
        panelFDest.add(nomFDestRectos, new java.awt.GridBagConstraints());

        bouttonParcourirSource1.setText("...");
        bouttonParcourirSource1.setMinimumSize(new java.awt.Dimension(43, 19));
        bouttonParcourirSource1.setPreferredSize(new java.awt.Dimension(43, 19));
        bouttonParcourirSource1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouttonParcourirSource1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        panelFDest.add(bouttonParcourirSource1, gridBagConstraints);

        jLabelFDest.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabelFDest.setText("Non s\u00e9lectionn\u00e9");
        jLabelFDest.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLabelFDestPropertyChange(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        panelFDest.add(jLabelFDest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(panelFDest, gridBagConstraints);

        bas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

        bouttonGenerer.setText("Generer !");
        bouttonGenerer.setEnabled(false);
        bouttonGenerer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bouttonGenererActionPerformed(evt);
            }
        });

        bas.add(bouttonGenerer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        jPanel1.add(bas, gridBagConstraints);

        panelOptions.setLayout(new java.awt.GridBagLayout());

        panelOptions.setBorder(new javax.swing.border.TitledBorder("Options"));
        panelOptions.setMinimumSize(new java.awt.Dimension(520, 110));
        panelOptions.setPreferredSize(new java.awt.Dimension(500, 110));
        jSpinnerNbPages.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerNbPages.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerNbPages.setName("");
        jSpinnerNbPages.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panelOptions.add(jSpinnerNbPages, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setText("Nbre pages");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelOptions.add(jLabel8, gridBagConstraints);

        jSpinnerTailleY.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerTailleY.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerTailleY.setName("");
        jSpinnerTailleY.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerTailleY.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerTailleYStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panelOptions.add(jSpinnerTailleY, gridBagConstraints);

        labelTailley_vrai.setFont(new java.awt.Font("Dialog", 0, 12));
        labelTailley_vrai.setText(" ");
        labelTailley_vrai.setMaximumSize(new java.awt.Dimension(60, 15));
        labelTailley_vrai.setMinimumSize(new java.awt.Dimension(40, 15));
        labelTailley_vrai.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelOptions.add(labelTailley_vrai, gridBagConstraints);

        labelNbre.setFont(new java.awt.Font("Dialog", 0, 12));
        labelNbre.setText("Nombre");
        labelNbre.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        panelOptions.add(labelNbre, gridBagConstraints);

        jSpinnerNbre.setBackground(new java.awt.Color(255, 255, 255));
        jSpinnerNbre.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerNbre.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerNbre.setEnabled(false);
        jSpinnerNbre.setName("");
        jSpinnerNbre.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerNbre.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerNbreStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panelOptions.add(jSpinnerNbre, gridBagConstraints);

        labelTaillex_vrai.setFont(new java.awt.Font("Dialog", 0, 12));
        labelTaillex_vrai.setText(" ");
        labelTaillex_vrai.setMaximumSize(new java.awt.Dimension(60, 15));
        labelTaillex_vrai.setMinimumSize(new java.awt.Dimension(40, 15));
        labelTaillex_vrai.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panelOptions.add(labelTaillex_vrai, gridBagConstraints);

        jSpinnerTailleX.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerTailleX.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerTailleX.setName("");
        jSpinnerTailleX.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerTailleX.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerTailleXStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 8);
        panelOptions.add(jSpinnerTailleX, gridBagConstraints);

        buttonGroup1.add(jRadioNombre);
        jRadioNombre.setFont(new java.awt.Font("Dialog", 0, 12));
        jRadioNombre.setText("Nombre");
        jRadioNombre.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioNombreItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panelOptions.add(jRadioNombre, gridBagConstraints);

        buttonGroup1.add(jRadioTaille);
        jRadioTaille.setFont(new java.awt.Font("Dialog", 0, 12));
        jRadioTaille.setSelected(true);
        jRadioTaille.setText("Taille");
        jRadioTaille.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioTailleItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        panelOptions.add(jRadioTaille, gridBagConstraints);

        jCheckPortrait.setFont(new java.awt.Font("Dialog", 0, 12));
        jCheckPortrait.setText("Portrait");
        jCheckPortrait.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckPortraitItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelOptions.add(jCheckPortrait, gridBagConstraints);

        jCheckTaillex.setFont(new java.awt.Font("Dialog", 0, 12));
        jCheckTaillex.setSelected(true);
        jCheckTaillex.setText("Taille x");
        jCheckTaillex.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckTaillexItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panelOptions.add(jCheckTaillex, gridBagConstraints);

        jCheckTailley.setFont(new java.awt.Font("Dialog", 0, 12));
        jCheckTailley.setText("Taille y");
        jCheckTailley.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckTailleyItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panelOptions.add(jCheckTailley, gridBagConstraints);

        labelNbre_vrai.setFont(new java.awt.Font("Dialog", 0, 12));
        labelNbre_vrai.setText(" ");
        labelNbre_vrai.setMaximumSize(new java.awt.Dimension(60, 15));
        labelNbre_vrai.setMinimumSize(new java.awt.Dimension(40, 15));
        labelNbre_vrai.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        panelOptions.add(labelNbre_vrai, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(panelOptions, gridBagConstraints);

        panelMarges.setLayout(new java.awt.GridBagLayout());

        panelMarges.setBorder(new javax.swing.border.TitledBorder("Marges"));
        panelMarges.setFont(new java.awt.Font("Dialog", 0, 11));
        m_widget.setMinimumSize(new java.awt.Dimension(300, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 10, 10);
        panelMarges.add(m_widget, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText("Haut");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("Bas");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("Gauche");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setText("Droite");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel2.add(jLabel4, gridBagConstraints);

        jSpinnerMh.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerMh.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerMh.setName("");
        jSpinnerMh.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerMh.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerMhStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        jPanel2.add(jSpinnerMh, gridBagConstraints);

        jSpinnerMd.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerMd.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerMd.setName("");
        jSpinnerMd.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerMd.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerMdStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        jPanel2.add(jSpinnerMd, gridBagConstraints);

        jSpinnerMg.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerMg.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerMg.setName("");
        jSpinnerMg.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerMg.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerMgStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        jPanel2.add(jSpinnerMg, gridBagConstraints);

        jSpinnerMb.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerMb.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerMb.setName("");
        jSpinnerMb.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerMb.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerMbStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        jPanel2.add(jSpinnerMb, gridBagConstraints);

        lmh.setFont(new java.awt.Font("Dialog", 0, 12));
        lmh.setText(" ");
        lmh.setMaximumSize(new java.awt.Dimension(60, 15));
        lmh.setMinimumSize(new java.awt.Dimension(40, 15));
        lmh.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel2.add(lmh, gridBagConstraints);

        lmb.setFont(new java.awt.Font("Dialog", 0, 12));
        lmb.setMaximumSize(new java.awt.Dimension(60, 15));
        lmb.setMinimumSize(new java.awt.Dimension(40, 15));
        lmb.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel2.add(lmb, gridBagConstraints);

        lmg.setFont(new java.awt.Font("Dialog", 0, 12));
        lmg.setText(" ");
        lmg.setMaximumSize(new java.awt.Dimension(60, 15));
        lmg.setMinimumSize(new java.awt.Dimension(40, 15));
        lmg.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel2.add(lmg, gridBagConstraints);

        labeldy.setFont(new java.awt.Font("Dialog", 0, 12));
        labeldy.setText("dy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        jPanel2.add(labeldy, gridBagConstraints);

        jSpinnerDy.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerDy.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerDy.setName("");
        jSpinnerDy.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerDy.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerDyStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        jPanel2.add(jSpinnerDy, gridBagConstraints);

        labeldy_vrai.setFont(new java.awt.Font("Dialog", 0, 12));
        labeldy_vrai.setText(" ");
        labeldy_vrai.setMaximumSize(new java.awt.Dimension(60, 15));
        labeldy_vrai.setMinimumSize(new java.awt.Dimension(40, 15));
        labeldy_vrai.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        jPanel2.add(labeldy_vrai, gridBagConstraints);

        labeldx.setFont(new java.awt.Font("Dialog", 0, 12));
        labeldx.setText("dx");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        jPanel2.add(labeldx, gridBagConstraints);

        jSpinnerDx.setFont(new java.awt.Font("Dialog", 0, 12));
        jSpinnerDx.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(15), new Float(.5f)));
        jSpinnerDx.setName("");
        jSpinnerDx.setPreferredSize(new java.awt.Dimension(50, 20));
        jSpinnerDx.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerDxStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 0, 15);
        jPanel2.add(jSpinnerDx, gridBagConstraints);

        labeldx_vrai.setFont(new java.awt.Font("Dialog", 0, 12));
        labeldx_vrai.setText(" ");
        labeldx_vrai.setMaximumSize(new java.awt.Dimension(60, 15));
        labeldx_vrai.setMinimumSize(new java.awt.Dimension(40, 15));
        labeldx_vrai.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        jPanel2.add(labeldx_vrai, gridBagConstraints);

        jCheckABAuto.setFont(new java.awt.Font("Dialog", 0, 12));
        jCheckABAuto.setText("Auto");
        jCheckABAuto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckABAutoItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        jPanel2.add(jCheckABAuto, gridBagConstraints);

        lmd.setFont(new java.awt.Font("Dialog", 0, 12));
        lmd.setText(" ");
        lmd.setMaximumSize(new java.awt.Dimension(60, 15));
        lmd.setMinimumSize(new java.awt.Dimension(40, 15));
        lmd.setPreferredSize(new java.awt.Dimension(40, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jPanel2.add(lmd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelMarges.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(panelMarges, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents
    
    private void bouttonGenererActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouttonGenererActionPerformed
        generer();
    }//GEN-LAST:event_bouttonGenererActionPerformed
    
    private void jLabelFDestPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLabelFDestPropertyChange
        
    }//GEN-LAST:event_jLabelFDestPropertyChange
    
    private void jCheckTailleyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckTailleyItemStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jCheckTailleyItemStateChanged
    
    private void jCheckTaillexItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckTaillexItemStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jCheckTaillexItemStateChanged
    
    private void jRadioTailleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioTailleItemStateChanged
        if (jRadioTaille.isSelected()) {
            activerGroupe1();
        }
        MAJMiseEnPage();
    }//GEN-LAST:event_jRadioTailleItemStateChanged
    
    private void jRadioNombreItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioNombreItemStateChanged
        if (jRadioNombre.isSelected()) {
            activerGroupe2();
        }
        MAJMiseEnPage();
    }//GEN-LAST:event_jRadioNombreItemStateChanged
    
    private void jCheckPortraitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckPortraitItemStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jCheckPortraitItemStateChanged
    
    private void jSpinnerTailleYStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerTailleYStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerTailleYStateChanged
    
    private void jCheckABAutoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckABAutoItemStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jCheckABAutoItemStateChanged
    
    private void jSpinnerNbreStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerNbreStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerNbreStateChanged
    
    private void jSpinnerTailleXStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerTailleXStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerTailleXStateChanged
    
    private void jSpinnerDyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerDyStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerDyStateChanged
    
    private void jSpinnerDxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerDxStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerDxStateChanged
    
    private void bouttonParcourirSource1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouttonParcourirSource1ActionPerformed
        selectionFichierDest();
    }//GEN-LAST:event_bouttonParcourirSource1ActionPerformed
    
    private void bouttonParcourirSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bouttonParcourirSourceActionPerformed
        selectionFichierSource();
    }//GEN-LAST:event_bouttonParcourirSourceActionPerformed
    
    private void jSpinnerMdStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerMdStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerMdStateChanged
    
    private void jSpinnerMgStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerMgStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerMgStateChanged
    
    private void jSpinnerMbStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerMbStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerMbStateChanged
    
    private void jSpinnerMhStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerMhStateChanged
        MAJMiseEnPage();
    }//GEN-LAST:event_jSpinnerMhStateChanged
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CFenetre f = new CFenetre();
                f.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bas;
    private javax.swing.JButton bouttonGenerer;
    private javax.swing.JButton bouttonParcourirSource;
    private javax.swing.JButton bouttonParcourirSource1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox jCheckABAuto;
    private javax.swing.JCheckBox jCheckPortrait;
    private javax.swing.JCheckBox jCheckTaillex;
    private javax.swing.JCheckBox jCheckTailley;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelFDest;
    private javax.swing.JLabel jLabelFSource;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioNombre;
    private javax.swing.JRadioButton jRadioTaille;
    private javax.swing.JSpinner jSpinnerDx;
    private javax.swing.JSpinner jSpinnerDy;
    private javax.swing.JSpinner jSpinnerMb;
    private javax.swing.JSpinner jSpinnerMd;
    private javax.swing.JSpinner jSpinnerMg;
    private javax.swing.JSpinner jSpinnerMh;
    private javax.swing.JSpinner jSpinnerNbPages;
    private javax.swing.JSpinner jSpinnerNbre;
    private javax.swing.JSpinner jSpinnerTailleX;
    private javax.swing.JSpinner jSpinnerTailleY;
    private javax.swing.JLabel labelNbre;
    private javax.swing.JLabel labelNbre_vrai;
    private javax.swing.JLabel labelTaillex_vrai;
    private javax.swing.JLabel labelTailley_vrai;
    private javax.swing.JLabel labeldx;
    private javax.swing.JLabel labeldx_vrai;
    private javax.swing.JLabel labeldy;
    private javax.swing.JLabel labeldy_vrai;
    private javax.swing.JLabel lmb;
    private javax.swing.JLabel lmd;
    private javax.swing.JLabel lmg;
    private javax.swing.JLabel lmh;
    private nup.CWPreviewMEP m_widget;
    private javax.swing.JTextField nomFDestRectos;
    private javax.swing.JTextField nomFSource;
    private javax.swing.JPanel panelFDest;
    private javax.swing.JPanel panelFSource;
    private javax.swing.JPanel panelMarges;
    private javax.swing.JPanel panelOptions;
    // End of variables declaration//GEN-END:variables
    
}
