#!/bin/bash
URI_DOWNLOAD=https://github.com/furio/alfred-mpi/blob/files/mpj-v0_38.zip?raw=true 

if [ $# -ne 1 ]; then
  echo "Missing user name as parameter."
  exit 1
fi

HOMEDIR=/home/$@
cd $HOMEDIR

if [ ! -f .bashrc ]; then
  touch .bashrc
fi

# Updated bash
mv .bashrc .bashrc-old
echo "export MPJ_HOME=$HOMEDIR/mpj-v_038" > .bashrc
echo "export PATH=\$PATH:\$MPJ_HOME/bin" >> .bashrc
cat .bashrc-old >> .bashrc
rm .bashrc-old

# Wget and unzip mpj
wget $URI_DOWNLOAD -O mpj-v0_38.zip
unzip mpj-v0_38.zip

exit 0
