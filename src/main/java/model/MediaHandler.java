package model;

import java.io.InputStream;
import java.net.URL;

/* 
 *  Class used to handle different media located in the resources folder
 */
public class MediaHandler {

    public static InputStream getAudioInputStream(String fileName) {
        return getMediaInputStream("audio", fileName);
    }

    public static InputStream getImageInputStream(String fileName) {
        return getMediaInputStream("image", fileName);
    }

    public static InputStream getFXMLInputStream(String fileName) {
        return getMediaInputStream("fxml", fileName);

    }

    public static URL getFXMLURL(String fileName) {
        return getMediaURL("fxml", fileName);
    }

    public static URL getCSSURL(String fileName) {
        return getMediaURL("css", fileName);
    }

    public static InputStream getFontInputStream(String fileName) {
        return getMediaInputStream("font", fileName);
    }

    /**
     * @param mediatype the type of the media ex: "audio", "image",
     *                  it's the name of the folder where your file is located in,
     *                  the aforementioned folder must be located in the "resources"
     *                  folder
     * @param fileName  the name of your file ex: "song.wav", "terrain.png"
     * @return the InputStream of your media file
     */
    public static InputStream getMediaInputStream(String mediatype, String fileName) {
        return MediaHandler.class.getClassLoader()
                .getResourceAsStream(mediatype + "/" + fileName);
    }

    /**
     * @param mediatype the type of the media ex: "audio", "image",
     *                  it's the name of the folder where your file is located in,
     *                  the aforementioned folder must be located in the "resources"
     *                  folder
     * @param fileName  the name of your file ex: "song.wav", "terrain.png"
     * @return the URL of your media file
     */
    public static URL getMediaURL(String mediatype, String fileName) {
        return MediaHandler.class.getClassLoader()
                .getResource(mediatype + "/" + fileName);
    }

}