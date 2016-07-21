package com.dofus.coder;

/**
 * Permet de crypter et décrypter les packets dofus envoyés avec le packet AKXYY
 * X = une valeur entre 0-9 ou A-F
 * YY = la valeur X en hexadecimal
 * @author Manghao
 * @version 1.0
 * */
public class Main {

	public static void main(String[] args) {
		Encryption pp = new Encryption("AK8697A4C776E755169");
		System.out.println("chiffrement : " + pp.prepareData("1.29.1"));
		System.out.println("dechiffrement : " + pp.unprepareData("897D595C4C7F58"));
	}

}
