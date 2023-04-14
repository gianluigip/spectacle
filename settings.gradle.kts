rootProject.name = "spectacle"

includeBuild("convention-plugins")
include("spectacle-common")

include("spectacle-dsl:spectacle-dsl-assertions")
include("spectacle-dsl:spectacle-dsl-bdd")
include("spectacle-dsl:spectacle-dsl-http")
include("spectacle-dsl:spectacle-dsl-protobuf")
include("spectacle-dsl:spectacle-dsl-publisher")

include("spectacle-central:common")
include("spectacle-central:webapp")
include("spectacle-central:domain")
include("spectacle-central:application")
