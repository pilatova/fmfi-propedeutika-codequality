import java.util.*;
import java.io.*;

public class ConcatenateMatrices {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new File("vstup.txt"));
        PrintStream output = new PrintStream("vystup.txt");
        
        if (!scanner.hasNextInt()) {
            scanner.close();
            System.err.println("Nespravny vstupny subor");
            return;
        }
        int rows = scanner.nextInt();
        if (!scanner.hasNextInt()) {
            scanner.close();
            System.err.println("Nespravny vstupny subor");
            return;
        }
        int columns = scanner.nextInt();

        String[][] matrix = new String[rows][]; //matica a jej vynulovanie
        for (int k = 0; k < rows; k++) {
            matrix[k] = new String[columns];
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = "";
            }
        }
        while (scanner.hasNextLine()) { //cita pokial nenarazi na koniec (neexistujuci prvy riadok matice)
            for (int i = 0; i < rows; i++) { //ak je dalsi riadok, tak urcite bude m-riadkov matice
                String riadok = scanner.nextLine(); //jeden riadok matice
                String[] prvky = riadok.split(" "); //rozdelenie riadku na prvky (stlpce)
                for (int j = 0; j < columns; j++) { //prvky_length = n
                    matrix[i][j] += prvky[j]; //pricitanie do vyslednej matice
                }
            }
        }
        for (int i = 0; i < rows; i++) { //formatovany vystup vyslednej matice
            for (int j = 0; j < columns; j++) {
                output.printf("[%d,%d]: %s\n", i, j, matrix[i][j]);
            }
        }
        scanner.close();
    }
}
