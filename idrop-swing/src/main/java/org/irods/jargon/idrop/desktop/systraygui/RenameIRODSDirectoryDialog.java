
/*
 * NewIRODSDirectoryDialog.java
 *
 * Created on Sep 3, 2010, 9:52:12 AM
 */
package org.irods.jargon.idrop.desktop.systraygui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.query.CollectionAndDataObjectListingEntry;
import org.irods.jargon.idrop.desktop.systraygui.services.IRODSFileService;
import org.irods.jargon.idrop.desktop.systraygui.utils.TreeUtils;
import org.irods.jargon.idrop.desktop.systraygui.viscomponents.IRODSFileSystemModel;
import org.irods.jargon.idrop.desktop.systraygui.viscomponents.IRODSNode;
import org.irods.jargon.idrop.desktop.systraygui.viscomponents.IRODSOutlineModel;
import org.irods.jargon.idrop.desktop.systraygui.viscomponents.IRODSTree;
import org.irods.jargon.idrop.exceptions.IdropException;
import org.irods.jargon.idrop.exceptions.IdropRuntimeException;
import org.slf4j.LoggerFactory;

/**
 * Dialog to gather a new directory name
 * 
 * @author mikeconway
 */
public class RenameIRODSDirectoryDialog extends javax.swing.JDialog {

    private final iDrop idrop;
    private final String currentAbsolutePath;
    private final IRODSTree stagingViewTree;
    private final IRODSNode currentNode;
    public static org.slf4j.Logger log = LoggerFactory.getLogger(RenameIRODSDirectoryDialog.class);

