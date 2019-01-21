# formonline
a light and powerfull form engine

## Requirements
java7 / tomcat / mysql

## Installation
- Download (or clone) the code
- Build the project (`mvn install`)
- Create another project using Maven Overlay like this POM.xml :

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>fr.cridf</groupId>
	<artifactId>prodsi</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<packaging>war</packaging>

	<name>prodsi</name>
	
        <repositories>
            		<repository>
			<id>plugins</id>
			<name>releases</name>
			<url>http://dev.lutece.paris.fr/nexus/content/repositories/central/</url>
		</repository>
                <repository>
			<id>alfrescopublic</id>
			<name>releasesAlfres</name>
			<url>https://artifacts.alfresco.com/nexus/content/repositories/public/</url>
		</repository>
        </repositories>
        
        
	<dependencies>
		<dependency>
			<groupId>fr.cridf</groupId>
			<artifactId>formonline</artifactId>
			<version>2.0.31</version>
			<type>war</type>  
		</dependency>

		<dependency>
			<groupId>fr.cridf</groupId>
			<artifactId>formonline</artifactId>
			<version>2.0.31</version>
			<type>jar</type>  
                        <classifier>classes</classifier>   
		</dependency>
		  
		
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>servlet-api</artifactId>
			<version>2.5</version>
			<scope>provided</scope>
		</dependency>
                
  <dependency>
    <groupId>org.codehaus.cargo</groupId>
    <artifactId>simple-war</artifactId>
    <version>1.6.4</version>
    <type>war</type>
  </dependency>
  
  <dependency>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat8-maven-plugin</artifactId>
    <version>3.0-r1655215</version>
</dependency>

	</dependencies>




	<build>
	
		<finalName>${project.artifactId}-${project.version}-${profile.warsuffix}</finalName>
    
    
		<pluginManagement>
			<plugins>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-surefire-plugin</artifactId>
					<version>2.16</version>
				</plugin>

				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-war-plugin</artifactId>
					<version>2.6</version>  
					<configuration>
						<webResources>
							<resource>
								<directory>${basedir}/src/main/resources/META-INF</directory>
								<filtering>true</filtering>
								<targetPath>META-INF</targetPath>
								<includes>
									<include>**/context.xml</include>
								</includes>
							</resource>
							<resource>
								<directory>${basedir}/src/main/webapp/WEB-INF</directory>
								<filtering>true</filtering>
								<targetPath>WEB-INF</targetPath>
								<includes>
									<include>**/web.xml</include>
								</includes>
							</resource>
						</webResources>
						
						<overlays>
			              <overlay>
			                <groupId>fr.cridf</groupId>
							<artifactId>formonline</artifactId>
							
                		  </overlay>
                		</overlays>
                		
                		<packagingExcludes>WEB-INF/lib/formonline*.jar</packagingExcludes>
						
					</configuration>
				</plugin>


 				<plugin>
				    <groupId>org.codehaus.cargo</groupId>
                                    <artifactId>cargo-maven2-plugin</artifactId>
                                    <version>1.6.4</version>
                                    <configuration>
                                      <container>
                                        <containerId>tomcat8x</containerId>
                                        <zipUrlInstaller>
                                            <url>http://repo1.maven.org/maven2/org/apache/tomcat/tomcat/8.5.9/tomcat-8.5.9.zip</url>
                                        </zipUrlInstaller>
                                      </container>
                                      <configuration>
                                        <type>existing</type>
                                        <properties>
                                          <cargo.start.jvmargs>
                                            -Xdebug
                                            -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000
                                            -Xnoagent
                                            -Djava.compiler=NONE
                                          </cargo.start.jvmargs>
                                        </properties>
                                      </configuration>
                                    <deployables>
                                      <deployable>
                                        <groupId>org.codehaus.cargo</groupId>
                                        <artifactId>simple-war</artifactId>
                                        <type>war</type>
                                        <properties>
                                          <context>prodsi2</context>
                                        </properties>
                                      </deployable>
                                    </deployables>
                                    </configuration>
                                </plugin>
				  
				  <plugin>
                                    <groupId>org.apache.tomcat.maven</groupId>
                                    <artifactId>tomcat8-maven-plugin</artifactId>
                                    <version>3.0-r1655215</version>
                                 </plugin>

			</plugins>
		</pluginManagement>



	</build>
	<profiles>
		
		
		<profile>
			<id>Dev</id>	
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<properties>
				<jdbc.url>jdbc:mysql://localhost:3306/FOL_DSI</jdbc.url>
				<jdbc.username>lutece</jdbc.username>
				<jdbc.password></jdbc.password>				
				<profile.warsuffix>Local</profile.warsuffix>			
				
				<os.fileuploaddir>/var/prodsi</os.fileuploaddir>
                                
                                <adminPwd>e77989ed21758e78331b20e477fc5582</adminPwd>
			</properties>
			
			<dependencies>
				<dependency>
					<groupId>mysql</groupId>
					<artifactId>mysql-connector-java</artifactId>
					<version>5.1.6</version>
				</dependency>
			</dependencies>
		</profile>
		
		
	</profiles>
</project>
```

- Execute the SQL script to create the database (/sql/formonline_init_db.sql)
- Modify if necessary the database source in the profile section of the POM.xml
- Run `mvn tomcat7:run` to build and deploy in a dev server

- see the result at `http://localhost:9090/projectname`

- you can override the Jsp and Css files in your application project
- you can create your own "src/main/java/ContextUserAction.java" class that extends "UserAction" class to code specific behaviours on events (before or after creating form, modifying form, validate, update...)

## Screenshots


## API

- Opérations on forms are available with REST webRPC calls
