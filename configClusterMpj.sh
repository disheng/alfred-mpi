#!/bin/bash

HOMEDIR=/home/$@
cd $HOMEDIR

# Updated bash
mv .bashrc .bashrc-old
echo "export MPJ_HOME=$HOMEDIR/mpj-v_038" > .bashrc
echo "export PATH=\$PATH:\$MPJ_HOME/bin" >> .bashrc
cat .bashrc-old >> .bashrc
rm .bashrc-old

# Wget and unzip mpj