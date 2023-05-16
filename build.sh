#!/bin/bash.
clear;

# compilation framework
cd framework/build;
javac -parameters -d . ../src/*.java;

# jar
jar -cf fw-10.jar .;

# copie vers lib
cp fw-10.jar  ../../test-framework/WEB-INF/lib/;

# war
cd /home/ralph/ralph/apache-tomcat-10.0.27/webapps/framework3/test-framework/src;

# compilation de mon projet
javac -parameters -d ../WEB-INF/classes *.java;
cd ../../test-framework;

# war
jar -cf framework6.war .;

# deployement vers tomcat
cp  framework6.war /home/ralph/ralph/apache-tomcat-10.0.27/webapps;
