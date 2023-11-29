#!/bin/bash
#@Author Hafner Peter
#Date: 2023-11-10

bin=target/classes
testDir="test"
app="dev.hafnerp.Main"

########################################################################################################################
# Run the program...
########################################################################################################################


if [ ! -d "./target" ]; then
  ./build.sh
fi

if [ $1 == "--test" ]; then
  java -classpath "$bin" "$app" --first &> /dev/null
  if [ $? == 0 ]; then echo -e "\e[1;32m ###################### \e[0m - \"--first\" - Test successful!"
  elif [ $? == 1 ]; then echo -e "\e[1;31m ###################### \e[0m - \--first\" - Test failed!"
  fi

  java -classpath "$bin" "$app" --first --word "if" &> /dev/null
  if [ $? == 0 ]; then echo -e "\e[1;32m ###################### \e[0m - \"--first --word if\" - Test successful!"
  elif [ $? == 1 ]; then echo -e "\e[1;31m ###################### \e[0m - \"--first --word if\" - Test failed!"
  fi

  java -classpath "$bin" "$app" --first --word &> /dev/null
  if [ $? == 255 ]; then echo -e "\e[1;32m ###################### \e[0m - \"--first --word\" - Test successful!"
  elif [ $? == 1 ]; then echo -e "\e[1;31m ###################### \e[0m - \"--first --word\" - Test failed!"
  fi

  java -classpath "$bin" "$app" --first --directory &> /dev/null
  if [ $? == 255 ]; then echo -e "\e[1;32m ###################### \e[0m - \"-first --directory\" - Test successful!"
  elif [ $? == 1 ]; then echo -e "\e[1;31m ###################### \e[0m - \"-first --directory\" - Test failed!"
  fi

  java -classpath "$bin" "$app" --first --directory ./ &> /dev/null
  if [ $? == 0 ]; then echo -e "\e[1;32m ###################### \e[0m - \"--first --directory ./\" - Test successful!"
  elif [ $? == 1 ]; then echo -e "\e[1;31m ###################### \e[0m - \"--first --directory ./\" - Test failed!"
  fi

elif [ -z $1 ]; then
  java -classpath $bin $app --word "if" --first

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