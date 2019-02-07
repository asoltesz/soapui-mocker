package org.soapui_mocker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("mocker")
public class AppConfiguration {

	/**
	 * SoapUI project sets to be loaded
	 */
	List<ProjectSet> projectSets = new ArrayList<>();

	/**
	 * The amount of time to wait between checking for changes (millisecs)
	 */

	int pollInterval = 2000;

	/**
	 * The amount of time to wait after a change is detected (in order to allow
	 * SoapUI gui to save all of the changes) (millisecs)
	 */
	int quietPeriod = 1000;

	// -------------------------------------------------------------------------

	public List<ProjectSet> getProjectSets() {
		return projectSets;
	}

	public void setProjectSets(List<ProjectSet> projectSets) {
		this.projectSets = projectSets;
	}

	public int getPollInterval() {
		return pollInterval;
	}

	public void setPollInterval(int pollInterval) {
		this.pollInterval = pollInterval;
	}

	public int getQuietPeriod() {
		return quietPeriod;
	}

	public void setQuietPeriod(int quietPeriod) {
		this.quietPeriod = quietPeriod;
	}
}
