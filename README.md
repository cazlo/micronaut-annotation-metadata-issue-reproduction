# micronaut-annotation-metadata-issue-reproduction
Attempting to reproduce an issue with non-deterministic AnnotationMetadata generation with Micronaut

This is basically a copy of the hello-world-java example from https://github.com/micronaut-projects/micronaut-examples

## demonstrating non-deterministic AnnotationMetadata generation

```shell
./gradlew clean build && cp build/libs/example-0.1-all.jar firstRun.jar  
./gradlew clean build && cp build/libs/example-0.1-all.jar secondRun.jar
cmp --silent firstRun.jar secondRun.jar || echo "Generated JAR is different for exact same code state"
```

To investigate the differences I compared the 2 jars in Intellij
There are differences in 2 classfiles:

`$$HelloControllerDefinition$InterceptedDefinitionClass.class`

| first generated jar | second generated jar |
| --- | --- |
| `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_3(), AnnotationUtil.internMapOf(new Object[]{"produces", new String[]{"application/json"}, "value", "/", "consumes", new String[]{"application/json"}}));` | `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_3(), AnnotationUtil.internMapOf(new Object[]{"produces", new String[]{"application/json"}, "consumes", new String[]{"application/json"}, "value", "/"}));` |
| `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_12(), AnnotationUtil.internMapOf(new Object[]{"uris", new String[]{"/"}, "value", "/"}));` | `        DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_12(), AnnotationUtil.internMapOf(new Object[]{"value", "/", "uris", new String[]{"/"}}));` |
| `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_13(), AnnotationUtil.internMapOf(new Object[]{"consumes", new Object[0], "produces", new Object[0], "single", false, "headRoute", true, "value", "/", "uri", "/", "processes", new Object[0], "uris", new String[]{"/"}}));` | `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_13(), AnnotationUtil.internMapOf(new Object[]{"uri", "/", "single", false, "headRoute", true, "value", "/", "processes", new Object[0], "produces", new Object[0], "uris", new String[]{"/"}, "consumes", new Object[0]}));` |

The only difference between the 2 class files is the order of the new Object[]. The order seems to have no impact here because the behavior of both jars is identical.


`$HelloControllerDefinitionClass.class`

| first generated jar | second generated jar |
| --- | --- |
| `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_3(), AnnotationUtil.internMapOf(new Object[]{"produces", new String[]{"application/json"}, "value", "/", "consumes", new String[]{"application/json"}}));` | `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_3(), AnnotationUtil.internMapOf(new Object[]{"produces", new String[]{"application/json"}, "consumes", new String[]{"application/json"}, "value", "/"}));` |
| `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_7(), AnnotationUtil.internMapOf(new Object[]{"uris", new String[]{"/"}, "value", "/"}));` | `DefaultAnnotationMetadata.registerAnnotationDefaults($micronaut_load_class_value_7(), AnnotationUtil.internMapOf(new Object[]{"value", "/", "uris", new String[]{"/"}}));` |

Again the only difference between the 2 class files is the order of the new Object[]. The order seems to have no impact here because the behavior of both jars is identical.