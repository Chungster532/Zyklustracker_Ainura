package com.example.tracker_ainura.Models;

public class WissenThema {
    public String thema;
    public String infos;
    public boolean istAusgeklappt;

    public WissenThema(String thema, String infos) {
        this.thema = thema;
        this.infos = infos;
        this.istAusgeklappt = false;
    }

    public boolean isIstAusgeklappt() {
        return istAusgeklappt;
    }

    public void setIstAusgeklappt(boolean istAusgeklappt) {
        this.istAusgeklappt = istAusgeklappt;
    }

    public String getThema() {
        return thema;
    }

    public void setThema(String thema) {
        this.thema = thema;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }
}
