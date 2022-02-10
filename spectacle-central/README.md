# Spectacle Central

## Config

Environment Variables:

* `DATABASE_URL`: one variable holding all the connection detail using the
  format `USERNAME:PASSWORD@HOST:PORT/DATABASE`, additionally you can use the below `DATABASE_XXX`
  variables, if booth options are present the more specific variable take precedence.
* `DATABASE_HOST`
* `DATABASE_PORT`
* `DATABASE_NAME`
* `DATABASE_USERNAME`
* `DATABASE_PASSWORD`

## Build and Run

Run Server:

```
./gradlew spectacle-central:application:run
```

Run Web UI only:

```
./gradlew jsBrowserRun --continuous
```