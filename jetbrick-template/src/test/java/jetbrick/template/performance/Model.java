package jetbrick.template.performance;

import java.util.ArrayList;
import java.util.List;

public final class Model {
    public static int CAPACITY_1 = 1;
    public static int CAPACITY_2 = 2;
    public static int CAPACITY_3 = 3;
    public static int CAPACITY_4 = 4;
    public static int CAPACITY_5 = 5;

    private static int capacity = CAPACITY_2;
    private final int id;
    private final String code;
    private final String name;
    private final double price;
    private final double range;
    private final String amount;
    private final double gravity;

    public Model(int id, String code, String name, double price, double range, String amount, double gravity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.range = range;
        this.amount = amount;
        this.gravity = gravity;
    }

    public final int getId() {
        return this.id;
    }

    public final String getCode() {
        return this.code;
    }

    public final String getName() {
        return this.name;
    }

    public final double getPrice() {
        return this.price;
    }

    public final double getRange() {
        return this.range;
    }

    public final String getAmount() {
        return this.amount;
    }

    public final double getGravity() {
        return this.gravity;
    }

    public static int getCapacity() {
        return capacity;
    }

    public static void setCapacity(int capacity) {
        if (capacity <= 0) {
            Model.capacity = 1;
        } else {
            Model.capacity = capacity;
        }
    }

