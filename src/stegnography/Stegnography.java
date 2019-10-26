
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegnography;
/**
 *
 * @author lovlin-thakkar
 * @author SSHAH
 */


import java.io.*;
import java.awt.Image;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.stream.ImageInputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Stegnography {

    /**
     * @param args the command line arguments
     */
    private String message;
    private String key;
    
    public byte[] encrypter() throws FileNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // TODO code application logic here
        // gets the hashed key
        Hashing h = new Hashing();
        byte[] encodedkey = h.genHash(key);              
        SecretKeySpec kobj = new SecretKeySpec(encodedkey, "AES");
        Cipher cobj = Cipher.getInstance("AES");
        cobj.init(Cipher.DECRYPT_MODE, kobj);
        // String textData="sfagdasdfa";              //text data
        
        byte[] datatosend = message.getBytes();
        System.out.println("Size of the text data " + message.length());
        byte[] finalData = cobj.doFinal(datatosend);         //data in encrypted format
        
        String finalstr = finalData.toString();
        System.out.println("Intended messge " +message);
        System.out.println("Encyrpted message "+finalstr);
       
	return finalData;
    }
    
    public String decrypter(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Hashing h = new Hashing();
        byte[] encodedKey = h.genHash(key); 

        byte[] encryptedData = stringToBytes(str);
        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(encodedKey, "AES");
        c.init(Cipher.DECRYPT_MODE, k); 
        byte[] decryptedData = c.doFinal(encryptedData);

        System.out.println("Decrypted message:\n" + new String(decryptedData) + "\n");

	return str;
    }
    
    public byte[] stringToBytes(String str) throws UnsupportedEncodingException{
        byte[] bdata = str.getBytes("UTF-8");
        return bdata;
    }
    
    public void hideImage(){
	    try{
		Scanner in = new Scanner(System.in);
		File f = new File("music.wav");
		InputStream is = AudioSystem.getAudioInputStream(f);
		byte[] arr = new byte[903271];
		is.read(arr, 0, 903271);
		int i = 0;
		byte[] a = {(byte)0b00000001, (byte)0b00000010, (byte)0b00000100, (byte)0b00001000, (byte)0b00010000, (byte)0b00100000,(byte)0b01000000, (byte)0b10000000};
		String message = in.nextLine();
		byte[] msg = message.getBytes();

		for(int j=0; j<msg.length; j++){
		    for(i=0; i<8; i++){
			arr[8*j+i] |= (byte) (((byte) (msg[j] & a[i])) >> i);
		    }
		}      

		// TODO: remove hard-coding
		File f2 = new File("specialmusic.wav");
		InputStream mine = new ByteArrayInputStream(arr);
		AudioInputStream mine1 = new AudioInputStream(mine, (AudioSystem.getAudioInputStream(f)).getFormat(), arr.length);
		AudioSystem.write(mine1, AudioFileFormat.Type.WAVE, f2);

	    } catch (Exception e) {
		    e.printStackTrace();
	    }
    }
    
    public void recoverImage(){
        try{		
		File f = new File("specialmusic.wav");
		InputStream is = AudioSystem.getAudioInputStream(f);
		byte arr[] = new byte[903271];
		is.read(arr, 0, 903271);
		int i = 0;
		byte[] mes = new byte[112909];
		for(int j=0; (8*j+i)<mes.length; j++){
			for(i=0; i<8; i++){
				mes[8*j+i] += (byte)((byte)(arr[j] & ((byte) 0b00000001))<<i);
			}
		}
		for(i=0; i<mes.length; i++){
			System.out.print(byteToString(mes[i]));
		}
	} catch(Exception e) {
		e.printStackTrace();
	}
    }
    
    public static String byteToString(byte b) {
    	byte[] masks = { -128, 64, 32, 16, 8, 4, 2, 1 };
    	StringBuilder builder = new StringBuilder();
    	for (byte m : masks) {
		if((b & m) == m) {
                    builder.append('1');
                } else {
                    builder.append('0');
                }
	}
    	return builder.toString();
    }

    public static void main(String[] args) {
        
    }
                
}
