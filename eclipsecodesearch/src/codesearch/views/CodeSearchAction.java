/*
* We reserve all rights in this document and in the information contained 
* therein. Reproduction, use or disclosure to third parties without express
* authority is strictly forbidden.
* 
* Copyright(c) ALSTOM (Switzerland) Ltd. 2007
*/


package codesearch.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class CodeSearchAction implements IViewActionDelegate {
        
        private CodeSearchView view;

        public CodeSearchAction() {
//            codeSearchView = view;
        }

        public void init(IViewPart view) {
            this.view = (CodeSearchView) view;
        }

        public void run(IAction action) {
            String id = action.getId();
            if ("org.adsf.codesearch.buttons.next".equals(id)) {
                view.next();
            } else if ("org.adsf.codesearch.buttons.previous".equals(id)) {
                view.previous();
            }
        }

        public void selectionChanged(IAction action, ISelection selection) {
            // this action does not listen for selections
        }

    }