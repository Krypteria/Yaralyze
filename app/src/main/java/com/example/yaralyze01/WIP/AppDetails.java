package com.example.yaralyze01.WIP;

//En la primera activity muestro: Nombre, Foto

//En la segunda además de lo de la primera muestro: firstInstallTime, installLocation, lastUpdateTime, providers, (permissions), (requested permissions),
public class AppDetails {
    private String name;
    private int image;

    public AppDetails(String name, int Image){
        this.name = name;
        this.image = Image;
    }

    public String getName(){
        return this.name;
    }

    public int getImage(){
        return this.image;
    }
}
