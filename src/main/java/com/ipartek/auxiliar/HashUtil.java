package com.ipartek.auxiliar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class HashUtil {

	public static String generarSalt() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String sha256(String texto) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error calculando SHA-256", e);
		}
	}

	public static String calcularHashFinal(String hashCliente, String salt) {
		return sha256(hashCliente + salt);
	}
}
