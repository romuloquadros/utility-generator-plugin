package fixturegeneratorplugin.popup.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.fixture.generator.builder.ClassGenerator;
import com.fxture.generator.configuration.FixtureConfiguration;

import fixturegeneratorplugin.classloader.GeneratorClassLoader;
import fixturegeneratorplugin.factory.GeneratorClassLoaderFactory;

public class GenerateFixtureAction implements IObjectActionDelegate {

	private Shell shell;
	private IWorkbenchPage activePage;

	/**
	 * Constructor for Action1.
	 */
	public GenerateFixtureAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
		activePage = targetPart.getSite().getWorkbenchWindow().getActivePage();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		try {
			GeneratorClassLoader loader = GeneratorClassLoaderFactory.create(activePage);
			FixtureConfiguration config = new FixtureConfiguration();
			config.setMethodPrefix("com");

			config.setRootPath(loader.getAbsolutePath() + config.getRootPath());

			ClassGenerator classGenerator = new ClassGenerator(config);

			classGenerator.generateFixture(loader.loadSelectedClass());

			loader.refresh();

			MessageDialog.openInformation(shell, "Success", "Fixture generated.");
		} catch (Exception e) {
			MessageDialog.openInformation(shell, "Error", "Error while generating fixture. " + e.getMessage());
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
