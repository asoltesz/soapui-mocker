package org.soapui_mocker;

import com.eviware.soapui.impl.rest.mock.RestMockService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockRunner;
import com.eviware.soapui.impl.wsdl.mock.WsdlMockService;
import com.eviware.soapui.model.mock.MockRunner;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soapui_mocker.config.AppConfiguration;
import org.soapui_mocker.config.ProjectSet;
import org.soapui_mocker.model.LoadedProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class RunnerApplication {

	private Logger log = LoggerFactory.getLogger(RunnerApplication.class);

	private int loadedProjectSetCnt = 0;
	private int loadedProjectCnt = 0;
	private int failedProjectCnt = 0;
	private int startedServicesCnt = 0;
	private int startedRestServicesCnt = 0;
	private int startedSoapServicesCnt = 0;

	private List<LoadedProject> loadedProjects = new ArrayList<>();

	private FileSystemWatcher watcher;

	@Autowired
	AppConfiguration appConfig;

	//--------------------------------------------------------------------------

	public static void main(String[] args) {

		SpringApplication.run(RunnerApplication.class, args);
	}

	/**
	 * Runs when the application starts.
	 *
	 * Loads all of the SoapUI projects defined in the configuration.
	 */
	@Bean
	public CommandLineRunner startup(ApplicationContext ctx) {
		return args ->
		{
			loadProjectSets();

			activateWatch();

			log.info("--------------------------------------------------");
			log.info("Successfully loaded projects that were configured");
			log.info("--------------------------------------------------");
			log.info("Project sets: " + loadedProjectSetCnt);
			log.info("Loaded projects: " + loadedProjectCnt);
			log.info("Failed projects: " + failedProjectCnt);
			log.info("Started REST services: " + startedRestServicesCnt);
			log.info("Started SOAP services: " + startedSoapServicesCnt);
			log.info("All started services: " + startedServicesCnt);
			log.info("--------------------------------------------------");

			if (startedServicesCnt == 0)
			{
				log.warn("No mock services in the SoapUI projects were loaded. Execution will end.");
			}
		};
	}

	/**
	 * Loads the configured projectSets and starts all mock services.
	 */
	private void loadProjectSets()
	{
		log.debug("Loading project sets");

		for (ProjectSet projectSet : appConfig.getProjectSets()) {

			loadProjectSet(projectSet);

			loadedProjectSetCnt++;
		}

		log.debug("Finished loading project sets");
	}

	private void loadProjectSet(ProjectSet projectSet)
	{
		log.debug("Loading project set: " + projectSet.getName());
		log.debug("Root folder: " + projectSet.getRootFolder());

		List<String> excludedProjects = projectSet.getExcludedProjectNames();
		log.debug("Excluded projects: " + projectSet.getName());

		String[] extensions = projectSet.getProjectFileExtensions();
		log.debug("Extensions: " + extensions);

		File rootDir = new File(projectSet.getRootFolder());
		String postFix = projectSet.getProjectFileNamePostfix();

		Collection<File> files = FileUtils.listFiles(
				rootDir, extensions, projectSet.isRecursive() );

		log.debug("Found " + files.size() + " in root folder.");

		files = files.stream()
			.filter(file ->
			{
				String name = file.getName();

				// File name is in the excluded project file name list
				if (excludedProjects.contains(name))
				{
					return false;
				}

				String nameWithoutExt = name;
				int extPos = name.indexOf(".");
				if (extPos != 0)
				{
					nameWithoutExt = name.substring(0, extPos);
				}

				// No postfix defined or filename ends with the postfix
				if (postFix == null && postFix.equals("")
					|| nameWithoutExt.endsWith(postFix))
				{
					return true;
				}
				// Postfix defined and filename conflicts with it
				else
				{
					return false;
				}
			})
			.collect(Collectors.toList());

		log.debug("Iterating project files.");

		for (File file : files)
		{
			WsdlProject project = loadProject(file.getPath(), projectSet, false);

			if (project != null)
			{
				loadedProjectCnt++;
				loadedProjects.add(new LoadedProject(file, project, projectSet));
			}
			else
			{
				failedProjectCnt++;
			}

		}

		log.debug("Loaded project set: " + projectSet.getName());
	}

	private WsdlProject loadProject(
		String projectFilePath,
		ProjectSet projectSet,
		boolean reload
	) {

		log.debug("Loading project: " + projectFilePath);

		List<String> excludedServices = projectSet.getExcludedMockServiceNames();
		log.debug("Excluded services: " + excludedServices);

		try
		{
			WsdlProject project = new WsdlProject( projectFilePath );

			// Starting SOAP service mocks

			List<WsdlMockService> mockServices = project.getMockServiceList()
				.stream().filter(service ->
					!excludedServices.contains(service.getName())
				)
				.collect(Collectors.toList());

			startWsdlMocks(mockServices);

			// Starting REST service mocks

			List<RestMockService> restMockServices = project.getRestMockServiceList()
				.stream().filter(service ->
					!excludedServices.contains(service.getName())
				)
				.collect(Collectors.toList());

			startRestMocks(restMockServices);

			log.debug("Loaded project: " + projectFilePath);

			return project;
		}
		catch (Exception e)
		{
			log.warn("Loading the project has failed.", e);

			return null;
		}

	}

	private void unloadProject(WsdlProject project)
	{
		log.debug("Unloading project: " + project.getName());

		for (RestMockService service : project.getRestMockServiceList())
		{
			MockRunner runner = service.getMockRunner();
			if (runner != null && runner.isRunning())
			{
				log.debug("Stopping service: " + service.getName());
				runner.stop();
			}
		}

		for (WsdlMockService service : project.getMockServiceList())
		{
			MockRunner runner = service.getMockRunner();
			if (runner != null && runner.isRunning())
			{
				log.debug("Stopping service: " + service.getName());
				runner.stop();
			}
		}
		log.debug("Project unloaded.");
	}

	private void startRestMocks(List<RestMockService> restMockServices) throws Exception
	{
		for (RestMockService mockService : restMockServices)
		{
			log.debug("REST Mockservice: " + mockService.getName());

			WsdlMockRunner mockRunner = mockService.start();
			mockRunner.start();

			startedServicesCnt++;
			startedRestServicesCnt++;

			log.debug("Mockrunner for REST service started: " + mockService.getName());
		}
	}

	private void startWsdlMocks(List<WsdlMockService> mockServices) throws Exception
	{
		for (WsdlMockService mockService : mockServices)
		{
			log.debug("SOAP Mockservice: " + mockService.getName());

			WsdlMockRunner mockRunner = mockService.start();
			mockRunner.start();

			startedServicesCnt++;
			startedSoapServicesCnt++;

			log.debug("Mockrunner for SOAP service started: " + mockService.getName());
		}
	}


	private void activateWatch()
	{
		Duration pollInterval = Duration.ofMillis(appConfig.getPollInterval());
		Duration quietPeriod = Duration.ofMillis(appConfig.getQuietPeriod());

		watcher = new FileSystemWatcher(true, pollInterval, quietPeriod);

		log.debug("Activating file watchers on project set root folders");

		for (ProjectSet projectSet : appConfig.getProjectSets()) {

			watchProjectSet(projectSet);
		}

		watcher.addListener(new FileChangeListener()
		{
			@Override
			public void onChange(Set<ChangedFiles> changeSet) {
				onFileChanges(changeSet);
			}
		});

		watcher.start();

		log.debug("Finished activating watchers on project sets");
	}

	/**
	 * Start watching a folder for possible SoapUI project changes
	 */
	private void watchProjectSet(ProjectSet projectSet)
	{
		File folder = new File(projectSet.getRootFolder());

		watcher.addSourceFolder(folder);
	}

	/**
	 * Fires when a project file has changed on the disk.
	 */
	private void onFileChanges(Set<ChangedFiles> changeSet)
	{
		for (ChangedFiles changedFiles : changeSet)
		{
			for (ChangedFile changedFile : changedFiles.getFiles())
			{
				if (changedFile.getType() == ChangedFile.Type.MODIFY)
				{
					// we only react to changes, not to deletions or new projects
					onFileModified(changedFile.getFile());
				}

			}
		}
	}

	/**
	 * Fires when a file changed in a ProjectSet root folder.
	 *
	 * Quietly ignores non-loaded, non-project files
	 */
	private void onFileModified(File changedFile)
	{
		// Finding the project in the registry
		LoadedProject loadedProject = loadedProjects.stream()
				.filter(lp -> changedFile.equals(lp.getProjectFile()))
				.findFirst()
				.orElse(null);

		if (loadedProject == null)
		{
			return;
		}

		log.info("Project changed, reloading: " + loadedProject.getProjectFile());

		unloadProject(loadedProject.getProject());

		WsdlProject newProject =
			loadProject(
				loadedProject.getProjectFile().getPath(),
				loadedProject.getProjectSet(),
				true
			);

		loadedProject.setProject(newProject);

		log.info("Changed project has been reloaded.");
	}
}

