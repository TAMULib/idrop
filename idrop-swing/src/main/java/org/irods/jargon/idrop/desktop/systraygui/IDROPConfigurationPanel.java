
/*
 * IDROPConfigurationPanel.java
 *
 * Created on Jul 18, 2011, 9:17:35 AM
 */
package org.irods.jargon.idrop.desktop.systraygui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.irods.jargon.idrop.desktop.systraygui.services.IDROPConfigurationService;
import org.irods.jargon.idrop.desktop.systraygui.utils.IDROPConfig;
import org.irods.jargon.idrop.desktop.systraygui.viscomponents.SynchConfigTableModel;
import org.irods.jargon.idrop.exceptions.IdropException;
import org.irods.jargon.idrop.exceptions.IdropRuntimeException;
import org.irods.jargon.idrop.finder.IRODSFinderDialog;
import org.irods.jargon.transfer.dao.domain.Synchronization;
import org.irods.jargon.transfer.engine.synch.SynchException;
import org.irods.jargon.transfer.engine.synch.SynchManagerService;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mikeconway
 */
public class IDROPConfigurationPanel extends javax.swing.JDialog {

    private final IDROPCore idropCore;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(IDROPConfigurationPanel.class);
    private JTable jTableSynch = null;

    /** Creates new form IDROPConfigurationPanel */
    public IDROPConfigurationPanel(java.awt.Frame parent, boolean modal, IDROPCore idropCore) {
        super(parent, modal);
        this.idropCore = idropCore;
        initComponents();
        initWithConfigData();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlTop = new javax.swing.JPanel();
        pnlCenter = new javax.swing.JPanel();
        tabConfig = new javax.swing.JTabbedPane();
        pnlConfigIdrop = new javax.swing.JPanel();
        checkShowGUI = new javax.swing.JCheckBox();
        pnlConfigGrids = new javax.swing.JPanel();
        pnlConfigTransfers = new javax.swing.JPanel();
        checkLogSuccessfulTransfer = new javax.swing.JCheckBox();
        pnlConfigSynch = new javax.swing.JPanel();
        pnlConfigSynchListing = new javax.swing.JPanel();
        scrollSynchTable = new javax.swing.JScrollPane();
        pnlConfigSynchDetails = new javax.swing.JPanel();
        pnlSynchData = new javax.swing.JPanel();
        pnlLocalSynch = new javax.swing.JPanel();
        txtLocalPath = new javax.swing.JTextField();
        btnChooseLocalSynch = new javax.swing.JButton();
        pnlSynchMode = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        radioBackup = new javax.swing.JRadioButton();
        radioFeed = new javax.swing.JRadioButton();
        radioSynch = new javax.swing.JRadioButton();
        pnlSynchFrequency = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jcomboSynchFrequency = new javax.swing.JComboBox();
        pnlIrodsSynch = new javax.swing.JPanel();
        txtIrodsPath = new javax.swing.JTextField();
        btnChooseIrodsSynch = new javax.swing.JButton();
        panelSynchToolbar = new javax.swing.JPanel();
        btnDeleteSelected = new javax.swing.JButton();
        btnNewSynch = new javax.swing.JButton();
        btnUpdateSynch = new javax.swing.JButton();
        btnSynchNow = new javax.swing.JButton();
        pnlBottom = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.title")); // NOI18N

        org.jdesktop.layout.GroupLayout pnlTopLayout = new org.jdesktop.layout.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 739, Short.MAX_VALUE)
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(pnlTop, java.awt.BorderLayout.NORTH);

        pnlCenter.setLayout(new java.awt.GridLayout(1, 0));

        pnlConfigIdrop.setLayout(new java.awt.GridBagLayout());

        checkShowGUI.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.checkShowGUI.text")); // NOI18N
        checkShowGUI.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.checkShowGUI.toolTipText")); // NOI18N
        checkShowGUI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkShowGUIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlConfigIdrop.add(checkShowGUI, gridBagConstraints);

