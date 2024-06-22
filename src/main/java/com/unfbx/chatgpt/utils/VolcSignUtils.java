package com.unfbx.chatgpt.utils;

import cn.hutool.json.JSONUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 火山方舟volc签名工具
 *
 * @author max
 * @date 2024/04/17 10:51
 */
@Slf4j
public class VolcSignUtils {

    // 过期时间；默认30分钟
    private static final long expireMillis = 30 * 60 * 1000L;

    // 缓存服务
    public static Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireMillis - (60 * 1000L), TimeUnit.MILLISECONDS)
            .build();

    private static final BitSet URLENCODER = new BitSet(256);

    private static final String CONST_ENCODE = "0123456789ABCDEF";
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    private final String region;
    private final String service;
    private final String schema;
    private final String host;
    private final String path;
    private final String ak;
    private final String sk;

    static {
        int i;
        for (i = 97; i <= 122; ++i) {
            URLENCODER.set(i);
        }

        for (i = 65; i <= 90; ++i) {
            URLENCODER.set(i);
        }

        for (i = 48; i <= 57; ++i) {
            URLENCODER.set(i);
        }
        URLENCODER.set('-');
        URLENCODER.set('_');
        URLENCODER.set('.');
        URLENCODER.set('~');
    }

    public VolcSignUtils(String region, String service, String schema, String host, String path, String ak, String sk) {
        this.region = region;
        this.service = service;
        this.host = host;
        this.schema = schema;
        this.path = path;
        this.ak = ak;
        this.sk = sk;
    }

    public Map<String,String> getSignature(String method, Map<String, String> queryList, byte[] body,
                             Date date, String action, String version) throws Exception {
        if (body == null) {
            body = new byte[0];
        }
        String xContentSha256 = hashSHA256(body);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String xDate = sdf.format(date);
        System.out.println("x-date:"+xDate);
        String shortXDate = xDate.substring(0, 8);
        String signHeader = "host;x-content-sha256;x-date";
        System.out.println("signHeader:"+signHeader);

        SortedMap<String, String> realQueryList = new TreeMap<>(queryList);
        realQueryList.put("Action", action);
        realQueryList.put("Version", version);
        StringBuilder querySB = new StringBuilder();
        for (String key : realQueryList.keySet()) {
            querySB.append(signStringEncoder(key)).append("=").append(signStringEncoder(realQueryList.get(key))).append("&");
        }
        querySB.deleteCharAt(querySB.length() - 1);

        String canonicalStringBuilder = method + "\n" + path + "\n" + querySB + "\n" +
                "host:" + host + "\n" +
                "x-content-sha256:" + xContentSha256 + "\n" +
                "x-date:" + xDate + "\n" +
                "\n" +
                signHeader + "\n" +
                xContentSha256;
        String hashcanonicalString = hashSHA256(canonicalStringBuilder.getBytes());
        String credentialScope = shortXDate + "/" + region + "/" + service + "/request";
        System.out.println("canonicalString:\n"+canonicalStringBuilder);
        String signString = "HMAC-SHA256" + "\n" + xDate + "\n" + credentialScope + "\n" + hashcanonicalString;
        System.out.println("signString:\n"+signString );

        byte[] signKey = genSigningSecretKeyV4(sk, shortXDate, region, service);
        byte[] bytes = hmacSHA256(signKey, signString);
        // 最终签名值
        StringBuffer signature = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < bytes.length; i++) {
            shaHex = Integer.toHexString(bytes[i] & 0xFF);
            if (shaHex.length() < 2) {
                signature.append(0);
            }
            signature.append(shaHex);
        }
        Map<String,String> map = new HashMap<>(3);
        map.put("credentialScope",credentialScope);
        map.put("signHeaders",signHeader);
        map.put("signature",signature.toString());
    return map;
