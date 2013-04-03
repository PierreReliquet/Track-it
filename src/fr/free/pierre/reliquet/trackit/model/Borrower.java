/**
 * Copyright 2013 Pierre Reliquet©
 * 
 * Track-it is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Track-it is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Track-it. If not, see <http://www.gnu.org/licenses/>
 */
package fr.free.pierre.reliquet.trackit.model;

import org.apache.commons.lang.WordUtils;

public class Borrower {
    
    private long   averageTime   = 0;
    private int    id;
    private String name;
    private int    numberOfLoans = 0;
    
    public Borrower() {
        
    }
    
    public Borrower(int id, String name) {
        super();
        this.setId(id);
        this.setName(name);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Borrower) {
            return ((Borrower) o).getId() == this.getId();
        }
        return false;
    }
    
    /**
     * @return the averageTime
     */
    public long getAverageTime() {
        return this.averageTime;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @return the loansAmount
     */
    public int getNumberOfLoans() {
        return this.numberOfLoans;
    }
    
    /**
     * @param averageTime
     *            the averageTime to set
     */
    public void setAverageTime(long averageTime) {
        this.averageTime = averageTime;
    }
    
    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = WordUtils.capitalizeFully(name, new char[] { ' ', '-' });
    }
    
    /**
     * @param loansAmount
     *            the loansAmount to set
     */
    public void setNumberOfLoans(int loansAmount) {
        this.numberOfLoans = loansAmount;
    }
    
    @Override
    public String toString() {
        return this.getId() + ": " + this.getName();
    }
    
}
