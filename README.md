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
 
 The base URL part of the short URL is also configurable. 
 
 You can use "htp://me.li" or "http://me.io", etc.

 To install locally:


- You need the following tools installed:
    - JDK 11
    - Docker
    - aws cli (configured with AWS credentials) (see aws configure command)
    - sam cli (for building Serverless Application Model templates)
    - optionally docker-compose
    - maven (for building with sam)
    - AWS DynamoDb Workbench, download from:
    https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/workbench.settingup.html

- You'll need to run a local instance of DynamoDb

- You'll need to run a local instance of AWS Api Gateway and Lambda Function.

- The Lambda function needs to connect to your local DynamoDb instance, 
  and for that they must run in the same network.

- Both instances run as a docker image. There're many ways to see each other, but you can create a docker network by running the follwing command:

"docker network create local-api-network" where local-api-network is the name of your network.

- To run a local instance of DynamoDb, run the following:
"docker run -d -p 8000:8000 --network=local-api-network --name dynamo-local amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb"

- Create your tables, by importing the model "URLS-workbench.json" with AWS DynamoDb Workbench. This file is located in the root directory of the repo.

- To run a local instance of your AWS function. Go to the directory of the function. E.g. /createshorturl. There you have a file named "template.yaml". You can build the AWS Function by running "sam build".

- to run your AWS API function run the following:

"sam local start-api --docker-network local-api-network". 

This tells to run your function in the already created docker network named "local-api-network". 

- You can optionally configure your AWS Functions to be integrated with a Kafka Instance. This instance must also be running in the same network as DynamoDb and your AWS API Function.

- 
