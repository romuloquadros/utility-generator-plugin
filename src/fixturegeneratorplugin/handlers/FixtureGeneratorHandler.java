package fixturegeneratorplugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.fixture.generator.builder.ClassGenerator;
import com.fxture.generator.configuration.FixtureConfiguration;

import fixturegeneratorplugin.Activator;
import fixturegeneratorplugin.Preferences;
import fixturegeneratorplugin.classloader.GeneratorClassLoader;
import fixturegeneratorplugin.factory.GeneratorClassLoaderFactory;

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
			IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
			String methodPrefix = preferenceStore.getString(Preferences.METHOD_PREFIX);

			GeneratorClassLoader loader = GeneratorClassLoaderFactory.create(window.getActivePage());
			FixtureConfiguration config = new FixtureConfiguration();
			config.setMethodPrefix(methodPrefix);

			config.setRootPath(loader.getAbsolutePath() + config.getRootPath());

			ClassGenerator classGenerator = new ClassGenerator(config);

			classGenerator.generateFixture(loader.loadSelectedClass());

			loader.refresh();

			MessageDialog.openInformation(window.getShell(), "Success", "Fixture generated.");
		} catch (Exception e) {
			MessageDialog.openInformation(window.getShell(), "Error",
					"Error while generating fixture. " + e.getMessage());
		}
		return null;
	}
}
