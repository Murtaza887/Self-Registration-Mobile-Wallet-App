package com.murtaza.simplifiedselfregistrationonmobilewallet;

public class Shortcut {
    public static String shortcut;

    Shortcut() {}
    Shortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public static String getShortcut() {
        return shortcut;
    }

    public static void setShortcut(String shortcut) {
        Shortcut.shortcut = shortcut;
    }
}