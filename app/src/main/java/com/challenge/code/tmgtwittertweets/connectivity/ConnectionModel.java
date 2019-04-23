package com.challenge.code.tmgtwittertweets.connectivity;


/**
 * Created by Harry Anuszewski on 4/22/2019.
 */
public class ConnectionModel {
    private int type;
    private boolean isConnected;

    public ConnectionModel(int type, boolean isConnected) {
        this.type = type;
        this.isConnected = isConnected;
    }

    public int getType() {
        return type;
    }

    public boolean getIsConnected() {
        return isConnected;
    }
}
