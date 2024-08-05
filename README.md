## Project's Deployment
In the project folder, open a terminal and launch the following commands: `./mvnw clean` and `./mvnw package`.

After running the Tomcat Server on the local pc, go to Tomcat App Manager (usually http://localhost:8080).
Login with the credentials and then in deploy section open the File Explorer and choose the `.war` file in
the `target` folder created by maven.

Clicking on the webapp name will bring to the project's initial page.