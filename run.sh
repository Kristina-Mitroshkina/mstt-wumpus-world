#!/bin/bash

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-host localhost -agents world:jade.wumpus.agents.WumpusWorldAgent;navigator:jade.wumpus.agents.NavigatorAgent;speleologist:jade.wumpus.agents.SpeleologistAgent"
