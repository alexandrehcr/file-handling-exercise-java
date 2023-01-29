import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);

        // Non-english speaking countries normally use semicolon as list delimiters and comma as decimal separators.
        final String DELIMITER = ";";
        final String FILE_PATH = "C:\\temp\\items.csv";
        // Reads the file and stores the products within the items.csv file in the list
        List<Product> products = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Instantiates the products
                String[] temp = line.split(DELIMITER);
                String name = temp[0];
                double price = Double.parseDouble(temp[1].replace(",", "."));
                int quantity = Integer.parseInt(temp[2]);
                products.add(new Product(name, price, quantity));
            }
            System.out.println();
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read input file: " + FILE_PATH, e);
        }

        // Sets the paths to output folder and file
        final String FILE_PARENT = new File(FILE_PATH).getParent();
        final String SUBDIRECTORY = "out";
        final String OUTPUT_PATH = FILE_PARENT + "\\" + SUBDIRECTORY + "\\summary.csv";

        // Creates the output folder
        File outputDirectory = new File(FILE_PARENT + "\\" + SUBDIRECTORY);
        if (!outputDirectory.mkdir() && !outputDirectory.exists()) {
            throw new IllegalStateException("Cannot create output directory: " + outputDirectory.getAbsolutePath());
        } else {
            System.out.println("Created new directory: " + outputDirectory.getParent() + "\\" + outputDirectory.getName());
        }

        // Creates the output file
        File outputFile = null;
        try {
            outputFile = new File(OUTPUT_PATH);
            if (!outputFile.exists()) {
                System.out.println("Created: " + outputFile.getName());
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
                    for (Product product : products) {
                        bw.write(product.toString() + "\n");
                    }
                }
            } else {
                System.out.println(outputFile.getName() + " already exists.");
                System.out.print("What would you like to do: (o)verwrite, (a)ppend, (q)uit? ");
                String option = scanner.next();
                scanner.nextLine(); // Consumes input buffer
                switch (option) {
                    case "o":
                        System.out.println("Overwriting file...");
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
                            for (Product product : products) {
                                bw.write(product.toString() + "\n");
                            }
                        }
                        break;
                    case "a":
                        System.out.println("Appending to file...");
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true))) {
                            for (Product product : products) {
                                bw.write(product.toString() + "\n");
                            }
                        }
                        break;
                    case "q":
                        System.out.println("Aborting...");
                        return;
                    default:
                        System.out.println("Invalid option. Aborting...");
                        return;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write output file: " + outputFile.getAbsolutePath(), e);
        }

        // Provides the ability to remove the file and folder if it is empty.
        System.out.println();
        System.out.print("Would you like to delete the created files created after finish the execution (y/n)? ");
        String option = scanner.next();
        scanner.nextLine(); // Consumes input buffer
        if (option.equals("y")) {
            System.out.print("Press ENTER to finish the execution and delete the files created... ");
            scanner.nextLine();
            if (outputFile.exists() && outputFile.delete()) {
                System.out.println("Deleted file: '" + outputFile.getName() + "'");
            }
            if (outputDirectory.exists() && outputDirectory.delete()) {
                System.out.println("Deleted directory: '" + SUBDIRECTORY + "'");
            }
        }
    }
}
