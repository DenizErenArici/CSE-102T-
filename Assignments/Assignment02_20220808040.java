
import java.util.ArrayList;
/*@author:Deniz Eren Arıcı
  @since:14.04.2023 */

public class Assignment02_20220808040 {
    public static void main(String[] args) {

    }
}

class CustomerNotFoundException extends IllegalArgumentException {
    private String phone;

    public CustomerNotFoundException(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CustomerNotFoundException:" + phone;
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

    public InvalidAmountException(int amount, int quantity) {
        this.amount = amount;
        this.quantity = quantity;

    }

    public String toString() {
        // I couldn't figured out different way I hope its true.
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

    public void setName(String name) {
        this.name = name;
    }

    public ProductNotFoundException(Long Id) {
        this.Id = Id;
        setName(null);
    }

    public ProductNotFoundException(String str) {
        this.Id = 1L;
    }

    @Override
    public String toString() {
        if (name == null)
            return "ProductNotFoundException:ID- " + Id;
        else
            return "ProductNotFoundException:Name- " + name;
    }
}

class Product {
    private Long Id;
    private String Name;
    private int quantity;
    private double price;

    public Product(Long Id, String Name, int quantity, double price) throws InvalidPriceException, InvalidAmountException {
        this.Name = Name;
        this.Id = Id;
        this.price = price;
        this.quantity = quantity;
        if (quantity < 0) {
            throw new InvalidAmountException(quantity);
        }
        if (price <= 0) {
            throw new InvalidPriceException(price);
        }
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

    public void setPrice(double price) throws Exception {
        if (price <= 0)
            throw new InvalidPriceException(price);
        this.price = price;
    }

    public int remaining() {
        return quantity;
    }

    public int addToInventory(int amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException(amount);
        } else {
            quantity = amount + quantity;
            return quantity;
        }

    }

    public double purchase(int amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException(amount);
        }
        if (amount > quantity) {
            throw new InvalidAmountException(amount, quantity);
        } else {
            quantity -= amount;
            return amount * price;
        }
    }

    @Override
    public String toString() {
        return "Product " + Name + " has " + quantity + " remaining";
    }

    public boolean equals(Object o) {
        if (o instanceof Product) {
            Product pr = (Product) o;
            return pr.price == price;
        }
        return false;
    }
}

class FoodProduct extends Product {
    private int Calories;
    private boolean Dairy;
    private boolean Eggs;
    private boolean Peanuts;
    private boolean Gluten;

    public FoodProduct(Long Id, String Name, int quantity, double price, int Calories, boolean Dairy, boolean Peanuts,
                       boolean Eggs, boolean Gluten) throws InvalidAmountException, InvalidPriceException {
        super(Id, Name, quantity, price);
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

class CleaningProduct extends Product {
    private boolean Liquid;
    private String WhereToUse;


    public CleaningProduct(Long Id, String Name, int quantity, double Price,
                           boolean Liquid, String WhereToUse) throws InvalidAmountException, InvalidPriceException {
        super(Id, Name, quantity, Price);
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

class Customer {
    ArrayList<Product> cart = new ArrayList<Product>();
    ArrayList<Integer> cartCounter = new ArrayList<Integer>();
    private String Name;
    private int count;

    Customer(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String toString() {
        return "Customer's name is " + Name;
    }

    public double getTotalDue() {
        double total = 0;
        for (int i = 0; i < cart.size(); i++) {
            total += cart.get(i).getPrice() * cartCounter.get(i);
        }
        return total;
    }

    public int getCount(Product pr, ArrayList<Product> p) {
        int counter = 0;
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).equals(pr)) {
                counter++;
            }
        }
        return counter;
    }


    public void addToCart(Product product, int count) {
        try {
            product.purchase(count);
            cart.add(product);
            cartCounter.add(count);
        } catch (InvalidAmountException a) {
            System.out.println("ERROR: " + a);
        }

    }

    public String receipt() {
        String receipt = "";
        for (int i = 0; i < cart.size(); i++) {
            Product product = cart.get(i);
            receipt += product.getName() + " - " + product.getPrice() + " X " + cartCounter.get(i) + " = "
                    + (product.getPrice() * cartCounter.get(i)) + "\n";

        }
        receipt += "----------------------------------------------------------" + "\n";
        receipt += "Total due is " + getTotalDue();

        return receipt;
    }

    public double pay(double amount) throws InsufficientFundsException {
        if (amount >= getTotalDue()) {
            System.out.println("Thank you");
            double temp = getTotalDue();
            cart.clear();
            return amount - temp;
        } else
            throw new InsufficientFundsException(getTotalDue(), amount);
    }

}

class ClubCustomer extends Customer {
    private String Phone;
    private int Points;

    public ClubCustomer(String Name, String Phone) {
        super(Name);
        this.Phone = Phone;
        Points = 0;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public int getPoints() {
        return Points;
    }

    public void addPoints(int Points) {
        if (Points > 0) {
            this.Points += Points;
        }
    }

    @Override
    public String toString() {
        return " " + getName() + " has " + Points + "points";
    }

    public double pay(double amount, boolean usePoints) throws InsufficientFundsException {
        double totalDue = getTotalDue();
        double forSubstract = getPoints() * 0.01;
        if (amount < totalDue) {
            throw new InsufficientFundsException(totalDue, amount);
        }
        if (usePoints == true && getPoints() > 0) {
            if (totalDue < getPoints() * 0.01) {
                Points = (short) (getPoints() * 0.01 - totalDue);
            }
            totalDue -= getPoints() * 0.01;
            Points = 0;
            addPoints((int) totalDue);
            return super.pay(amount + forSubstract);
        } else if (usePoints == !true) {
            addPoints((short) totalDue);
            return super.pay(amount);
        } else {
            return super.pay(amount);
        }
    }
}

class Store {
    private String Name;
    private String Website;
    ArrayList<Product> products = new ArrayList<Product>();
    ArrayList<ClubCustomer> customers = new ArrayList<ClubCustomer>();

    Store(String Name, String Website) {
        this.Name = Name;
        this.Website = Website;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public int getInventorySize() {
        return products.size();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Product getProduct(Long ID) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == ID) {
                return products.get(i);
            }
        }
        throw new ProductNotFoundException(ID);
    }

    public Product getProduct(String Name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(Name)) {
                return products.get(i);
            }
        }
        throw new ProductNotFoundException(Name);
    }

    public void addCustomer(ClubCustomer customer) {
        customers.add(customer);
    }

    public ClubCustomer getCustomer(String phone) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getPhone() == phone) {
                return customers.get(i);
            }
        }
        throw new CustomerNotFoundException(phone);
    }

    public void removeProduct(Long ID) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == ID) {
                products.remove(i);
            }
        }
        throw new ProductNotFoundException(ID);
    }

    public void removeProduct(String Name) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equalsIgnoreCase(Name)) {
                products.remove(i);
            }
        }
        throw new ProductNotFoundException(Name);
    }

    public void removeCustomer(String phone) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getPhone() == phone) {
                customers.remove(i);
            }
        }
        throw new CustomerNotFoundException(phone);
    }
}


