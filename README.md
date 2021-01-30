# micronaut-annotation-metadata-issue-reproduction
Attempting to reproduce an issue with non-deterministic AnnotationMetadata generation with Micronaut

This is basically a copy of the hello-world-java example from https://github.com/micronaut-projects/micronaut-examples

I added on a few services, injected dynamodb stuff, added on some lombok stuff too.

The combination we have here seems to be the minimum reproduction necessary for this issue, although I haven't done a huge amount of testing here to nail it down more specifically.

I was unable to reproduce the issue when removing the factory.

## demonstrating non-deterministic BeanDefinitionReference generation

*NOTE: due to the non-deterministic nature of this issue you may need to run this a few times!*

```shell
./gradlew clean build && cp build/libs/example-0.1-all.jar firstRun.jar  
./gradlew clean build && cp build/libs/example-0.1-all.jar secondRun.jar
unzip -p firstRun.jar META-INF/services/io.micronaut.inject.BeanDefinitionReference	> firstRunBeanDefinitionReference 
unzip -p secondRun.jar META-INF/services/io.micronaut.inject.BeanDefinitionReference > secondRunBeanDefinitionReference
cmp --silent firstRunBeanDefinitionReference secondRunBeanDefinitionReference || echo "Generated JAR is different for exact same code state"
```

To investigate the differences I compared the 2 jars in Intellij
There are differences only in the `META-INF/services/io.micronaut.inject.BeanDefinitionReference` file

Snippet from First Run:
```shell
example.$$HelloController3Definition$InterceptedDefinitionClass
example.$ExampleFactoryDefinitionClass
example.$HelloController2DefinitionClass
example.$ExampleFactory$DynamoDBMapper0DefinitionClass
example.$HelloController3DefinitionClass
example.$$HelloController2Definition$InterceptedDefinitionClass
io.micronaut.http.client.netty.$RxNettyHttpClientRegistryDefinitionClass
```

Second Run:
```shell
example.$$HelloController3Definition$InterceptedDefinitionClass
example.$ExampleFactoryDefinitionClass
example.$ExampleFactory$DynamoDBMapper0DefinitionClass
example.$HelloController2DefinitionClass
example.$HelloController3DefinitionClass
example.$$HelloController2Definition$InterceptedDefinitionClass
io.micronaut.http.client.netty.$RxNettyHttpClientRegistryDefinitionClass
```

The only difference is the order of classes here.