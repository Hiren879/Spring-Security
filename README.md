This readMe file is created along with the journey of learning the Spring Security concepts using free course available on [JavaBrains youtube course](https://www.youtube.com/playlist?list=PLqq-6Pq4lTTYTEooakHchTGglSvkZAjnE)
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
configure(AuthenticationManagerBuilder)
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
	
	/**
	 * Clear text password is strict NO in Spring.
	 * Just for tutorial purpose we are passing this.
	 * @return
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
```

### Configuration Authorization using HttpSecurity
- We will be configuring who will access WHAT !!
- One user would be having one specific role and the other will be having the other specific role.
- We will use again the same method we used to configure authentication which is **configure()** method.
```
configure(httpSecurity)
```
**httpSecurity** can be used to configure authorization here.

Following will be our user-role mapping :
|API end point | Roles allows to access it |
| ------ | ------ |
| / | All (unauthenticated) |
| /user | USER/ADMIN roles |
| /admin | ADMIN role |
```
	http.authorizeRequests()
			.antMatchers("/**").hasRole("ADMIN")
			.and().formLogin();
```
In above code snippet :
1.  "/**" means any URL and any nested URL.
2. hasRole("ADMIN") means all URLs (in this case) are only accessible by users who has ADMIN role.

Below code snippet is the more advance version :
```
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user").hasAnyRole("ADMIN", "USER")
			.antMatchers("/").permitAll()
			.and().formLogin();
	}
```
Here,
1. Higher & more restrictive role must come at the first place and then less restrictive.
2. If you will not follow the point number 1 then Spring security may not behave as per your configuration.

## How internally Spring performs authentication & authorization ?
### Filters
When you add spring security dependency, Spring will automatically start intercepting all the incoming requests using **DelegatingFilterProxy** before delegating it to servlets.
So there are two specific filters which performs authentication and authorization.

### Authentication using filters
When Spring start authentication it takes credentials as input and returns **principal** as output. Object type will be **authentication**.

Here is the method signature from interface **AuthenticationProvider**
```
Authentication authenticate(Authentication authentication)
```
![image](https://user-images.githubusercontent.com/2741709/125129805-a0064a00-e11d-11eb-87d5-821c2371044e.png)
As shown in above image :
1. First filter will intercept the incoming request
2. Then it will pass the credential to the **AuthenticationManager**
3. Authentication manager then identify the implementation.
4. Based on **AuthenticationProvider** it will use credential storage resources to authenticate the the user and returns the **Principal** object in the form of Authentication object.
5. It will be then stored in the **ThreadLocal** object to extract user related information like username, password, isAuthenticatd boolean etc.

### Adding MySql Database
Database will be having one table "user" with the fields
1. id : PK
2. active : If user is active or not. Boolean field
3. password : Password of a user
4. roles : all the comma saperated roles
5. user_name : User name of the user

Required dependencies :
1. Spring web starter
2. Spring security starter
3. JPA
4. MySql driver

Requirement :
1. /user : should only be accessd by someone having role of USER & ADMIN.
2. /admin : should only be accessd by someone having role of only ADMIN.

### Extend WebSecutiryConfigurerAdapter
Here we will get two methods which need to be extended to fullfil our requirement.

**Setup authentication** : Tell spring security using which implementation you want to get your user authenticated

```
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
```

**Setup Authorization**: Tell spring securtiy who can access what

```
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/admin").hasRole("ADMIN")
				.antMatchers("/user").hasAnyRole("ADMIN", "USER")
				.antMatchers("/").permitAll()
				.and().formLogin();
	}
```

**Authentication Manager -> AuthenticationProvider -> UserDetailsService -> loadByUserName**

1. UserDetailsService will act like plug and play for source of the data. It could be the text file, excel or the database. In our case it will be JPA [and then MySql as a data source].
2. We just need to create an implementation of the **org.springframework.security.core.userdetails.UserDetailsService** which will return the object of **org.springframework.security.core.userdetails.UserDetails**
3. This implementation can get the UserDetails object from anywhere. In our case we will fetch this user object from MySql DB via JPA and transform it into the UserDetails object.
4. So now everytime this implementation of UserDetails will call its method **loadUserByUsername** with the UserName it gets from the UI.

~~~
@Service
public class SpringSecurityUserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found for userName :: " + username));
		return user.map(MyUserDetails::new).get();
	}
}
~~~

5. **UserDetails** : We need to provide implementation of this class as well. So that we can get the user from DB and conver those fields into UserDetails type of object.
~~~
public class MyUserDetails implements UserDetails
~~~

6. We have created above class whiche implements **org.springframework.security.core.userdetails.UserDetails**

**Extracted JPA query** :
Hibernate: select user0_.id as id1_0_, user0_.active as active2_0_, user0_.password as password3_0_, user0_.roles as roles4_0_, user0_.user_name as user_nam5_0_ from user user0_ where user0_.user_name=?

### Author
---

[Hiren Savalia](https://www.linkedin.com/in/hiren879/)

### Credits
[JavaBrains youtube course](https://www.youtube.com/playlist?list=PLqq-6Pq4lTTYTEooakHchTGglSvkZAjnE)