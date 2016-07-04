package fixturegeneratorplugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.fixture.generator.builder.ClassGenerator;
import com.fxture.generator.configuration.FixtureConfiguration;

import fixturegeneratorplugin.classloader.FixtureGeneratorClassLoader;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class FixtureGeneratorHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public FixtureGeneratorHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		try {
			FixtureGeneratorClassLoader loader = new FixtureGeneratorClassLoader(event);
			FixtureConfiguration config = new FixtureConfiguration();
			config.setMethodPrefix("com");
			
			config.setRootPath(loader.getAbsolutePath() + config.getRootPath());

			ClassGenerator classGenerator = new ClassGenerator(config);

			classGenerator.generate(loader.loadSelectedClass());
			MessageDialog.openInformation(window.getShell(), "Success", "Fixture generated.");
		} catch (Exception e) {
			MessageDialog.openInformation(window.getShell(), "Error", "Error while generating fixture. " + e.getMessage());
		}

		return null;
	}
}