    /** Creates new form NewIRODSDirectoryDialog */
    public RenameIRODSDirectoryDialog(final iDrop parent, final boolean modal,
            final String currentAbsolutePath, final IRODSTree stagingViewTree,
            final IRODSNode currentNode) {
        super(parent, modal);
        this.idrop = parent;
        this.currentAbsolutePath = currentAbsolutePath;
        this.stagingViewTree = stagingViewTree;
        this.currentNode = currentNode;
        initComponents();
        int lastPathPartIdx = currentAbsolutePath.lastIndexOf("/");
        if (lastPathPartIdx == -1) {
            String msg = "could not find last path component of absolute path:"
                    + currentAbsolutePath;
            log.error(msg);
            idrop.showIdropException(new IdropException(msg));
            return;
        }

        String parentAbsPath = currentAbsolutePath.substring(0, lastPathPartIdx);
        String currentCollectionOrFileName = currentAbsolutePath.substring(lastPathPartIdx + 1);

        log.debug("computed parent abs path as:{}", parentAbsPath);
        log.debug("computed current file or collection name as:{}",
                currentCollectionOrFileName);

        txtCurrentFolder.setText(currentCollectionOrFileName);
        registerKeystrokeListener();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		lblTitle = new javax.swing.JLabel();
		pnlFolderData = new javax.swing.JPanel();
		pnlCurrentParent = new javax.swing.JPanel();
		lblCurrentParent = new java.awt.Label();
		lblNewDiretoryName = new java.awt.Label();
		txtCurrentFolder = new javax.swing.JTextField();
		txtNewFolder = new javax.swing.JTextField();
		pnlBottom = new javax.swing.JPanel();
		btnCancel = new javax.swing.JButton();
		btnOK = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Rename iRODS File or Folder");
		setName("NewParentDialog"); // NOI18N

		lblTitle.setText("Please enter a new name for the file or collection");

		lblCurrentParent.setText("Current name:");

		lblNewDiretoryName.setText("New name:");

		txtCurrentFolder.setColumns(50);
		txtCurrentFolder.setEditable(false);
		txtCurrentFolder
				.setToolTipText("A name for the new folder underneath the displayed parent");
		txtCurrentFolder.setFocusable(false);

		txtNewFolder.setColumns(50);
		txtNewFolder
				.setToolTipText("A name for the new folder underneath the displayed parent");

		btnCancel.setText("Cancel");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

		btnOK.setText("OK");
		btnOK.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				btnOKActionPerformed(evt);
			}
		});

		org.jdesktop.layout.GroupLayout pnlBottomLayout = new org.jdesktop.layout.GroupLayout(
				pnlBottom);
		pnlBottom.setLayout(pnlBottomLayout);
		pnlBottomLayout.setHorizontalGroup(pnlBottomLayout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				pnlBottomLayout.createSequentialGroup().add(576, 576, 576)
						.add(btnCancel).add(5, 5, 5).add(btnOK)));
		pnlBottomLayout.setVerticalGroup(pnlBottomLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(pnlBottomLayout.createSequentialGroup().add(5, 5, 5)
						.add(btnCancel))
				.add(pnlBottomLayout.createSequentialGroup().add(5, 5, 5)
						.add(btnOK)));

		org.jdesktop.layout.GroupLayout pnlCurrentParentLayout = new org.jdesktop.layout.GroupLayout(
				pnlCurrentParent);
		pnlCurrentParent.setLayout(pnlCurrentParentLayout);
		pnlCurrentParentLayout
				.setHorizontalGroup(pnlCurrentParentLayout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(org.jdesktop.layout.GroupLayout.TRAILING,
								pnlCurrentParentLayout
										.createSequentialGroup()
										.addContainerGap(30, Short.MAX_VALUE)
										.add(pnlCurrentParentLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.TRAILING)
												.add(lblCurrentParent,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
												.add(lblNewDiretoryName,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED)
										.add(pnlCurrentParentLayout
												.createParallelGroup(
														org.jdesktop.layout.GroupLayout.TRAILING,
														false)
												.add(txtNewFolder, 0, 0,
														Short.MAX_VALUE)
												.add(txtCurrentFolder,
														org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
														421, Short.MAX_VALUE))
										.add(213, 213, 213))
						.add(pnlCurrentParentLayout
								.createSequentialGroup()
								.add(pnlBottom,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										747,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		pnlCurrentParentLayout
				.setVerticalGroup(pnlCurrentParentLayout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(pnlCurrentParentLayout
								.createSequentialGroup()
								.add(58, 58, 58)
								.add(pnlCurrentParentLayout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.LEADING)
										.add(lblCurrentParent,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.add(txtCurrentFolder,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED)
								.add(pnlCurrentParentLayout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.LEADING)
										.add(txtNewFolder,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.add(lblNewDiretoryName,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
												org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED,
										22, Short.MAX_VALUE)
								.add(pnlBottom,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));

		lblNewDiretoryName.getAccessibleContext().setAccessibleName(
				"New directory name:");

		org.jdesktop.layout.GroupLayout pnlFolderDataLayout = new org.jdesktop.layout.GroupLayout(
				pnlFolderData);
		pnlFolderData.setLayout(pnlFolderDataLayout);
		pnlFolderDataLayout.setHorizontalGroup(pnlFolderDataLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(pnlFolderDataLayout
						.createSequentialGroup()
						.add(pnlCurrentParent,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								757,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		pnlFolderDataLayout.setVerticalGroup(pnlFolderDataLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(pnlFolderDataLayout
						.createSequentialGroup()
						.add(pnlCurrentParent,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								.add(lblTitle,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										747,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(pnlFolderData,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.add(lblTitle)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED)
						.add(pnlFolderData,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));

		getAccessibleContext()
				.setAccessibleName("Rename file or folder dialog");

		pack();
	}// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }// GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOKActionPerformed
        doRename();
    }// GEN-LAST:event_btnOKActionPerformed
		// Variables declaration - do not modify//GEN-BEGIN:variables

	private javax.swing.JButton btnCancel;

	private javax.swing.JButton btnOK;

	private java.awt.Label lblCurrentParent;

	private java.awt.Label lblNewDiretoryName;

	private javax.swing.JLabel lblTitle;

	private javax.swing.JPanel pnlBottom;

	private javax.swing.JPanel pnlCurrentParent;

	private javax.swing.JPanel pnlFolderData;

	private javax.swing.JTextField txtCurrentFolder;

	private javax.swing.JTextField txtNewFolder;

	// End of variables declaration//GEN-END:variables
    private void doRename() {
        // add the new folder to irods, add to the tree, and scroll the tree
        // into view

        if (txtNewFolder.getText().isEmpty()) {
            txtNewFolder.setBackground(Color.red);
            idrop.showMessageFromOperation("please enter a new file or collection name");
            return;
        }

        final RenameIRODSDirectoryDialog thisDialog = this;

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                log.info("renaming a file named:{}", txtCurrentFolder.getText());
                thisDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {

                    IRODSFileService irodsFileService = new IRODSFileService(
                            idrop.getIrodsAccount(), idrop.getiDropCore().getIrodsFileSystem());
                    String newPath = irodsFileService.renameIRODSFileOrDirectory(currentAbsolutePath,
                            txtNewFolder.getText());

                    log.debug("New path:{}", newPath);
                    IRODSOutlineModel irodsOutlineModel = (IRODSOutlineModel) stagingViewTree.getModel();
                    // get the parent of the new directory, and force a reload
                    // of that parent
                    String[] dirs = newPath.split("/");
                    if (dirs.length == 0) {
                        throw new IdropRuntimeException(
                                "unable to find dir components");
                    }

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < dirs.length - 1; i++) {
                        if (i > 0) {
                            sb.append("/");
                        }
                        sb.append(dirs[i]);
                    }

                    String parentOfNewDir = sb.toString();
                    log.info("parent of new dir:{}", parentOfNewDir);

                    TreePath pathForOld = TreeUtils.buildTreePathForIrodsAbsolutePath(stagingViewTree,
                            currentAbsolutePath);

                    if (pathForOld == null) {
                        log.warn(
                                "could not find old path for node:{}, ignoring",
                                currentAbsolutePath);
                    } else {
                        IRODSFileSystemModel irodsFileSystemModel = irodsOutlineModel.getTreeModel();
                        IRODSNode oldNode = (IRODSNode) pathForOld.getLastPathComponent();
                        CollectionAndDataObjectListingEntry nodesEntry = (CollectionAndDataObjectListingEntry) oldNode.getUserObject();
                        IRODSFile newEntryAsFile = idrop.getiDropCore().getIRODSFileFactoryForLoggedInAccount().instanceIRODSFile(newPath);
                        if (newEntryAsFile.isDirectory()) {
                            nodesEntry.setParentPath(newEntryAsFile.getParent());
                            nodesEntry.setPathOrName(newEntryAsFile.getAbsolutePath());
                        } else {
                            nodesEntry.setParentPath(newEntryAsFile.getParent());
                            nodesEntry.setPathOrName(newEntryAsFile.getName());
                        }

                        oldNode.setUserObject(nodesEntry);
                        irodsFileSystemModel.nodeChanged(oldNode);

                    }

                    // TreePath pathForNew =
                    // TreeUtils.buildTreePathForIrodsAbsolutePath(stagingViewTree,
                    // parentOfNewDir);
                    // log.debug("computed new path:{}", pathForNew);
					/*
                     * IRODSNode targetParentNode = (IRODSNode)
                     * pathForNew.getParentPath().getLastPathComponent();
                     * targetParentNode.forceReloadOfChildrenOfThisNode();
                     * irodsOutlineModel.reload(targetParentNode);
                     * stagingViewTree.expandPath(pathForNew);
                     */

                    idrop.showMessageFromOperation("The rename was successful");

                } catch (Exception ex) {
                    Logger.getLogger(RenameIRODSDirectoryDialog.class.getName()).log(Level.SEVERE, null, ex);
                    idrop.showIdropException(ex);
                } finally {
                    thisDialog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                thisDialog.dispose();
            }
        });
    }

    /**
     * Register a listener for the enter event, so login can occur.
     */
    private void registerKeystrokeListener() {

        KeyStroke enter = KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_ENTER, 0);
        Action enterAction = new AbstractAction() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                doRename();
            }
        };
        btnOK.registerKeyboardAction(enterAction, enter,
                JComponent.WHEN_IN_FOCUSED_WINDOW);

    }
}
