package codesearch.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;

public class CodeSearchAction implements IViewActionDelegate, IEditorActionDelegate {

	private IEditorPart currentEditor;
	private String selectionFromEditor;

	public void init(IViewPart view) {}

	public void run(IAction action) {
		try {
			String id = action.getId();
			CodeSearchView view = CodeSearchView.getInstance();
			
			if ("org.adsf.codesearch.buttons.next".equals(id)) {
				view.next();
			} else if ("org.adsf.codesearch.buttons.previous".equals(id)) {
				view.previous();
			} else if ("org.asdf.codesearch.actions.search".equals(id)) {
				if (selectionFromEditor != null) view.search(selectionFromEditor);
			}
		} catch (PartInitException e) { e.printStackTrace(); }
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof TextSelection) // some text has been selected in the editor
			selectionFromEditor = ((TextSelection) selection).getText();
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.currentEditor = targetEditor;
	}
}