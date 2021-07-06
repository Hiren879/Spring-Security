# Spring-Security
Spring-security concepts

|Security pillers | Description |
| ------ | ------ |
| Authentication | Who are you ? This can be knowledge base or posession based. |
| Authorization | What do you want ? Are you authorized to access this ? |
| Principal | Once you logged in to the system, "principal" will store credentials of user. |
| Granted Authority | What they can do i.e. place_order/do_checkOut. Authority to perform certain operation.|
| Roles | Basically, a group of authority so that it can be well managed and easily assignable to anyone. |

**spring-boot-starter-security** - it is responsible to provide all APIs related to spring security in our application.

## What spring-boot-starter-security will provide ?
Once you will add this maven dependency, you will get
1. Input form for username/password
2. Validation for the same
3. If you have not provided any username/passoword then Spring will generate one password for user "*User*" everytime you will start the spring application.
4. Username & password can be configured via providing following properties in application.properties file OR application.yml file :
	```
	spring.security.user.name=<userName>
	spring.security.user.password=<password>
	```

## Congifuring Spring-Security
### AuthenticationManager
Authentication manager manages authentication.
```
authenticate()
```
Authenticate() method perform authentication on uesr and return if user  is successfully authenticated or throws exception.

### AuthenticationManagerBuilder
This is a builder pattern. You should use it to configure authentication for your application.
It is a 2 steps process :
1. Get hold of **AuthenticationManagerBuilder**
2. Set the configuaration on it.

### Extends WebSecurityConfigurerAdapter
```
configure()
```
When you extend this class, spring framework gives you opportunity to override **configure()** method where you will get instance of **AuthenticationManagerBuilder**, You can now modify it and pass your spring security configuration as per the requirement.
It is 2 step process to configure builder instance :
1. Tell which type of authetication you want i.e. inMemoryAuthentication()
2. Pass user, password & role.

Example :
```
@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("hiren")
			.password("hiren")
			.roles("User");
	}
```

### Author
---

[Hiren Savalia](https://www.linkedin.com/in/hiren879/)
