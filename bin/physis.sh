#!/bin/sh

# the environment variable PHYSIS_HOME must be set!

export CLASSPATH=$PHYSIS_HOME/src:$CLASSPATH

java -Xms96m -Xmx128m physis.core.PHYSIS $PHYSIS_HOME/data/physis.props

#in order to know did it finish
date