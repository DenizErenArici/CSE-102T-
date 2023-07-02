import javax.naming.Name;
import java.util.ArrayList;
//@date:25.03.2023
//@author:Deniz Eren Arıcı
public class Assignment01_20220808040 {
    public static void main(String[] args) {

    }
}

class Product {
    private String Id;
    private String Name;
    private int Quantity;
    private double Price;

    Product() {
    }

    ;

    public Product(String Id, String Name, int Quantity, double Price) {
        this();
        this.Name = Name;
        this.Id = Id;
        this.Price = Price;
        this.Quantity = Quantity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public int remaining() {
        return Quantity;
    }

    int count = 0;

    public int addToInventory(int amount) {
        if (amount < 0) {
            return Quantity;
        } else {
            Quantity = amount + Quantity;
            return Quantity;
        }

    }

    public double purchase(int amount) {
        if (amount < 0 || amount > Quantity)
            return 0;

        Quantity -= amount;
        return amount * Price;
    }

    @Override
    public String toString() {
        return "Product " + Name + " has " + Quantity + " remaining";
    }

    public boolean equals(Object o) {
        if (o instanceof Product) {
            Product pr = (Product) o;
            return pr.Price == Price;
        }
        return false;
    }
}

class FoodProduct extends Product {
    FoodProduct() {
    }

    private int Calories;
    private boolean Dairy;
    private boolean Eggs;
    private boolean Peanuts;
    private boolean Gluten;

    public FoodProduct(String Id, String Name, int Quantity, double Price, int Calories, boolean Dairy, boolean Peanuts,
                       boolean Eggs, boolean Gluten) {
        super(Id, Name, Quantity, Price);
        this.Calories = Calories;
        this.Dairy = Dairy;
        this.Peanuts = Peanuts;
        this.Eggs = Eggs;
        this.Gluten = Gluten;

    }

    public int getCalories() {
        return Calories;
    }

    public void setCalories(int Calories) {
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

    CleaningProduct() {
    }

    ;

    public CleaningProduct(String Id, String Name, int Quantity, double Price,
                           boolean Liquid, String WhereToUse) {
        super(Id, Name, Quantity, Price);
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
    private String Name;

    Customer() {

    }

    ;

    public Customer(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public String toString() {
        return Name;
    }

}

class ClubCustomer extends Customer {
    private String Phone;
    private int Points;

    ClubCustomer() {

    }

    ;

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

    int total;

    public void addPoints(int Points) {
        if (Points > 0) {
            this.Points += Points;
        }
    }

    @Override
    public String toString() {
        return " " + getName() + " has " + Points + "points";
    }
}

class Store {
    private String Name;
    private String Website;
    private ArrayList<Product> pro;

    Store() {
    }

    ;

    Store(String Name, String Website) {
        pro = new ArrayList<Product>();
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
        return pro.size();
    }

    public void addProduct(Product product, int index) {
        if (index > 0 && index <= pro.size()) {
            pro.add(index, product);
        } else {
            addProduct(product);
        }
    }

    public void addProduct(Product product) {
        pro.add(product);
    }

    public Product getProduct(int index) {
        if (index >= 0 && index <= pro.size()) {
            return pro.get(index);
        } else
            return null;
    }

    public int getProductIndex(Product p) {
        if (p == null) {
            return -1;
        } else
            return pro.indexOf(p);
    }
}


