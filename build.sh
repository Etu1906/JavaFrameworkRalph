#!/bin/bash.
clear;
build_framework="framework/build";
jar_name="fw-10.jar";
projet_source="/home/ralph/ralph/s4/Mr_Naina/tomcat10/framework3/test-framework/src";
projet_deploye="framework6.war";
path_deploiement="/home/ralph/ralph/apache-tomcat-10.0.27/webapps";
temporary_repository="projet_temp";
test_framework="/home/ralph/ralph/s4/Mr_Naina/tomcat10/framework3/test-framework";

# compilation framework
cd $build_framework;
javac -parameters -d . ../src/*.java;

# jar
jar -cf $jar_name .;

# copie vers lib
cp $jar_name  $test_framework/WEB-INF/lib/;

# war
cd ../..;

mkdir $temporary_repository;

cd $test_framework;

# repertorie temporaire
cp -r . ../$temporary_repository;

cd ../$temporary_repository/src;

# compilation de mon projet
find . -name "*.java" -type f -print > sources.txt;

javac  -d ../WEB-INF/classes @sources.txt;
cd ../../$temporary_repository;

# war
jar -cf $projet_deploye .;

# deployement vers tomcat
cp  $projet_deploye $path_deploiement;

cd ..;

rm -rf $temporary_repository;