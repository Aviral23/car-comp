# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:17-alpine-jdk

################################################### Install Maven ###################################################
# Below apk command will install latest maven which may not be ideal for production systems (There are pros and cons both to use latest version of any artifacts, and you may decide based on your use case)
#RUN apk add --no-cache maven

ENV MAVEN_VERSION 3.8.8
# Install required dependencies to install specific maven version
RUN apk add --no-cache curl tar

# Download and install Maven
RUN curl -O https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven

# Set environment variables for Maven
ENV MAVEN_HOME /opt/maven
ENV PATH $MAVEN_HOME/bin:$PATH

#####################################################################################################################

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project (including the pom.xml) to the container for Maven evaluation
COPY . /app

# Get the project artifact ID and version using Maven, and then copy the correct JAR file
RUN PROJECT_ARTIFACT_ID=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout) && \
    PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout) && \
    echo "Artifact ID: $PROJECT_ARTIFACT_ID" && \
    echo "Project Version: $PROJECT_VERSION" && \
    JAR_FILE=target/${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.jar && \
    cp $JAR_FILE /app/my-app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]