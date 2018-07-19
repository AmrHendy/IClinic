package application.ui.handler;

import javafx.scene.image.ImageView;

public class CustomImage {

    private ImageView image;

    CustomImage(ImageView img) {
        this.image = img;
    }

    public void setImage(ImageView value) {
        image = value;
    }

    public ImageView getImage() {
        return image;
    }
}
