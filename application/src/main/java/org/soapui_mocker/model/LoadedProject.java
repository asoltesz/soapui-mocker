package org.soapui_mocker.model;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import org.soapui_mocker.config.ProjectSet;

import java.io.File;

/**
 * A SoapUI project file loaded from the file system.
 */
public class LoadedProject {

	/** The project definition XML file in the filesystem. */
	File projectFile;

	/**
	 * The in-memory project definiton.
	 */
	WsdlProject project;

	/**
	 * The project set it belongs to
	 */
	ProjectSet projectSet;


	//--------------------------------------------------------------------------

	public LoadedProject(File projectFile, WsdlProject project, ProjectSet set)
	{
		this.projectFile = projectFile;
		this.project = project;
		this.projectSet = set;
	}

	//--------------------------------------------------------------------------

	public File getProjectFile() {
		return projectFile;
	}

	public void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}

	public WsdlProject getProject() {
		return project;
	}

	public void setProject(WsdlProject project) {
		this.project = project;
	}

	public ProjectSet getProjectSet() {
		return projectSet;
	}

	public void setProjectSet(ProjectSet projectSet) {
		this.projectSet = projectSet;
	}
}