//        URL url = new URL(schema + "://" + host + path + "?" + querySB);
//
//
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod(method);
//        conn.setRequestProperty("Host", host);
//        conn.setRequestProperty("X-Date", xDate);
//        conn.setRequestProperty("X-Content-Sha256", xContentSha256);
//        conn.setRequestProperty("Content-Type", contentType);
//        conn.setRequestProperty("Authorization", "HMAC-SHA256" +
//                " Credential=" + ak + "/" + credentialScope +
//                ", SignedHeaders=" + signHeader +
//                ", Signature=" + signature);
//        if (!Objects.equals(conn.getRequestMethod(), "GET")) {
//            conn.setDoOutput(true);
//            OutputStream os = conn.getOutputStream();
//            os.write(body);
//            os.flush();
//            os.close();
//        }
//        conn.connect();
//
//        int responseCode = conn.getResponseCode();
//
//        InputStream is;
//        if (responseCode == 200) {
//            is = conn.getInputStream();
//        } else {
//            is = conn.getErrorStream();
//        }
//        String responseBody = new String(is.readAllBytes());
//        is.close();
    }

    private String signStringEncoder(String source) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(source.length());
        ByteBuffer bb = UTF_8.encode(source);
        while (bb.hasRemaining()) {
            int b = bb.get() & 255;
            if (URLENCODER.get(b)) {
                buf.append((char) b);
            } else if (b == 32) {
                buf.append("%20");
            } else {
                buf.append("%");
                char hex1 = CONST_ENCODE.charAt(b >> 4);
                char hex2 = CONST_ENCODE.charAt(b & 15);
                buf.append(hex1);
                buf.append(hex2);
            }
        }

        return buf.toString();
    }

    public static String hashSHA256(byte[] content) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(content);
            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            throw new Exception(
                    "Unable to compute hash while signing request: "
                            + e.getMessage(), e);
        }
    }

    public static byte[] hmacSHA256(byte[] key, String content) throws Exception {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(content.getBytes());
        } catch (Exception e) {
            throw new Exception(
                    "Unable to calculate a request signature: "
                            + e.getMessage(), e);
        }
    }

    private byte[] genSigningSecretKeyV4(String secretKey, String date, String region, String service) throws Exception {
        byte[] kDate = hmacSHA256((secretKey).getBytes(), date);
        byte[] kRegion = hmacSHA256(kDate, region);
        byte[] kService = hmacSHA256(kRegion, service);
        return hmacSHA256(kService, "request");
    }


    public static void main(String[] args) throws Exception {
        String SecretAccessKey = "WmpFME1UTTRNekprTm1Wak5EbGlNV0ptT0dVNU5Ua3daR016T0RJeU9XVQ==";
        String AccessKeyID = "AKLTNDVjNTk3OGE0MDVlNDliOGEzNTM2ZjQwZjVjY2YxOTc";
        // 请求地址
        String host = "maas-api.ml-platform-cn-beijing.volces.com";
        // 路径，不包含 Query// 请求接口信息
        String path = "/api/v2/endpoint/ep-20240417143031-9hvqz/chat";
        String service = "ml_maas";
        String region = "cn-beijing";
        String schema = "https";
        VolcSignUtils sign = new VolcSignUtils(region, service, schema, host, path, AccessKeyID, SecretAccessKey);

        String action = "";
        String version = "";

        HashMap<String, String> queryMap = new HashMap<>();
        Date date = new Date(System.currentTimeMillis());
        String body = "{\"stream\": true,\"parameters\": {\"max_new_tokens\": 1024,\"temperature\": 0.9},\"messages\": [{\"role\": \"user\",\"content\": \"你好\"},{\"role\": \"assistant\",\"content\": \"你好，有什么可以帮助您？\"},{\"role\": \"user\",\"content\": \"你可以做些什么？\"}]}";
        Map<String, String> stringStringMap = sign.getSignature("POST", queryMap, body.getBytes(StandardCharsets.UTF_8), date, action, version);
        System.out.println(JSONUtil.toJsonStr(stringStringMap));
    }

}
