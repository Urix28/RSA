package rsachat;

import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class RSA {
   int tamPrimo;
 BigInteger n, q, p;
 BigInteger totient;
 BigInteger e, d;

 /** Constructor de la clase RSA */
 public RSA(int tamPrimo) {
 this.tamPrimo = tamPrimo;
 generaPrimos(); //Genera p y q
 generaClaves(); //Genera e y d
 }

 /************
31 *
32 */
 public void generaPrimos()
 {
 p = new BigInteger(tamPrimo, 10, new Random());
 do q = new BigInteger(tamPrimo, 10, new Random());
 while(q.compareTo(p)==0);
 }
 /************************
40 *
41 */

 public void generaClaves()

 {
 // n = p * q
 n = p.multiply(q);
 // toltient = (p-1)*(q-1)
 totient = p.subtract(BigInteger.valueOf(1));
 totient = totient.multiply(q.subtract(BigInteger.valueOf(1)));

 // Elegimos un e coprimo de y menor que n
 do e = new BigInteger(2 * tamPrimo, new Random());
 while((e.compareTo(totient) != -1) ||
 (e.gcd(totient).compareTo(BigInteger.valueOf(1)) != 0));
 // d = e^1 mod totient
 d = e.modInverse(totient);
 }

 /**
 * Cifra el texto usando la clave p´ublica
 *
 * @param mensaje Mensaje a cifrar
 * @return El mensaje Cifrado
 */
 public BigInteger[] cifrar(String mensaje)
 {
 int i;
 byte[] temp = new byte[1];
 byte[] digitos = mensaje.getBytes();
 BigInteger[] bigdigitos = new BigInteger[digitos.length];

 for(i=0; i<bigdigitos.length;i++){
 temp[0] = digitos[i];
 bigdigitos[i] = new BigInteger(temp);
 }

 BigInteger[] cifrado = new BigInteger[bigdigitos.length];

 for(i=0; i<bigdigitos.length; i++)
 cifrado[i] = bigdigitos[i].modPow(e,n);

 return(cifrado);
 }

 /**
 * Descifra el texto cifrado usando la clave privada
 *
 * @param Array objetos
 * @return El texto en claro
 */
 public String descifrar(BigInteger[] cifrado) {
 BigInteger[] descifrado = new BigInteger[cifrado.length];

 for(int i=0; i<descifrado.length; i++)
 descifrado[i] = cifrado[i].modPow(d,n);

 char[] charArray = new char[descifrado.length];

 for(int i=0; i<charArray.length; i++)
 charArray[i] = (char) (descifrado[i].intValue());

 return(new String(charArray));
 }

 public BigInteger get_p() {return(p);}

 public BigInteger get_q() {return(q);}

 public BigInteger get_totient() {return(totient);}

 public BigInteger get_n() {return(n);}

public BigInteger get_e() {return(e);}

 public BigInteger get_d() {return(d);}
 
}