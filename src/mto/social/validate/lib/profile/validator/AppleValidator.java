/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.social.validate.lib.profile.validator;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mto.social.validate.lib.profile.AppleProfile;
import mto.social.validate.lib.type.AppleGrantType;
import mto.social.validate.lib.utils.CommonUtils;
import org.apache.commons.net.util.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author huuloc.tran89
 */
public class AppleValidator extends AbstractCallHttp {

    private static final NetHttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();

    private static final Logger logger = Logger.getLogger(AppleValidator.class);

    private static String createAppleClientSecret(String appleKey, String appleKeyID, String teamID, String appleAudience, String appleClientID) {
        try {
            byte[] keyBytes = Base64.decodeBase64(appleKey); // text key đã remove header, footer & new line
            final KeyFactory keyFactory = KeyFactory.getInstance("EC");
            final PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

            return Jwts.builder()
                    .setHeaderParam("kid", appleKeyID)
                    .setIssuer(teamID)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + (3600L * 1000L)))
                    .setAudience(appleAudience)
                    .claim("sub", appleClientID)
                    .signWith(SignatureAlgorithm.ES256, privateKey)
                    .compact();
        } catch (Exception ex) {
            System.out.println("ex:  " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    private static AppleProfile getProfileFromIdToken(String gameID, String idToken, String rsaN, String rsaE) {
        try {
            // gen public key từ n, e
            RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(new BigInteger(1, Base64.decodeBase64(rsaN)),
                    new BigInteger(1, Base64.decodeBase64(rsaE)));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(pubSpec);

            Jws<Claims> claims = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(idToken);
            String sub = claims.getBody().get("sub").toString();

            JSONObject publicProfile = new JSONObject();
            Set<Map.Entry<String, Object>> entrySet = claims.getBody().entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                publicProfile.put(entry.getKey(), entry.getValue());
            }

            AppleProfile appleProfile = new AppleProfile(0, gameID, sub);
            appleProfile.setPublicProfile(publicProfile);
            return appleProfile;

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
    
    public static AppleAuthTokenResp validateToken(String clientID, String clientSecret, String oauthCode, String grantType){
        try {
            String url = "https://appleid.apple.com/auth/token";
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("client_id", clientID));
            params.add(new BasicNameValuePair("client_secret", clientSecret));
            params.add(new BasicNameValuePair("code", oauthCode));
            params.add(new BasicNameValuePair("grant_type", grantType));
            String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8.name());

            String rs = doHttp(url, "POST", query);
            logger.info("validateAppleAuth req - url: " + url + " - query:" + query + " rs: " + rs);
            return CommonUtils.jsonToObject(rs, AppleAuthTokenResp.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static AppleProfile validateAppleAuth(
            String clientID,
            String appleKey,
            String appleKeyID,
            String teamID,
            String appleAudience,
            String gameID,
            String oauthCode,
            String n,
            String e,
            AppleGrantType appleGrantType) {
        try {
            String clientSecret = createAppleClientSecret(appleKey, appleKeyID, teamID, appleAudience, clientID);
            if (Strings.isNullOrEmpty(clientSecret)) {
                logger.error("createAppleClientSecret return null");
                return null;
            }
            //
            String url = "https://appleid.apple.com/auth/token";
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("client_id", clientID));
            params.add(new BasicNameValuePair("client_secret", clientSecret));
            params.add(new BasicNameValuePair("code", oauthCode));
            params.add(new BasicNameValuePair("grant_type", appleGrantType.getType()));
            String query = URLEncodedUtils.format(params, StandardCharsets.UTF_8.name());

            String rs = doHttp(url, "POST", query);
            logger.info("validateAppleAuth req - url: " + url + " - query:" + query + " rs: " + rs);
            //
            AppleAuthTokenResp token = CommonUtils.jsonToObject(rs, AppleAuthTokenResp.class);
            if (token == null || Strings.isNullOrEmpty(token.idToken)) {
                logger.error("Invalid AppleAuthTokenResp - token: " + CommonUtils.objectToString(token));
                return null;
            }
            //
            AppleProfile appleProfile = getProfileFromIdToken(gameID, token.idToken, n, e);
            appleProfile.accessToken = token.accessToken;
            appleProfile.refreshToken = token.refreshToken;

            //
            return appleProfile;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    public static class AppleAuthTokenResp {

        @SerializedName("access_token")
        public String accessToken;
        @SerializedName("token_type")
        public String tokenType;
        @SerializedName("expires_in")
        public String expiresIn;
        @SerializedName("refresh_token")
        public String refreshToken;
        @SerializedName("id_token")
        public String idToken;
    }
}
