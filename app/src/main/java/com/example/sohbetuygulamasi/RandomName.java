package com.example.sohbetuygulamasi;

import java.util.Random;

public class RandomName {
    //static oluşturduğum için nesne oluşturmadan sınıf ismiyle çağırabilirim metodu.
    public static String getSaltString(){
        String SALTCHARS ="ABCDEFGHIJKLMNOPRSTUVYZ1234567890";
        StringBuilder salt=new StringBuilder();
        Random rnd=new Random();
        while (salt.length()<18){
            int index=(int)(rnd.nextFloat()*SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr=salt.toString();
        return saltStr;
    }
}
