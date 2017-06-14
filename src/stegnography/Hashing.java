/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegnography;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * 
 * @author SSHAH
 */
public class Hashing {
    private byte[] enc;
    MessageDigest md;
    /**
     * It returns the hashed value of the argument password passed by the user.
     * 
     * @param password to unlock the given document
     * @return the encoded value of the password
     */ 
    public Hashing() throws NoSuchAlgorithmException{
        enc=null;
        md = MessageDigest.getInstance("SHA-256");
        md.reset();
    }
    
    public byte[] genHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    
        enc = password.getBytes("UTF-8");
        return md.digest(enc);    
    } 
    
}
