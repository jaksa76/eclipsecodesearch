/**
 * 
 */
package codesearch.test;

import codesearch.Activator;
import codesearch.views.CodeSearchView;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:jaksa76@gmail.com">Jaksa Vuckovic</a>
 */
public class PluginTest extends TestCase {
	public void testSearch() throws Exception {
		CodeSearchView codesearchView = (CodeSearchView) Activator
		.getDefault()
		.getWorkbench()
		.getActiveWorkbenchWindow()
		.getActivePage()
		.findView(CodeSearchView.ID);
		
		codesearchView.test();
	}
}
