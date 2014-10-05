#API Endpoints
 
##### Request
* [POST /translate](post-translate): translates a user request into a an intent with fields
* [POST /request](post-request): submits a request for processing 
* [GET /request?status=<status>](get-requeststatusstatus): gets requests from storage
* [PUT /request/id/<id>](put-requestidid): modifies a request (e.g. change its state)

##### Account
* [POST /account](post-account): registers a user
* [PUT /account](put-account): updates a user
* [POST /authenticate](post-authenticate): authenticates a user
 
* * * 

## POST `/translate `

```
{ "request":"make a reservation tmrw at 8pm for 3 at x" } 
```


__200 OK__
```
{ "result": {
	  “request”: {
	  "intent":"RESTAURANT_RESERVATION", 
    "restaurant":"Roscoes", 
    “dateTime”:”Thu, 28 Aug 2014 09:30:00 GMT”
	  },
    “missingFields”: [{
		  “field”: “partySize”,
		  “type”: “integer”,
		  “friendlyString”: “For how many people?”
    }],
    “validatedFields”: [{
		  “field”: “restaurant”,
		  “type”: “string”,
		  “friendlyString”: “Where?”
	  }]
    “Confirmation”:”We’ll make a reservation at $restaurant for $partySize people at $dateTime.”
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
	“email”:”allenwoot@gmail.com”,
	“number”:”6363366066”,
	“firstName”:”Allen”,
	“lastName”:”Wu”,
	“password”:”super_secure_password”,
	“userType”:”USER” | “WORKER”
}
```
__200 OK__
```
{
	“result”: {
		“user”: {
		  “id”:”9382938”,
      “authToken”:”2eu89e28u90u92”,
			“email”:”allenwoot@gmail.com”,
			“number”:”6363366066”,
	    “firstName”:”Allen”,
	    “lastName”:”Wu”,
	    “password”:”super_secure_password”,
			“userType”:”USER” | “WORKER”
		}
	},
	“error”: null
}
```


```
{
	“number”:”6363366066”,
	“firstName”:”Allen”,
	“lastName”:”Wu”,
	“password”:”doge”
}
```

__400 BAD REQUEST__
```
{
	“result”: null
	“error”: {
		“code”:400
		“message”:”Missing field {email}”
  }
}
```

## POST `/authenticate`

```
{
	“email”:”allenwoot@gmail.com”,
  “password”:”doge”
}
```
__200 OK__
```
{
	“result”: {
		“user”: {
			“id”:”9483939”,
			“authToken”:”2eu89e28u90u92”,
			“email”:”allenwoot@gmail.com”,
			“number”:”6363366066”,
	“firstName”:”Allen”,
	“lastName”:”Wu”,
	“password”:”doge”
		}
	},
	“error”: null
}
```

```
{
	“email”:”allenwoot@gmail.com”,
  “password”:”doge”
}
```

__400 (or 410?) BAD REQUEST__
```
{
	“result”: null
	“error”: {
		“code”:410
		“message”:”Authentication failed”
  }
}
```

Example of auth token use case
Request:
Auth-Token: 2eu89e28u90u92
{
	“status”:”DONE”
}
Response:
401 UNAUTHORIZED
{
	“result”: null,
	“error”: {
		“code”: 401,
		“message”, “Invalid or expired auth token”
	}
}


