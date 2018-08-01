## Pre-requesite

Download Zookeeper and Kafka.

## zookeeper

1. Go to your Kafka config directory. For me its C:\kafka_2.11-0.9.0.0\config

2. Edit file “server.properties”

3. Find & edit line “log.dirs=/tmp/kafka-logs” to “log.dir= C:\kafka_2.11-0.9.0.0\kafka-logs”.

4. If your Zookeeper is running on some other machine or cluster you can edit “zookeeper.connect:2181” to your custom IP and port. For this demo we are using same machine so no need to change. Also Kafka port & broker.id are configurable in this file. Leave other settings as it is.

5. Your Kafka will run on default port 9092 & connect to zookeeper’s default port which is 2181.

## kafka

1. Go to your Kafka installation directory C:\kafka_2.11-0.9.0.0\

2. Open a command prompt here by pressing Shift + right click and choose“Open command window here” option)

3. Now type .\bin\windows\kafka-server-start.bat .\config\server.properties and press Enter.

4. Goto your Zookeeper config directory. For me its C:\zookeeper-3.4.7\conf

5. Rename file “zoo_sample.cfg” to “zoo.cfg”

6. Open zoo.cfg in any text editor like notepad but I prefer notepad++.

7. Find & edit dataDir=/tmp/zookeeper to :\zookeeper-3.4.7\data

8. Add entry in System Environment Variables as we did for Java

    a. Add in System Variables ZOOKEEPER_HOME = C:\zookeeper-3.4.7

    b. Edit System Variable named “Path” add ;%ZOOKEEPER_HOME%\bin;

9. You can change the default Zookeeper port in zoo.cfg file (Default port 2181).

10. Run Zookeeper by opening a new cmd and type zkserver.


## topic creation

.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic fast-messages

## list topics

.\bin\windows\kafka-topics.bat --list --zookeeper localhost:2181

## start kafka

.\bin\windows\kafka-server-start.bat .\config\server.properties
