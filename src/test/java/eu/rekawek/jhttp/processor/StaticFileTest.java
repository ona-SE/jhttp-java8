package eu.rekawek.jhttp.processor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

import eu.rekawek.jhttp.api.HttpRequest;
import eu.rekawek.jhttp.api.HttpResponse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StaticFileTest {

    private static final URL STATIC_FILE = StaticFileTest.class.getResource("/static-file/some-file.html");

    private ByteArrayOutputStream responseOutput;

    private HttpRequest request;

    private HttpResponse response;

    @Before
    public void setup() throws IOException, URISyntaxException {
        responseOutput = new ByteArrayOutputStream();

        request = mock(HttpRequest.class);
        when(request.resolvePath()).thenReturn(Path.of(STATIC_FILE.toURI()));

        response = mock(HttpResponse.class);
        when(response.getOutputStream()).thenReturn(responseOutput);
    }

    @Test
    public void testIndexHtml() throws IOException, URISyntaxException {
        assertTrue(new StaticFile().process(request, response));
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        Files.copy(Path.of(STATIC_FILE.toURI()), expected);
        assertEquals(new String(expected.toByteArray()), new String(responseOutput.toByteArray()));
    }

}
