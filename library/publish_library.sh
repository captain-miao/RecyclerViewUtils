#!/usr/bin/env bash

##############################################################################
##
##  Just execute ../gradlew uploadArchives
##
##############################################################################
RED='\033[0;31m' # red color
NC='\033[0m' # No Color
echo -n "ready go..."

../gradlew uploadArchives

result=$?
if [ ${result} -eq 0 ];then
   echo "uploadArchives successful"
else
   echo  -e "${RED} uploadArchives failure ${NC}"
fi