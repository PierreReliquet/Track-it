package org.pierrrrrrrot.loanmanager.model;

public class Borrower {

    private int id;
    private String name;
    private int averageTime = 0;
    private int loansAmount = 0;

    public Borrower() {

    }

    public Borrower(int id, String name) {
        super();
        this.id = id;
        this.name = name;
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
        this.name = name;
    }

    /**
     * @return the averageTime
     */
    public int getAverageTime() {
        return averageTime;
    }

    /**
     * @param averageTime
     *            the averageTime to set
     */
    public void setAverageTime(int averageTime) {
        this.averageTime = averageTime;
    }

    /**
     * @return the loansAmount
     */
    public int getLoansAmount() {
        return loansAmount;
    }

    /**
     * @param loansAmount
     *            the loansAmount to set
     */
    public void setLoansAmount(int loansAmount) {
        this.loansAmount = loansAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Borrower)
            return ((Borrower) o).getId() == getId();
        return false;
    }

}
