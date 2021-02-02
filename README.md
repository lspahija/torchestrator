# Torchestrator

Torchestrator can spin up Tor containers and expose ports for proxying HTTP requests via these Tor instances.

The IP address of the exit node of each Tor instance will vary. This is useful for IP address rotation.

To run:
    
- `cd torchestrator/docker/torprivoxydocker`
- `docker build -t torprivoxy:1.0 .`
- set number of Tor containers you wish to spin up with config property `tor.containerQuantity` in `torchestrator/src/main/resources/application.properties` 
  (default is 10, but can be a much higher number depending on RAM)
- `./gradlew bootRun` (with JDK 15) or run in an IDE like IntelliJ IDEA
- get next proxy port with `GET localhost:8080/port`

A sample HTTP client proxying requests via Tor instances can be found in `torchestrator/src/main/kotlin/com/alealogic/torchestrator/client/ExampleClient.kt`
    