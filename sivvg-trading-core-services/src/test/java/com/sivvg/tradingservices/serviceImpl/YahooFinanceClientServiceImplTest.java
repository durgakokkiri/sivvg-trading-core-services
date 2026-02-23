package com.sivvg.tradingservices.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class YahooFinanceClientServiceImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @SuppressWarnings("unchecked")
    @Test
    public  void fetchCurrentPrice_shouldReturnPrice_whenResponseIsValid() throws Exception {

        String jsonResponse = """
            {
              "quoteResponse": {
                "result": [
                  {
                    "regularMarketPrice": 1450.75
                  }
                ]
              }
            }
        """;

        when(httpResponse.body()).thenReturn(jsonResponse);
        when(httpResponse.statusCode()).thenReturn(200);  // 🔥 MUST

        when(httpClient.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)
        )).thenReturn(httpResponse);

        YahooFinanceClientServiceImpl service =
                new YahooFinanceClientServiceImpl();

        ReflectionTestUtils.setField(service, "client", httpClient);
        ReflectionTestUtils.setField(service, "apiKey", "dummy-key");

        double price = service.fetchCurrentPrice("INFY");

        assertThat(price).isEqualTo(1450.75);
    }

    // ✅ EXCEPTION CASE
    @SuppressWarnings("unchecked")
	@Test
	public  void fetchCurrentPrice_shouldReturnZero_whenExceptionOccurs() throws Exception {

        when(httpClient.send(
                any(HttpRequest.class),
                any(HttpResponse.BodyHandler.class)
        )).thenThrow(new RuntimeException("API error"));

        YahooFinanceClientServiceImpl service =
                new YahooFinanceClientServiceImpl();

        ReflectionTestUtils.setField(service, "client", httpClient);
        ReflectionTestUtils.setField(service, "apiKey", "dummy-key");

        double price = service.fetchCurrentPrice("INFY");

        assertThat(price).isEqualTo(0.0);
    }
}
