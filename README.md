# URL Shortener
This project is for URL shortening.
Given a URL like http://www.google.com, a shorter URL like http://me.li/JKSD8X is associated. 
The domain or protocol (http or https) the short URL can be configured, so the short URL can be http://do.li/JSDLXK or https://con.me/JSDLXK or anything else.

The size, the algorithm, and the alphabet for generating the short part, like "JSDLXK" can also be configured. Anyway, it's strongly suggested to use an alphabet which is supported by browsers and mobile platforms over SMS and mobile apps. In general, a Base62 encoding is a good election with the alphabet "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ". 

The following codes: "+" and "/" are generally part of a Bas64 encoding scheme, but they are special characters when used in URLs, so they shouldn't be used for URL shortening. 

Any other UTF-8 special characters, can be used, like "$", "€", "¬", etc. But using these characters also means that short URLs can be more difficult to communicate, e.g. over a phone to a help desk. Other characters like cyrillic letters like "Д" or "ф" can also be used, but that means yeat more difficult short URLs that are escaped when used in browsers. For example, http://me.li/алматы/ is escaped when used in browsers and becomes http://me.li/%D0%B0%D0%BB%D0%BC%D0%B0%D1%82%D1%8B. Cyrillic is also used by scammers to create fake URLs, used over Whatsapp also. So try to avoid using it.

Anyway, you can use the "ñ" for URLs, just as in http://www.peñarol.org.










The following are the implemented REST services:

 **Short URL Creation**: Creates a short URL associated with a given original URL.
 
 **Short URL Deletion**: Deletes a short URL already created.

 **Short URL Query**: Returns the original URL associated with a given short URL.