```bash
    mvn package
```

```bash
    mvn clean
```

```bash
    mvn clean install
```

```bash
    mvn spring-boot:run
```

```bash
    mongosh
    show dbs
    use vibeme
    show collections
    db.users.find()
    db.users.deleteMany({})
```


# heirarchy of functional calling: 

controller -> service -> repository
scheema = entity e.g.: chatEntry, userEntry

# Spring documentation

## Spring Security

- By default, Spring uses HTTP based Basic Authentication.
- Basic Authentication is Stateless.

# TODOs:

- add 'roles' in user entity for authorization.

# UserEntity

- username and phoneNumber are same things here.