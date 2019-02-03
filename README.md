# soapui-mocker
Configurable headless application that runs SoapUI mock services defined in multiple projects

Java application that utilizes the official SoapUI JAR libraries to open and start mock services (REST + SOAP) defined in multiple SoapUI projects.

Features:
- 

Features (planned):

- Accept multiple folders as sources for SoapUI project definitions (.xml)
  -- By default, all of the projects are loaded in the folder and all REST/SOAP mock services are started
  -- Project-Excludes can be provided so that not all of the SoapUI projects are opened automatically
  -- Service-Excludes can be proided so that ot all of the services are started in the loaded projects
  -- Port override for the services
- Docker container definition on OpenJDK 8 and uploaded to Docker Hub + documentation for easy usage

