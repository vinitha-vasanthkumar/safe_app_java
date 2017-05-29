# safe_app_java

**Maintainer:** Krishna Kumar (krishna.kumar@maidsafe.net)

safe_app java library.

## Build instruction

### Prerequisites

safe_app_java is compatible with **Java 8**.

Maven must be [installed and configured](https://maven.apache.org/install.html).

### Building JAR

Execute, `mvn clean install` for generating the JAR. The JAR will be packaged with all its dependencies.


### For Development

`mvn clean test` will always download the native libraries based on profile from the configured location in the POM file. 

For running the test cases while development, can skip the download of libraries each time by passing the `wagon.skip` flag.
`mvn clean test -Dwagon.skip`


# License

Licensed under either of

* the MaidSafe.net Commercial License, version 1.0 or later ([LICENSE](LICENSE))
* the General Public License (GPL), version 3 ([COPYING](COPYING) or http://www.gnu.org/licenses/gpl-3.0.en.html)

at your option.

# Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted for inclusion in the
work by you, as defined in the MaidSafe Contributor Agreement, version 1.1 ([CONTRIBUTOR]
(CONTRIBUTOR)), shall be dual licensed as above, and you agree to be bound by the terms of the
MaidSafe Contributor Agreement, version 1.1.


