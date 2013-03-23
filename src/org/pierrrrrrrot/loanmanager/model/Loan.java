package org.pierrrrrrrot.loanmanager.model;

import java.util.Date;

public class Loan {

    private long id;
    private Product product;
    private Borrower borrower;
    private Date date;
    private Date endDate = null;

    public Loan() {

    }

    public Loan(Product product, Borrower borrower, Date date) {
        super();
        this.product = product;
        this.borrower = borrower;
        this.date = date;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the borrower
     */
    public Borrower getBorrower() {
        return borrower;
    }

    /**
     * @param borrower
     *            the borrower to set
     */
    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Loan) {
            Loan l = (Loan) o;
            if (!getDate().equals(l.getDate()))
                return false;
            if (!getProduct().equals(l.getProduct()))
                return false;
            if (!getBorrower().equals(l.getBorrower()))
                return false;
            return true;
        }
        return false;
    }
}
