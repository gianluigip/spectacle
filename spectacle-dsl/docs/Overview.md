[//]: # ( {{ title: Overview }} )

# Spectacle DSL

A Kotlin multiplatform library for writing readable tests as specifications, it includes an
Assertion DSL and a BDD DSL, optionally you can publish your specs into the specs
repository `Spectacle Central`.

## How It Works

Lifecycle of the library.

```mermaid
sequenceDiagram
    participant BuildTool as Build Tool
    participant Repo as Code Repository
    participant DSL as Spectacle DSL
    participant Central as Spectacle Central

    BuildTool   ->> Repo:   Execute Test
    Repo        ->> Repo:   Use Spectacle Junit Extension
    Repo        ->> DSL:    Register tests <br>anntoted as @Specifications
    DSL         ->> Repo:   Collect all specs and interactions
    Repo        ->> DSL:    Finish executing tests
    DSL         ->> Central:Publish Specs and Interactions
    DSL         ->> Repo:   Search for Markdown files
    DSL         ->> Central:Publish Local Wiki Pages
```
