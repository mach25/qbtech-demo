# Qbtech demo counter application

Notes:

* Written in ~3 hours
* Spring webflux
* Java
* Maven
* H2 database is blocking hence all the blocking code and not using ReactiveCrudRepository
* No tests :-(
* No database migrations (flyway or liquibase), using hibernate auto ddl
* Had issue with CrudRepository.deleteAll custom work around
* Sorry no heroku or aws deployment
* Usage of git minimal (just one amendment to add this line)