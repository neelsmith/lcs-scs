# lcs-scs

## What it is

`lcs-scs` is a cross-platform library for comparing Vectors.

## Current version: 0.1.0

Status: active development. [Release notes](releases).

## License

[GPL 3.0](https://opensource.org/licenses/gpl-3.0.html)

## Using, building, testing

`lcs-scs` is compiled for both the JVM and ScalaJS using scala 2.10, 2.11 and 2.12.

To build from source and test for a given version, use normal sbt commands (`compile`, `test` ...).

You can also test or run tasks against all versions, using + before the task name. E.g., `sbt "+ test"` runs the test task against all versions.