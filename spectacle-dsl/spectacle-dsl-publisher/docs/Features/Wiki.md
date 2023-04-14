[//]: # ( {{ title: Wiki Publisher }} {{ features: Wiki }} )

[//]: # ( {{ team: Spectacle }} {{ tags: Wiki Publisher }} )

# Wiki Publisher

Additionally to the writing and publishing of specs, `Spectacle DSL` can find all the Markdown files in a given folder and sync it then
in `Spectacke Central`, the idea is that you can write your tech docs close to your code but search it in a central place that any team member or
stakeholder can quickle reference.

## Markdown Files Publisher

You need to add to your `spectacle.properties` the following properties:

* `specification.publisher.central.wiki.enabled`
* `specification.publisher.central.wiki.localFolderLocation`: the path is relative to the root folder of your module, for example if you have a docs
  in `ROOT_PROJECT/spectacle-dsl/docs` and your Gradle module is `ROOT_PROJECT/spectacle-dsl` then a valid location is `docs`, because Gradle will
  execute the tests from the relative location of `ROOT_PROJECT/spectacle-dsl`, if your project structure is flat without any module, you can just use
  the relative path from the root of your project to your docs.

The tool store a checksum for the content of every file, so it will only republish a doc file if it has any changes.

## Markdown Metadata

You can enhance the metadata that will be published by providing some special tags in any part of the content of the file:

* `{{ title: VALUE' }}`: By default the name of the file is the title, but can be overwriting by this tag.
* `{{ team: VALUE }}`: By default the team is the one defined in `spectacle.properties` using the property `specification.team` but you can change it
  per file if you have more than one team collaborating in the same source.
* `{{ tags: VALUE1, VALUE2 }}`: A list of values separated by comma, if it's not present the wiki page will not include any tag.
* `{{ features: VALUE1, VALUE2 }}`: A list of values separated by comma, if it's not present the wiki page will not be related to any particular
  feature.

You can use any of the supported characters as a value for a tag:

* Any word
* Any number
* Any space
* `,`
* `.`
* `-`
* `[`
* `]`
* `/`
* \

Spectacle ignores any tag that uses a non-supported char, so if your metadata is not present in `Central` is possible that you wrote an invalid tag.

If you don't want your metadata to be visible when viewing the doc, Markdown support comments where you can put text that won't be displayed:

```markdown
[//]: # ( TEXT )
```

A full example:

```markdown
[//]: # ( {{ title: BDD DSL }} {{ tags: DSL, BDD }} {{ features: Specifications  }} )

[//]: # ( {{ team: Spectacle Docs Writers }} )

# BDD DSL

The simpler use case is to use the DSL to make your tests more readable, it's particularly useful for writing Integration Tests because it usually
validates complex behaviour and requires more setup and preconditions.
...
...
```