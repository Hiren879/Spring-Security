# Spring-Security
Spring-security concepts

|Security pillers | Description |
| ------ | ------ |
| Authentication | Who are you ? This can be knowledge base or posession based. |
| Authorization | What do you want ? Are you authorized to access this ? |
| Principal | Once you logged in to the system, "principal" will store credentials of user. |
| Granted Authority | What they can do i.e. place_order/do_checkOut. Authority to perform certain operation.|
| Roles | Basically, a group of authority so that it can be well managed and easily assignable to anyone. |

"spring-boot-starter-security" - it is responsible to provide all APIs related to spring security in our application.

## What spring-boot-starter-security will provide ?
Once you will add this maven dependency, you will get
1. Input form for username/password
2. Validation for the same
3. If you have not provided any username/passoword then Spring will generate one password for user "User" everytime you will start the spring application.


### Author
---

[Hiren Savalia](https://www.linkedin.com/in/hiren879/)
