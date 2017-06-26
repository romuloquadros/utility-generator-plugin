package fixturegeneratorplugin.handlers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

public class MapperTestGeneratorHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		try {
			IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
			String methodName = preferenceStore.getString(Preferences.METHOD_NAME);
			String classNameSuffix = preferenceStore.getString(Preferences.CLASS_NAME_SUFFIX);

			GeneratorClassLoader loader = GeneratorClassLoaderFactory.create(window.getActivePage());
			FixtureConfiguration config = new FixtureConfiguration();
			config.setClassNameSuffix(classNameSuffix);
			config.setMethodName(methodName);
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

			MessageDialog.openInformation(window.getShell(), "Success", "Mapper Test generated.");
		} catch (Exception e) {
			MessageDialog.openInformation(window.getShell(), "Error", "Error while generating test. " + e.getMessage());
		}

		return null;
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

}
