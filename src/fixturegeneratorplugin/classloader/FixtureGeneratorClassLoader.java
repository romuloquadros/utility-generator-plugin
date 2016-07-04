package fixturegeneratorplugin.classloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.handlers.HandlerUtil;

public class FixtureGeneratorClassLoader {

	private ICompilationUnit compilationUnit;

	public FixtureGeneratorClassLoader(ExecutionEvent event) {
		this.compilationUnit = (ICompilationUnit) HandlerUtil.getCurrentStructuredSelection(event).getFirstElement();
	}

	public String getAbsolutePath() {
		IResource resource = Platform.getAdapterManager().getAdapter(compilationUnit, IResource.class);
		return resource.getProject().getLocation().toFile().getAbsolutePath() + File.separator;
	}

	public Class<?> loadSelectedClass() throws Exception {
		IJavaProject javaProject = compilationUnit.getJavaProject();
		String[] classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
		List<URL> urlList = new ArrayList<URL>();
		for (int i = 0; i < classPathEntries.length; i++) {
			String entry = classPathEntries[i];
			IPath path = new Path(entry);
			URL url = url = path.toFile().toURI().toURL();
			urlList.add(url);
		}

		ClassLoader parentClassLoader = javaProject.getClass().getClassLoader();
		URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
		URLClassLoader classLoader = new URLClassLoader(urls, parentClassLoader);
		return classLoader.loadClass(compilationUnit.findPrimaryType().getFullyQualifiedName());
	}

}
