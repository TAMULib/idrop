package org.irods.jargon.idrop.desktop.systraygui.viscomponents;

import java.text.DateFormat;

import org.irods.jargon.core.query.CollectionAndDataObjectListingEntry;
import org.netbeans.swing.outline.RenderDataProvider;

/**
 * @author Mike Conway - DICE (www.irods.org)
 */
public class OutlineRenderProvider implements RenderDataProvider {
	private final IRODSTree tree;
	private final DateFormat dateFormat = DateFormat.getDateTimeInstance();

	public OutlineRenderProvider(final IRODSTree tree) {
		this.tree = tree;
	}

	@Override
	public java.awt.Color getBackground(final Object o) {
		return null;
	}

	@Override
	public String getDisplayName(final Object o) {
		return o.toString();
	}

	@Override
	public java.awt.Color getForeground(final Object o) {
		return null;
	}

	@Override
	public javax.swing.Icon getIcon(final Object o) {
		return null;
	}

	@Override
	public String getTooltipText(final Object o) {
		IRODSNode node = (IRODSNode) o;
		CollectionAndDataObjectListingEntry entry = (CollectionAndDataObjectListingEntry) node
				.getUserObject();
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<h3>");
		sb.append(entry.getFormattedAbsolutePath());
		sb.append("</h3>");
		sb.append("<b>size:</b>");
		sb.append(entry.getDisplayDataSize());
		sb.append("<br/><b>last mod:</b>");
		if (entry.getModifiedAt() != null) {
			sb.append(dateFormat.format(entry.getModifiedAt()));
		}
		sb.append("</html>");
		return sb.toString();
	}

	@Override
	public boolean isHtmlDisplayName(final Object o) {
		return false;
	}
}