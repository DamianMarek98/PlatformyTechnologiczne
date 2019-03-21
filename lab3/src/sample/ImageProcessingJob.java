package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageProcessingJob extends Task<Void> {
    protected File sourceFile;
    protected File targetDirectory;


    public ImageProcessingJob(File file) {
        sourceFile = file;
        targetDirectory = null;

        updateMessage("In queue");
    }

    public File getFile() {
        return sourceFile;
    }

    @Override
    protected Void call() throws Exception {
        if (targetDirectory == null) {
            updateMessage("Choosen dir not existing!");
            return null;
        } else if (sourceFile == null) {
            updateMessage("Problem with source file!");
            return null;
        }

        updateMessage("Processing...");
        updateProgress(0, 1);

        try {
            //wczytanie oryginalnego pliku do pamięci
            BufferedImage original = ImageIO.read(sourceFile);

            //przygotowanie bufora na grafikę w skali szarości
            BufferedImage grayscale = new BufferedImage(
                    original.getWidth(),
                    original.getHeight(),
                    original.getType()
            );

            //przetwarzanie piksel po pikselu
            for (int i = 0; i < original.getWidth(); i++) {
                for (int j = 0; j < original.getHeight(); j++) {
                    //pobranie składowych RGB
                    int red = new Color(original.getRGB(i, j)).getRed();
                    int green = new Color(original.getRGB(i, j)).getGreen();
                    int blue = new Color(original.getRGB(i, j)).getBlue();

                    //obliczenie jasności piksela dla obrazu w skali szarości
                    int luminosity = (int) (0.21 * red + 0.71 * green + 0.07 * blue);

                    //przygotowanie wartości koloru w oparciu o obliczoną jaskość
                    int newPixel = new Color(luminosity, luminosity, luminosity).getRGB();

                    //zapisanie nowego piksela w buforze
                    grayscale.setRGB(i, j, newPixel);
                }

                //obliczenie postępu przetwarzania jako liczby z przedziału [0, 1]
                //aktualizacja własności zbindowanej z paskiem postępu w tabeli
                double curProgress = (1.0 + i) / original.getWidth();
                Platform.runLater(() -> updateProgress(curProgress, 1));
            }

            //przygotowanie ścieżki wskazującej na plik wynikowy
            Path outputPath = Paths.get(
                    targetDirectory.getAbsolutePath(),
                    sourceFile.getName()
            );

            //zapisanie zawartości bufora do pliku na dysku
            ImageIO.write(grayscale, "jpg", outputPath.toFile());
        } catch (IOException ex) {
            //translacja wyjątku
            updateMessage("ERROR!");
            throw new RuntimeException(ex);
        }

        updateMessage("Converted");
        return null;
    }
}
