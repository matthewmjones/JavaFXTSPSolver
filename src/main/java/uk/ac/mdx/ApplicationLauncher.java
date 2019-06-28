package uk.ac.mdx;

public class ApplicationLauncher {

    /*
    This is a pointless class that starts the application.
    Note the packaging in Maven will not work if the main class is in a class that
    extends Application from JavaFX
     */

    public static void main(String[] args) {
        App.main(args);
    }
}
