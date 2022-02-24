[//]: # ( {{ title: Specifications Report }} {{ features: Specifications }} )

## Specifications Report

You can search and bookmark any specification published, this allows you to easily share the
behavior of the product with your stakeholder, and to consolidate the documentation of features that
could be defined across several teams and components.

The specs are grouped by feature, and can be filtered by its metadata like:

* `Features`: A feature is expecto to have multiple specs, and specs belong to only one feature.
* `Tags`: Tags can be reused across multiple specs and features, so you can group specs according to
  your domain.
* `Teams`: This will who all the specs a single team wrote.
* `Statuses`: The specs can be registered while it is still not implemented or partially
  implemented, so it is useful to search only the implemented specs or review what is still missing
  for implementation.
* `Components`: Components are independent deployable parts of the system, usually it refers to
  services, but it can refer to other elements like libraries such as `spectacle-dsl`.
* `Sources`: A source is a part of the codebase that use `spectacle-dsl` to publish specs, a
  component can have multiple sources, for example your service could have multiple Gradle modules,
  each module will execute only a subset of your test and generate only a part of all the specs, so
  each module became a different source for the same component.

![Specifciations Page](https://github.com/gianluigip/spectacle/raw/master/spectacle-central/docs/images/SpecificationsPage.png)
