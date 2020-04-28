/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guijframe;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author w1682889 Hou In Lei
 */
public class PasswordUtils {
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@£!£&*^";
    private static final int ITERATIONS =100;
    private static final int KEY_LENGTH=512;
    
    public static String getSalt(int length){
        StringBuilder returnValue = new StringBuilder(length);
        for(int i=0;i<length;i++){
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
            System.out.println(returnValue);
        }
        System.out.println("final salt generated: "+returnValue);
        return new String(returnValue);
    }
    
    public static byte[] hash(char[] password, byte[] salt){
        PBEKeySpec spec = new PBEKeySpec(password,salt,ITERATIONS,KEY_LENGTH);
        Arrays.fill(password,Character.MIN_VALUE);
        try{
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            System.out.println("skf: "+skf);
            System.out.println(("skf return: "+skf.generateSecret(spec).getEncoded()));
            return skf.generateSecret(spec).getEncoded();
        }catch (NoSuchAlgorithmException|InvalidKeySpecException e){
            throw new AssertionError("Error while hashing a password: "+e.getMessage(),e);
        }finally{
            spec.clearPassword();
            System.gc();
        }
    }
    
    public static String generateSecurePassword(String password,String salt){
        String returnValue=null;
        System.out.println("password: "+password+"salt : "+salt);
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        System.out.println("secure pwd: "+securePassword);
        returnValue = Base64.getEncoder().encodeToString(securePassword);
        return returnValue;
    }
    
    public static boolean verifyUserPassword(String providedPassword,String securedPassword,String salt){
        boolean returnValue=false;
        String newSecurePassword=generateSecurePassword(providedPassword,salt);
        returnValue=newSecurePassword.equalsIgnoreCase(securedPassword);
        return returnValue;
    }

}
