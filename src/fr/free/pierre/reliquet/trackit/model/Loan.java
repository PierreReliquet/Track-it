/**
 * Copyright 2013 Pierre Reliquet©
 * 
 * Track-it is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Track-it is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Track-it. If not, see <http://www.gnu.org/licenses/>
 */
package fr.free.pierre.reliquet.trackit.model;

import java.util.Date;

public class Loan {
    
    private Borrower borrower;
    private Date     date;
    private Date     endDate = null;
    private long     id;
    private Product  product;
    
    public Loan() {
        
    }
    
    public Loan(Product product, Borrower borrower, Date date) {
        super();
        this.product = product;
        this.borrower = borrower;
        this.date = date;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Loan) {
            Loan l = (Loan) o;
            if (!this.getDate().equals(l.getDate())) {
                return false;
            }
            if (!this.getProduct().equals(l.getProduct())) {
                return false;
            }
            if (!this.getBorrower().equals(l.getBorrower())) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    /**
     * @return the borrower
     */
    public Borrower getBorrower() {
        return this.borrower;
    }
    
    /**
     * @return the date
     */
    public Date getDate() {
        return this.date;
    }
    
    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return this.endDate;
    }
    
    /**
     * @return the id
     */
    public long getId() {
        return this.id;
    }
    
    /**
     * @return the product
     */
    public Product getProduct() {
        return this.product;
    }
    
    /**
     * @param borrower
     *            the borrower to set
     */
    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }
    
    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * @param product
     *            the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }
}
