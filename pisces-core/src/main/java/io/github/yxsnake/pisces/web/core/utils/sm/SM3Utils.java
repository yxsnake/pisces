package io.github.yxsnake.pisces.web.core.utils.sm;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.legacy.math.linearalgebra.ByteUtils;

import java.io.UnsupportedEncodingException;

@Slf4j
public final class SM3Utils {

    /**
     * SM3加密
     * @param content  要加密的内容
     */
    public static String  encryptText(String content) throws UnsupportedEncodingException {
        byte[] bytes = content.getBytes();
        byte[] hash = hash(bytes);
        String sm3 = ByteUtils.toHexString(hash);
        return sm3;
    }

    public static byte[] hash(byte[] srcData){
        SM3Digest digest=new SM3Digest();
        digest.update(srcData,0,srcData.length);
        byte[] bytes = new byte[digest.getDigestSize()];
        digest.doFinal(bytes,0);
        return bytes;
    }
    /**
     * sm3算法通过密钥进行加密，不可逆加密
     * @param keyText 密钥字符串
     * @param plainText 需加密的明文字符串
     * @return 加密后固定长度64的16进制字符串
     */
    public static String encryptByKey(String keyText, String plainText) {
        return ByteUtils.toHexString(encryptByKey(keyText.getBytes(), plainText.getBytes()));
    }

    /**
     * sm3算法通过密钥进行加密，不可逆加密
     * @param keyByte 密钥数组
     * @param plainByte 需加密的明文数组
     * @return 加密后固定长度64的16进制数组
     */
    public static byte[] encryptByKey(byte[] keyByte, byte[] plainByte) {
        KeyParameter keyParameter = new KeyParameter(keyByte);
        SM3Digest sm3Digest = new SM3Digest();
        HMac hMac = new HMac(sm3Digest);
        hMac.init(keyParameter);
        hMac.update(plainByte, 0, plainByte.length);
        byte[] result = new byte[hMac.getMacSize()];
        hMac.doFinal(result, 0);
        return result;
    }
    /**
     * 主函数
     * @param args
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        String encryptText = encryptText("ceshi001");
        String encryptedByKey = encryptByKey("key001", "abc");
        log.info("encryptText:{}",encryptText);
        log.info("encryptedByKey:{}",encryptedByKey);
    }

}
