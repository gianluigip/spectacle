[//]: # ( {{ title: Configure DSL }} {{ tags: DSL, Config }} {{ features: Specifications }} )

# Configure DSL

To change the default behaviour when publishing you need to add `spectacle.properties` to your
test `resources` folder with the following content:

```properties
# Name of the main team maintaining the code
# You don't need to use @Team if the name is the same
specification.team=Spectacle
# Unique identifier for your Gradle module
# If you repeat the name you can overwrite specs when using some publishers
specification.source=spectacle-dsl
# Name of the component/service, it can be repeated
# Multiple sources (modules) are common for one service
specification.component=Spectacle DSL
# List separated by comma of publishers that you want to use
specification.publisher=terminal,central
# Central is the main publisher that allow to centralize all the specs in one place
# These properties can be also defined as env vars, so it's easier to use in CI
# Can be overwritten with the env var SPECIFICATION_PUBLISHER_CENTRAL_ENABLED
specification.publisher.central.enabled=false
# Can be overwritten with the env var SPECIFICATION_PUBLISHER_CENTRAL_HOST
specification.publisher.central.host=https://central.spectacle.com
# You can publish your Markdown files to Spectacle Central
specification.publisher.central.wiki.enabled=true
# If you are publishing your Markdown docs you need to define which folder will be published
specification.publisher.central.wiki.localFolderLocation=docs
```

Spectacle use the `source` to identify what specs are new and what specs were removed, so if you
reuse the same `source` for multiple tests executions like different services or several modules in
the same repo, you will overwrite the specs, each `spectacle.properties` should have a different
source.