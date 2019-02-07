package org.soapui_mocker.config;

import java.util.ArrayList;
import java.util.List;

/**
 * A SoapUI project set with a root folder and potentially excludes.
 */
public class ProjectSet {

	/**
	 * Name of the project set.
	 *
	 * Not mandatory, serves only logging and debugging purposes.
	 *
	 * Defaults to "default".
	 */
	String name;

	/**
	 * The root folder in which the SoapUI project files are placed for
	 * this set.
	 */
	String rootFolder;

	/**
	 * The file extensions that are considered as SoapUI project files.
	 *
	 * Optional, defaults to ["xml"]
	 */
	String[] projectFileExtensions;

	/**
	 * The list of project files (case sensitive) that are considered
	 * as excluded and should not be loaded.
	 */
	List<String> excludedProjectFiles;

	/**
	 * The list of Mock service names (case sensitive) that are considered
	 * as excluded and should not be started.
	 */
	List<String> excludedMockServiceNames;


	public ProjectSet() {

		name = "default";

		projectFileExtensions = new String[] {"xml"};

		excludedProjectFiles = new ArrayList<>();

		excludedMockServiceNames = new ArrayList<>();

	}


	//--------------------------------------------------------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	public String[] getProjectFileExtensions() {
		return projectFileExtensions;
	}

	public void setProjectFileExtensions(String[] projectFileExtensions) {
		this.projectFileExtensions = projectFileExtensions;
	}

	public List<String> getExcludedProjectFiles() {
		return excludedProjectFiles;
	}

	public void setExcludedProjectFiles(List<String> excludedProjectFiles) {
		this.excludedProjectFiles = excludedProjectFiles;
	}

	public List<String> getExcludedMockServiceNames() {
		return excludedMockServiceNames;
	}

	public void setExcludedMockServiceNames(List<String> excludedMockServiceNames) {
		this.excludedMockServiceNames = excludedMockServiceNames;
	}
}
