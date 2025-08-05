cd /kafka-connect

#Let's see directory connect-plugins exists or not
if [ -d "./connect-plugins" ]
then
    echo "Directory /connect-plugins exists!"
else
    echo "Directory /connect-plugins doesn't exist. Let's create it!"
    mkdir connect-plugins
fi

mkdir connect-plugins/debezium-connector-postgres

# shellcheck disable=SC2164
cd connect-plugins

if [ -z "$(ls -A debezium-connector-postgres)" ]; then
    echo "Downloading of debezium-connector-postgres:"

    curl https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/2.3.0.Final/debezium-connector-postgres-2.3.0.Final-plugin.tar.gz --output /tmp/debezium-connector-postgres-2.3.0.Final-plugin.tar.gz
    tar -xf /tmp/debezium-connector-postgres-2.3.0.Final-plugin.tar.gz -C .
else
    echo "<---- The debezium-connector-postgres folder exists! Step was skipped! -->"
fi


if [ -z "$(ls -A debezium-connector-postgres)" ]; then
    echo "One or several plugins were not downloaded! Script was aborted!" 
    exit 9999
fi