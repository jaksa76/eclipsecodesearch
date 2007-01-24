package codesearch.views;

import com.google.gdata.client.codesearch.CodeSearchService;
import com.google.gdata.data.codesearch.CodeSearchEntry;
import com.google.gdata.data.codesearch.CodeSearchFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */


// TODO add listening to selection in the editor
// TODO add navigation for results
public class CodeSearchView extends ViewPart {
	CodeSearchService myService = new CodeSearchService("exampleCo-exampleApp-1");
	
	/**
	 * @author jvu
	 *
	 */
	private final class SearchListener extends org.eclipse.swt.events.KeyAdapter {
		public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
			// Handle the press of the Enter key in the locationText.
			// This will browse to the entered text.
			if (e.character == SWT.LF || e.character == SWT.CR) {
				e.doit = false;
				search(searchString.getText());
			}
		}
	}

	private Text searchString;

	private Browser browser;

	private Text area;

	private List<CodeSearchEntry> entries;
	
	private int currentSelection;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 * {@inheritDoc}
	 */
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);
		
		createSearchString(parent);
		createBrowser(parent);
//		createTextArea(parent);
	}

	/**
	 * @param parent
	 */
	private void createSearchString(Composite parent) {
		searchString = new Text(parent, SWT.NONE);
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = GridData.FILL;
		searchString.setLayoutData(gridData1);
		searchString.addKeyListener(new SearchListener());
	}

	/**
	 * @param parent
	 */
	private void createBrowser(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.grabExcessVerticalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.verticalAlignment = GridData.FILL;
		browser.setLayoutData(gridData2);
	}
	
	private void createTextArea(Composite parent) {
		area = new Text(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.grabExcessVerticalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.verticalAlignment = GridData.FILL;
		area.setLayoutData(gridData2);
	}

	private void search(String searchString) {
		try {
			URL feedUrl = new URL("http://www.google.com/codesearch/feeds/search?q=" + searchString);
			
			CodeSearchFeed resultFeed = myService.getFeed(feedUrl, CodeSearchFeed.class);

			entries = resultFeed.getEntries();
			if (entries.size() > 0) {
				loadEntry(entries.get(1));
			}
			
		} catch (MalformedURLException e) {
			showMessage(e.getMessage());
		} catch (IOException e) {
			showMessage(e.getMessage());
			e.printStackTrace();
		} catch (ServiceException e) {
			showMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	
	/**
	 * @param entry
	 */
	private void loadEntry(CodeSearchEntry entry) {
		browser.setUrl(entry.getHtmlLink().getHref());
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(null, null, message);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		searchString.setFocus();
	}

	/**
	 * 
	 */
	public void next() {
		if (currentSelection < entries.size() - 1) {
			loadEntry(entries.get(currentSelection++));
		}
	}

	/**
	 * 
	 */
	public void previous() {
		if (currentSelection > 0) {
			loadEntry(entries.get(currentSelection--));
		}
	}
}
