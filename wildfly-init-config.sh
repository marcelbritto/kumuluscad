#! /bin/sh

#start wildfly
/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &

#install the driver and the data source
#the challenge is to have the server ready---it may take a while
RC=1
count=0
while [ $RC -ne 0 ] && [ $count -lt 5 ]
do
    sleep 5
    /opt/jboss/wildfly/bin/jboss-cli.sh "connect","deploy --force --url=https://jdbc.postgresql.org/download/postgresql-42.7.3.jar","data-source add --name=MyPostgresDS --driver-name=postgresql-42.7.3.jar  --driver-class=org.postgresql.Driver --jndi-name=java:/MyPostgresDS --connection-url=jdbc:postgresql://database:5432/mydatabase  --user-name=postgres --password=mydbpasskumulus"
    RC=$?
    let count++
done

#keep running forever
tail -f /dev/null