package alararestaurant.common;

public class OutputMessages {

    public static final String INVALID_DATA_FOR_ENTITY =
            String.format("Invalid data format!%s", System.lineSeparator());

    public static final String SUCCESSFULLY_IMPORT_ENTITY =
            "Record %s successfully imported." + System.lineSeparator();

    public static final String SUCCESSFULLY_IMPORT_ORDER =
            "Order for %s on %s added." + System.lineSeparator();

}
