# soapui-mocker

Mocker is a configurable headless application that runs SoapUI mock services defined in multiple projects.

Spring Boot based Java application that utilizes the official SoapUI JAR libraries to open and start mock services (REST + SOAP) defined in multiple SoapUI projects.

Externalized configuration so that settings (project folders...etc) can be 

## Features:


## Features (planned):

Accept multiple folders as sources for SoapUI project definitions (.xml)
- By default, all of the projects are loaded in the folder and all REST/SOAP mock services are started
- Project-Excludes can be provided so that not all of the SoapUI projects are opened automatically
- Service-Excludes can be proided so that ot all of the services are started in the loaded projects
- Port override for the services
  
Reloading capability
- Watches the file system for project file changes on the disk. If any of them changes, it automatically
  stops, reloads, starts the affected SoapUI projects (after a 2 sec etails so that the SoapUI GUI can 
  finish saving the workspace)
- This makes it possible to use the SoapUI editor in parallel even if Mocker runs on a remote host and sees
  the project files only via shared folders

Docker container 
- Container definition files based on the OpenJDK 8 image
- Uploaded to Docker Hub
- documentation for easy usage

