package pl.marczak.tauronrealityhack.model;

/**
 * Created by Admin on 2016-06-11.
 */
public class SectorResponse {
    private int id;
    private String name;
    private String majority;


    public SectorResponse(Integer id, String name, String majority) {
        this.id = id;
        this.name = name;
        this.majority = majority;
    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getMajority() {
        return majority;
    }

    public void setMajority(String majority) {
        this.majority = majority;
    }
}
