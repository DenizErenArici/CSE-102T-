import java.util.ArrayList;
import java.util.HashMap;

public class Assignment03_20220808040 {
    public static void main(String[] args) {


    }
}
class CustomerNotFoundException extends IllegalArgumentException {
    private String phone;
    public Customer customer;
    public CustomerNotFoundException(Customer customer){
        this.customer=customer;
    }


    public CustomerNotFoundException(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CustomerNotFoundException: Name - " + customer.getName();
    }
}


class InsufficientFundsException extends RuntimeException {
    private double total;
    private double payment;

    public InsufficientFundsException(double total, double payment) {
        this.total = total;
        this.payment = payment;
    }


    @Override
    public String toString() {
        return "InsufficientFundsException:" + total + " due,but only" + payment + "given";
    }
}

class InvalidAmountException extends RuntimeException {
    private int amount;
    private int quantity;

    public InvalidAmountException(int amount) {
        this.amount = amount;
    }
    public InvalidAmountException(Product product){this.amount=amount;}
    public InvalidAmountException(int amount, int quantity) {
        this.amount = amount;
        this.quantity = quantity;

    }

    public String toString() {
        if (quantity == 0) {
            return "InvalidAmountException: " + amount;
        } else {
            return "InvalidAmountException: " + amount + " is requested, but we have only " + quantity + " remaining";
        }
    }

}

class InvalidPriceException extends RuntimeException {
    private double price;

    public InvalidPriceException(double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "InvalidPriceException: " + price;
    }
}

class ProductNotFoundException extends IllegalArgumentException {
    private Long Id;
    private String name;
    private Product product;

    public void setName(String name) {
        this.name = name;
    }


    ProductNotFoundException(Product product) {
        this.product = product;
    }

    public ProductNotFoundException(String str) {
        this.Id = 1L;
    }

    @Override
    public String toString() {
        return "ProductNotFoundException: ID-"+product.getId()+" Name-"+product.getName();
    }

}
class StoreNotFoundException extends IllegalArgumentException{
    private String name;
    public StoreNotFoundException(String name){
        this.name=name;
    }
    @Override
    public String toString(){
        return  "StoreNotFoundException: "+name;
    }
}


class Product {
    private Long Id;
    private String Name;
    private double price;
    private int points;

    public Product(Long Id, String Name, double price) throws InvalidPriceException {
        this.Id = Id;
        this.Name = Name;
        setPrice(price);
        this.points = 0;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidPriceException {
        if (price <= 0)
            throw new InvalidPriceException(price);
        this.price = price;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return Id + " - " + Name + " @" + price;
    }

    public boolean equals(Object o) {
        if (o instanceof Product) {
            Product pr = (Product) o;
            return pr.price == price;
        }
        return false;
    }
}
class Customer {
    private String name;
    private HashMap<Store, HashMap<Product,Integer>> cart = new HashMap<>();
    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalDue(Store store) throws StoreNotFoundException {
        if (!cart.containsKey(store)) {
            throw new StoreNotFoundException("Store not found");
        }

        double total = 0;
        HashMap<Product,Integer> storeCart = cart.get(store);
        for (Product product : storeCart.keySet()) {
            total += product.getPrice()*storeCart.get(product);
        }
        return total;
    }

    public int getPoints(Store store) throws StoreNotFoundException {
        if (!cart.containsKey(store)) {
            throw new StoreNotFoundException(store.getName());
        }
        return store.getCustomerPoints(this);
    }

