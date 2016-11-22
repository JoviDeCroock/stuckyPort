# Stuckytoys API


## Table
| Part             |
|:----------------:|
|[auth](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#auth)   |
|[figure](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#figure)   |
|[story](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#story)  |
|[theme](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#theme)  |
|[widget](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#widget)  |
|[Objects](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#objects)    |
|[Explanations](https://github.com/HoGentTIN/projecten-3-g_st_di_1100/tree/WebDevS3#Explanations)    |



## API Methods

### /auth
| URL           | Method        | Body             |returns|
|:-------------:|:-------------:|:----------------:|:-----:|
|  /register    | Post          | RegisterObject   |token  |
|  /login       | Post          | loginUserObject  |token  |
|  /adminLogin  | Post          | loginAdminObject |token  |

### /figure
| URL                        | Method        | Body                    |returns         |
|:--------------------------:|:-------------:|:-----------------------:|:--------------:|
|  /addFigure                | Post          | figureObject            | Createdfigure  |
|  /{userId}/getFigures      | Get           | nothing | allFigures    | allFigures     |

### /profile
| URL                                 | Method        | Body                        |returns        |
|:-----------------------------------:|:-------------:|:---------------------------:|:-------------:|
|/users/{userId}/addMember            | Post          | MemberObject                | createdMember |
|/users/{userId}/getMember/{memberId} | Get           | Nothing                     | specificMember|
|/users/{userId}/getAllMembers        | Get           | Nothing                     | allMembers    |

### /story
| URL                                  | Method    | Body                        |returns         |
|:------------------------------------:|:---------:|:---------------------------:|:--------------:|
|  /createStory                        | Post      |  StoryObject                | newStory       |
|  /{storyId}/addScene                 | Post      |  SceneObject                | modified Story |
|  /getStory/{storyId}                 | Get       |  Nothing                    | Requested Story|
|  /getAllStories                      | Get       |  Nothing                    | AllStories     |
|  /{StoryObject}/DeleteScene/{SceneId}| Get       |  Nothing                    | modified story | 
|  /editScene                          | Post      |  SceneObject                | modifiedScene  |
|  /publish/{storyId}                  | Post      |  Nothing                    | bool succes    |
|  /download/{WidgetFileId}            | Get       |  Nothing                    | File           |

### /theme
| URL                                  | Method    | Body                        |returns         |
|:------------------------------------:|:---------:|:---------------------------:|:--------------:|
|  /getAllThemes                       | Get       |  Nothing                    | AllThemes      |
|  /addTheme                           | Post      |  ThemeObject                | newTheme       |
|  /themes/{themeId}                   | Get       |  Nothing                    | Requested theme|
|  /editTheme                          | Post      |  ThemeObject                | modifiedTheme  |
|  /removeTheme/{themeId}              | Post      |  Nothing                    | succes/fail    |

### /widget
| URL                                  | Method    | Body                        |returns         |
|:------------------------------------:|:---------:|:---------------------------:|:--------------:|
|  /getAllWidgets                      | Get       |  Nothing                    | AllWidgets     |
|  /addWidget                          | Post      |  WidgetObject               | newWidget      |
|  /widgets/{widgetId}                 | Get       |  Nothing                    |Requested widget|
|  /widgetTypes                        | Get       |  Nothing                    | allWidgetTypes |
|  /WigetsOfType                       | Post      |  type                       | allItemsOfType |
|  /removeWidget/{widgetId}            | Post      |  Nothing                    | succes/fail    |


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

```
in your code.
AKA fill your headers before requesting.

### WidgetFiles:

| Type          | nameFile                |
|:-------------:|:-----------------------:|
| Music         | Path in node            |
| Game          | AndroidActivity         |
| Image         | Pah in node             |
| Hint          | Text to help story      |
| Recording     | Command towards Android |
