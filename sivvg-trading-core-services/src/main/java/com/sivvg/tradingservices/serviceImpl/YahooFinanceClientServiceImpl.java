package com.sivvg.tradingservices.serviceImpl;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.service.YahooFinanceClient;

@Service
public class YahooFinanceClientServiceImpl implements YahooFinanceClient {

	private static final Logger logger = LoggerFactory.getLogger(YahooFinanceClientServiceImpl.class);

	@Value("${rapidapi.key}")
	private String apiKey;

	private static final String API_URL = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-quotes?region=IN&symbols=";

	private final HttpClient client = HttpClient.newHttpClient();

	@Override
	public double fetchCurrentPrice(String symbol) {

		logger.debug("Fetching live price from Yahoo | symbol={}", symbol);

		try {
			// 1️⃣ Encode symbol safely
			String encodedSymbol = URLEncoder.encode(symbol, StandardCharsets.UTF_8);

			// 2️⃣ Build URI
			URI uri = URI.create(API_URL + encodedSymbol);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(uri)
					.header("x-rapidapi-key", apiKey)
					.header("x-rapidapi-host", 
							"apidojo-yahoo-finance-v1.p.rapidapi.com")
					.GET()
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				logger.error("Yahoo API returned non-200 status | symbol={}, status={}", symbol, response.statusCode());
				return 0.0;
			}

			JSONObject json = new JSONObject(response.body());

			if (!json.has("quoteResponse")) {
				logger.warn("Invalid Yahoo response structure | symbol={}", symbol);
				return 0.0;
			}

			JSONArray resultArray = json.getJSONObject("quoteResponse").optJSONArray("result");

			if (resultArray == null || resultArray.length() == 0) {
                logger.warn("No quote data returned from Yahoo | symbol={}", symbol);
                return 0.0;
            }

			JSONObject quote = resultArray.getJSONObject(0);
			double price = quote.optDouble("regularMarketPrice", 0.0);

			logger.debug("Live price fetched successfully | symbol={}, price={}", symbol, price);

			return price;

		} catch (Exception e) {

			logger.error("Exception while fetching Yahoo price | symbol={}", symbol, e);
			return 0.0;
		}
	}
}
