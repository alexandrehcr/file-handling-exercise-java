import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        final String INPUT_PATH = "C:\\temp\\items.csv";
        // Non-english speaking countries normally use semicolon as list delimiters and comma as decimal separators.
        final String DELIMITER = ";";

        List<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_PATH))) {
            String line;

            // Print CSV lines
            System.out.println("CSV lines:");
            while ((line = br.readLine()) != null) {
                System.out.println(line.replace(";", ", "));

                // Instantiate products
                String[] temp = line.split(DELIMITER);
                String name = temp[0];
                double price = Double.parseDouble(temp[1].replace(",", "."));
                int quantity = Integer.parseInt(temp[2]);
                products.add(new Product(name, price, quantity));
            }
            System.out.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set paths for output folder and file.
        final String INPUT_PARENT = new File(INPUT_PATH).getParent();
        final String SUBDIR_NAME = "out";
        final String OUTPUT_PATH = INPUT_PARENT + "\\" + SUBDIR_NAME + "\\summary.csv";

        // Create output folder
        boolean mkdirSuccess = new File(INPUT_PARENT + "\\" + SUBDIR_NAME).mkdir();
        if (mkdirSuccess) {
            System.out.println("Created new directory: '" + SUBDIR_NAME + "'");

            // Create output file
            try {
                File fileOutput = new File(OUTPUT_PATH);
                if (fileOutput.createNewFile()) {
                    System.out.println("Created: " + fileOutput.getName());
                } else {
                    System.out.println(fileOutput.getName() + " already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_PATH))){
            for (Product product : products) {
                bw.write(product.toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}