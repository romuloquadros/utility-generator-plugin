package utilitygeneratorplugin.factory;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;

import utilitygeneratorplugin.classloader.GeneratorClassLoader;

public class GeneratorClassLoaderFactory {

	public static GeneratorClassLoader create(IWorkbenchPage activePage) {
		ISelection selection = activePage.getSelection();
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		ICompilationUnit compilationUnit = (ICompilationUnit) structuredSelection.getFirstElement();
		return new GeneratorClassLoader(compilationUnit);
	}

}
