package com.hanix.myapplication.common.utils;



import com.hanix.myapplication.common.app.GLog;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 암복호화 유틸
 */
public class SecretUtils {

    private static String keyDataDecodeStr = null;
    private static String ivDataDecodeStr = null;

    private static byte[] keyData = null;
    private static byte[] iv = null;

    public static void setKeyDataDecodeStr(String decodeStr) {
        keyDataDecodeStr = Utils.byteArrayToHexString(Utils.addPaddingStr(decodeStr, 16).getBytes());
        keyData = Hex.decode(keyDataDecodeStr);
    }

    public static void setIvDataDecodeStr(String decodeStr) {
        ivDataDecodeStr = Utils.byteArrayToHexString(Utils.addPaddingStr(decodeStr, 16).getBytes());
        iv = Hex.decode(ivDataDecodeStr);
    }

    /** 복호화 **/
    public static String getDecStr(String encStr) {
        String decStr = "";
        try {
            Security.addProvider(new BouncyCastleProvider());
            SecretKey secureKey = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv));
            byte[] enc = Base64.decodeBase64(encStr.getBytes());
            decStr = new String(c.doFinal(enc), "utf-8");
        } catch ( Exception e) {
            GLog.e(e.getMessage(), e);
        }
        return decStr;
    }


    /** 암호화 **/
    public static String getEncStr(String plainStr) {
        String encStr = "";
        try  {
            Security.addProvider(new BouncyCastleProvider());
            SecretKey secureKey = new SecretKeySpec(keyData,"AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding", BouncyCastleProvider.PROVIDER_NAME);
            c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv));
            byte[] results = c.doFinal(plainStr.getBytes());
            encStr = new String(Base64.encodeBase64(results), "utf-8");
        } catch (Exception e) {
            GLog.e(e.getMessage(), e);
        }
        return encStr;
    }
}
