#!/usr/bin/env bash
# Performs all the necessary steps for releasing the bot

echo "Ensuring unofficial builds are up to date..."
sh "./CompileUnofficialBuilds.sh"

echo "Compiling the bot..."
sh "gradlew" "build"
open "./build/libs/"

echo "Generating javadocs..."
echo "NOTE: Ignore any 'BUILD FAILED' errors"
rm -rf "./build/docs/plugindoc/"
rm -rf "./build/docs/javadoc/"
sh "gradlew" "pluginDoc"
open "./build/docs/plugindoc/"
sh "gradlew" "apiDoc"
open "./build/docs/javadoc"
