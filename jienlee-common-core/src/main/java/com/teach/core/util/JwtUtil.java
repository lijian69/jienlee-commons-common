package com.teach.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * JwtUtil 工具类
 *
 * @author jien.lee
 */
public class JwtUtil {
    // 过期时间5分钟
    private static final long EXPIRE_TIME = 5 * 60 * 1000;


    public static String sign(String key, String secret) {
        return sign(key, secret, EXPIRE_TIME);
    }

    /**
     * 生成签名,5min后过期
     *
     * @param key        加密的报文
     * @param secret     用户的密码
     * @param expireTime 过期的时间
     * @return 加密的token
     */
    public static String sign(String key, String secret, long expireTime) {
        if (expireTime > 0) {
            expireTime = EXPIRE_TIME;
        }
        Date date = new Date(System.currentTimeMillis() + expireTime);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("key", key)
                .withClaim("as", "a")
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 检验相关参数的合法性
     *
     * @param token
     * @param key
     * @param secret
     * @return
     */
    public static boolean verify(String token, String key, String secret) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("key", key)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
