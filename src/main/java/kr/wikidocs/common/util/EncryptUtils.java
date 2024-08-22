package kr.wikidocs.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES256 방식을 이용한 대칭키 암호화 함수
 */
@Slf4j
public class EncryptUtils {

	private static final String DEFAULT_KEY = "EDLQCcAFYFlLstCq";
	private static final String DEFAULT_IV = "hzRJCEqqJRY15F==";

	private static final String AES256_PKCS5 = "AES/CBC/PKCS5Padding";
	private static final String AES256_PKCS7 = "AES/CBC/PKCS7Padding";

	public static EncryptUtils instance = null;

	/**
	 * 인트턴스 생성
	 * @return
	 */
	public static EncryptUtils getInstance() {
		if (instance == null) {
			synchronized (EncryptUtils.class) {
				if (instance == null)
					instance = new EncryptUtils();
			}
		}
		return instance;
	}

	/**
	 * 키 스펙
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKeySpec getScretKeySpec(final String key) throws NoSuchAlgorithmException {

		byte[] seed = key.getBytes();
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(seed);

		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128, random);

		SecretKey secretKey = generator.generateKey();
		return new SecretKeySpec(secretKey.getEncoded(), "AES");
	}

	/**
	 * base64 암호화
	 * @param text
	 * @param kscs
	 * @return
	 * @throws Exception
	 */
	public static String encryptBase64(final String text) throws Exception{

		byte[] textBytes = null;
		String encryptString = null;

		try{

			textBytes = text.getBytes(StandardCharsets.UTF_8);
			encryptString = Base64.encodeBase64String(textBytes);

		}catch(Exception e){
			log.info("EncryptUtils encryptAES256 ERROR ::: [{}]", e);
		}


		return encryptString;
	}

	/**
	 * base64 복호화
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String decriptBase64(final String text) throws Exception{


		byte[] textBytes = null;
		String decriptString = null;

		try{

			textBytes = Base64.decodeBase64(text);
			decriptString = new String(textBytes, StandardCharsets.UTF_8);

		}catch(Exception e){
			log.info("EncryptUtils decriptAES256 ERROR ::: [{}]", e);
		}

		return decriptString;
	}

	/**
	 * AES128 암호화
	 * @param text
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String encryptAES128(final String text) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec keySpec = getScretKeySpec(DEFAULT_KEY);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		return Hex.encodeHexString(cipher.doFinal(text.getBytes()));
	}

	/**
	 * AES128 암호화
	 * @param key
	 * @param text
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String encryptAES128(final String key, final String text) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec keySpec = getScretKeySpec(key);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);
		return Hex.encodeHexString(cipher.doFinal(text.getBytes()));
	}

	/**
	 * AES128 복호화
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws DecoderException
	 */
	public static String decriptAES128(final String text) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, DecoderException {
		SecretKeySpec keySpec = getScretKeySpec(DEFAULT_KEY);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		return new String(cipher.doFinal(Hex.decodeHex(text.toCharArray())));
	}

	/**
	 * AES128 복호화
	 * @param key
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws DecoderException
	 */
	public static String decriptAES128(final String key, final String text) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, DecoderException {
		SecretKeySpec keySpec = getScretKeySpec(key);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		return new String(cipher.doFinal(Hex.decodeHex(text.toCharArray())));
	}

