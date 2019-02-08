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
	 * Whether project files should be looked up recursively in the root folders
	 */
	boolean recursive = true;

	/**
	 * Postfix of the project file name before the extension.
	 * Defaults to "soapui-project"
	 */
	String projectFileNamePostfix;

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
	List<String> excludedProjectNames;

	/**
	 * The list of Mock service names (case sensitive) that are considered
	 * as excluded and should not be started.
	 */
	List<String> excludedMockServiceNames;


	public ProjectSet() {

		name = "default";

		projectFileNamePostfix = "soapui-project";

		projectFileExtensions = new String[] {"xml"};

		excludedProjectNames = new ArrayList<>();

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

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public String[] getProjectFileExtensions() {
		return projectFileExtensions;
	}

	public void setProjectFileExtensions(String[] projectFileExtensions) {
		this.projectFileExtensions = projectFileExtensions;
	}

	public String getProjectFileNamePostfix() {
		return projectFileNamePostfix;
	}

	public void setProjectFileNamePostfix(String projectFileNamePostfix) {
		this.projectFileNamePostfix = projectFileNamePostfix;
	}

	public List<String> getExcludedProjectNames() {
		return excludedProjectNames;
	}

	public void setExcludedProjectNames(List<String> excludedProjectNames) {
		this.excludedProjectNames = excludedProjectNames;
	}

	public List<String> getExcludedMockServiceNames() {
		return excludedMockServiceNames;
	}

	public void setExcludedMockServiceNames(List<String> excludedMockServiceNames) {
		this.excludedMockServiceNames = excludedMockServiceNames;
	}
}
