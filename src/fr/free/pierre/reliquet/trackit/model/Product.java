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

public class Product {
    
    private long   barcode = 0;
    
    private String info    = "";
    
    private String title   = "";
    
    public Product() {
    }
    
    public Product(long barcode, String title) {
        super();
        this.barcode = barcode;
        this.title = title;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            return ((Product) o).getBarcode() == this.getBarcode();
        }
        return false;
    }
    
    /**
     * @return the barcode
     */
    public long getBarcode() {
        return this.barcode;
    }
    
    /**
     * @return the info
     */
    public String getInfo() {
        return this.info;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * @param barcode
     *            the barcode to set
     */
    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }
    
    /**
     * @param info
     *            the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }
    
    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String toString() {
        return this.getBarcode() + " - " + this.getTitle() + " - "
                + this.getInfo();
    }
    
}
