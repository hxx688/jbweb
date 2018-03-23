package com.lfgj.clinet.payHqf.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	private String algorithm = "MD5";
	private boolean encodeHashAsBase64 = false;
	private int iterations = 1;

	public String encode(String data, Object salt) {
		String saltedData = mergeDataAndSalt(data, salt, false);

		MessageDigest messageDigest = getMessageDigest();

		byte[] digest = messageDigest.digest(Utf8.encode(saltedData));

		// "stretch" the encoded value if configured to do so
		for (int i = 1; i < iterations; i++) {
			digest = messageDigest.digest(digest);
		}

		if (getEncodeHashAsBase64()) {
			return Utf8.decode(Base64.encode(digest));
		} else {
			return new String(Hex.encode(digest));
		}
	}

	private String mergeDataAndSalt(String data, Object salt, boolean strict) {
		if (data == null) {
			data = "";
		}

		if (strict && (salt != null)) {
			if ((salt.toString().lastIndexOf("{") != -1)
					|| (salt.toString().lastIndexOf("}") != -1)) {
				throw new IllegalArgumentException(
						"Cannot use { or } in salt.toString()");
			}
		}

		if ((salt == null) || "".equals(salt)) {
			return data;
		} else {
			return data + "{" + salt.toString() + "}";
		}
	}

	private final MessageDigest getMessageDigest()
			throws IllegalArgumentException {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
	}

	public boolean getEncodeHashAsBase64() {
		return encodeHashAsBase64;
	}

	public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
		this.encodeHashAsBase64 = encodeHashAsBase64;
	}
}
