#!/bin/bash.
clear;

# compilation framework
cd framework/build;
javac -d framework ../src/*.java;

# jar
cd framework;
jar -cf ../fw.jar .;
cd ..;

# copie vers lib
cp fw.jar  ../../test-framework/WEB-INF/lib/;

# war
cd /home/ralph/ralph/apache-tomcat-8.5.82/webapps/framework3/test-framework/src;

# compilation de mon projet
javac -d ../WEB-INF/classes *.java;
cd ../../test-framework;

# war
jar -cf framework4.war .;

# deployement vers tomcat
cp  framework4.war /home/ralph/ralph/apache-tomcat-8.5.82/webapps;
