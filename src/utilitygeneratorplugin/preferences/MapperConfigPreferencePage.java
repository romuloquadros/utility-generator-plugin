package utilitygeneratorplugin.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import utilitygeneratorplugin.Activator;
import utilitygeneratorplugin.Preferences;

public class MapperConfigPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench arg0) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("General configurations for the mapper test process.");
		
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(Preferences.METHOD_NAME, "Method name:", getFieldEditorParent()));
		addField(new StringFieldEditor(Preferences.CLASS_NAME_SUFFIX, "Class name suffix:", getFieldEditorParent()));
	}

	
	
}
