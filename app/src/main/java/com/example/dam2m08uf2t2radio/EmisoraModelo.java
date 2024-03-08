package com.example.dam2m08uf2t2radio;

import java.io.Serializable;

public class EmisoraModelo implements Serializable {

    private final String[] nomEmisoras = {"Catalunya Ràdio","Catalunya Informació","Catalunya Mùsica ","Icat FM"};
    private final String[] urlEmisora = {
            "https://shoutcast.ccma.cat/ccma/catalunyaradioHD.mp3",
            "https://shoutcast.ccma.cat/ccma/catalunyainformacioHD.mp3",
            "https://shoutcast.ccma.cat/ccma/catalunyamusicaHD.mp3",
            "https://shoutcast.ccma.cat/ccma/icatHD.mp3"
            };

    /*
    https://streamingv2.shoutcast.com/beatles-radio",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
    * */

    private final String[] descripcioEmissora = {"1", "2" ,"3" ,"4"};

    private String nom;

    private String url;

    private String descripcio;

    public EmisoraModelo(int emisora) {
        if (emisora < nomEmisoras.length){
            nom = this.nomEmisoras[emisora];
            url = this.urlEmisora[emisora];
            descripcio = this.descripcioEmissora[emisora];
        }
    }

    public String getNom() {
        return nom;
    }

    public String getUrl() {
        return url;
    }

    public String getDescripcio() {
        return descripcio;
    }
}
