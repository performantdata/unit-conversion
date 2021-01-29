# Unit conversion example

To build a docker image, run:
```shell
sbt docker:publishLocal
```
The server listens on port 8080. Run it in the usual way:
```shell
docker run -p 8080:8080 unit-conversion
```
