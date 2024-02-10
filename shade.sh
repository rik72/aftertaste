#!/bin/zsh
mvn clean install -Pshade && java -jar target/aftertaste.jar
