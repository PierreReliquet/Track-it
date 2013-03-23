package org.pierrrrrrrot.loanmanager.model;

public class Product {

    private long barcode;

    private String title = "";

    private String info = "";

    public Product(long barcode, String title) {
        super();
        this.barcode = barcode;
        this.title = title;
    }

    public Product() {
    }

    /**
     * @return the barcode
     */
    public long getBarcode() {
        return barcode;
    }

    /**
     * @param barcode
     *            the barcode to set
     */
    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info
     *            the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Product) {
            return ((Product) o).getBarcode() == getBarcode();
        }
        return false;
    }

    @Override
    public String toString() {
        return getBarcode() + " - " + getTitle() + " - " + getInfo();
    }

}
