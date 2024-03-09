package com.example.dam2m08uf2t2radio;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.Serializable;

public class PlayerSerializable implements Serializable {

    private SimpleExoPlayer player;
    public PlayerSerializable(SimpleExoPlayer player) {
        this.player = player;
    }
    public SimpleExoPlayer getPlayer() {
        return player;
    }
    public void setPlayer(SimpleExoPlayer player) {
        this.player = player;
    }
}
