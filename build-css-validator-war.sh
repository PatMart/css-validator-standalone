#!/bin/sh

## this script probably runs only on a Linux-based machine
## you need CVS, Java and ant to be already installed

rm -Rf 2002

# check out the source
export CVSROOT=":pserver:anonymous@dev.w3.org:/sources/public"
echo
echo "IMPORTANT: enter anonymous as the password for cvs"
echo
cvs login
cvs get 2002/css-validator

# build the WAR file
cd 2002/css-validator
and
ant war

# stages the WAR so that it's part of the SBT project
cp 2002/css-validator/css-validator.war /src/main/resources