    public static List<Model> dummyItems() {
        List<Model> items = new ArrayList<Model>();
        if (capacity >= CAPACITY_1) {
            items.add(new Model(1, "600663", "Company 01", 20.550000000000001D, 10.01D, "2.13 HM", 24.289999999999999D));
            items.add(new Model(2, "600822", "Company 02", 14.69D, 10.039999999999999D, "1.56 HM", 36.789999999999999D));
            items.add(new Model(3, "600831", "Company 03", 8.69D, 10.0D, "1.18 HM", 34.68D));
            items.add(new Model(4, "600037", "Company 04", 8.470000000000001D, 10.0D, "1.17 HM", 28.43D));
            items.add(new Model(5, "600880", "Company 05", 25.66D, 6.78D, "1.05 HM", 11.199999999999999D));
            items.add(new Model(6, "600827", "Company 06", 9.369999999999999D, 9.98D, "9310 HM", 23.32D));
            items.add(new Model(7, "000917", "Company 07", 17.030000000000001D, 4.8D, "8312 HM", 8.16D));
            items.add(new Model(8, "603128", "Company 08", 16.559999999999999D, 10.029999999999999D, "7505 HM", 10.619999999999999D));
            items.add(new Model(9, "002315", "Company 09", 52.609999999999999D, 9.99D, "6486 HM", 24.82D));
            items.add(new Model(10, "600633", "Company 10", 45.740000000000002D, 10.0D, "6307 HM", 19.18D));
        }
        if (capacity >= CAPACITY_2) {
            items.add(new Model(11, "601888", "Company 11", 40.979999999999997D, 8.039999999999999D, "5556 HM", 15.460000000000001D));
            items.add(new Model(12, "600650", "Company 12", 12.800000000000001D, 7.74D, "5516 HM", 17.27D));
            items.add(new Model(13, "002277", "Company 13", 12.779999999999999D, 6.95D, "5285 HM", 15.029999999999999D));
            items.add(new Model(14, "002405", "Company 14", 13.85D, 3.2D, "5027 HM", 14.949999999999999D));
            items.add(new Model(15, "002185", "Company 15", 9.710000000000001D, 3.96D, "5019 HM", 27.460000000000001D));
            items.add(new Model(16, "600637", "Company 16", 42.219999999999999D, 5.6D, "4962 HM", 8.300000000000001D));
            items.add(new Model(17, "000538", "Company 17", 105.0D, -1.39D, "4942 HM", 9.449999999999999D));
            items.add(new Model(18, "002317", "Company 18", 23.489999999999998D, 7.65D, "4566 HM", 22.23D));
            items.add(new Model(19, "000839", "Company 19", 7.28D, 6.28D, "4475 HM", 22.059999999999999D));
            items.add(new Model(20, "000419", "Company 20", 6.79D, 8.289999999999999D, "4330 HM", 19.829999999999998D));
        }
        if (capacity >= CAPACITY_3) {
            items.add(new Model(21, "000665", "Company 21", 13.609999999999999D, 10.02D, "4079 HM", 15.890000000000001D));
            items.add(new Model(22, "002161", "Company 22", 9.470000000000001D, 3.61D, "3960 HM", 10.369999999999999D));
            items.add(new Model(23, "002027", "Company 23", 7.98D, 5.7D, "3712 HM", 8.07D));
            items.add(new Model(24, "600202", "Company 24", 6.43D, 0.94D, "3561 HM", 22.969999999999999D));
            items.add(new Model(25, "600535", "Company 25", 43.829999999999998D, 2.62D, "3511 HM", 8.960000000000001D));
            items.add(new Model(26, "000909", "Company 26", 7.03D, 4.93D, "3478 HM", 25.66D));
            items.add(new Model(27, "300294", "Company 27", 43.780000000000001D, 10.0D, "3388 HM", 24.129999999999999D));
            items.add(new Model(28, "600406", "Company 28", 16.16D, 0.31D, "3336 HM", 13.49D));
            items.add(new Model(29, "600867", "Company 29", 17.030000000000001D, 2.96D, "3151 HM", 19.050000000000001D));
            items.add(new Model(30, "300347", "Company 30", 58.380000000000003D, 7.22D, "3088 HM", 15.83D));
        }
        if (capacity >= CAPACITY_4) {
            items.add(new Model(31, "000156", "Company 31", 27.93D, 5.08D, "2955 HM", 15.050000000000001D));
            items.add(new Model(32, "300330", "Company 32", 16.73D, 9.99D, "2879 HM", 18.100000000000001D));
            items.add(new Model(33, "600415", "Company 33", 7.15D, 2.88D, "2819 HM", 4.5D));
            items.add(new Model(34, "002238", "Company 34", 13.779999999999999D, 6.82D, "2741 HM", 18.379999999999999D));
            items.add(new Model(35, "600832", "Company 35", 8.039999999999999D, 4.69D, "2683 HM", 5.07D));
            items.add(new Model(36, "002428", "Company 36", 13.359999999999999D, 0.23D, "2652 HM", 21.640000000000001D));
            items.add(new Model(37, "002223", "Company 37", 21.280000000000001D, 5.03D, "2617 HM", 16.859999999999999D));
            items.add(new Model(38, "000058", "Company 38", 5.0D, 2.88D, "2610 HM", 20.960000000000001D));
            items.add(new Model(39, "300246", "Company 39", 20.129999999999999D, 10.0D, "2440 HM", 21.23D));
            items.add(new Model(40, "300038", "Company 40", 17.77D, 10.029999999999999D, "2375 HM", 20.789999999999999D));
        }
        if (capacity >= CAPACITY_5) {
            items.add(new Model(41, "601928", "Company 41", 13.07D, 2.91D, "2345 HM", 4.04D));
            items.add(new Model(42, "601933", "Company 42", 13.42D, 1.05D, "2287 HM", 16.350000000000001D));
            items.add(new Model(43, "002648", "Company 43", 28.09D, 3.73D, "2286 HM", 11.630000000000001D));
            items.add(new Model(44, "600551", "Company 44", 14.19D, 6.37D, "2256 HM", 14.4D));
            items.add(new Model(45, "300232", "Company 45", 16.280000000000001D, 10.0D, "2245 HM", 25.829999999999998D));
            items.add(new Model(46, "002007", "Company 46", 27.199999999999999D, 2.41D, "2238 HM", 9.039999999999999D));
            items.add(new Model(47, "000548", "Company 47", 6.36D, 9.66D, "2213 HM", 16.48D));
            items.add(new Model(48, "300274", "Company 48", 27.300000000000001D, 9.199999999999999D, "2155 HM", 22.309999999999999D));
            items.add(new Model(49, "300045", "Company 49", 15.710000000000001D, 6.65D, "2106 HM", 13.81D));
            items.add(new Model(50, "300247", "Company 50", 11.15D, 9.960000000000001D, "2083 HM", 13.869999999999999D));
        }
        for (int i = 0; i < capacity - CAPACITY_5; i++) {
            items.add(new Model(51, "601929", "Company 51", 13.07D, 2.91D, "2345 HM", 4.04D));
            items.add(new Model(52, "601934", "Company 52", 13.42D, 1.05D, "2287 HM", 16.350000000000001D));
            items.add(new Model(53, "002649", "Company 53", 28.09D, 3.73D, "2286 HM", 11.630000000000001D));
            items.add(new Model(54, "600552", "Company 54", 14.19D, 6.37D, "2256 HM", 14.4D));
            items.add(new Model(55, "300233", "Company 55", 16.280000000000001D, 10.0D, "2245 HM", 25.829999999999998D));
            items.add(new Model(56, "002008", "Company 56", 27.199999999999999D, 2.41D, "2238 HM", 9.039999999999999D));
            items.add(new Model(57, "000549", "Company 57", 6.36D, 9.66D, "2213 HM", 16.48D));
            items.add(new Model(58, "300275", "Company 58", 27.300000000000001D, 9.199999999999999D, "2155 HM", 22.309999999999999D));
            items.add(new Model(59, "300046", "Company 59", 15.710000000000001D, 6.65D, "2106 HM", 13.81D));
            items.add(new Model(60, "300248", "Company 60", 11.15D, 9.960000000000001D, "2083 HM", 13.869999999999999D));
        }
        return items;
    }
}
