package utilitygeneratorplugin.classloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.JavaRuntime;

public class GeneratorClassLoader {

	private ICompilationUnit compilationUnit;

	public GeneratorClassLoader(ICompilationUnit iCompilationUnit) {
		this.compilationUnit = iCompilationUnit;
	}

	public String getAbsolutePath() {
		IResource resource = getCompilationUnitResource();
		return resource.getProject().getLocation().toFile().getAbsolutePath() + File.separator;
	}

	public Class<?> loadSelectedClass() throws Exception {
		return loadClass(compilationUnit.findPrimaryType().getFullyQualifiedName());
	}

	public Class<?> loadClass(String name) throws Exception {
		return getClassLoader().loadClass(name);
	}

	public void refresh() throws CoreException {
		IResource resource = getCompilationUnitResource();
		IProject project = resource.getProject();
		IResource resourceProject = Platform.getAdapterManager().getAdapter(project, IResource.class);
		resourceProject.refreshLocal(IResource.DEPTH_INFINITE, null);
	}

	private IResource getCompilationUnitResource() {
		return Platform.getAdapterManager().getAdapter(compilationUnit, IResource.class);
	}

	public URLClassLoader getClassLoader() throws Exception {
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
		return new URLClassLoader(urls, parentClassLoader);
	}
}
