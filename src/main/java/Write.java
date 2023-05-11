import java.io.FileWriter;
import java.io.IOException;

public class Write {
    public static void writeToFile(String text, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(text);
            System.out.println("Текст успешно записан в файл: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}