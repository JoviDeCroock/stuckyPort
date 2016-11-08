# Stuckytoys API


## Table
| Part             |
|:----------------:|
|[auth](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#auth)   |
|[figure](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#figure)   |
|[story](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#story)  | 
|[Objects](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#objects)    |
|[Explanations](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#Explanations)    |



## API Methods

### /auth
| URL           | Method        |expects (body)    |returns|    
|:-------------:|:-------------:|:----------------:|:-----:|
|  /register    | Post          | RegisterObject   |token  |
|  /login       | Post          | loginUserObject  |token  |
|  /adminLogin  | Post          | loginAdminObject |token  |

### /figure
| URL                        | Method        |expects (body)           |returns         |    
|:--------------------------:|:-------------:|:-----------------------:|:--------------:|
|  /addFigure                | Post          | figureObject            | Createdfigure  |
|  /{userId}/getFigures      | Get           | nothing | allFigures    | allFigures     |

### /profile
| URL                                 | Method        |expects                      |returns        |    
|:-----------------------------------:|:-------------:|:---------------------------:|:-------------:|
|/users/{userId}/addMember            | Post          | MemberObject                | createdMember |
|/users/{userId}/getMember/{memberId} | Get           | Nothing                     | specificMember|
|/users/{userId}/getAllMembers        | Get           | Nothing                     | allMembers    |

### /story
| URL                   | Method    |expects                      |returns         |    
|:---------------------:|:---------:|:---------------------------:|:--------------:|
|  /download/{SceneId}  | Get       |  Nothing                    | Scenefiles     |
|  /createStory         | Post      |  StoryObject                | newStory       |
|  /{storyId}/addScene  | Post      |  SceneObject                | modified Story |
|  /getStory/{storyId}  | Get       |  Nothing                    | Requested Story|
|  /getAllThemes        | Get       |  Nothing                    | AllThemes      |
|  /getAllWidgets       | Get       |  Nothing                    | AllWidgets     |



## API Objects

### Objects
| name             | variables                                                                                     |  
|:----------------:|:---------------------------------------------------------------------------------------------:|
|RegisterObject    | username, password & email                                                                    | 
|loginUserObject   | username(email) & password                                                                    | 
|loginAdminObject  | username(email) & password                                                                    | 
|figureObject      | name, type, default (boolean), description & pictureObject                                    | 
|pictureObject     | path and or a base64String                                                                    | 
|StoryObject       | Name, date(DD MM YYYY), ThemeObject & SceneObject(Array)                                      |
|SceneObject       | sceneNr, WidgetObject & figureObject                                                          |
|WidgetObject      | id(androidName), nameFile (associatedFile) & Type(of file)                                    |
|ThemeObject       | name & description                                                                            |
|UserObject        | email, username, password, arrays of MemberObjects, figureObjects & StoryObjects              |
|MemberObject      |firstname, nickname, role, authority(boolean), dateOfBirth(DD MM YYYY) & array of figureObjects|



## Explanations

### expects: Nothing

This is never literally nothing for example in every method apart from the auth it will require you to:

```javascript

headers: {Authorization: 'Bearer ' + token}

in code
