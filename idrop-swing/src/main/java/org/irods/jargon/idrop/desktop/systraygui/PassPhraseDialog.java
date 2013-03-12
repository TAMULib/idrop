/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.irods.jargon.idrop.desktop.systraygui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.irods.jargon.conveyor.core.ConveyorExecutionException;
import org.irods.jargon.transfer.exception.PassPhraseInvalidException;

/**
 *
 * @author lisa
 */
public class PassPhraseDialog extends javax.swing.JDialog {

    private IDROPCore idropCore;
    private boolean validated = false;
    
    /**
     * Creates new form PassPhraseDialog
     */
    public PassPhraseDialog(java.awt.Frame parent, boolean modal, IDROPCore idropCore) {
        super(parent, modal);
        this.idropCore = idropCore;
        initComponents();
        makeTextAreaLikeLabel();
    }
    
    private void makeTextAreaLikeLabel() {
        
        txtAreaWelcomeLabel.setEditable(false);  
        txtAreaWelcomeLabel.setCursor(null);  
        txtAreaWelcomeLabel.setOpaque(false);  
        txtAreaWelcomeLabel.setFocusable(false);  
        txtAreaWelcomeLabel.setFont(UIManager.getFont("Label.font"));      
        txtAreaWelcomeLabel.setWrapStyleWord(true);  
        txtAreaWelcomeLabel.setLineWrap(true);
    }
    
    public boolean isValidated() {
        return this.validated;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        txtAreaWelcomeLabel = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPassPhrase = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnForgotPassPhrase = new javax.swing.JButton();
        btnOkay = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(PassPhraseDialog.class, "PassPhraseDialog.title")); // NOI18N
        setPreferredSize(new java.awt.Dimension(535, 200));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 14, 4, 14));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 70));
        jPanel1.setLayout(new java.awt.BorderLayout());

        txtAreaWelcomeLabel.setEditable(false);
        txtAreaWelcomeLabel.setBackground(new java.awt.Color(238, 238, 238));
        txtAreaWelcomeLabel.setColumns(20);
        txtAreaWelcomeLabel.setRows(5);
        txtAreaWelcomeLabel.setText(org.openide.util.NbBundle.getMessage(PassPhraseDialog.class, "PassPhraseDialog.txtAreaWelcomeLabel.text")); // NOI18N
        jPanel1.add(txtAreaWelcomeLabel, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 14, 4, 14));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText(org.openide.util.NbBundle.getMessage(PassPhraseDialog.class, "PassPhraseDialog.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        jPanel2.add(jLabel1, gridBagConstraints);

        txtPassPhrase.setText(org.openide.util.NbBundle.getMessage(PassPhraseDialog.class, "PassPhraseDialog.txtPassPhrase.text")); // NOI18N
        txtPassPhrase.setPreferredSize(new java.awt.Dimension(400, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.6;
        jPanel2.add(txtPassPhrase, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 14, 8, 14));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel3.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel5.setLayout(new java.awt.BorderLayout());

        btnForgotPassPhrase.setText(org.openide.util.NbBundle.getMessage(PassPhraseDialog.class, "PassPhraseDialog.btnForgotPassPhrase.text")); // NOI18N
        btnForgotPassPhrase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForgotPassPhraseActionPerformed(evt);
            }
        });
        jPanel5.add(btnForgotPassPhrase, java.awt.BorderLayout.CENTER);

        btnOkay.setText(org.openide.util.NbBundle.getMessage(PassPhraseDialog.class, "PassPhraseDialog.btnOkay.text")); // NOI18N
        btnOkay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkayActionPerformed(evt);
            }
        });
        jPanel5.add(btnOkay, java.awt.BorderLayout.EAST);

        btnCancel.setText(org.openide.util.NbBundle.getMessage(PassPhraseDialog.class, "PassPhraseDialog.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel5.add(btnCancel, java.awt.BorderLayout.WEST);

        jPanel3.add(jPanel5, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnForgotPassPhraseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForgotPassPhraseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnForgotPassPhraseActionPerformed

    private void btnOkayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkayActionPerformed
        
        String passPhrase = txtPassPhrase.getText();
        
        // make sure pass phrase is entered
        if ((passPhrase == null) || (passPhrase.length() <= 0)) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a pass phrase", "Validate Pass Phrase", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            idropCore.getConveyorService().validatePassPhrase(passPhrase);
        } catch (PassPhraseInvalidException ex) {
            Logger.getLogger(IDROPDesktop.class.getName()).log(
                    Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(
                    this, "Pass phrase is invalid, Please try again.", "Validate Pass Phrase", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ConveyorExecutionException ex) {
            Logger.getLogger(IDROPDesktop.class.getName()).log(
                    Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(
                    this, "Validation of pass phrase failed", "Validate Pass Phrase", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(
            this, "Pass phrase validated successfully", "Validate Pass Phrase", JOptionPane.INFORMATION_MESSAGE);
        this.validated = true;
        
        this.dispose();
    }//GEN-LAST:event_btnOkayActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnForgotPassPhrase;
    private javax.swing.JButton btnOkay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextArea txtAreaWelcomeLabel;
    private javax.swing.JTextField txtPassPhrase;
    // End of variables declaration//GEN-END:variables
}
