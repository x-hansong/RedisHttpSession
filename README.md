# RedisHttpSession
RedisHttpSession provides an way to transparently store http session in redis, which allows multi-clients to share the session.

# Features
- **RESTful APIs** - RedisHttpSession allows providing session ids in headers to work with RESTful APIs.
- **Transparently** - RedisHttpSession allows using HttpSession APIs directly, while the magic work with redis is totally transparent.

# Quick Start

 Use `RedisHttpSessionFilter` or a subclass of it as a Filter. 
 
 For example:
 
 - With `web.xml`
 
        <filter>
            <filter-name>redisHttpSessionFilter</filter-name>
            <filter-class>com.hansong.session.RedisHttpSessionFilter</filter-class>
        </filter>
        <filter-mapping>
            <filter-name>redisHttpSessionFilter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>
        
- With Spring

        @Component
        public class MySessionFilter extends RedisHttpSessionFilter{}
        
After that, for each request/response, their header will have a field -- `x-auth-token`, which is the session id.

And we can use the `HttpSession` as we always do, but the session is now in redis. If you check the redis, you will see something following.

    localhost:63679> keys *
    1) "session:fd9ec3cf-fb9b-4672-ade6-67a810e7db9f"
    2) "session:cbaa057c-85a4-475d-b399-38c320e85dcc"
    3) "session:13e030f5-de3d-458f-8d25-fd5643c40ff0"
    4) "session:262596b3-3d13-4df1-8328-714153c1ae83"
    5) "session:0b7d04c6-eaac-4eed-a9aa-8366f25f04f0"
      
    localhost:63679> hgetall session:fd9ec3cf-fb9b-4672-ade6-67a810e7db9f
    1) "lastAccessedTime"
    2) "\xac\xed\x00\x05sr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x01T\x91\x03\"\xec"
    3) "maxInactiveInterval"
    4) "\xac\xed\x00\x05sr\x00\x11java.lang.Integer\x12\xe2\xa0\xa4\xf7\x81\x878\x02\x00\x01I\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\a\b"
    5) "creationTime"
    6) "\xac\xed\x00\x05sr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x01T\x91\x03\"\xb4"

As you see, RedisHttpSession store the Serialized Object to the redis. For each request(except the first request), it needs have a `x-auth-token` in headers, which can be got from the response, so the server can use the session associated with the request. 
