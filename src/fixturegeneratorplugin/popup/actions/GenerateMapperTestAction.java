package fixturegeneratorplugin.popup.actions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

public class GenerateMapperTestAction implements IObjectActionDelegate {

	private Shell shell;
	private IWorkbenchPage activePage;

	/**
	 * Constructor for Action1.
	 */
	public GenerateMapperTestAction() {
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
			config.setClassNameSuffix("Test");
			config.setMethodName("generated");
			Class<?> loadSelectedClass = loader.loadSelectedClass();

			ParameterizedType superType = loadType(loadSelectedClass);
			Type[] arguments = superType.getActualTypeArguments();
			Class<?> type = loader.loadClass(arguments[0].getTypeName());
			Class<?> entity = loader.loadClass(arguments[1].getTypeName());

			config.setTypeClass(type);
			config.setEntityClass(entity);

			config.setRootPath(loader.getAbsolutePath() + config.getRootPath());

			ClassGenerator classGenerator = new ClassGenerator(config);

			classGenerator.generateMapperTest(loadSelectedClass);

			loader.refresh();

			MessageDialog.openInformation(shell, "Success", "Mapper Test generated.");
		} catch (Exception e) {
			MessageDialog.openInformation(shell, "Error", "Error while generating test. " + e.getMessage());
		}
	}

	private ParameterizedType loadType(Class<?> loadSelectedClass) {
		ParameterizedType type = null;

		try {
			type = (ParameterizedType) loadSelectedClass.getAnnotatedSuperclass().getType();
		} catch (Exception e) {
			type = (ParameterizedType) loadSelectedClass.getAnnotatedInterfaces()[0].getType();
		}

		return type;
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
