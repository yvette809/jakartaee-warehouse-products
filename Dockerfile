# Use the bitnami/wildfly image as the base image
FROM bitnami/wildfly:29.0.1
#ENV WILDFLY_USERNAME=user, WILDFLY_PASSWORD=password

# Copy your Jakarta EE application (WAR) to the WildFly deployments directory
#COPY target/warehouse-1.0-SNAPSHOT.war /opt/bitnami/wildfly/standalone/deployments/
COPY target/warehouse-1.0-SNAPSHOT.war /app

# Expose the ports (if needed)
EXPOSE 8080

# Start WildFly
CMD ["/opt/bitnami/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
