#!/bin/bash
#@Author Hafner Peter
#Date: 2023-11-10

bin=target/classes
testDir="test"
app="dev.hafnerp.Main"

########################################################################################################################
# Run the program...
########################################################################################################################


if [ $1 == "--test" ]; then
  origin_test_directory=${testDir}/origin

  cd $origin_test_directory || exit

  for fileName in *; do
    out=$(java -classpath "../../$bin" "$app" "$fileName")
    if [ $? == 0 ]; then echo -e "\e[1;32m ###################### \e[0m - $fileName - Test successful! \n$out"
    elif [ $? == 1 ]; then echo -e "\e[1;31m ###################### \e[0m - $fileName - Test failed! \n$out"
    fi
  done

elif [ -z $1 ]; then
  java -classpath $bin $app --word a

  if [ $? == 0 ]; then
    echo -e "\e[1;32m ***OK*** \e[0m app executed successfully!"
  else
    echo -e "\e[1;31m ***ERR*** \e[0m app executed NOT successfully!"
  fi

else
  echo "*********************************"
  echo "Help Command --help | -h"
  echo "Test Command --test"
  echo "*********************************"

fi