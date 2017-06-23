package fixturegeneratorplugin.preferences.initializers;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import fixturegeneratorplugin.Activator;
import fixturegeneratorplugin.Preferences;

public class MapperConfigPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(Preferences.CLASS_NAME_SUFFIX, "Mapper");
		store.setDefault(Preferences.METHOD_NAME, "shouldTestAllParams");
	}

}
