package example;

import io.micronaut.http.HttpResponseFactory;
import io.micronaut.http.HttpStatus;
import lombok.RequiredArgsConstructor;

import java.net.http.HttpResponse;

@RequiredArgsConstructor
public abstract class AbstractController {

    private final String resource;

    protected String generatePath(final String id) {
        return id;
    }

    HttpResponse buildResponse(final String string) {
        return (HttpResponse) HttpResponseFactory.INSTANCE.status(HttpStatus.OK).body(string);
    }
}
