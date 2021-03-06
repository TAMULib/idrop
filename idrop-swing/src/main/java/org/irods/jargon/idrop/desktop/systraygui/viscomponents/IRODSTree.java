package org.irods.jargon.idrop.desktop.systraygui.viscomponents;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.irods.jargon.core.query.CollectionAndDataObjectListingEntry;
import org.irods.jargon.idrop.desktop.systraygui.DeleteIRODSDialog;
import org.irods.jargon.idrop.desktop.systraygui.IRODSTreeContainingComponent;
import org.irods.jargon.idrop.desktop.systraygui.NewIRODSDirectoryDialog;
import org.irods.jargon.idrop.desktop.systraygui.RenameIRODSDirectoryDialog;
import org.irods.jargon.idrop.desktop.systraygui.iDrop;
import org.irods.jargon.idrop.exceptions.IdropException;
import org.irods.jargon.idrop.exceptions.IdropRuntimeException;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.TreePathSupport;
import org.slf4j.LoggerFactory;

/**
 * Swing JTree component for viewing iRODS server file system
 * 
 * @author Mike Conway - DICE (www.irods.org)
 */
public class IRODSTree extends Outline implements TreeWillExpandListener,
		TreeExpansionListener, IRODSTreeContainingComponent {

	/**
     *
     */
	private static final long serialVersionUID = -7815706939610881953L;
	public static org.slf4j.Logger log = LoggerFactory
			.getLogger(IRODSTree.class);
	protected iDrop idropParentGui = null;
	protected JPopupMenu m_popup = null;
	protected Action m_action;
	protected TreePath m_clickedPath;
	protected IRODSTree thisTree;
	private boolean refreshingTree = false;
	TreePathSupport tps;

	@Override
	public boolean isRefreshingTree() {
		synchronized (this) {
			return refreshingTree;
		}
	}

	public void setRefreshingTree(final boolean refreshingTree) {
		synchronized (this) {
			this.refreshingTree = refreshingTree;
		}
	}

	public IRODSTree(final TreeModel newModel, final iDrop idropParentGui) {
		super();

		DefaultOutlineModel.createOutlineModel(newModel, new IRODSRowModel(),
				true, "File System");
		this.idropParentGui = idropParentGui;

		initializeMenusAndListeners();
	}

	public IRODSTree() {
		super();
	}

	public IRODSTree(final iDrop idropParentGui) {
		super();
		this.idropParentGui = idropParentGui;
		initializeMenusAndListeners();
	}

	private void initializeMenusAndListeners() {
		tps = new TreePathSupport(getOutlineModel(), getLayoutCache());
		tps.addTreeExpansionListener(this);
		tps.addTreeWillExpandListener(this);
		setDragEnabled(true);
		setTransferHandler(new IRODSTreeTransferHandler(idropParentGui,
				"selectionModel"));
		setDropMode(javax.swing.DropMode.USE_SELECTION);
		setRenderDataProvider(new OutlineRenderProvider(this));
		setUpTreeMenu();
		IrodsSelectionListenerForBuildingInfoPanel treeListener;
		try {
			treeListener = new IrodsSelectionListenerForBuildingInfoPanel(
					idropParentGui);
		} catch (IdropException ex) {
			Logger.getLogger(IRODSTree.class.getName()).log(Level.SEVERE, null,
					ex);
			throw new IdropRuntimeException(
					"error initializing selection listener", ex);
		}

		getSelectionModel().addListSelectionListener(treeListener);

		/*
		 * ActionMap map = this.getActionMap();
		 * map.put(IRODSTreeTransferHandler.
		 * getCutAction().getValue(Action.NAME),
		 * IRODSTreeTransferHandler.getCutAction());
		 * map.put(IRODSTreeTransferHandler
		 * .getCopyAction().getValue(Action.NAME),
		 * IRODSTreeTransferHandler.getCopyAction());
		 * map.put(IRODSTreeTransferHandler
		 * .getPasteAction().getValue(Action.NAME),
		 * IRODSTreeTransferHandler.getPasteAction());
		 * 
		 * InputMap imap = this.getInputMap();
		 * imap.put(KeyStroke.getKeyStroke("ctrl X"),
		 * TransferHandler.getCutAction().getValue(Action.NAME));
		 * imap.put(KeyStroke.getKeyStroke("ctrl C"),
		 * TransferHandler.getCopyAction().getValue(Action.NAME));
		 * imap.put(KeyStroke.getKeyStroke("ctrl V"),
		 * TransferHandler.getPasteAction().getValue(Action.NAME));
		 */

	}

	/**
	 * Set up context sensitive tree menu
	 */
	private void setUpTreeMenu() {
		thisTree = this;
		m_popup = new JPopupMenu();
		m_action = new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7892834665485518474L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (m_clickedPath == null) {
					return;
				}

				if (thisTree.isExpanded(m_clickedPath)) {
					thisTree.collapsePath(m_clickedPath);
				} else {
					thisTree.expandPath(m_clickedPath);
				}
			}
		};

		m_popup.add(m_action);

		Action newAction = new AbstractAction("New Folder") {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5440066731375232885L;

			@Override
			public void actionPerformed(final ActionEvent e) {

				log.info("adding new node");

				IRODSNode parent = (IRODSNode) m_clickedPath
						.getLastPathComponent();
				log.info("parent of new node is: {}", parent);
				CollectionAndDataObjectListingEntry dataEntry = (CollectionAndDataObjectListingEntry) parent
						.getUserObject();
				if (dataEntry.getObjectType() == CollectionAndDataObjectListingEntry.ObjectType.DATA_OBJECT) {
					JOptionPane
							.showMessageDialog(
									thisTree,
									"The selected item is not a folder, cannot create a new directory",
									"Info", JOptionPane.INFORMATION_MESSAGE);
					log.info("new folder not created, the selected parent is not a collection");
					return;
				}
				// show a dialog asking for the new directory name...
				NewIRODSDirectoryDialog newDirectoryDialog = new NewIRODSDirectoryDialog(
						idropParentGui, true, dataEntry.getPathOrName(),
						thisTree, parent);
				newDirectoryDialog
						.setLocation(
								(int) (idropParentGui.getLocation().getX() + idropParentGui
										.getWidth() / 2), (int) (idropParentGui
										.getLocation().getY() + idropParentGui
										.getHeight() / 2));
				newDirectoryDialog.setVisible(true);
			}
		};
		m_popup.add(newAction);

		m_popup.addSeparator();

		Action a1 = new AbstractAction("Delete") {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5755781062958243058L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				log.info("deleting a node");
				int[] rows = thisTree.getSelectedRows();
				log.debug("selected rows for delete:{}", rows);

				DeleteIRODSDialog deleteDialog;

				if (rows.length == 1) {

					IRODSNode toDelete = (IRODSNode) thisTree.getValueAt(
							rows[0], 0);
					log.info("deleting a single node: {}", toDelete);
					deleteDialog = new DeleteIRODSDialog(idropParentGui, true,
							thisTree, toDelete);
				} else {
					List<IRODSNode> nodesToDelete = new ArrayList<IRODSNode>();
					for (int row : rows) {
						nodesToDelete.add((IRODSNode) thisTree.getValueAt(row,
								0));

					}

					deleteDialog = new DeleteIRODSDialog(idropParentGui, true,
							thisTree, nodesToDelete);
				}

				deleteDialog
						.setLocation(
								(int) (idropParentGui.getLocation().getX() + idropParentGui
										.getWidth() / 2), (int) (idropParentGui
										.getLocation().getY() + idropParentGui
										.getHeight() / 2));
				deleteDialog.setVisible(true);
			}
		};

		m_popup.add(a1);
		Action a2 = new AbstractAction("Rename") {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2540520099138042530L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				log.info("renaming node");

				IRODSNode toRename = (IRODSNode) m_clickedPath
						.getLastPathComponent();
				log.info("node to rename  is: {}", toRename);
				CollectionAndDataObjectListingEntry dataEntry = (CollectionAndDataObjectListingEntry) toRename
						.getUserObject();

				// dialog uses absolute path, so munge it for files
				StringBuilder sb = new StringBuilder();
				if (dataEntry.getObjectType() == CollectionAndDataObjectListingEntry.ObjectType.COLLECTION) {
					sb.append(dataEntry.getPathOrName());
				} else {
					sb.append(dataEntry.getParentPath());
					sb.append('/');
					sb.append(dataEntry.getPathOrName());
				}

				// show a dialog asking for the new directory name...
				RenameIRODSDirectoryDialog renameDialog = new RenameIRODSDirectoryDialog(
						idropParentGui, true, sb.toString(), thisTree, toRename);
				renameDialog
						.setLocation(
								(int) (idropParentGui.getLocation().getX() + idropParentGui
										.getWidth() / 2), (int) (idropParentGui
										.getLocation().getY() + idropParentGui
										.getHeight() / 2));
				renameDialog.setVisible(true);
			}
		};
		m_popup.add(a2);
		thisTree.add(m_popup);
		thisTree.addMouseListener(new PopupTrigger());

	}

	@Override
	public void treeExpanded(final TreeExpansionEvent event) {
	}

	@Override
	public void treeCollapsed(final TreeExpansionEvent event) {
	}

	class PopupTrigger extends MouseAdapter {

		@Override
		public void mouseReleased(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				int x = e.getX();
				int y = e.getY();

				TreePath path = thisTree.getClosestPathForLocation(x, y);
				if (path != null) {
					if (thisTree.isExpanded(path)) {
						m_action.putValue(Action.NAME, "Collapse");
					} else {
						m_action.putValue(Action.NAME, "Expand");
					}
					m_popup.show(thisTree, x, y);
					m_clickedPath = path;
				}
			} else if (e.getClickCount() == 2) {
				int x = e.getX();
				int y = e.getY();
				TreePath path = thisTree.getClosestPathForLocation(x, y);

				IRODSNode inode = null;
				String fullPath = null;
				if (path != null) {
					Object node = path.getLastPathComponent();
					if (node instanceof IRODSNode) {
						inode = (IRODSNode) node;
						CollectionAndDataObjectListingEntry entry = (CollectionAndDataObjectListingEntry) inode
								.getUserObject();

						if (entry.isCollection()) {
							fullPath = entry.getPathOrName();
						} else {
							fullPath = entry.getParentPath();
						}
					}

					if (fullPath != null) {
						idropParentGui.getiDropCore().setBasePath(fullPath);
						idropParentGui.buildTargetTree(false);
					}
				}
			}
		}

		@Override
		public void mousePressed(final MouseEvent e) {
			if (e.isPopupTrigger()) {
				int x = e.getX();
				int y = e.getY();
				TreePath path = thisTree.getClosestPathForLocation(x, y);
				if (path != null) {
					if (thisTree.isExpanded(path)) {
						m_action.putValue(Action.NAME, "Collapse");
					} else {
						m_action.putValue(Action.NAME, "Expand");
					}
					m_popup.show(thisTree, x, y);
					m_clickedPath = path;
				}
			}
		}
	}

	@Override
	public void treeWillCollapse(final TreeExpansionEvent event)
			throws ExpandVetoException {
	}

	@Override
	public void treeWillExpand(final TreeExpansionEvent event)
			throws ExpandVetoException {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		log.debug("tree expansion event:{}", event);
		IRODSNode expandingNode = (IRODSNode) event.getPath()
				.getLastPathComponent();
		// If I am refreshing the tree, then do not close the connection after
		// each load. It will be closed in the thing
		// doing the refreshing
		try {
			expandingNode.lazyLoadOfChildrenOfThisNode(!isRefreshingTree());
		} catch (IdropException ex) {
			Logger.getLogger(IRODSTree.class.getName()).log(Level.SEVERE, null,
					ex);
			idropParentGui.showIdropException(ex);
			throw new IdropRuntimeException("error expanding irodsNode");
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void highlightPath(final TreePath pathToHighlight) {
		final IRODSTree highlightTree = this;
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				highlightTree.collapsePath(pathToHighlight);
				highlightTree.expandPath(pathToHighlight);
				// highlightTree.sc
				// highlightTree.scrollPathToVisible(pathToHighlight);
			}
		});
	}
	// FIXME: move out?
	/*
	 * public class TransferActionListener implements ActionListener,
	 * PropertyChangeListener { private JComponent focusOwner = null;
	 * 
	 * public TransferActionListener() { KeyboardFocusManager manager =
	 * KeyboardFocusManager. getCurrentKeyboardFocusManager();
	 * manager.addPropertyChangeListener("permanentFocusOwner", this); }
	 * 
	 * public void propertyChange(PropertyChangeEvent e) { Object o =
	 * e.getNewValue(); if (o instanceof JComponent) { focusOwner =
	 * (JComponent)o; } else { focusOwner = null; } }
	 * 
	 * public void actionPerformed(ActionEvent e) { if (focusOwner == null)
	 * return; String action = (String)e.getActionCommand(); Action a =
	 * focusOwner.getActionMap().get(action); if (a != null) {
	 * a.actionPerformed(new ActionEvent(focusOwner,
	 * ActionEvent.ACTION_PERFORMED, null)); } } }
	 */

}
