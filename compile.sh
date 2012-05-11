#!/bin/sh
rm -f Deduplicate.class
javac Deduplicate.java Levenshtein.java
java -classpath .:sqlite-jdbc-3.7.2.jar Deduplicate archive.db
