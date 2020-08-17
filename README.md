# InstantMessage
An anonymous chat room web application based on vertx.

## Backend
The repo [backend](./backend) uses [gradle](https://gradle.org) as the build tool. To run it locally, the following 
steps should be completed: 
 1. Install MySQL database.
 2. Run the [create_table](./backend/create_table.sql) to create necessary tables.
 3. Set [config](./backend/src/main/resources/databaseConfig.properties) of MySQL connection.
 4. Execute `gradle build` and `gradle run` under the [backend](./backend) directory.
 
For deployment on a server, executing `gradle build` command, then gradle will create `build/distributions/` directory.
There exist compressed files with different formats, but the same content under the above directory. Unzipping any file,
running shell or bat script under the `bin` directory, then service will start.
