package product;

public class ProductPayLoad {
    private int quantity;
    Product product;

    public ProductPayLoad(Product product, int quantity){
        this.product=product;
        this.quantity=quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setCode(Product product) {
        this.product = product;
    }
}
