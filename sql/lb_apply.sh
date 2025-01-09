LB_HOME=/Users/petr/liquibase-4.30.0
"$LB_HOME"/liquibase --driver=org.postgresql.Driver \
--classpath=$LB_HOME/lib \
--changeLogFile=databaseChangeLog.sql \
--url="jdbc:postgresql://petrtitov.com:35432/masterjava" \
--username=admin \
--password=Karamba1987 \
migrate