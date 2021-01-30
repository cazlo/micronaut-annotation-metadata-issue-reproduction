package example;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Factory
@Requires(classes = DynamoDB.class)
public class ExampleFactory {

    private static final String region = "us-east-1";

    private final AwsClientBuilder.EndpointConfiguration endpointConfiguration;

    @Inject
    public ExampleFactory(@Value("${dynamodb.config.endpoint}") final String endpoint) {
        this.endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }

    @Singleton
    public DynamoDBMapper dynamoDBMapper() {
        final AmazonDynamoDBAsync client = AmazonDynamoDBAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(this.endpointConfiguration)
                .build();
        final DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
                .build();
        return new DynamoDBMapper(client, config);
    }
}
