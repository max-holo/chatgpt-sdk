package com.unfbx.chatgpt.utils;

import okhttp3.HttpUrl;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.Objects;

public class SparkAuthUtils {
    public static final DateTimeFormatter dateTimeFormatter;
    public static final String preStr = "host: %s\ndate: %s\nGET %s HTTP/1.1";

    public SparkAuthUtils() {
    }

    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws MalformedURLException, InvalidKeyException, NoSuchAlgorithmException {
        URL url = new URL(hostUrl);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        String date = now.format(dateTimeFormatter);
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        String format = String.format("host: %s\ndate: %s\nGET %s HTTP/1.1", url.getHost(), date, url.getPath());
        byte[] hexDigits = mac.doFinal(format.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = ((HttpUrl)Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath()))).newBuilder().addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).addQueryParameter("date", date).addQueryParameter("host", url.getHost()).build();
        String s = httpUrl.toString();
        return s;
    }

    static {
        dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    }
}
