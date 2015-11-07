#!/bin/bash

# Compiles unofficial builds of NanoHTTPD and Discord4J because the necessary versions aren't distributed
export JAVA_HOME="`/usr/libexec/java_home -v '1.8*'`" #Because macs be stupid with JAVA_HOME
echo "Checking if maven is installed..."
isMvn=$(which mvn)
if [$isMvn == ""]; then
	echo "Maven not installed! Please install it and run this again"
else
	mkdir "./libs/"
	echo "Attempting to compile NanoHTTPD..."
	cd "./nanohttpd/core/"
	mvn "package" "-Dmaven.test.skip=true"
	find ./target/ -name '*.jar' -exec mv {} ../../libs/ \;
	cd "../../"
	echo "Attempting to compile Discord4J..."
	cd "./Discord4J/"
	mvn "package"
	find ./target/ -name '*.jar' -exec mv {} ../libs/ \;
	cd "../"
	ls ./libs/ > ./libs/build-list.txt
fi
