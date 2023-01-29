public class Product {
    private final String name;
    private final Double price;
    private final Integer quantity;

    public Product(String name, Double price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public double totalPrice(){
        return price * quantity;
    }

    // In non-english speaking countries, the list delimiter is usually the semicolon.
    // Thus, printing this way will separate a columns for the name and for the total price in spreadsheets;
    @Override
    public String toString() {
        return String.format("%s; %.2f", name, totalPrice());
    }
}
