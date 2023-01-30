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
        if (outputDirectory.exists()) {
            System.out.println(outputDirectory.getPath() + "\\" + outputDirectory.getName() + " already exists.");
        } else {
            if (!outputDirectory.mkdir() && !outputDirectory.exists()) {
                throw new IllegalStateException("Cannot create output directory: " + outputDirectory.getPath());
            } else {
                System.out.println("Created directory: '" + outputDirectory.getName() + "' in " + outputDirectory.getParent());
            }
        }

        // Creates the output file
        File outputFile = null;
        try {
            outputFile = new File(OUTPUT_PATH);
            if (!outputFile.exists()) {
                System.out.println("Created file: " + outputFile.getName() + " in " + outputFile.getParent());
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
                    for (Product product : products) {
                        bw.write(product.toString() + "\n");
                    }
                }
            } else {
                System.out.println(outputFile.getName() + " already exists.");
                System.out.println();
                System.out.print("What would you like to do to " + outputFile.getName() + ": (a)ppend, (o)verwrite, (q)uit? ");
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
        System.out.print("Would you like to delete the created file and directory after finish the execution? (y/n)" +
                "\nWarning: This action cannot be undone! ");
        String option = scanner.next();
        scanner.nextLine(); // Consumes input buffer
        if (option.equals("y")) {
            System.out.println();
            System.out.print("Press ENTER to finish the execution... ");
            scanner.nextLine();
            System.out.println();
            if (outputFile.exists() && outputFile.delete()) {
                System.out.println("Removed file: " + outputFile.getName());
            }
            if (outputDirectory.exists() && outputDirectory.delete()) {
                System.out.println("Removed directory: '" + outputDirectory.getName() + "' from " + outputDirectory.getParent() + "\\");
            }
        }
    }
}
