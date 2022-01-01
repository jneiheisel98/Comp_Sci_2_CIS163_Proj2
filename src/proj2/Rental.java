package proj2;


import javax.swing.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Rental implements Serializable {

    /**
     * Serial identifier for this serializable class
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Name of person that is reserving the Rental
     */
    protected String nameOfRenter;

    /**
     * The date the Rental was rented on
     */
    protected GregorianCalendar rentedOn;

    /**
     * The date the Rental was dueBack on
     */
    protected GregorianCalendar dueBack;

    /**
     * The actual date the Rental was returned on
     */
    protected GregorianCalendar actualDateReturned;

    //We can specify the year, month, dayOfMonth, hourOfDay, minute,
    // and second for the default time zone with the default locale:

    /**
     * Default Constructor for Rental
     */
    public Rental() {
        nameOfRenter = "null";
        rentedOn = new GregorianCalendar(2021, 1, 1);
        dueBack = new GregorianCalendar(2021, 1, 2);
        //actualDateReturned = new GregorianCalendar(2021,1,2);
        //don't need this, it will auto return items
    }

    /**
     * Get the cost of the item based on the dates associated with it
     *
     * @param checkOut the date of checkout
     * @return the cost of the item (polymorphic)
     */
    public abstract double getCost(GregorianCalendar checkOut);

    /**
     * Constructor that creates a new rental object
     *
     * @param nameOfRenter       the name of the person renting
     * @param rentedOn           the date of rental
     * @param dueBack            the date the rental is due back
     * @param actualDateReturned the date the rental was actually
     *                           returned
     */
    public Rental(String nameOfRenter,
                  GregorianCalendar rentedOn,
                  GregorianCalendar dueBack,
                  GregorianCalendar actualDateReturned) {

        this.nameOfRenter = nameOfRenter;
        this.rentedOn = rentedOn;
        this.dueBack = dueBack;
        this.actualDateReturned = actualDateReturned;
    }

    /**
     * Get the name of the renter
     *
     * @return nameOfRenter
     */
    public String getNameOfRenter() {
        return nameOfRenter;
    }

    /**
     * Set the name of the renter
     *
     * @param nameOfRenter the name of the rentee
     */
    public void setNameOfRenter(String nameOfRenter) {
        this.nameOfRenter = nameOfRenter;
    }

    /**
     * Get the date the rented item was rented on
     *
     * @return rentedOn the date the item was rented
     */
    public GregorianCalendar getRentedOn() {
        return rentedOn;
    }

    /**
     * Set the date the rented item was rented on
     *
     * @param rentedOn the date the item was rented
     */
    public void setRentedOn(GregorianCalendar rentedOn) {

        this.rentedOn = rentedOn;
    }

    /**
     * Get the actual date of return
     *
     * @return actualDateReturned the date the item was returned
     */
    public GregorianCalendar getActualDateReturned() {
        return actualDateReturned;
    }

    /**
     * Set the actual return date for the item
     *
     * @param actualDateReturned the date the item was returned on
     */
    public void setActualDateReturned(GregorianCalendar
                                              actualDateReturned) {

        this.actualDateReturned = actualDateReturned;
    }

    /**
     * Get the day the item is supposed to be due back
     *
     * @return dueBack the date the item is supposed to be returnedr
     */
    public GregorianCalendar getDueBack() {
        return dueBack;
    }

    /**
     * Sets the due date for rentals
     *
     * @param dueBack the date that a rental is due back
     */
    public void setDueBack(GregorianCalendar dueBack) {

        this.dueBack = dueBack;
    }

    /**
     * A toString method to output given information
     * Used for debugging only to display in the IntelliJ debugger
     *
     * @return a toString that formats the information
     */
    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        String rentedOnStr;
        if (getRentedOn() == null)
            rentedOnStr = "";
        else
            rentedOnStr = formatter.format(getRentedOn().getTime());

        String estdueBackStr;
        if (getDueBack() == null)
            estdueBackStr = "";
        else
            estdueBackStr = formatter.format(getDueBack().getTime());

        String acutaulDateReturnedStr;
        if (getActualDateReturned() == null)
            acutaulDateReturnedStr = "";
        else
            acutaulDateReturnedStr = formatter.format
                    (getActualDateReturned().getTime());

        return "RentUnit{" +
                "guestName='" + nameOfRenter + ' ' +
                ", rentedOn =" + rentedOnStr +
                ", dueBack =" + estdueBackStr +
                ", actualDateReturned =" + acutaulDateReturnedStr +
                '}';
    }
}