        tabConfig.addTab(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.pnlConfigIdrop.TabConstraints.tabTitle"), pnlConfigIdrop); // NOI18N

        org.jdesktop.layout.GroupLayout pnlConfigGridsLayout = new org.jdesktop.layout.GroupLayout(pnlConfigGrids);
        pnlConfigGrids.setLayout(pnlConfigGridsLayout);
        pnlConfigGridsLayout.setHorizontalGroup(
            pnlConfigGridsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 718, Short.MAX_VALUE)
        );
        pnlConfigGridsLayout.setVerticalGroup(
            pnlConfigGridsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 494, Short.MAX_VALUE)
        );

        tabConfig.addTab(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.pnlConfigGrids.TabConstraints.tabTitle"), pnlConfigGrids); // NOI18N

        pnlConfigTransfers.setLayout(new java.awt.GridBagLayout());

        checkLogSuccessfulTransfer.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.checkLogSuccessfulTransfer.text")); // NOI18N
        checkLogSuccessfulTransfer.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.checkLogSuccessfulTransfer.toolTipText")); // NOI18N
        checkLogSuccessfulTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkLogSuccessfulTransferActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(235, 233, 236, 232);
        pnlConfigTransfers.add(checkLogSuccessfulTransfer, gridBagConstraints);

        tabConfig.addTab(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.pnlConfigTransfers.TabConstraints.tabTitle"), pnlConfigTransfers); // NOI18N

        pnlConfigSynch.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlConfigSynchComponentShown(evt);
            }
        });
        pnlConfigSynch.setLayout(new java.awt.BorderLayout());

        pnlConfigSynchListing.setLayout(new java.awt.GridLayout());
        pnlConfigSynchListing.add(scrollSynchTable);

        pnlConfigSynch.add(pnlConfigSynchListing, java.awt.BorderLayout.CENTER);

        pnlConfigSynchDetails.setLayout(new java.awt.BorderLayout());

        pnlSynchData.setLayout(new java.awt.GridBagLayout());

        txtLocalPath.setColumns(80);
        txtLocalPath.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.txtLocalPath.text")); // NOI18N
        txtLocalPath.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.txtLocalPath.toolTipText")); // NOI18N
        txtLocalPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalPathActionPerformed(evt);
            }
        });
        pnlLocalSynch.add(txtLocalPath);

        btnChooseLocalSynch.setMnemonic('c');
        btnChooseLocalSynch.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnChooseLocalSynch.text")); // NOI18N
        btnChooseLocalSynch.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnChooseLocalSynch.toolTipText")); // NOI18N
        btnChooseLocalSynch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseLocalSynchActionPerformed(evt);
            }
        });
        pnlLocalSynch.add(btnChooseLocalSynch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        pnlSynchData.add(pnlLocalSynch, gridBagConstraints);

        pnlSynchMode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlSynchMode.setLayout(new java.awt.GridLayout(0, 1));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.jLabel1.text")); // NOI18N
        pnlSynchMode.add(jLabel1);

        radioBackup.setSelected(true);
        radioBackup.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.radioBackup.text")); // NOI18N
        pnlSynchMode.add(radioBackup);

        radioFeed.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.radioFeed.text")); // NOI18N
        radioFeed.setEnabled(false);
        pnlSynchMode.add(radioFeed);

        radioSynch.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.radioSynch.text")); // NOI18N
        radioSynch.setEnabled(false);
        pnlSynchMode.add(radioSynch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        pnlSynchData.add(pnlSynchMode, gridBagConstraints);

        pnlSynchFrequency.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlSynchFrequency.setLayout(new java.awt.GridLayout(0, 1));

        jLabel5.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.jLabel5.text")); // NOI18N
        pnlSynchFrequency.add(jLabel5);

        jcomboSynchFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hourly", "Weekly", "Daily", "Every 15 Minutes", " " }));
        jcomboSynchFrequency.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.jcomboSynchFrequency.toolTipText")); // NOI18N
        pnlSynchFrequency.add(jcomboSynchFrequency);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 20, 20);
        pnlSynchData.add(pnlSynchFrequency, gridBagConstraints);

        txtIrodsPath.setColumns(80);
        txtIrodsPath.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.txtIrodsPath.text")); // NOI18N
        txtIrodsPath.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.txtIrodsPath.toolTipText")); // NOI18N
        txtIrodsPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIrodsPathActionPerformed(evt);
            }
        });
        pnlIrodsSynch.add(txtIrodsPath);

        btnChooseIrodsSynch.setMnemonic('i');
        btnChooseIrodsSynch.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnChooseIrodsSynch.text")); // NOI18N
        btnChooseIrodsSynch.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnChooseIrodsSynch.toolTipText")); // NOI18N
        btnChooseIrodsSynch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseIrodsSynchActionPerformed(evt);
            }
        });
        pnlIrodsSynch.add(btnChooseIrodsSynch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        pnlSynchData.add(pnlIrodsSynch, gridBagConstraints);

        pnlConfigSynchDetails.add(pnlSynchData, java.awt.BorderLayout.CENTER);

        btnDeleteSelected.setMnemonic('d');
        btnDeleteSelected.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnDeleteSelected.text")); // NOI18N
        btnDeleteSelected.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnDeleteSelected.toolTipText")); // NOI18N
        btnDeleteSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedActionPerformed(evt);
            }
        });
        panelSynchToolbar.add(btnDeleteSelected);

        btnNewSynch.setMnemonic('n');
        btnNewSynch.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnNewSynch.text")); // NOI18N
        btnNewSynch.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnNewSynch.toolTipText")); // NOI18N
        btnNewSynch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSynchActionPerformed(evt);
            }
        });
        panelSynchToolbar.add(btnNewSynch);

        btnUpdateSynch.setMnemonic('u');
        btnUpdateSynch.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnUpdateSynch.text")); // NOI18N
        btnUpdateSynch.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnUpdateSynch.toolTipText")); // NOI18N
        btnUpdateSynch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSynchActionPerformed(evt);
            }
        });
        panelSynchToolbar.add(btnUpdateSynch);

        btnSynchNow.setMnemonic('s');
        btnSynchNow.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnSynchNow.text")); // NOI18N
        btnSynchNow.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnSynchNow.toolTipText")); // NOI18N
        btnSynchNow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSynchNowActionPerformed(evt);
            }
        });
        panelSynchToolbar.add(btnSynchNow);

        pnlConfigSynchDetails.add(panelSynchToolbar, java.awt.BorderLayout.SOUTH);

        pnlConfigSynch.add(pnlConfigSynchDetails, java.awt.BorderLayout.SOUTH);

        tabConfig.addTab(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.pnlConfigSynch.TabConstraints.tabTitle"), pnlConfigSynch); // NOI18N

        pnlCenter.add(tabConfig);

        getContentPane().add(pnlCenter, java.awt.BorderLayout.CENTER);

        pnlBottom.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnOK.setMnemonic('O');
        btnOK.setText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnOK.text")); // NOI18N
        btnOK.setToolTipText(org.openide.util.NbBundle.getMessage(IDROPConfigurationPanel.class, "IDROPConfigurationPanel.btnOK.toolTipText")); // NOI18N
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        pnlBottom.add(btnOK);

        getContentPane().add(pnlBottom, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void checkShowGUIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkShowGUIActionPerformed
        log.info("updating show gui at startup to:{}", checkShowGUI.isSelected());
        try {
            idropCore.getIdropConfigurationService().updateConfig(IDROPConfigurationService.SHOW_GUI, Boolean.toString(checkShowGUI.isSelected()));
        } catch (IdropException ex) {
            log.error("error setting show gui property", ex);
            throw new IdropRuntimeException(ex);
        }
    }//GEN-LAST:event_checkShowGUIActionPerformed

    private void checkLogSuccessfulTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkLogSuccessfulTransferActionPerformed
        log.info("updating log successful transfers to:{}", checkLogSuccessfulTransfer.isSelected());
        try {
            idropCore.getIdropConfigurationService().updateConfig(IDROPConfigurationService.TRANSFER_ENGINE_RECORD_SUCCESSFUL_FILES, Boolean.toString(checkShowGUI.isSelected()));
        } catch (IdropException ex) {
            log.error("error setting log successful property", ex);
            throw new IdropRuntimeException(ex);
        }

    }//GEN-LAST:event_checkLogSuccessfulTransferActionPerformed

    private void pnlConfigSynchComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pnlConfigSynchComponentShown
        log.info("lazily loading synch data");

        final IDROPConfigurationPanel thisPanel = this;


        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {


                SynchManagerService synchConfigurationService = idropCore.getTransferManager().getTransferServiceFactory().instanceSynchManagerService();

                try {
                    thisPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    List<Synchronization> synchronizations = synchConfigurationService.listAllSynchronizations();
                    if (jTableSynch == null) {
                       SynchConfigTableModel synchConfigTableModel = new SynchConfigTableModel(idropCore, synchronizations);
                        jTableSynch = new JTable(synchConfigTableModel);
                        jTableSynch.getSelectionModel().addListSelectionListener(new SynchListSelectionHandler(thisPanel));
                        scrollSynchTable.setViewportView(jTableSynch);
                        scrollSynchTable.validate();
                    } else {
                        SynchConfigTableModel synchConfigTableModel = (SynchConfigTableModel) jTableSynch.getModel();
                       synchConfigTableModel.setSynchronizations(synchronizations);
                       synchConfigTableModel.fireTableDataChanged();
                    }
                } catch (SynchException ex) {
                    log.error("error setting up synchs table", ex);
                    throw new IdropRuntimeException(ex);
                } finally {
                    thisPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

    }//GEN-LAST:event_pnlConfigSynchComponentShown

    private void txtLocalPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalPathActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtLocalPathActionPerformed

    private void btnChooseLocalSynchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseLocalSynchActionPerformed
        // TODO add your handling code here:
        JFileChooser localFileChooser = new JFileChooser();
        localFileChooser.setMultiSelectionEnabled(false);
        localFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = localFileChooser.showOpenDialog(this);
        txtLocalPath.setText(localFileChooser.getSelectedFile().getAbsolutePath());
}//GEN-LAST:event_btnChooseLocalSynchActionPerformed

    private void txtIrodsPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIrodsPathActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtIrodsPathActionPerformed

    private void btnChooseIrodsSynchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseIrodsSynchActionPerformed
        try {
            IRODSFinderDialog irodsFileSystemChooserView = new IRODSFinderDialog(null, true, idropCore);
            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolkit.getScreenSize();
            final int x = (screenSize.width - irodsFileSystemChooserView.getWidth()) / 2;
            final int y = (screenSize.height - irodsFileSystemChooserView.getHeight()) / 2;
            irodsFileSystemChooserView.setLocation(x, y);
            irodsFileSystemChooserView.setVisible(true);
            String absPath = irodsFileSystemChooserView.getSelectedAbsolutePath();
            irodsFileSystemChooserView.dispose();
            if (absPath != null) {
                txtIrodsPath.setText(irodsFileSystemChooserView.getSelectedAbsolutePath());
            }

            // int returnVal = irodsFileChooser.showSaveDialog(this);
        } catch (Exception e) {
            log.error("exception choosings iRODS file");
            throw new IdropRuntimeException("exception choosing irods fie", e);
        } finally {
            idropCore.getIrodsFileSystem().closeAndEatExceptions();
        }
}//GEN-LAST:event_btnChooseIrodsSynchActionPerformed

    private void btnDeleteSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteSelectedActionPerformed

    private void btnNewSynchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSynchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewSynchActionPerformed

    private void btnUpdateSynchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSynchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateSynchActionPerformed

    private void btnSynchNowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSynchNowActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSynchNowActionPerformed

    protected JTable getSynchTable() {
        return jTableSynch;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseIrodsSynch;
    private javax.swing.JButton btnChooseLocalSynch;
    private javax.swing.JButton btnDeleteSelected;
    private javax.swing.JButton btnNewSynch;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnSynchNow;
    private javax.swing.JButton btnUpdateSynch;
    private javax.swing.JCheckBox checkLogSuccessfulTransfer;
    private javax.swing.JCheckBox checkShowGUI;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JComboBox jcomboSynchFrequency;
    private javax.swing.JPanel panelSynchToolbar;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlConfigGrids;
    private javax.swing.JPanel pnlConfigIdrop;
    private javax.swing.JPanel pnlConfigSynch;
    private javax.swing.JPanel pnlConfigSynchDetails;
    private javax.swing.JPanel pnlConfigSynchListing;
    private javax.swing.JPanel pnlConfigTransfers;
    private javax.swing.JPanel pnlIrodsSynch;
    private javax.swing.JPanel pnlLocalSynch;
    private javax.swing.JPanel pnlSynchData;
    private javax.swing.JPanel pnlSynchFrequency;
    private javax.swing.JPanel pnlSynchMode;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JRadioButton radioBackup;
    private javax.swing.JRadioButton radioFeed;
    private javax.swing.JRadioButton radioSynch;
    private javax.swing.JScrollPane scrollSynchTable;
    private javax.swing.JTabbedPane tabConfig;
    private javax.swing.JTextField txtIrodsPath;
    private javax.swing.JTextField txtLocalPath;
    // End of variables declaration//GEN-END:variables

    private void initWithConfigData() {
        IDROPConfig idropConfig = idropCore.getIdropConfig();
        checkShowGUI.setSelected(idropConfig.isShowGuiAtStartup());
        checkLogSuccessfulTransfer.setSelected(idropConfig.isLogSuccessfulTransfers());
    }

    class SynchListSelectionHandler implements ListSelectionListener {

        private final IDROPConfigurationPanel idropConfigurationPanel;

        SynchListSelectionHandler(IDROPConfigurationPanel configurationPanel) {
            this.idropConfigurationPanel = configurationPanel;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (e.getValueIsAdjusting() == true) {
                return;
            }

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            boolean isAdjusting = e.getValueIsAdjusting();

            if (lsm.isSelectionEmpty()) {
                return;
            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        updateDetailsForSelectedSynch(i);
                    }
                }
            }

        }

        private void updateDetailsForSelectedSynch(int i) {
            // make sure the most up-to-date information is displayed
            int modelIdx = idropConfigurationPanel.getSynchTable().convertRowIndexToModel(i);
            SynchConfigTableModel model = (SynchConfigTableModel) idropConfigurationPanel.getSynchTable().getModel();

            Synchronization selected = model.getSynchronizationAt(modelIdx);

            if (selected == null) {
                model.removeRow(modelIdx);
                return;
            }

            // initialize data
            txtLocalPath.setText(selected.getLocalSynchDirectory());
            txtIrodsPath.setText(selected.getIrodsSynchDirectory());
            // FIXME: stuff is just defaulting for now
            // FIXME: display name and make changable, with all of the necessary updates (should be in txfr engine synch mgr

        }
    }
}
