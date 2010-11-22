@echo off
set CLASSPATH=${project.artifactId}-${project.version}.jar;lib\guava-r06.jar;lib\jaxb-api-2.2.jar;lib\jaxb-impl-2.2.jar;lib\not-yet-commons-ssl-0.3.11.jar;lib\commons-codec-1.4.jar

java mx.bigdata.sat.cfdi.tools.CLI %*
