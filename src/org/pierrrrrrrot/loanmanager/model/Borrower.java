package org.pierrrrrrrot.loanmanager.model;

import org.apache.commons.lang.WordUtils;

public class Borrower {

    private int id;
    private String name;
    private long averageTime = 0;
    private int numberOfLoans = 0;

    public Borrower() {

    }

    public Borrower(int id, String name) {
        super();
        setId(id);
        setName(name);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name, new char[] { ' ', '-' });
    }

    /**
     * @return the averageTime
     */
    public long getAverageTime() {
        return averageTime;
    }

    /**
     * @param averageTime
     *            the averageTime to set
     */
    public void setAverageTime(long averageTime) {
        this.averageTime = averageTime;
    }

    /**
     * @return the loansAmount
     */
    public int getNumberOfLoans() {
        return numberOfLoans;
    }

    /**
     * @param loansAmount
     *            the loansAmount to set
     */
    public void setNumberOfLoans(int loansAmount) {
        this.numberOfLoans = loansAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Borrower)
            return ((Borrower) o).getId() == getId();
        return false;
    }

    @Override
    public String toString() {
        return getId() + ": " + getName();
    }

}
