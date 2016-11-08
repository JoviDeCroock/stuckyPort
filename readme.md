# Stuckytoys API

## /auth
| URL           | Method        |expects           |returns|    
|:-------------:|:-------------:|:----------------:|:-----:|
|  /register    | Post          | RegisterObject   |token  |
|  /login       | Post          | loginUserObject  |token  |
|  /adminLogin  | Post          | loginAdminObject |token  |

## /figure
| URL                        | Method        |expects(body)            |returns         |    
|:--------------------------:|:-------------:|:-----------------------:|:--------------:|
|  /addFigure                | Post          | figureObject            | Createdfigure  |
|  /{userId}/getFigures      | Get           | nothing | allFigures    | allFigures     |

## /story
| URL           | Method        |expects                      |returns|    
|:-------------:|:-------------:|:---------------------------:|:-----:|
|  /download    | Get           |   |  |
|  /createStory | Post          |   |  |
|  /{storyId}/addScene| Post          |   |  |
|  /getStory/{storyId}  | Get         |   |  |
|  /getAllThemes  | Get         |   |  |
|  /getAllWidgets | Get         |   |  |

## Objects
| name             | variables                                                         |  
|:----------------:|:-----------------------------------------------------------------:|
|RegisterObject    | username, password & email                                        | 
|loginUserObject   | username(email) & password                                        | 
|loginAdminObject  | username(email) & password                                        | 
|figureObject      | name, type, default (boolean), description & pictureObject        | 
|pictureObject     | path and or a base64String                                        | 