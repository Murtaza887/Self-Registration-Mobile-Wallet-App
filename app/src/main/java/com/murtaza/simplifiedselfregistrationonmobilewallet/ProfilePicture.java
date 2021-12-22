package com.murtaza.simplifiedselfregistrationonmobilewallet;

import android.net.Uri;

public class ProfilePicture {
    public static Uri image;

    ProfilePicture() {}
    ProfilePicture(Uri image) {
        this.image = image;
    }

    public static Uri getImage() {
        return image;
    }

    public static void setImage(Uri image) {
        ProfilePicture.image = image;
    }
}
