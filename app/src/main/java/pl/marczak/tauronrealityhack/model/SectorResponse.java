package pl.marczak.tauronrealityhack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016-06-11.
 */
public class SectorResponse {
    private Integer id;
    private String name;
    private List<Object> arenaUsers = new ArrayList<Object>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
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

    /**
     *
     * @return
     * The arenaUsers
     */
    public List<Object> getArenaUsers() {
        return arenaUsers;
    }

    /**
     *
     * @param arenaUsers
     * The arenaUsers
     */
    public void setArenaUsers(List<Object> arenaUsers) {
        this.arenaUsers = arenaUsers;
    }
}
