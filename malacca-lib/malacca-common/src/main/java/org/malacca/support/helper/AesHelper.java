package org.malacca.support.helper;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;
import org.malacca.support.util.HexKit;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * AES对称加密和解密
 */
public class AesHelper {

    private final static String ALGORITHM = "AES";
    private final static String CHARSET = "utf-8";

    private AesMode mode;
    private Integer keyLength;
    private String iv;
    private OutputType outputType;
    private String password;

    //"算法/模式/补码方式"
    private String cipherInstance = "AES/ECB/PKCS5Padding";

    /**
     * @param mode       AES加密模式
     * @param badPadding 填充（秘钥长度不是128时，需要填充）
     *                   //     * @param keyLength  秘钥长度
     * @param iv         偏移量，ECB模式不用
     * @param password   密码
     * @param outputType 输出模式
     */
    public AesHelper(AesMode mode, BadPadding badPadding, String iv, String password, OutputType outputType) {
        this.cipherInstance = "AES/" + mode.name() + "/" + badPadding.name();
        this.mode = mode;
//        this.keyLength = keyLength.getValue();
        this.iv = iv;
        this.password = password;
        this.outputType = outputType;
    }

    /**
     * 加密
     *
     * @param data 明文
     * @return 密文
     */
    public String encrypt(String data) throws Exception {
        SecretKeySpec keyspec = generateKeySpec();
        Cipher cipher = Cipher.getInstance(cipherInstance);
        if (AesMode.ECB.equals(mode)) {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
        } else {
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes(CHARSET));
            //初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        }
        byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
        if (OutputType.Hex.equals(outputType)) {
            return HexKit.byte2HexStr(encrypted);
        } else if (OutputType.Base64.equals(outputType)) {
            return Base64.getEncoder().encodeToString(encrypted);
        }
        return data;
    }

    /*
     * 解密
     *
     * @param data 密文
     * @return 明文
     */
    public String decrypt(String data) throws Exception {
        // 根据字节数组生成AES密钥
        SecretKey keyspec = generateKeySpec();

        // 根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance(cipherInstance);
        if (AesMode.ECB.equals(mode)) {
            cipher.init(Cipher.DECRYPT_MODE, keyspec);
        } else {
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        }
        // 将加密并编码后的内容解码成字节数组
        byte[] byteContent;
        if (OutputType.Hex.equals(outputType)) {
            byteContent = HexKit.hexStr2Byte(data);
        } else if (OutputType.Base64.equals(outputType)) {
            byteContent = Base64.getDecoder().decode(data);
        } else {
            return data;
        }
        byte[] byteDecode = cipher.doFinal(byteContent);
        return new String(byteDecode, CHARSET);
    }

    /**
     * 获得密钥
     */
    private SecretKeySpec generateKeySpec() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        /*
        //防止linux下 随机生成key
        Provider provider = Security.getProvider("SUN");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", provider);
        secureRandom.setSeed(secretKey.getBytes(CHARSET));
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
        keygen.init(keyLength, secureRandom);
        // 生成密钥原始对称密钥
        SecretKey originalKey = keygen.generateKey();
        // 获得原始对称密钥的字节数组
        byte[] raw = originalKey.getEncoded();
        SecretKeySpec keySpec = new SecretKeySpec(raw, ALGORITHM);
        */
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(CHARSET), ALGORITHM);
        return keySpec;
    }

    public enum AesMode {
        ECB("ECB"), CBC("CBC"), CTR("CTR"), OFB("OFB"), CFB("CFB");

        private String code;

        AesMode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public enum BadPadding {
        NoPadding("NoPadding"),  // 明文必须是16个字节的整数倍
        PKCS5Padding("PKCS5Padding"), // 默认，在明文的末尾进行填充，填充的数据时当前和16个字节相差的数据，最后一个字节肯定为填充数据的长度(？)
        PKCS7Padding("PKCS7Padding"), // 同PKCS5Padding
        ISO10126Padding("ISO10126Padding"); // 在明文的末尾进行填充，当前和16个字节相差的数量填写在最后，其余字节填充随机数

        private String name;

        BadPadding(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum OutputType {
        Base64("Base64"), Hex("Hex");

        private String code;

        OutputType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public enum KeyLength {
        Len128(128), Len192(192), Len256(256);

        private Integer value;

        KeyLength(Integer len) {
            this.value = len;
        }

        public Integer getValue() {
            return value;
        }
    }

}
