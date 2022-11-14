[//]: # ( {{ title: HTTP DSL }} {{ tags: DSL, Interactions, API }} {{ features: API, Interactions  }} )

[//]: # ( {{ team: Spectacle Docs Writers }} )

# HTTP DSL

The HTTP DSL is wrapper for the Ktor client that collects metadata about the calls that you make and is able to register them as spec interactions.

Then all the calls registered during your integration tests can be published to `Spectacle Central` and use a dedicated API page to review all the
HTTP endpoints in your system.

If you use this DSL in your integration tests you will be generating the API documentation for your service for free.

## How It Works

First you need to initialize the http client according to your test setup:

```kotlin
    httpInteractionsConfig("http://localhost:$testPort", httpClient = HttpClient {
        install(ContentNegotiation) { json() }
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = "TEST_USER", password = "TEST_PASSWORD")
                }
            }
        }
    })
```

There are several overloaded functions to config the DSL but if you need to have full control over your client, you can send it directly.

Once the DSL is configured you can use the following functions in your tests:
* `receivesGetRequest`
* `receivesPutRequest`
* `receivesPostRequest`
* `receivesDeleteRequest`

The naming is from the point of view of your service under test, your service/component is receiving requests.

Full example:

```kotlin
 
    receivesPutRequest(
        path = "/api/specs/{SPEC_ID}",
        pathParameters = mapOf("SPEC_ID" to "1234"),
        queryParameters = mapOf("p1" to "v1", "p2" to "v2"),
        contentType = ContentType.Application.Json,
        body = """
            {
                "field1": "val1",
                "field2": "val2"
            }
            """.trimIndent(),
    )
```
