package utilitygeneratorplugin.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import utilitygeneratorplugin.Activator;
import utilitygeneratorplugin.Preferences;

public class FixtureConfigPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench arg0) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("General configurations for the fixture process.");
		
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(Preferences.METHOD_PREFIX, "Method Prefix:", getFieldEditorParent()));
		addField(new StringFieldEditor(Preferences.RANDOM_UTIL_CLASS, "Random Util Class", getFieldEditorParent()));
	}

}
