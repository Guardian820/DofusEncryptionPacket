# DofusEncryptionPacket
Permet de chiffrer / déchiffrer les packets de Dofus 1.29.1, lorsque ceux-ci sont envoyés a l'aide du packet AK ou ATK

## Version
1.0.0 - Chiffrer et déchiffrer les packets

## Auteur
Manghao

## Utilisation
Pour instancier la classe Encryption il vous suffit de faire comme ceci:
```java
String packet = "AK8697A4C776E755169";
Encryption encryp = new Encryption(packet);
```

Pour chiffrer une donnée il faut utiliser la méthode **prepareData**
Cette méthode prend un paramètre, la chaîne à chiffrer.
```java
String value = encryp.prepareData("Bonjour");
```

Pour déchiffrer une donnée il faut utiliser la méthode **unprepareData**
Cette méthode prend un paramètre, la chaîne à déchiffrer.
```java
String value = encryp.unprepareData("8F1306071023021C");
```
