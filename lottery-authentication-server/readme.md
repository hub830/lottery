```
curl -X POST \
  http://127.0.0.1:8210/oauth/token \
  -H 'Accept: */*' \
  -H 'Authorization: Basic Y2xpZW50OnNlY3JldA==,Basic aW9zOmlvcw==' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: 127.0.0.1:8210' \
  -H 'Postman-Token: 333565ba-1143-4d77-b33f-77281daf1944,6b0abc3e-f0b5-4fa0-9b00-71b8d131bd9a' \
  -H 'User-Agent: PostmanRuntime/7.13.0' \
  -H 'accept-encoding: gzip, deflate' \
  -H 'cache-control: no-cache' \
  -H 'content-length: 505' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F grant_type=password \
  -F username=admin \
  -F password=123456 \
  -F scope=all
```



```
curl -X GET \
  http://localhost:8210/user \
  -H 'Accept: */*' \
  -H 'Authorization: bearer 0eca3576-1e8b-4cb7-a12d-861081a9c2ee' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: localhost:8210' \
  -H 'Postman-Token: 90bea901-b534-4aaf-a5d5-c74e363d16a8,08904a50-7ae2-483b-aa6b-627bd3d364a9' \
  -H 'User-Agent: PostmanRuntime/7.13.0' \
  -H 'accept-encoding: gzip, deflate' \
  -H 'cache-control: no-cache'

```



