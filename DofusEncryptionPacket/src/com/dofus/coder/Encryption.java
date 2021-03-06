package com.dofus.coder;

/**
 * Permet de crypter et décrypter les packets dofus envoyés avec le packet AKXYY
 * X = une valeur entre 0-9 ou A-F
 * YY = la valeur X en hexadecimal
 * @author Manghao
 * @version 1.0
 * */
public class Encryption {

	private String[] keys;
	private int currentKey;
	private String HEX_CHARS[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
	
	public Encryption(String packet) {
		if (packet.substring(0, 2).compareTo("AK") == 0) this.onKey(packet.substring(2));
		if (packet.substring(0, 2).compareTo("AT") == 0) this.onKey(packet.substring(3));
	}
	
	public void setKey(String packet) {
		if (packet.substring(0, 2).compareTo("AK") == 0) this.onKey(packet.substring(2));
		if (packet.substring(0, 2).compareTo("AT") == 0) this.onKey(packet.substring(3));
	}
	
	private void onKey(String data) { 
		int i = Integer.parseInt(data.substring(0, 1), 16);
		String str = data.substring(1);
		this.addKeyToCollection(i, str);
		this.startUsingKey(i);
	}
	
	private void addKeyToCollection(int keyId, String key) {
		if (this.keys == null) {
			this.keys = new String[keyId + 1];
		}
		this.keys[keyId] = this.prepareKey(key);
	}

	private void startUsingKey(int keyId) {
		this.currentKey = keyId;
	}
	
	private String prepareKey(String d) {
		String str = new String();
		for (int i = 0; i < d.length(); i = i + 2) {
			str = str + (char)Integer.parseInt(d.substring(i, (d.length() % 2 == 0) ? i + 2 : i + 1), 16);
		}
		return str;
	}
	
	private String d2h(int d) {
		if (d > 255) d = 255;
		return this.HEX_CHARS[(int) Math.floor(d / 16)] + this.HEX_CHARS[d % 16];
	}

	private String preEscape(String s) {
		String str = new String();
		int i = 0;
		while (i < s.length()) {
			String ope = Character.toString(s.charAt(i));
			int j = (int)s.charAt(i);
			if (j < 32 || (j > 127 || (ope == "%" || ope == "+")))
			{
				str = str + ope.replaceAll("%", "").replaceAll("+", "");
				continue;
			}
			str = str + ope;
			i++;
		}
		return str;
	}

	private String checksum(String s) {
		int j = 0;
		for (int i = 0; i < s.length(); i++) {
			j = j + (((int) s.charAt(i)) % 16);
		}
		return this.HEX_CHARS[j % 16];
	}
	
	public String prepareData(String s) {
		if (this.currentKey == 0 || this.currentKey < 0) return s;
		if (this.keys[this.currentKey] == null) return s;
		String str = HEX_CHARS[this.currentKey];
		String checkum = checksum(s);
		str = str + checkum;
		return str + this.cypherData(s, this.keys[this.currentKey], Integer.parseInt(checkum, 16) * 2);
	}

	private String cypherData(String d, String k, int c) {
		String str = new String();
		int j = k.length();
		d = preEscape(d);
		for (int i = 0; i < d.length(); i++) {
			str = str + this.d2h((int)d.charAt(i) ^ (int)k.charAt((i + c) % j));
		}
		return str;
	}

	public String unprepareData(String s) {	
		if (this.currentKey == 0) return s;
		String str = this.keys[Integer.parseInt(s.substring(0, 1), 16)];
		if (str == null) return s;
		String sub = s.substring(1, 2).toUpperCase();
		String decypherDaga = this.decypherData(s.substring(2), str, Integer.parseInt(sub, 16) * 2);
		//if (checksum(decypherDaga).compareTo(sub) != 0) return s;
		return decypherDaga;
	}

	private String decypherData(String d, String k, int c) {		
		String str = new String();
		int j = k.length();
		int l = 0;
		for (int i = 0; i < d.length(); i = i + 2) {
			str = str + Character.toString((char) (Integer.parseInt(d.substring(i, i + 2), 16) ^ (int)k.charAt((l++ + c) % j)));
		}
		return str;
	}
}