	/**
	 * AES -> base64 암호화
	 * @param text
	 * @param kscs
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES256(final String text) throws Exception{

		byte[] reBytes = new byte[32];
		byte[] keyBytes = DEFAULT_KEY.getBytes("UTF-8");

		int keyBytesLength = keyBytes.length;
		if(keyBytesLength > reBytes.length){
			keyBytesLength = reBytes.length;
		}

		System.arraycopy(keyBytes, 0, reBytes, 0, keyBytesLength);

		byte[] textBytes = null;
		AlgorithmParameterSpec ivSpec = null;
		SecretKeySpec secretKeySpec = null;
		Cipher cipher = null;

		try{

			textBytes = text.getBytes(StandardCharsets.UTF_8);
			secretKeySpec = new SecretKeySpec(reBytes, "AES");
			ivSpec = new IvParameterSpec(DEFAULT_IV.getBytes(StandardCharsets.UTF_8));
			cipher = Cipher.getInstance(AES256_PKCS7, "BC");

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

		}catch(Exception e){
			log.info("EncryptUtils encryptAES256 ERROR ::: [{}]", e);
		}


		return Base64.encodeBase64String(cipher.doFinal(textBytes));
	}

	/**
	 * AES -> base64 암호화
	 * @param text
	 * @param kscs
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES256(final String text, String kscs) throws Exception{

		byte[] reBytes = new byte[32];
		byte[] keyBytes = DEFAULT_KEY.getBytes("UTF-8");

		int keyBytesLength = keyBytes.length;
		if(keyBytesLength > reBytes.length){
			keyBytesLength = reBytes.length;
		}

		System.arraycopy(keyBytes, 0, reBytes, 0, keyBytesLength);

		byte[] textBytes = null;
		AlgorithmParameterSpec ivSpec = null;
		SecretKeySpec secretKeySpec = null;
		Cipher cipher = null;

		try{

			textBytes = text.getBytes(StandardCharsets.UTF_8);
			secretKeySpec = new SecretKeySpec(reBytes, "AES");
			ivSpec = new IvParameterSpec(DEFAULT_IV.getBytes(StandardCharsets.UTF_8));
			if("5".equals(kscs)) {
				cipher = Cipher.getInstance(AES256_PKCS5);
			}else if("7".equals(kscs)) {
				cipher = Cipher.getInstance(AES256_PKCS7, "BC");
			}

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

		}catch(Exception e){
			log.info("EncryptUtils encryptAES256 ERROR ::: [{}]", e);
		}


		return Base64.encodeBase64String(cipher.doFinal(textBytes));
	}



	/**
	 * base64 -> AES 복호화
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String decriptAES256(final String text) throws Exception{

		byte[] reBytes = new byte[32];
		byte[] keyBytes = DEFAULT_KEY.getBytes("UTF-8");

		int keyBytesLength = keyBytes.length;
		if(keyBytesLength > reBytes.length){
			keyBytesLength = reBytes.length;
		}

		System.arraycopy(keyBytes, 0, reBytes, 0, keyBytesLength);

		byte[] textBytes = null;
		SecretKeySpec secretKeySpec = null;
		AlgorithmParameterSpec ivSpec = null;
		Cipher cipher = null;

		try{

			textBytes = Base64.decodeBase64(text);
			secretKeySpec = new SecretKeySpec(reBytes, "AES");
			ivSpec = new IvParameterSpec(DEFAULT_IV.getBytes(StandardCharsets.UTF_8));
			cipher = Cipher.getInstance(AES256_PKCS7, "BC");

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

		}catch(Exception e){
			log.info("EncryptUtils decriptAES256 ERROR ::: [{}]", e);
		}

		return new String(cipher.doFinal(textBytes), StandardCharsets.UTF_8);
	}

	/**
	 * base64 -> AES 복호화
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public static String decriptAES256(final String text, String kscs) throws Exception{

		byte[] reBytes = new byte[32];
		byte[] keyBytes = DEFAULT_KEY.getBytes("UTF-8");

		int keyBytesLength = keyBytes.length;
		if(keyBytesLength > reBytes.length){
			keyBytesLength = reBytes.length;
		}

		System.arraycopy(keyBytes, 0, reBytes, 0, keyBytesLength);

		byte[] textBytes = null;
		SecretKeySpec secretKeySpec = null;
		AlgorithmParameterSpec ivSpec = null;
		Cipher cipher = null;

		try{

			textBytes = Base64.decodeBase64(text);
			secretKeySpec = new SecretKeySpec(reBytes, "AES");
			ivSpec = new IvParameterSpec(DEFAULT_IV.getBytes(StandardCharsets.UTF_8));

			if("5".equals(kscs)) {
				cipher = Cipher.getInstance(AES256_PKCS5);
			}else if("7".equals(kscs)) {
				cipher = Cipher.getInstance(AES256_PKCS7, "BC");
			}

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

		}catch(Exception e){
			log.info("EncryptUtils decriptAES256 ERROR ::: [{}]", e);
		}

		return new String(cipher.doFinal(textBytes), StandardCharsets.UTF_8);
	}


	/**
	 * AES -> base64 암호화
	 * @param text
	 * @param kscs
	 * @param  skeys
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES256(final String text, String kscs, String skeys) throws Exception{

		byte[] textBytes = null;
		AlgorithmParameterSpec ivSpec = null;
		SecretKeySpec secretKeySpec = null;
		Cipher cipher = null;

		try{

			textBytes = text.getBytes(StandardCharsets.UTF_8);
			secretKeySpec = new SecretKeySpec(skeys.getBytes(), "AES");
			String iv = skeys.substring(0,16);
			ivSpec = new IvParameterSpec(iv.getBytes());
			if("5".equals(kscs)) {
				cipher = Cipher.getInstance(AES256_PKCS5);
			}else if("7".equals(kscs)) {
				cipher = Cipher.getInstance(AES256_PKCS7, "BC");
			}

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

		}catch(Exception e){
			log.info("EncryptUtils encryptAES256 ERROR ::: [{}]", e);
		}


		return Base64.encodeBase64String(cipher.doFinal(textBytes));
	}


	/**
	 * base64 -> AES 복호화
	 * @param text
	 * @param kscs
	 * @param  skeys
	 * @return
	 * @throws Exception
	 */
	public static String decriptAES256(final String text, String kscs, String skeys) throws Exception{

		byte[] textBytes = null;
		SecretKeySpec secretKeySpec = null;
		AlgorithmParameterSpec ivSpec = null;
		Cipher cipher = null;

		try{

			textBytes = Base64.decodeBase64(text);
			secretKeySpec = new SecretKeySpec(skeys.getBytes(), "AES");
			String iv = skeys.substring(0,16);
			ivSpec = new IvParameterSpec(iv.getBytes());

			if("5".equals(kscs)) {
				cipher = Cipher.getInstance(AES256_PKCS5);
			}else if("7".equals(kscs)) {
				cipher = Cipher.getInstance(AES256_PKCS7, "BC");
			}

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

		}catch(Exception e){
			log.info("EncryptUtils decriptAES256 ERROR ::: [{}]", e);
		}

		return new String(cipher.doFinal(textBytes), StandardCharsets.UTF_8);
	}

