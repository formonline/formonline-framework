# formonline
a light and powerfull form engine

## Requirements
java7 / tomcat / mysql

## Installation
- Download (or clone) the code
- Build the project (`mvn install`)
- Create another project with this POM.xml :

```
pom
```

- Execute the SQL script to create the database (/sql/formonline_init_db.sql)
- Modify if necessary the database source in the profile section of the POM.xml
- Run `mvn tomcat7:run` to build and deploy in a dev server

- see the result at `http://localhost:9090/projectname`

## Screenshots


## API
