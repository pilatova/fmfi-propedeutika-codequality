import java.util.*;
import java.io.*;
import java.util.function.BiFunction;

public class ConcatenateMatrices {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new File("vstup.txt"));
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

        if (rows < 0 || columns < 0) {
            System.err.println("Nevhodne rozmery matice");
            scanner.close();
            return;
        }

        String[][] resultMatrix = null;
        String[][] nextMatrix;
        BiFunction<String, String, String> joinStrings = (a, b) -> a + b;
        while (scanner.hasNext()) {
            try {
                nextMatrix = readMatrix(scanner, rows, columns);
            } catch (Exception e){
                System.err.println("Nevhodny format matice");
                scanner.close();
                return;
            }
            if (resultMatrix == null) {
                resultMatrix = nextMatrix;
            } else {
                resultMatrix = joinMatrices(resultMatrix, nextMatrix, joinStrings);
            }
        }
        
        PrintStream output = new PrintStream("vystup.txt");
        outputMatrix(resultMatrix, output);
        scanner.close();
        output.close();
    }

    /**
     * Reads a 2D matrix of strings from a Scanner with the specified number of rows and columns.
     * Each element of the matrix is read from the scanner.
     *
     * @param scanner The Scanner from which to read the matrix elements.
     * @param rows    The number of rows in the matrix.
     * @param columns The number of columns in the matrix.
     * @return A 2D array of strings representing the matrix read from the scanner.
     * @throws NullPointerException      If scanner is null.
     * @throws NoSuchElementException    If the scanner does not contain enough elements to populate the matrix.
     */
    private static String[][] readMatrix(Scanner scanner, int rows, int columns) {
        String[][] matrix = new String[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = scanner.next();
            }
        }
        return matrix;
    }

    /**
     * Outputs the contents of a 2D matrix to a PrintStream.
     *
     * @param matrix The matrix to be output.
     * @param output The PrintStream to which the matrix will be written.
     * @throws NullPointerException If either matrix or output is null.
     */
    public static void outputMatrix(String[][] matrix, OutputStream output) {
        try (PrintWriter writer = new PrintWriter(output)) {
            int rows = matrix.length;
            int columns = matrix[0].length;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    writer.printf("[%d,%d]: %s\n", i, j, (matrix[i][j] != null) ? matrix[i][j] : "");
                }
                writer.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Joins two 2D matrices using a specified operation.
     *
     * @param <T>                The type of elements in the first matrix.
     * @param <U>                The type of elements in the second matrix.
     * @param <V>                The type of elements in the resulting matrix.
     * @param firstMatrix  The first matrix to be joined.
     * @param secondMatrix       The second matrix to be joined.
     * @param operation          The operation to be applied to corresponding elements of the matrices.
     * @return The matrix resulting from applying the operation to corresponding elements of the input matrices.
     * @throws IllegalArgumentException If the dimensions of the input matrices do not match.
     */
    private static <T, U, V> V[][] joinMatrices(T[][] firstMatrix, U[][] secondMatrix, BiFunction<T, U, V> operation) {
        if (firstMatrix.length != secondMatrix.length || firstMatrix[0].length != secondMatrix[0].length) {
            throw new IllegalArgumentException("Nekompatibilne matice");
        }

        int rows = firstMatrix.length;
        int columns = firstMatrix[0].length;
        @SuppressWarnings("unchecked")
        V[][] result = (V[][]) java.lang.reflect.Array.newInstance(operation.apply(firstMatrix[0][0], secondMatrix[0][0]).getClass(), rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = operation.apply(firstMatrix[i][j], secondMatrix[i][j]);
            }
        }

        return result;
    }

    public static class Test {
        public static void main(String[] args) {
            testJoinMatricesStrings();
            testJoinMatricesIntegers();
        }

        public static void testJoinMatricesStrings() {
            String[][] matrix1 = {
                    {"a", "b"},
                    {"c", "d"}
            };
            String[][] matrix2 = {
                    {"e", "f"},
                    {"g", "h"}
            };

            BiFunction<String, String, String> joinStrings = (a, b) -> a + b;

            String[][] resultMatrix = ConcatenateMatrices.joinMatrices(matrix1, matrix2, joinStrings);
            String[][] expectedMatrix = {
                    {"ae", "bf"},
                    {"cg", "dh"}
            };
            assertMatrixEquals(expectedMatrix, resultMatrix);
        }

        public static void testJoinMatricesIntegers() {
            Integer[][] matrix1 = {
                    {1, 3},
                    {2, 4}
            };
            Integer[][] matrix2 = {
                    {-4, -2},
                    {-3, -1}
            };

            BiFunction<Integer, Integer, Integer> addIntegers = (a, b) -> a + b;

            Integer[][] resultMatrix = ConcatenateMatrices.joinMatrices(matrix1, matrix2, addIntegers);
            Integer[][] expectedMatrix = {
                    {-3, 1},
                    {-1, 3}
            };
            assertMatrixEquals(expectedMatrix, resultMatrix);
        }

        public static <T> void assertMatrixEquals(T[][] expected, T[][] actual) {
            assert expected.length == actual.length : "Matrix dimensions mismatch";
            for (int i = 0; i < expected.length; i++) {
                assert expected[i].length == actual[i].length : "Row " + i + " dimensions mismatch";
                for (int j = 0; j < expected[i].length; j++) {
                    assert expected[i][j].equals(actual[i][j]) : "Element at [" + i + "," + j + "] mismatch";
                }
            }
        }
    }
}
