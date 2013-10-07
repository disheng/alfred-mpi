#!/bin/bash
URI_DOWNLOAD=https://github.com/furio/alfred-mpi/blob/files/mpj-v0_38.zip?raw=true 

HOMEDIR=/home/$@
cd $HOMEDIR

# Updated bash
mv .bashrc .bashrc-old
echo "export MPJ_HOME=$HOMEDIR/mpj-v_038" > .bashrc
echo "export PATH=\$PATH:\$MPJ_HOME/bin" >> .bashrc
cat .bashrc-old >> .bashrc
rm .bashrc-old

# Wget and unzip mpj
wget $URI_DOWNLOAD -O mpj-v0_38.zip
unzip mpj-v0_38.zip