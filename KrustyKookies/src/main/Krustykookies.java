package main;

import model.Database;
import view.KookiesGUI;

public class Krustykookies {
    public static void main(String[] args) {
        Database db = new Database();
        new KookiesGUI(db);
    }
}
