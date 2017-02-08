# About this document

This document describes a few user stories. These user stories will ultimately form the way culverin behaves.
All featuers and mechanisms are designed to support these stories.

# User stories

## NGINX simple error count
We want to extract and count 500 error codes from nginx logs, which are usually in the following format :

```
$remote_addr - $remote_user [$time_local] "$request" $status $body_bytes_sent "$http_referer" "$http_user_agent"
```

like

```
127.0.0.1 - - [03/Sep/2016:12:15:48 +0430] "POST /api/3a5ef265-26ff-436e-a00d HTTP/1.1" 500 28301 "http://127.0.0.1/test.html" "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36"
```

Finally, there should be a way to query current values.
(Note that this story focuses on 'counter' features)

## NGINX simple status count

In this case, we want to extract status codes from nginx logs and count it per code(200,400,etc...) per minute.
Nginx logs are used as source (like the one in previous story).
(this story focuses on 'counter' features)
