package swiggy.interview.parking.db;

public interface Table {

    String name();

    Table createTable() throws Exception;

    void deleteTable() throws Exception;

}
