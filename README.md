CSS Validator Standalone
========================

This project continues at https://github.com/w3c/validators

The W3C CSS Validation service.

This project packages the CSS Validator service so that it can easily be run locally.

How to build the CSS Validator WAR
----------------------------------

    ./build-css-validator-war.sh

You need CVS, Java and ant.  The script takes the code from the official repository at W3C.

How to run the CSS Validator
----------------------------

    ./sbt run

Then go to [http://localhost:8080](http://localhost:8080).

How to generate a standalone jar
--------------------------------

    ./sbt assembly

Then you can do the following:

    java -jar target/css-validator-standalone.jar 8080

Licence
-------

This source code is made available under the [W3C Licence](http://opensource.org/licenses/W3C).
