## Project's Deployment
In the project folder, open a terminal and launch the following commands: `./mvnw clean` and `./mvnw package`.

After running the Tomcat Server on the local pc, go to Tomcat App Manager (usually http://localhost:8080).
Login with the credentials and then in deploy section open the File Explorer and choose the `.war` file in
the `target` folder created by maven.

Clicking on the webapp name will bring to the project's initial page.

If using [JetBrains IntelliJ IDEA](https://www.jetbrains.com/idea/) as IDE, it's simpler to use the [Smart Tomcat Plugin](https://plugins.jetbrains.com/plugin/9492-smart-tomcat) to deploy the app directly
from the IDE. Setup is easy, just add the path to your Tomcat installation folder and then it's ready to go!
