#!/bin/bash

#@Author - Hafner Peter
#Date: 2023/11/22


mvn compile

if [ $? == 0 ]; then
  echo -e "\e[1;32mSUCCESS\e[0m - The build was successful."
else
  echo -e "\e[1;31mFAILED\e[0m - The build was not successful"
fi