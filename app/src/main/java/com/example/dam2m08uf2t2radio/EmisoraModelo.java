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

    private final String[] descripcioEmissora = {"\n" +
            "Catalunya Ràdio és l'emissora de ràdio pública de Catalunya, oferint una àmplia varietat de programes en català sobre notícies, cultura, música i esports.",
            "Catalunya Info és una emisora de ràdio que ofereix informació contínua sobre notícies i serveis d'interès general a Catalunya. Pertany a la CCMA i té una programació centrada en notícies i reportatges.",
            "Catalunya Música és una emisora de ràdio pública de Catalunya, dedicada exclusivament a la música clàssica i de qualitat. Ofereix una programació variada que inclou concerts en directe, recitals, entrevistes i informació sobre el món de la música clàssica.",
            "iCat FM és una emisora de ràdio pública de Catalunya centrada en la música i la cultura catalanes. Ofereix una programació variada amb una gran diversitat de gèneres musicals, des de la música independent fins a les últimes novetats del panorama musical català i internacional. A més de la música, iCat FM també ofereix programes culturals, entrevistes i contingut informatiu relacionat amb la música i la cultura."};

    private final int[] drawables = new int[4];

    private String nom;

    private String url;

    private String descripcio;

    private int draw;

    private int num;

    public EmisoraModelo(int emisora) {

        drawables[0] = R.drawable.catalunyar;
        drawables[1] = R.drawable.catalunyai;
        drawables[2] = R.drawable.catalunyam;
        drawables[3] = R.drawable.icatfm;

        if (emisora < nomEmisoras.length){
            nom = this.nomEmisoras[emisora];
            url = this.urlEmisora[emisora];
            descripcio = this.descripcioEmissora[emisora];
            num = emisora;
            draw = drawables[num];
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

    public int getDraw() {
        return draw;
    }

    public int getNum(){
        return num;
    }

}
