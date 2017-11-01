NeoEMF
======
[![Build Status](https://travis-ci.org/atlanmod/NeoEMF.svg?branch=master)](https://travis-ci.org/atlanmod/NeoEMF)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.inria.atlanmod.neoemf/neoemf/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fr.inria.atlanmod.neoemf/neoemf)
[![CodeCov](https://codecov.io/gh/atlanmod/NeoEMF/branch/backend-abstraction/graph/badge.svg)](https://codecov.io/gh/atlanmod/NeoEMF/branch/backend-abstraction)
[![Codacy](https://api.codacy.com/project/badge/Grade/82af9c0b0354424f93e1044cbdc85a9e)](https://www.codacy.com/app/atlanmod/NeoEMF?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=atlanmod/NeoEMF&amp;utm_campaign=Badge_Grade)
[![Javadoc](https://img.shields.io/badge/javadoc--blue.svg)](https://atlanmod.github.io/NeoEMF/releases/latest/doc/)
[![Licence](https://img.shields.io/badge/licence-EPL--2.0-blue.svg)](https://www.eclipse.org/legal/epl-2.0/)

## What is NeoEMF?

NeoEMF is an [AtlanMod][atlanmod-home] research team project that aims at handling large EMF models in an efficient and scalable way.

It is based on our work on Neo4EMF, a scalable and graph-based backend for persisting EMF models.

NeoEMF is a multi-backend framework developped to allow an easy integration of custom backends depending on user needs.

Check out our [wiki][wiki] for further information on the supported backends, code examples, and developer resources.

## What are the supported backends?

NeoEMF is provided with a complete support of MapDB and Blueprints databases.

Some Blueprints connectors are also available to ease the integration of graph databases (Neo4j for the moment).

## What are the features?

NeoEMF comes with some features:

-   **Lazy-Loading**: A model is loaded part by part while needed.
-   **Caching**: NeoEMF relies on database caches to retrieve _EObjects_. But, in some situation, where time performance is important, this is not enough and it is possible to use application-level caches to speed up information loading.
-   **Auto-Save**: For backends having restrictions on transaction sizes, it is possible to use the auto-commit feature to split large transaction into several small ones.
-   **Dirty Saving**: Handle large models that haven't been persisted to avoid memory overhead.

## Why should I use NeoEMF?

NeoEMF is fully EMF compliant, that means there is no modification to do to use it with existing EMF based applications.

Additionally NeoEMF can be plugged with several persistence solutions (for the moment Blueprints and MapDB) and is configurable to fit the best your needs (custom caches, in-memory resources, auto-commit,...).

In addition, NeoEMF provides a query API to perform optimized OCL queries over models and reify the results as navigable models.


## Usage

### Latest release

The most recent release is NeoEMF 1.0.2, released May 21, 2017.
-   Javadoc: [neoemf][release-doc]
-   Eclipse Plugin: [neoemf][release-plugin]

To add a dependency on NeoEMF using Maven, use the following:
```xml
<dependencies>
  <dependency>
    <groupId>fr.inria.atlanmod.neoemf</groupId>
    <artifactId>neoemf-core</artifactId>
    <version>1.0.2</version>
  </dependency>

  <dependency>
    <groupId>fr.inria.atlanmod.neoemf</groupId>
    <artifactId>neoemf-io</artifactId>
    <version>1.0.2</version>
  </dependency>
</dependencies>
```

#### Supported implementations

All native implementations are located under the `neoemf-data` artifact.
To add the dependency of the specific implementation you want to use, simply use:

```xml
<dependency>
  <groupId>fr.inria.atlanmod.neoemf</groupId>
  <artifactId>neoemf-data-{name}</artifactId>
  <version>1.0.2</version>
</dependency>
```

Where `{name}` is any of:
*   Blueprints:
    -   TinkerGraph : `blueprints-core`
    -   Neo4j : `blueprints-neo4j` *(requires `blueprints-core`)*
*   MapDB : `mapdb`
*   BerkeleyDB: `berkeleydb`
*   HBase: `hbase`

### Snapshots

Snapshots are automatically build from the `master` and are available throught Maven using `1.0.3-SNAPSHOT`.
-   Javadoc: [neoemf][snapshot-doc]
-   Eclipse Plugin: [neoemf][snapshot-plugin]

## Installation

### Update site
The simplest way to install NeoEMF is to use the update-site that is available [here][release-plugin].
Install the **NeoEMF Persistence Framework** component, which provides NeoEMF core classes and utils.
Select the backend specific implementation(s) you want to plug in NeoEMF.

### Specific backend setup
Backend specific implementations may need configuration files or require other plugins to work properly.
These requirements are available in the root of the related subprojects.

### Local build
You can build locally NeoEMF by following the instructions in the Build section.
It is then possible to install NeoEMF from the local built update-site, or by importing the generated plugins.


## Build

### NeoEMF
Download the latest release [here][release] and unzip it.
Go to the root of the extracted directory and run the following command:
```bash
mvn clean install
```
This will build NeoEMF core component and database specific implementations in your `.m2` directory.

### NeoEMF : Eclipse plugin
To build NeoEMF Eclipse plugin, you need to run the following command:
```bash
mvn clean install -f plugins/eclipse
```
NeoEMF plugins and update-site are built and stored in your local m2 repository at (`.m2/fr/inria/atlanmod/neoemf`).

## Opening in Eclipse

In order to import NeoEMF Git repository in Eclipse, you will need the following plugins in your installation (installable from the software repository of your Eclipse release):

-   **EGit** : v3.4.2 or later
-   **m2e** : v1.5.1 or later

And the following m2e connectors (installable through `Preferences > Maven > Discovery / m2e Marketplace`):

-   **Maven SCM Handler for EGit (m2e-egit)** : v0.14 or later
-   **Tycho Project Configurators (Tycho configurator)** : v0.8.0 or later

Steps:

1.  Import the project using `File > Import > Maven > Check out Maven Projects from SCM`
2.  Select "git" in the SCM URL field and paste the repository address [https://github.com/atlanmod/NeoEMF.git](https://github.com/atlanmod/NeoEMF.git)
3.  Choose your workspace and working sets setting and finish

The projects **project**, **parent**, **core**, **graph**, **graph.blueprints** and **map** will be imported and built.

If you want to import the projects generating Eclipse specific plugins (features, update-site), you have to import them by
hand using `File > Import > Maven > Existing Maven Projects`, and select the projects under the "plugins" folder in your local repository.

## Benchmarks

A full benchmarks description is available at [here][benchmarks].

## Issues

If you experience issues installing or using NeoEMF, you can [submit an issue on github][issues] or contact us at neoemf@googlegroups.com

### Known issues:
-   Only Luna plugins are fetched (the compatibility is not ensured for previous Eclipse versions)
-   The Eclipse plugins can not be built if Maven can not access internet (it is needed to fetch Luna p2 repositories)
-   Sometimes the plugin build crashes and/or freezes during p2 index fetching from Luna repositories. It is generally sufficient to cancel the build (`ctrl-c`) and to relaunch it.


## Develop your backend implementation
If you have [developped a specific backend implementation](Create-A-New-Module) that is not covered by the actual release of NeoEMF, you can submit it as a pull request.


## Credits
Performance problems and memory leaks are diagnosed with [Java Profiler](https://www.ej-technologies.com/products/jprofiler/overview.html)


[atlanmod-home]: http://www.emn.fr/z-info/atlanmod/index.php/Main_Page

[wiki]: https://github.com/atlanmod/NeoEMF/wiki
[issues]: https://github.com/atlanmod/NeoEMF/issues
[benchmarks]: https://github.com/atlanmod/NeoEMF/tree/backend-abstraction/benchmarks

[release]: https://github.com/atlanmod/NeoEMF/releases/latest
[release-doc]: https://atlanmod.github.io/NeoEMF/releases/latest/doc/
[release-plugin]: https://atlanmod.github.io/NeoEMF/releases/latest/plugin/

[snapshot-doc]: https://atlanmod.github.io/NeoEMF/releases/snapshot/doc/
[snapshot-plugin]: https://atlanmod.github.io/NeoEMF/releases/snapshot/plugin/