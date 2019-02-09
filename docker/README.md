# Soapui-Mocker Docker Image

Mocker is a configurable headless application that runs mock services defined in multiple SoapUI project files. It is capable of monitoring and reloading changed project files so it can be used for iterative mock service development.

## Configuration

Configure the container with the following:

### SoapUI project files

Bind mount your local folders that contain SoapUI project files that include mock services which need to be run inside the container.

For the sake of simplicity, you may bind-mount to the /soapui-mocker/project-sets/set-X folders within the containers where X is 1-10. This is not mandatory, the project folders may be mounted anywhere in the container as long as the application.properties file correctly references them.


### The application.properties file

Bind mount an application.properties file that defines the project folders from which Mocker will look up the SoapUI project files.

NOTE: This is a Java property file, exotic characters need to be encoded. 

### Port mapping

Your SoapUI mock services contain port numbers on which the mock service needs to be available. If you need to access these services from the host, map these ports to host ports.

### Sample Docker Compose file

    version: '3.3'

    services:
      backend-soapui-mocks:
        image: soltesza/soapui-mocker:latest
        volumes:
        - "./application.properties:/soapui-mocker/application.properties"
        - "/home/user1/soapui-projects:/soapui-mocker/project-sets/set-1"
        ports:
        - "8080:8080"


### Sample application.properties file

    #
    # Project sets to be loaded
    #
    
    mocker.projectSets[0].name=My Awesome Backend Mocks
    mocker.projectSets[0].rootFolder=/soapui-mocker/project-sets/set-1
    
    # Look for SoapUI project files in the root folder recursively so that
    # subfolders are also scanned or not.
    # defaults to "true"
    # mocker.projectSets[0].recursive=
    
    # The project file names that are excluded (case-sensitive)
    # mocker.projectSets[0].excludedProjectNames[0]=
    
    # The mock service names that are excluded (case-sensitive)
    # mocker.projectSets[0].excludedMockServiceNames[0]=

Each projectSet is a root folder that contains one or more SoapUI project files.

[More details about the configuration.](https://github.com/asoltesz/soapui-mocker/blob/master/application/src/main/resources/application.properties)
    
### More detailed logs

In the application.properties file:

    logging.level.org.soapui_mocker=DEBUG

### Reloading parameters

Mocker constantly monitors changes on the SoapUI project files and reloads projects that have changed.

Fine tuning in the application.properties file:

    # The amount of time to wait between checking for changes (millisecs)
    # Optional, defaults to 2 seconds.
    # mocker.pollInterval=2000
    
    # The amount of time to wait after a change is detected (in order to allow
    # SoapUI gui to save all of the changes) (millisecs)
    # Must always be smaller than pollInterval
    # Optional, defaults to 1 second.
    # mocker.quietPeriod=1000

### Sources

The application and the Docker container image definition can be found [In the GitHub repo](https://github.com/asoltesz/soapui-mocker)
