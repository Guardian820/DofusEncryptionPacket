package com.dofus.coder;

/**
 * Permet de crypter et décrypter les packets dofus envoyés avec le packet AKXYY
 * X = une valeur entre 0-9 ou A-F
 * YY = la valeur X en hexadecimal
 * @author Manghao
 * @version 1.0
 * */
public class Encryption {

	private String[] _aKeys;
	private int _nCurrentKey;
	private String HEX_CHARS[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
	
	public Encryption(String packet) {
		if (packet.substring(0, 2).compareTo("AK") == 0) this.onKey(packet.substring(2));
		if (packet.substring(0, 2).compareTo("AT") == 0) this.onKey(packet.substring(3));
	}
	
	private void onKey(String sExtraData) { 
		int _loc3 = Integer.parseInt(sExtraData.substring(0, 1), 16);
		String _loc4 = sExtraData.substring(1);
		this.addKeyToCollection(_loc3, _loc4);
		this.startUsingKey(_loc3);
	}
	
	private void addKeyToCollection(int nKeyID, String sKey) {
		if (this._aKeys == null) {
			this._aKeys = new String[nKeyID + 1];
		}
		this._aKeys[nKeyID] = this.prepareKey(sKey);
	}

	private void startUsingKey(int nKeyID) {
		this._nCurrentKey = nKeyID;
	}
	
	private String prepareKey(String d) {
		String _loc3 = new String();
		for (int _loc4 = 0; _loc4 < d.length(); _loc4 = _loc4 + 2) {
			_loc3 = _loc3 + (char)Integer.parseInt(d.substring(_loc4, _loc4 + 2), 16);
		}
		return _loc3;
	}
	
	private String d2h(int d) {
		if (d > 255) d = 255;
		return this.HEX_CHARS[(int) Math.floor(d / 16)] + this.HEX_CHARS[d % 16];
	}

	private String preEscape(String s) {
		String _loc3 = new String();
		int _loc4 = 0;
		while (_loc4 < s.length()) {
			String _loc5 = Character.toString(s.charAt(_loc4));
			int _loc6 = (int)s.charAt(_loc4);
			if (_loc6 < 32 || (_loc6 > 127 || (_loc5 == "%" || _loc5 == "+")))
			{
				_loc3 = _loc3 + _loc5.replaceAll("%", "").replaceAll("+", "");
				continue;
			}
			_loc3 = _loc3 + _loc5;
			_loc4++;
		}
		return _loc3;
	}

	private String checksum(String s) {
		int _loc3 = 0;
		for (int _loc4 = 0; _loc4 < s.length(); _loc4++) {
			_loc3 = _loc3 + (((int) s.charAt(_loc4)) % 16);
		}
		return this.HEX_CHARS[_loc3 % 16];
	}
	
	public String prepareData(String s) {
		if (this._nCurrentKey == 0 || this._nCurrentKey < 0) return s;
		if (this._aKeys[this._nCurrentKey] == null) return s;
		String _loc3 = HEX_CHARS[this._nCurrentKey];
		String _loc4 = checksum(s);
		_loc3 = _loc3 + _loc4;
		return _loc3 + cypherData(s, this._aKeys[this._nCurrentKey], Integer.parseInt(_loc4, 16) * 2);
	}

	private String cypherData(String d, String k, int c) {
		String _loc5 = new String();
		int _loc6 = k.length();
		d = preEscape(d);
		for (int _loc7 = 0; _loc7 < d.length(); _loc7++) {
			_loc5 = _loc5 + d2h((int)d.charAt(_loc7) ^ (int)k.charAt((_loc7 + c) % _loc6));
		}
		return _loc5;
	}

	public String unprepareData(String s) {	
		if (this._nCurrentKey == 0) return s;
		String _loc3 = this._aKeys[Integer.parseInt(s.substring(0, 1), 16)];
		if (_loc3 == null) return s;
		String _loc4 = s.substring(1, 2).toUpperCase();
		String _loc5 = decypherData(s.substring(2), _loc3, Integer.parseInt(_loc4, 16) * 2);
		//if (checksum(_loc5).compareTo(_loc4) != 0) return s;
		return _loc5;
	}

	private String decypherData(String d, String k, int c) {		
		String _loc5 = new String();
		int _loc6 = k.length();
		int _loc7 = 0;
		for (int _loc9 = 0; _loc9 < d.length(); _loc9 = _loc9 + 2) {
			_loc5 = _loc5 + Character.toString((char) (Integer.parseInt(d.substring(_loc9, _loc9 + 2), 16) ^ (int)k.charAt((_loc7++ + c) % _loc6)));
		}
		return _loc5;
	}
}
