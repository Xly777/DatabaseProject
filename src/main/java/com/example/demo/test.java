package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Scanner;


public class test {

    public static void main(String[] args) {


    }
    public static void login(String account,WebClient w){
        System.out.println("login");
        Scanner sc=new Scanner(System.in);

        String s=w.get().uri("/abc/"+account).retrieve().bodyToMono(String.class).block();
        System.out.println(s);
    }
}