    public void addToCart(Store store, Product product, int count) {
        try {
            boolean is_in_store=store.products.containsKey(product);
            if(!is_in_store)
                throw new ProductNotFoundException(product);
            boolean store_is_in_cart=cart.containsKey(store);
            if (store_is_in_cart) {
                HashMap<Product,Integer> storeCart = cart.get(store);
                boolean product_is_in_cart = storeCart.containsKey(product);
                if(product_is_in_cart){
                    storeCart.put(product,storeCart.get(product)+count);
                }
                else{
                    storeCart.put(product,count);
                }
                store.purchase(product,count);

            } else {
                HashMap<Product,Integer> storeCart = new HashMap<>();
                storeCart.put(product,count);
                cart.put(store, storeCart);
                store.purchase(product,count);
            }
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: " + e);
        }catch(ProductNotFoundException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public String receipt(Store store) throws StoreNotFoundException {
        if (!cart.containsKey(store)) {
            throw new StoreNotFoundException("StoreNotFound");
        }

        String receipt = store.getName() + " Receipt:\n";
        HashMap<Product,Integer> storeCart = cart.get(store);
        double total = 0;
        for (Product product : storeCart.keySet()) {
            double totalPrice = product.getPrice() * storeCart.get(product);
            receipt += product.getId() + " - " + product.getName() + " @" + product.getPrice() + " X "
                    + storeCart.get(product) + " = " + totalPrice + "\n";

        }
        receipt+="*************************************\n";
        receipt += "Total Due - " + getTotalDue(store)+"\n";
        return receipt;
    }

    public double pay(Store store, double amount, boolean usePoints)
            throws InsufficientFundsException, StoreNotFoundException {
        if (!cart.containsKey(store)) {
            throw new StoreNotFoundException(store.getName());
        }

        double totalDue = getTotalDue(store);
        double points=0;
        if(store.customers.containsKey(this)) {
            points = getPoints(store);
        }
        if(usePoints){
            if(totalDue-amount-points*0.01>0){
                throw new InsufficientFundsException(totalDue-points*0.01,amount);
            }
            if (totalDue-points*0.01<0) {
                System.out.println("Thank you ");
                store.customers.put(this,getPoints(store)+(int)Math.abs((totalDue-points*0.01)*100));
                HashMap<Product,Integer> storeCart = cart.get(store);
                cart.remove(store);
                return amount ;
            } else {
                System.out.println("Thank you ");
                store.customers.put(this,(int)totalDue);
                cart.remove(store);
                return amount-totalDue-points*0.01;

            }
        }
        if(totalDue-amount>0){
            throw new InsufficientFundsException(totalDue,amount);
        }
        else{
            System.out.println("Thank you ");
            store.customers.put(this,getPoints(store)+(int)totalDue);
            cart.remove(store);
            return amount-totalDue ;
        }
    }
}
class Store {
    private String name;
    private String website;
    protected HashMap<Product, Integer> products = new HashMap<>();
    protected HashMap<Customer,Integer> customers = new HashMap<>();

    Store(String name, String website) {
        this.name = name;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getCount() {
        return products.size();
    }

    public int getProductCount(Product product) {

        return products.get(product);
    }

    public void addCustomer(Customer customer) {
        customers.put(customer,0);
    }

    public void removeProduct(Product product) {
        if (!products.containsKey(product)) {
            throw new ProductNotFoundException(product);
        }
        products.remove(product);
    }

    public void addToInventory(Product product, int amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException(amount);
        }

        boolean alreadyExists=products.containsKey(product);
        if(alreadyExists){
            products.put(product,products.get(product)+amount);
        }else{
            products.put(product,amount);
        }
    }

    public double purchase(Product product, int amount) throws InvalidAmountException {
        if (amount < 0 || amount > products.get(product)) {
            throw new InvalidAmountException(amount);
        }else{
            products.put(product,products.get(product)-amount);
        }
        return product.getPrice()*amount;

    }

    public int getCustomerPoints(Customer customer) {
        if(!customers.containsKey(customer))
            throw new CustomerNotFoundException(customer);

        return customers.get(customer);
    }


    public void removeCustomer(Customer customer) {
        if(!customers.containsKey(customer))
            throw new CustomerNotFoundException(customer);

        customers.remove(customer);
    }
}



class CleaningProduct extends Product {
    private boolean Liquid;
    private String WhereToUse;


    public CleaningProduct(Long Id, String Name, double Price,
                           boolean Liquid, String WhereToUse) throws InvalidAmountException, InvalidPriceException {
        super(Id, Name, Price);
        this.Liquid = Liquid;
        this.WhereToUse = WhereToUse;
    }

    public String getWhereToUse() {
        return WhereToUse;
    }

    public void setWhereToUse(String size) {
        WhereToUse = size;
    }

    public boolean isLiquid() {
        return Liquid;
    }

}
class FoodProduct extends Product {
    private int Calories;
    private boolean Dairy;
    private boolean Eggs;
    private boolean Peanuts;
    private boolean Gluten;

    public FoodProduct(Long Id, String Name, double price, int Calories, boolean Dairy, boolean Peanuts,
                       boolean Eggs, boolean Gluten) throws InvalidAmountException, InvalidPriceException {
        super(Id, Name, price);
        this.Calories = Calories;
        this.Dairy = Dairy;
        this.Peanuts = Peanuts;
        this.Eggs = Eggs;
        this.Gluten = Gluten;
        if (Calories < 0) {
            throw new InvalidAmountException(Calories);
        }
    }

    public int getCalories() {
        return Calories;
    }

    public void setCalories(int Calories) throws Exception {
        if (Calories < 0) {
            throw new InvalidAmountException(Calories);
        }
        this.Calories = Calories;
    }

    public boolean containsDairy() {
        return Dairy;
    }

    public boolean containsEggs() {
        return Eggs;
    }

    public boolean containsPeanuts() {
        return Peanuts;
    }

    public boolean containsGluten() {
        return Gluten;
    }
}
