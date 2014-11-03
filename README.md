#API Endpoints
 
##### Request
* [POST /translate](README.md#post-translate): translates a user request into a an intent with fields
* [POST /request](README.md#post-request): submits a request for processing 
* [GET /request?status=<status>](README.md#get-requeststatusstatus): gets requests from storage
* [PUT /request/id/<id>](README.md#put-requestidid): modifies a request (e.g. change its state)

##### Account
* [POST /account](README.md#post-account): registers a user
* [PUT /account](README.md#put-account): updates a user
* [POST /authenticate](README.md#post-authenticate): authenticates a user
 
* * * 

## POST `/translate `

```
{ "request":"Make a reservation tomorrow at 8pm at House of Prime Rib" } 
```


__200 OK__
```
{
    "result": {
    	"request": {
            "intent": "RESTAURANT_RESERVATION",
            "dateTime": "2014-11-04T04:00:00+0000",
            "restaurant": "House of Prime Rib"
        },
        "missingFields": [
            {
                "friendlyString": "For how many people?",
                "name": "partySize",
                "type": "Integer"
            }
        ],
        "validatedFields": [
            {
                "friendlyString": "Where?",
                "name": "restaurant",
                "type": "String"
            },
            {
                "friendlyString": "When?",
                "name": "dateTime",
                "type": "String"
            }
        ],
        "confirmation": "We'll make a reservation at $restaurant for $partySize people at $dateTime."
    },
    "error": null
}
```
 
## POST `/request`

```
{ 
  "intent":"RESTAURANT_RESERVATION", 
  "restaurant":"Roscoes", 
  "partySize":4, 
  "dateTime":"Thu, 28 Aug 2014 09:30:00 GMT" 
} 

```
__200 OK__

```
{ 
  "result": {
    “request”: {
  	  "intent":"RESTAURANT_RESERVATION", 
      "restaurant":"Roscoes", 
      "partySize":4, 
      "dateTime":"2014-08-27T14:20:03+00:00"
      “id”:”38282949”,
      “status”:”QUEUED”,
      “created”:”2014-08-27T14:20:03+00:00”
    } 
  }
  "error":null 
}
```
__400 bad request__

```
{
	“result”:null,
	“error”: {
		“code”:400,
		“message”:”Missing field: partySize”
	}
}
```
## GET `/request?status=<status>`

Gets all requests where the status is one of the enum `QUEUED`, `PROCESSING`, or `DONE`
```
{ 
  "result": { 
    "requests": [{
  	  "intent":"RESTAURANT_RESERVATION", 
      "restaurant":"Roscoes", 
      "partySize":4, 
      "dateTime":"2014-08-27T14:20:03+00:00"
      “id”:”38282949”,
      “status”:”QUEUED”,
      “created”:”2014-08-27T14:20:03+00:00”
    }, 
    { 
  	  "intent":"RESTAURANT_RESERVATION", 
      "restaurant":"House of prime rib", 
      "partySize":2, 
      "dateTime":"2014-08-29T14:20:03+00:00"
      “id”:”192842”,
      “status”:”QUEUED”,
      “created”:”2014-08-28T14:20:03+00:00”
    }] 
  }, 
  "error":null 
} 
```

## PUT `/request/id/<id>`
```
{
	“status”:”DONE”
}
```
__200 OK__
```
{
	“result”: {
  	"intent":"RESTAURANT_RESERVATION", 
    "restaurant":"House of prime rib", 
    "partySize":2, 
    "dateTime":"2014-08-29T14:20:03+00:00"
    “id”:”192842”,
    “status”:”DONE”,
    “created”:”2014-08-28T14:20:03+00:00”
	},
	“error”:null
}
```
```
{
}
```
__400 bad request__
```
Response:
{
	“result”: null,
	“error”: {
		“code”:400,
		“message”:”Request must contain a status”
	}
}
```

## POST `/account`

```
{
    "email": "allenwoot@gmail.com",
    "number": "6363366066",
    "firstName": "Allen",
    "lastName": "Wu",
    "password": "super_secure_password",
    "userType": "USER"
}
```
__200 OK__
```
{
    "result": {
        "user": {
            "authToken": "AUTHVVNFUjE4Mjk4ODMyMjRAMTQxNDk4OTI4NjcxNg==",
            "email": "allenwoot@gmail.com",
            "firstName": "Allen",
            "id": "USER1829883224",
            "lastName": "Wu",
            "number": "6363366066",
            "password": "super_secure_password",
            "userType": "USER"
        }
    },
    "error": null
}
```

__400 BAD REQUEST__
```
{
    "result": null,
    "error": {
        "code": 400,
        "message": "Missing field {email}"
    }
}
```

## POST `/authenticate`

```
{
    "email": "allenwoot@gmail.com",
    "password": "doge"
}
```
__200 OK__
```
{
    "result": {
        "user": {
            "authToken": "AUTHVVNFUjE2MzMyMzM2NjhAMTQxNDk4OTk0NDY1OA==",
            "email": "allenwoot@gmail.com",
            "firstName": "Allen",
            "id": "USER1633233668",
            "lastName": "Wu",
            "number": "6363366066",
            "password": "super_secure_password",
            "userType": "USER"
        }
    },
    "error": null
}
```

__403 FORBIDDEN__
```
{
    "result": null,
    "error": {
        "code": 403,
        "message": "Authentication failed"
    }
}
```

An authentication token must be passed with every request (with the exception of post account and post authenticate), as a header keyed with "Auth-Token".

401 UNAUTHORIZED
```
{
    "result": null,
    "error": {
        "code": 401,
        "message": "Invalid or expired auth token"
    }
}
```