	public static String hget(String message, String key) throws UnsupportedEncodingException {
        try {
            // hash 알고리즘과 암호화 key 적용
            Mac hasher = Mac.getInstance("HmacSHA1");
            hasher.init(new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1"));

            // messages를 암호화 적용 후 byte 배열 형태의 결과 리턴
            byte[] hash = hasher.doFinal(message.getBytes("UTF-8"));

            return toHexString(hash).replaceAll("-", "");
            //return byteToString(hash);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        catch (InvalidKeyException e){
            e.printStackTrace();
        }
        return "";
    }


    public static String toHexString(byte[] data) {
        StringBuffer buffer = new StringBuffer();

        for (byte b : data) {
            buffer.append(String.format("%02X", b&0xff));
        }

        return buffer.toString().trim();
    }


	public static String propertyDecrypt128(String skeys, String property) {
		if(property.startsWith("$ENC:")) {
			property=property.replace("$ENC:", "");
			try {
				property=decriptAES128(skeys, property);
			} catch (Exception e) {
				log.error("{} 프로퍼티 복호화 도중에 에러 발생, 에러 상세[{}]", property, e.getMessage());
				return property;
			}
		}
		return property;
	}

	public static String propertyDecrypt128(String property){
		return propertyDecrypt128(DEFAULT_KEY, property);
	}

	/*
	public static void main(String[] args) throws Exception {
		if(args.length >0) {
			//String text=args[0] ==null?"":args[0];
			String text = "";
			String eText = "";
			String tp = args[0] == null?"":args[0];
			if (tp.equals("en")) {
				text = args[1];
				eText=EncryptUtils.encryptAES128(EncryptUtils.DEFAULT_KEY ,text );
			}
			else {
				eText = args[1];
				text = EncryptUtils.decriptAES128(eText);
			}
			//String eText=EncryptUtils.encryptAES128(EncryptUtils.DEFAULT_KEY ,text );
			log.info("암호화 전:[{}]\n 암호화 후:[{}]", text, eText);
		}else {
			log.warn("Need 1 Parameter, Encrypt Target Text");
			System.exit(0);
		}
	}*/
}
