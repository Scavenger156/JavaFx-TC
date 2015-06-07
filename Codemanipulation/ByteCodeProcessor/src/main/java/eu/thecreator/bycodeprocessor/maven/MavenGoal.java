package eu.thecreator.bycodeprocessor.maven;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;

import eu.thecreator.bycodeprocessor.ByteCodeProcessor;

/**
 * Mavenplugin wird generiert mit dem goal "javassist"
 * 
 * @author andre
 *
 */
@Mojo(name = "javassist", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class MavenGoal extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	/** @component */
	@Parameter
	private BuildContext buildContext;

	public void setBuildContext(BuildContext buildContext) {
		this.buildContext = buildContext;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		final ClassLoader originalContextClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			final List<URL> classPath = new ArrayList<URL>();

			for (final String runtimeResource : project
					.getRuntimeClasspathElements()) {
				classPath.add(resolveUrl(runtimeResource));
			}

			final String inputDirectory = project.getBuild()
					.getOutputDirectory();
			classPath.add(resolveUrl(inputDirectory));

			loadAdditionalClassPath(classPath);

			applyTransformation(inputDirectory, inputDirectory);
		} catch (final Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);

		} finally {
			Thread.currentThread().setContextClassLoader(
					originalContextClassLoader);
		}
	}

	private void loadAdditionalClassPath(final List<URL> classPath) {
		if (classPath.isEmpty()) {
			return;
		}
		final ClassLoader contextClassLoader = Thread.currentThread()
				.getContextClassLoader();

		final URLClassLoader pluginClassLoader = URLClassLoader.newInstance(
				classPath.toArray(new URL[classPath.size()]),
				contextClassLoader);

		Thread.currentThread().setContextClassLoader(pluginClassLoader);
	}

	private URL resolveUrl(final String resource) {
		try {
			return new File(resource).toURI().toURL();
		} catch (final MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private void applyTransformation(String inputDirectory,
			String outputDirectory) throws NotFoundException, IOException {
		final ClassPool classPool = new ClassPool(ClassPool.getDefault());
		classPool.childFirstLookup = true;
		classPool.appendClassPath(inputDirectory);
		classPool.appendClassPath(new LoaderClassPath(Thread.currentThread()
				.getContextClassLoader()));
		classPool.appendSystemPath();

		ByteCodeProcessor processor = new ByteCodeProcessor();

		// Files.
		final Path path = Paths.get(inputDirectory);
		Files.walkFileTree(path, new FileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) throws IOException {

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				if (file.getFileName().toString().endsWith(".class")) {
					Path relativPath = path.relativize(file);
					String name = relativPath.toString();
					name = name.substring(0, name.lastIndexOf('.'));
					name = name.replaceAll(File.separator.equals("\\") ? "\\"
							+ File.separator : File.separator, ".");
					try {
						classPool.importPackage(name);
						final CtClass candidateClass = classPool.get(name);

						candidateClass.subtypeOf(ClassPool.getDefault().get(
								Object.class.getName()));

						if (processor.apply(candidateClass)) {
							candidateClass.writeFile(outputDirectory);

							// buildContext.addMessage(file.toFile(), 0, 0,
							// "muhaha", BuildContext.SEVERITY_ERROR, null);
							// buildContext.refresh(file.toFile());

						}
					} catch (NotFoundException e) {
						throw new RuntimeException(e);
					} catch (CannotCompileException e) {
						throw new RuntimeException(e);
					}
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
