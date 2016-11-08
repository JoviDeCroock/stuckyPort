# Stuckytoys API

## Auth
| URL           | Method        |expects                      |returns|    
|:-------------:|:-------------:|:---------------------------:|:-----:|
|  /register    | Post          | email, username & password  |token  |
|  /login       | Post          | username (email) & password |token  |
|  /adminLogin  | Post          | username (email) & password |token  |