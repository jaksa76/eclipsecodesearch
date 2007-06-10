package codesearch.views;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import codesearch.Activator;

import com.google.gdata.client.codesearch.CodeSearchService;
import com.google.gdata.data.codesearch.CodeSearchEntry;
import com.google.gdata.data.codesearch.CodeSearchFeed;
import com.google.gdata.util.ServiceException;



/**
 * @author Jaksa Vuckovic <jaksa76@gmail.com>
 *
 */
public class CodeSearchView extends ViewPart {
	// TODO add listening to selection in the editor
	// TODO add big browser window
	// TODO add search from context menu in editor
	
	public static final String ID = "codesearch.views.CodeSearchView";
	
	private Text area;

	private Browser browser;

	private int currentSelection;  // the currently loaded page of the feed

	private List<CodeSearchEntry> entries; // the feed entries

	private Text searchString;

	CodeSearchService myService = new CodeSearchService("exampleCo-exampleApp-1");

	public static CodeSearchView getInstance() throws PartInitException {
		IWorkbenchPage activePage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
		return (CodeSearchView) activePage.showView(CodeSearchView.ID);
	}

	
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

	private void createTextArea(Composite parent) {
		area = new Text(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gridData2 = new org.eclipse.swt.layout.GridData();
		gridData2.grabExcessVerticalSpace = true;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.verticalAlignment = GridData.FILL;
		area.setLayoutData(gridData2);
	}


	/**
	 * @param entry
	 */
	private void loadEntry(final CodeSearchEntry entry) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				browser.setUrl(entry.getHtmlLink().getHref());
			}
		});
	}

	public void test() {
		showMessage("testing");
	}
	
	public void search(final String searchString) {
		new Searcher(searchString).start();
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(getSite().getShell(), "Codesearch", message);
	}
    
    
    /**
	 * @author jvu
	 *
	 */
	private final class Searcher extends Thread {
		private final String string;

		private Searcher(String string) {
			this.string = string;
		}

		public void run() {
			try {
                // convert spaces to +
				String[] keywords = this.string.split(" ");
				String fixedSearchString = join(keywords, "+");
				System.out.println(fixedSearchString);
                
				URL feedUrl = new URL("http://www.google.com/codesearch/feeds/search?q=" + fixedSearchString);
				CodeSearchFeed resultFeed = myService.getFeed(feedUrl, CodeSearchFeed.class);
				entries = resultFeed.getEntries();
				System.out.println("Found " + entries.size() + " results.");
				
				if (entries.size() > 0) {
					loadEntry(entries.get(1));
				}
		
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

		private String join(String[] strings, String separator) {
		    if (strings == null) return null;
		    StringBuffer buf = new StringBuffer();
		
		    for (int i = 0; i < strings.length; i++) {
		        if (i > 0) buf.append(separator);
		        if (strings[i] != null) buf.append(strings[i]);
		    }
		    
		    return buf.toString();
		}
	}
	
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
	

}
