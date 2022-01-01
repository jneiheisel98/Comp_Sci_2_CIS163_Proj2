package proj2;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/***********************************************************************
 * @Author Gage Elenbaas
 * @Author Jacob Neiheisel
 * @Author Devin Elenbaas
 * @Author Professor Ferguson (previous author)
 *
 * @version 3/18/21
 *
 * The following is the List Model class, this class performs many
 * functions and is an integral part of the entire program.
 *
 * This class creates and initializes a list model, 2 array lists,
 * and a screen display among other things. This class will filter
 * a listofrentals into a filteredlistrentals in order to correctly
 * display corresponding information on a screen.
 ***********************************************************************/

public class ListModel extends AbstractTableModel {

    /**
     * holds all the rentals
     */
    private ArrayList<Rental> listOfRentals;

    /**
     * holds only the rentals that are to be displayed
     */
    private ArrayList<Rental> filteredListRentals;

    /**
     * current screen being displayed
     */
    private ScreenDisplay display = ScreenDisplay.CurrentRentalStatus;

    /**
     * column labels for current rentals screen
     */
    private String[] columnNamesCurrentRentals = {"Renter\'s Name",
            "Est. Cost",
            "Rented On", "Due Date ", "Console", "Name of the Game"};

    /**
     * column labels for returned screen
     */
    private String[] columnNamesReturned = {"Renter\'s Name",
            "Rented On Date",
            "Due Date", "Actual date returned ", "Est. Cost",
            " Real Cost"};

    /**
     * column labels for everything screen
     */
    private String[] columnNamesEverything = {"Renter\'s Name",
            "Rented On Date", "Due Date", "Actual date returned ",
            "Est. Cost", " Real Cost", "Console", "Name of Game"};

    /**
     * column labels for late rentals screen
     */
    private String[] columnNamesLateRentals = {"Renter\'s Name",
            "Rented On Date",
            "Due Date", "Number of Days Late", "Console",
            "Name of Game"};

    /**
     * formatter of dates throughout the program
     */
    private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    /******************************************************************
     * Listmodel constructor
     */
    public ListModel() {
        display = ScreenDisplay.CurrentRentalStatus;
        listOfRentals = new ArrayList<>();
        filteredListRentals = new ArrayList<>();
        updateScreen();
        createList();
    }

    /******************************************************************
     * This method determines if a valid set of dates
     * have been entered. The date rented must be before
     * the date due
     *
     * @param dateRentedOn the gregorian calendar date that the
     *                     rental was rented on
     * @param dateDue the gregorian calendar date that the rental is
     *                due on
     * @return boolean if a valid set of dates were entered
     * @throws IllegalArgumentException - if the dates are bad
     */
    public boolean dateBefore(GregorianCalendar dateRentedOn,
                              GregorianCalendar dateDue) {

        if(dateRentedOn.compareTo(dateDue) > 0){
            throw new IllegalArgumentException();
        }
        return true;
    }

    /******************************************************************
     * This method switches the current screen display
     * to the screen a user wishes to view
     *
     * @param selected the screen a user wishes to view
     */
    public void setDisplay(ScreenDisplay selected) {
        display = selected;
        updateScreen();
    }

    /******************************************************************
     * This method updates the current screen that the user is viewing
     */
    private void updateScreen() {

        //update the screen that is currently on display
        switch (display) {
            //Current rental screen
            case CurrentRentalStatus:
                try {
                    filteredListRentals = (ArrayList<Rental>)
                            listOfRentals.stream()
                                    .filter(n -> n.actualDateReturned
                                            == null)
                                    .filter(n -> dateBefore(n.getRentedOn()
                                            , n.getDueBack()))
                                    .map(r -> {
                                        r.setNameOfRenter(r.getNameOfRenter
                                                ().toLowerCase());
                                        return r;
                                    })
                                    .map(r -> {
                                        r.setNameOfRenter
                                                (r.getNameOfRenter().
                                                        substring(0, 1)
                                                        .toUpperCase()
                                                        + r.
                                                        getNameOfRenter()
                                                        .substring(1));
                                        return r;
                                    })
                                    .collect(Collectors.toList());

                    // Note: This uses Lambda function
                    Collections.sort(filteredListRentals,
                            (n1, n2) -> n1.nameOfRenter.
                                    toLowerCase().
                                    compareTo(n2.nameOfRenter.
                                            toLowerCase()));
                }
                catch (StringIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(null,
                            "Enter a Name");
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                catch (IllegalArgumentException e){
                    JOptionPane.showMessageDialog(null,
                            "Due date can't be before Rental date"
                    );
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                break;

            //returned screen
            case ReturnedItems:
                try {
                    filteredListRentals = (ArrayList<Rental>)
                            listOfRentals.stream()
                                    .filter(n -> n.actualDateReturned
                                            != null)
                                    .filter(n -> dateBefore(n
                                                    .getRentedOn(),
                                            n.getDueBack()))
                                    .map(r -> {
                                        r.setNameOfRenter(r.
                                                getNameOfRenter().
                                                toLowerCase());
                                        return r;
                                    })
                                    .map(r -> {
                                        r.setNameOfRenter
                                                (r.getNameOfRenter().
                                                        substring(0, 1)
                                                        .toUpperCase()
                                                        +
                                                        r.getNameOfRenter()
                                                                .substring(1));
                                        return r;
                                    })
                                    .collect(Collectors.toList());

                    // Note: This uses an anonymous class.
                    Collections.sort(filteredListRentals,
                            new Comparator<Rental>() {
                                @Override
                                public int compare(Rental n1, Rental n2) {
                                    return n1.nameOfRenter.toLowerCase().
                                            compareTo(n2.nameOfRenter.
                                                    toLowerCase());
                                }
                            });
                }
                catch(StringIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(null,
                            "Enter a Name");
                    listOfRentals.remove(listOfRentals.
                            size()-1);
                }
                catch (IllegalArgumentException e){
                    JOptionPane.showMessageDialog(null,
                            "Due date can't be before Rental " +
                                    "date");
                    listOfRentals.remove(listOfRentals.size()-1);
                }

                break;

            //due within week screen
            case DueWithInWeek:
                try {
                    filteredListRentals = (ArrayList<Rental>)
                            listOfRentals.stream()
                                    .filter(n -> n.actualDateReturned
                                            == null)
                                    .filter(rental -> daysBetween(rental.
                                            getRentedOn(), rental.
                                            getDueBack()) <= 7)
                                    .filter(n -> dateBefore(n.getRentedOn()
                                            , n.getDueBack()))
                                    .map(r -> {
                                        r.setNameOfRenter(r.getNameOfRenter
                                                ().toLowerCase());
                                        return r;
                                    })
                                    .map(r -> {
                                        r.setNameOfRenter
                                                (r.getNameOfRenter().
                                                        substring(0, 1)
                                                        .toUpperCase()
                                                        + r.
                                                        getNameOfRenter()
                                                        .substring(1));
                                        return r;
                                    })
                                    .collect(Collectors.toList());
                }catch(StringIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(null,
                            "Enter a Name");
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                catch (IllegalArgumentException e){
                    JOptionPane.showMessageDialog(null,
                            "Due date can't be before Rental " +
                                    "date");
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                break;

            //due within a week games first screen
            case DueWithinWeekGamesFirst:
                try {
                    filteredListRentals = (ArrayList<Rental>)
                            listOfRentals.stream()
                                    .filter(n -> n.actualDateReturned
                                            == null)
                                    .filter(r -> daysBetween(r.
                                                    getRentedOn(),
                                            r.getDueBack()) <= 7)
                                    .filter(n -> dateBefore(n.getRentedOn()
                                            , n.getDueBack()))
                                    .sorted(new GameConsoleSorter())
                                    .sorted(new GameConsoleAlphabetizer())
                                    .map(r -> {
                                        r.setNameOfRenter(r.
                                                getNameOfRenter().
                                                toLowerCase());
                                        return r;
                                    })
                                    .map(r -> {
                                        r.setNameOfRenter
                                                (r.getNameOfRenter().
                                                        substring(0, 1)
                                                        .toUpperCase()
                                                        + r.
                                                        getNameOfRenter()
                                                        .substring(1));
                                        return r;
                                    })
                                    .collect(Collectors.toList());
                }catch(StringIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(null,
                            "Enter a Name");
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                catch (IllegalArgumentException e){
                    JOptionPane.showMessageDialog(null,
                            "Due date can't be before Rental " +
                                    "date");
                    e.printStackTrace();
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                break;

            //capital days overdue screen
            case Cap14DaysOverdue:
                try {
                    filteredListRentals = (ArrayList<Rental>)
                            listOfRentals.stream()
                                    .filter(n -> n.actualDateReturned
                                            == null)
                                    .filter(n -> dateBefore(n.getRentedOn()
                                            , n.getDueBack()))
                                    .filter(r-> !r.getNameOfRenter()
                                            .isBlank())

                                    .filter(r -> daysBetween(
                                            r.getRentedOn()
                                            , r.getDueBack()) >= 14 ||
                                            daysBetween(r.getRentedOn(), r.
                                                    getDueBack()) <= 7)
                                    .map(r -> {
                                        if (daysBetween(r.getRentedOn(), r.
                                                getDueBack()) >= 14) {
                                            r.setNameOfRenter(r.getNameOfRenter().
                                                    toUpperCase());
                                        }
                                        return r;
                                    })
                                    .map(r -> {
                                        if (r.getNameOfRenter().length()==0) {
                                            throw new
                                                    StringIndexOutOfBoundsException();
                                        }
                                        return r;
                                    })
                                    .sorted(new fourteenDaySorterFirst())
                                    .collect(Collectors.toList());

                }catch(StringIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(null,
                            "Enter a Name");
                    listOfRentals.remove(listOfRentals.
                            size()-1);

                }
                catch (IllegalArgumentException e){
                    JOptionPane.showMessageDialog(null,
                            "Due date can't be before Rental " +
                                    "date");
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                break;

            case Everything:
                try {
                    filteredListRentals = (ArrayList<Rental>)
                            listOfRentals.stream()
                                    .filter(n -> dateBefore(n.getRentedOn()
                                            ,
                                            n.getDueBack()))
                                    .map(r -> {
                                        r.setNameOfRenter(r.
                                                getNameOfRenter()
                                                .toLowerCase());
                                        return r;
                                    })
                                    .map(r -> {
                                        r.setNameOfRenter
                                                (r.getNameOfRenter()
                                                        .substring(0, 1)
                                                        .toUpperCase()
                                                        + r
                                                        .getNameOfRenter()
                                                        .substring(1));
                                        return r;
                                    })
                                    .collect(Collectors.toList());

                    // Note: This uses an anonymous class.
                    //This sorts by alphabetical order'
                    Collections.sort(filteredListRentals,
                            new Comparator<Rental>() {
                                @Override
                                public int compare(Rental n1, Rental n2) {
                                    return n1.nameOfRenter.toLowerCase()
                                            .compareTo(n2.
                                                    nameOfRenter.toLowerCase());
                                }
                            });
                }catch(StringIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(null,
                            "Enter a Name");
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                catch (IllegalArgumentException e){
                    JOptionPane.showMessageDialog(null,
                            "Due date can't be before Rental" +
                                    " date");
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                break;

            //late rentals screen, this was added on for task 4
            case LateRentals:
                try{
                    filteredListRentals = (ArrayList<Rental>)
                            listOfRentals.stream()
                                    .filter(n -> n.actualDateReturned
                                            == null)
                                    .map(r -> {
                                        r.setNameOfRenter(r
                                                .getNameOfRenter()
                                                .toLowerCase());
                                        return r;
                                    })
                                    .map(r -> {
                                        r.setNameOfRenter
                                                (r.getNameOfRenter()
                                                        .substring(0, 1)
                                                        .toUpperCase()
                                                        + r
                                                        .getNameOfRenter()
                                                        .substring(1));
                                        return r;
                                    })
                                    .filter(r -> daysBetween(r.getDueBack(),
                                            new GregorianCalendar())>0)
                                    .collect(Collectors.toList());
                    Collections.sort(filteredListRentals,
                            new Comparator<Rental>() {
                                @Override
                                public int compare(Rental r1, Rental r2) {
                                    return r1.getClass().getName()
                                            .compareTo(r2.getClass()
                                                    .getName())*-1;

                                }
                            });
                    Collections.sort(filteredListRentals,
                            new Comparator<Rental>() {
                                @Override
                                public int compare(Rental n1, Rental n2) {
                                    Integer rental1 = (daysBetween(n1
                                            .getDueBack(), new
                                            GregorianCalendar()));
                                    Integer rental2 = (daysBetween(n2
                                                    .getDueBack(),
                                            new GregorianCalendar()));
                                    int comparer = 0;
                                    if (n1 instanceof Game && n2 instanceof
                                            Game) {
                                        if(rental1.compareTo(rental2)
                                                != 0) {
                                            comparer = rental1
                                                    .compareTo(rental2);
                                        }
                                        else{
                                            comparer = n1.getNameOfRenter()
                                                    .compareTo(n2.
                                                            getNameOfRenter
                                                                    ());
                                        }

                                    } else if (n1 instanceof Console && n2
                                            instanceof Console) {
                                        if(rental1.compareTo(rental2)!=0) {
                                            comparer = rental1
                                                    .compareTo(rental2);
                                        }
                                        else{
                                            comparer = n1.getNameOfRenter()
                                                    .compareTo(n2.
                                                            getNameOfRenter
                                                                    ());
                                        }
                                    }
                                    return comparer;
                                }
                            });
                }
                catch(StringIndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(null,
                            "Enter a Name");
                    listOfRentals.remove(listOfRentals.size()-1);
                }
                catch (IllegalArgumentException e){
                    JOptionPane.showMessageDialog(null,
                            "Due date can't be before Rental" +
                                    " date");
                    listOfRentals.remove(listOfRentals.size()-1);
                }

                break;

            default:
                throw new RuntimeException("upDate is in undefined " +
                        "state: " + display);
        }
        fireTableStructureChanged();
    }

    /******************************************************************
     * Private helper method to count the number of days between two
     * GregorianCalendar dates
     * Note that this is the proper way to do this; trying to use other
     * classes/methods likely won't properly account for leap days
     * @param startDate - the beginning/starting day
     * @param endDate - the last/ending day
     * @return int for the number of days between startDate and endDate
     */
    private int daysBetween(GregorianCalendar startDate,
                            GregorianCalendar endDate) {
        // Determine how many days the Game was rented out
        GregorianCalendar gTemp = new GregorianCalendar();
        gTemp = (GregorianCalendar) endDate.clone();
        //  gTemp = dueBack;  does not work!!
        int daysBetween = 0;
        while (gTemp.compareTo(startDate) > 0) {
            gTemp.add(Calendar.DATE, -1);
            // this subtracts one day from gTemp
            daysBetween++;
        }
        return daysBetween;
    }

    /******************************************************************
     * A method to get the name of a column at a specified integer
     * @param col - the column number
     * @return the label at the column
     * @throws RuntimeException if an undefined state happens for
     * column name
     */
    @Override
    public String getColumnName(int col) {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals[col];
            case ReturnedItems:
                return columnNamesReturned[col];
            case DueWithInWeek:
                return columnNamesCurrentRentals[col];
            case Cap14DaysOverdue:
                return columnNamesCurrentRentals[col];
            case DueWithinWeekGamesFirst:
                return columnNamesCurrentRentals[col];
            case Everything:
                return columnNamesEverything[col];
            case LateRentals:
                return columnNamesLateRentals[col];

        }
        throw new RuntimeException("Undefined state for Col Names: " +
                "" + display);
    }

    /******************************************************************
     * A method which returns the number of columns on a screen
     * @return an integer of the number of columns
     * @throws IllegalArgumentException - if an invalid case happens
     */
    @Override
    public int getColumnCount() {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals.length;
            case ReturnedItems:
                return columnNamesReturned.length;
            case DueWithInWeek:
                return columnNamesCurrentRentals.length;
            case Cap14DaysOverdue:
                return columnNamesCurrentRentals.length;
            case DueWithinWeekGamesFirst:
                return columnNamesCurrentRentals.length;
            case Everything:
                return columnNamesEverything.length;
            case LateRentals:
                return columnNamesLateRentals.length;

        }
        throw new IllegalArgumentException();
    }

    /******************************************************************
     * A method that returns the number of rows on the current screen
     * @return filteredListRentals.size(); - the number of rows
     * on a screen
     */
    @Override
    public int getRowCount() {
        return filteredListRentals.size();
        // returns number of items in the arraylist
    }

    /******************************************************************
     * A method that returns the value at a specified row and column
     * @param row - the row location
     * @param col - the column location
     * @return the value at the row column location
     * @throws IllegalArgumentException - if invalid case happens
     */
    @Override
    public Object getValueAt(int row, int col) {
        switch (display) {
            case CurrentRentalStatus:
                return currentRentScreen(row, col);
            case ReturnedItems:
                return rentedOutScreen(row, col);
            case DueWithInWeek:
                return currentRentScreen(row, col);
            case Cap14DaysOverdue:
                return currentRentScreen(row, col);
            case DueWithinWeekGamesFirst:
                return currentRentScreen(row, col);
            case Everything:
                return everythingScreen(row, col);
            case LateRentals:
                return lateRentalScreen(row, col);


        }
        throw new IllegalArgumentException();
    }

    /******************************************************************
     * A method that places values at specified rows and column
     * locations in the current rental screen
     * @param row - specified row location
     * @param col - specified column location
     * @return the object located at the specified location
     * @throws RuntimeException - if default case is reached
     */
    private Object currentRentScreen(int row, int col) {

        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);

            case 1:
                return (filteredListRentals.get(row).getCost
                        (filteredListRentals.get(row).dueBack));

            case 2:
                return (formatter.format(filteredListRentals.
                        get(row).rentedOn.getTime()));

            case 3:
                if (filteredListRentals.get(row).dueBack == null)
                    return "-";

                return (formatter.format(filteredListRentals.get(row).
                        dueBack.getTime()));

            case 4:
                if (filteredListRentals.get(row) instanceof Console)
                    return (((Console) filteredListRentals.get(row))
                            .getConsoleType());
                else {
                    if (filteredListRentals.get(row) instanceof Game)
                        if (((Game) filteredListRentals.get(row)).
                                getConsole() != null)
                            return ((Game) filteredListRentals.
                                    get(row)).getConsole();
                        else
                            return "";
                }

            case 5:
                if (filteredListRentals.get(row) instanceof Game)
                    return (((Game) filteredListRentals.get(row))
                            .getNameGame());
                else
                    return "";
            default:
                throw new RuntimeException("Row,col out of range: "
                        + row + " " + col);
        }
    }

    /******************************************************************
     * A method that returns the object values at a specified
     * column and row location for the rented screen
     *@param row - specified row location
     *@param col - specified column location
     *@return the object located at the specified location
     * @throws RuntimeException - if default case is reached
     */
    private Object rentedOutScreen(int row, int col) {
        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);

            case 1:
                return (formatter.format(filteredListRentals.get(row)
                        .rentedOn.
                                getTime()));
            case 2:
                return (formatter.format(filteredListRentals.get(row).
                        dueBack.
                        getTime()));
            case 3:
                return (formatter.format(filteredListRentals.get(row).
                        actualDateReturned.getTime()));

            case 4:
                return (filteredListRentals.
                        get(row).getCost(filteredListRentals.get(row).
                        dueBack));

            case 5:
                return (filteredListRentals.
                        get(row).getCost(filteredListRentals.get(row).
                        actualDateReturned
                ));

            default:
                throw new RuntimeException("Row,col out of range: " +
                        row + " " + col);
        }
    }

    /******************************************************************
     * A method that returns the object values at a specified
     * column and row location for the everything screen
     *@param row - specified row location
     *@param col - specified column location
     *@return the object located at the specified location
     * @throws RuntimeException - if default case is reached
     */
    private Object everythingScreen(int row, int col) {
        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);

            case 1:
                return (formatter.format(filteredListRentals.get(row)
                        .rentedOn.
                                getTime()));
            case 2:
                return (formatter.format(filteredListRentals.get(row)
                        .dueBack.
                                getTime()));
            case 3:
                if(filteredListRentals.get(row).
                        actualDateReturned!= null) {
                    return (formatter.format(filteredListRentals
                            .get(row).
                                    actualDateReturned.getTime()));
                }
                else{
                    return "";
                }

            case 4:
                return (filteredListRentals.
                        get(row).getCost(filteredListRentals.get(row)
                        .dueBack));

            case 5:
                if(filteredListRentals.
                        get(row).
                        actualDateReturned != null) {
                    return (filteredListRentals.
                            get(row).getCost(filteredListRentals
                            .get(row)
                            .actualDateReturned
                    ));
                }
                else{
                    return "";
                }

            case 6:
                if (filteredListRentals.get(row) instanceof Console)
                    return (((Console) filteredListRentals.get(row))
                            .getConsoleType());
                else {
                    if (filteredListRentals.get(row) instanceof Game)
                        if (((Game) filteredListRentals.get(row))
                                .getConsole() != null)
                            return ((Game) filteredListRentals
                                    .get(row))
                                    .getConsole();
                        else
                            return "";
                }
            case 7:
                if (filteredListRentals.get(row) instanceof Game)
                    return (((Game) filteredListRentals.get(row))
                            .getNameGame());
                else
                    return "";

            default:
                throw new RuntimeException("Row,col out of range: "
                        + row + " " + col);
        }
    }

    /******************************************************************
     * A method that returns the object values at a specified
     * column and row location for the late rental screen
     *@param row - specified row location
     *@param col - specified column location
     *@return the object located at the specified location
     * @throws RuntimeException - if row or column out of range
     */
    private Object lateRentalScreen(int row, int col) {
        switch (col) {
            case 0:
                return (filteredListRentals.get(row).nameOfRenter);

            case 1:
                return (formatter.format(filteredListRentals.get(row)
                        .rentedOn.
                                getTime()));
            case 2:
                return (formatter.format(filteredListRentals.get(row)
                        .dueBack.
                                getTime()));
            case 3:
                return daysBetween(filteredListRentals.get(row).
                        getDueBack(), new GregorianCalendar());



            case 4:
                if (filteredListRentals.get(row) instanceof Console)
                    return (((Console) filteredListRentals.
                            get(row)).getConsoleType());
                else {
                    if (filteredListRentals.get(row) instanceof Game)
                        if (((Game) filteredListRentals.get(row))
                                .getConsole() != null)
                            return ((Game) filteredListRentals
                                    .get(row)).getConsole();
                        else
                            return "";
                }
            case 5:
                if (filteredListRentals.get(row) instanceof Game)
                    return (((Game) filteredListRentals
                            .get(row)).getNameGame());
                else
                    return "";

            default:
                throw new RuntimeException("Row,col out of range: "
                        + row + " " + col);
        }
    }


    /******************************************************************
     * A method to add a rental to the list of rentals arraylist
     * @param a - the rental object to be added to list of rentals
     */
    public void add(Rental a) {
        listOfRentals.add(a);
        updateScreen();
    }

    /******************************************************************
     * A method which returns the rental object from a filtered list
     * of rentals at a specified integer
     * @param i - specified integer to locate rental object
     * @return filteredListRentals(i) the rental object
     */
    public Rental get(int i) {
        return filteredListRentals.get(i);
    }

    /******************************************************************
     * A method to update the rental screen
     * @param index location of object to be updated
     * @param unit object to be updated
     */
    public void update(int index, Rental unit) {
        updateScreen();
    }

    /******************************************************************
     * A method to save the current data in the entire program
     * as a new database
     * @param filename - the name of the file created or saved to
     * @throws RuntimeException - if problem with saving
     */
    public void saveDatabase(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            System.out.println(listOfRentals.toString());
            os.writeObject(listOfRentals);
            os.close();
        } catch (IOException ex) {
            throw new RuntimeException("Saving problem! " + display);
        }
    }

    /******************************************************************
     * A method to load a database of data
     * @param filename - name of the file to be loaded in
     */
    public void loadDatabase(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            listOfRentals.clear();

            listOfRentals = (ArrayList<Rental>) is.readObject();
            updateScreen();
            is.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error Opening File");
        }

    }

    /******************************************************************
     * This method saves the data as a text file
     * @param filename - the name of the file created or saved to
     * @return a boolean of success or failure
     */
    public boolean saveAsText(String filename) {
        if (filename.equals("")) {
            throw new IllegalArgumentException();
        }

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(filename)));
            out.println(listOfRentals.size());
            for (int i = 0; i < listOfRentals.size(); i++) {
                Rental unit = listOfRentals.get(i);
                out.println(unit.getClass().getName());
                out.println("Name is " + unit.getNameOfRenter());
                out.println("Rented on " + formatter.format(unit
                        .rentedOn.getTime()));
                out.println("DueDate " + formatter.format(unit
                        .dueBack.getTime()));

                if (unit.getActualDateReturned() == null)
                    out.println("Not returned!");
                else
                    out.println(formatter.format(unit
                            .actualDateReturned.getTime()));

                if (unit instanceof Game) {
                    out.println(((Game) unit).getNameGame());
                    if (((Game) unit).getConsole() != null)
                        out.println(((Game) unit).getConsole());
                    else
                        out.println("No Console");
                }

                if (unit instanceof Console)
                    out.println(((Console) unit).getConsoleType());
            }
            out.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /******************************************************************
     * A method to load from text files
     * @param filename - file to be loaded in
     * @throws FileNotFoundException - If a file is not found
     */
    public void loadFromText(String filename)
            throws FileNotFoundException {
        if (filename == null)
            throw new IllegalArgumentException();

        //variables for scanned entries
        String entries = null;
        String project = null;
        String nameOfRenter = null;
        String dateRented = null;
        String dueDate = null;
        String isReturned = null;
        String titleOfGame = null;
        String nameOfConsole = null;

        Scanner scanner = null;

        try {
            scanner = new Scanner(new File(filename));
            if(scanner.hasNext()) {
                entries = scanner.nextLine();
            }

            int sizeOfText = 0;

            sizeOfText = Integer.parseInt(entries);



            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            listOfRentals.clear(); //clear the list

            int i = 0;
            for(i = 0; i < sizeOfText; i++){
                project = scanner.nextLine();
                nameOfRenter= scanner.nextLine();
                dateRented = scanner.nextLine();
                dueDate = scanner.nextLine();
                isReturned = scanner.nextLine();

                nameOfRenter = nameOfRenter.substring(8);

                GregorianCalendar rented = new GregorianCalendar();
                GregorianCalendar dateDue = new GregorianCalendar();
                GregorianCalendar returnDate = new GregorianCalendar();

                Date temp = df.parse(dateRented.substring(10));
                rented.setTime(temp);

                temp = df.parse(dueDate.substring(8));
                dateDue.setTime(temp);

                //if the item is returned
                if(!isReturned.equals("Not returned!")){
                    temp = df.parse(isReturned);
                    returnDate.setTime(temp);
                }
                else{
                    returnDate = null;
                }

                if(project.equalsIgnoreCase("project2.Game")){
                    titleOfGame = scanner.nextLine();
                    nameOfConsole = scanner.nextLine();

                    //prevent tampered file from being loaded

                    if (nameOfConsole.equals("No Console")) {
                        Game game = new Game
                                (nameOfRenter, rented, dateDue,
                                        returnDate, titleOfGame,
                                        null);
                        listOfRentals.add(game);
                    }
                    else if (!ConsoleTypes.valueOf(nameOfConsole).toString().equals(nameOfConsole))
                        throw new IllegalArgumentException();
                    else{
                        Game game = new Game
                                (nameOfRenter, rented, dateDue,
                                        returnDate, titleOfGame,
                                        ConsoleTypes.valueOf(nameOfConsole));

                        listOfRentals.add(game);
                    }
                }
                else if(project.equalsIgnoreCase("project2.Console")){
                    nameOfConsole = scanner.nextLine();
                    Console console;
                    if(nameOfConsole.equals("No Console"))
                        throw new IllegalArgumentException();
                    else if (!ConsoleTypes.valueOf(nameOfConsole).toString().equals(nameOfConsole))
                        throw new IllegalArgumentException();
                    else{
                        console = new Console(nameOfRenter,
                                rented,dateDue,returnDate,
                                ConsoleTypes.valueOf(nameOfConsole));
                        listOfRentals.add(console);
                    }

                }
                else{
                    throw new IllegalArgumentException
                            ("File tampered");
                }

            }//for for loop
        }catch (FileNotFoundException | ParseException |
                IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
        updateScreen();
    }

    /******************************************************************
     *
     *  DO NOT MODIFY THIS METHOD!!!!!!
     */
    public void createList() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();
        GregorianCalendar g6 = new GregorianCalendar();
        GregorianCalendar g7 = new GregorianCalendar();
        GregorianCalendar g8 = new GregorianCalendar();

        try {
            Date d1 = df.parse("1/20/2020");
            g1.setTime(d1);
            Date d2 = df.parse("12/22/2020");
            g2.setTime(d2);
            Date d3 = df.parse("12/20/2019");
            g3.setTime(d3);
            Date d4 = df.parse("7/02/2020");
            g4.setTime(d4);
            Date d5 = df.parse("1/20/2010");
            g5.setTime(d5);
            Date d6 = df.parse("9/29/2020");
            g6.setTime(d6);
            Date d7 = df.parse("7/25/2020");
            g7.setTime(d7);
            Date d8 = df.parse("7/29/2020");
            g8.setTime(d8);

            Console console1 = new Console("Person1", g4, g6,
                    null, ConsoleTypes.PlayStation4);
            Console console2 = new Console("Person2", g5, g3,
                    null, ConsoleTypes.PlayStation4);
            Console console3 = new Console("Person5", g4, g8,
                    null, ConsoleTypes.SegaGenesisMini);
            Console console4 = new Console("Person6", g4, g7,
                    null, ConsoleTypes.SegaGenesisMini);
            Console console5 = new Console("Person1", g5, g4, g3,
                    ConsoleTypes.XBoxOneS);

            Game game1 = new Game("Person1", g3, g2, null,
                    "title1", ConsoleTypes.PlayStation4);
            Game game2 = new Game("Person1", g3, g1, null,
                    "title2", ConsoleTypes.PlayStation4);
            Game game3 = new Game("Person1", g5, g3, null,
                    "title2", ConsoleTypes.SegaGenesisMini);
            Game game4 = new Game("Person7", g4, g8, null,
                    "title2", null);
            Game game5 = new Game("Person3", g3, g1, g1, "title2",
                    ConsoleTypes.XBoxOneS);
            Game game6 = new Game("Person6", g4, g7, null,
                    "title1", ConsoleTypes.NintendoSwich);
            Game game7 = new Game("Person5", g4, g8, null,
                    "title1", ConsoleTypes.NintendoSwich);

            add(game1);
            add(game4);
            add(game5);
            add(game2);
            add(game3);
            add(game6);
            add(game7);

            add(console1);
            add(console2);
            add(console5);
            add(console3);
            add(console4);

            // create a bunch of them.
            int count = 0;
            Random rand = new Random(13);
            String guest = null;

            while (count < 100) {
                // change this number to 300 for a complete test of your code
                Date date = df.parse("7/" +
                        (rand.nextInt(10) + 2) + "/2020");
                GregorianCalendar g = new GregorianCalendar();
                g.setTime(date);
                if (rand.nextBoolean()) {
                    guest = "Game" + rand.nextInt(5);
                    Game game;
                    if (count % 2 == 0)
                        game = new Game(guest, g4, g, null,
                                "title2", ConsoleTypes.NintendoSwich);
                    else
                        game = new Game(guest, g4, g, null,
                                "title2", null);
                    add(game);


                } else {
                    guest = "Console" + rand.nextInt(5);
                    date = df.parse("7/" +
                            (rand.nextInt(20) + 2) + "/2020");
                    g.setTime(date);
                    Console console = new Console(guest, g4, g, null,
                            getOneRandom(rand));
                    add(console);
                }

                count++;
            }
        } catch (ParseException e) {
            throw new RuntimeException
                    ("Error in testing, creation of list");
        }
    }

    /******************************************************************
     * A method to get a random console type
     * @param rand - random value
     * @return consoletype
     */
    public ConsoleTypes getOneRandom(Random rand) {

        int number = rand.nextInt(ConsoleTypes.values().length - 1);
        switch (number) {
            case 0:
                return ConsoleTypes.PlayStation4;
            case 1:
                return ConsoleTypes.XBoxOneS;
            case 2:
                return ConsoleTypes.PlayStation4Pro;
            case 3:
                return ConsoleTypes.NintendoSwich;
            default:
                return ConsoleTypes.SegaGenesisMini;
        }
    }

    /******************************************************************
     * Method to sort rentals by game and console
     */
    public class GameConsoleSorter implements Comparator<Rental> {
        @Override
        public int compare(Rental r1, Rental r2) {
            return r1.getClass().getName()
                    .compareTo(r2.getClass().getName())*-1;

        }
    }

    /******************************************************************
     * Method to be used to compare games and consoles and
     * alphabetize them, keeping games and consoles seperate
     */
    public class GameConsoleAlphabetizer implements Comparator<Rental>
    {
        @Override
        public int compare(Rental r1, Rental r2){
            int comparer = 0;
            if(r1 instanceof Game && r2 instanceof Game){
                comparer =  r1.getNameOfRenter().toLowerCase()
                        .compareTo(r2.getNameOfRenter().toLowerCase());

            }
            else if(r1 instanceof Console && r2 instanceof Console){
                comparer =  r1.getNameOfRenter().toLowerCase()
                        .compareTo(r2.getNameOfRenter().toLowerCase());

            }
            return comparer;
        }
    }

    /******************************************************************
     * A method that sorts the fourteen day cap screen
     */
    public class fourteenDaySorterFirst implements Comparator<Rental>{
        @Override
        public int compare(Rental r1, Rental r2) {

            int comparer = 0;

            if(daysBetween(r1.getRentedOn(), r1.getDueBack()) >= 14
                    && daysBetween(r2.getRentedOn(), r2.getDueBack())
                    >= 14){
                comparer = r1.getNameOfRenter().compareTo(r2.
                        getNameOfRenter());
            }
            else if(daysBetween(r1.getRentedOn(), r1
                    .getDueBack()) <= 7 &&
                    daysBetween(r2.getRentedOn(), r2
                            .getDueBack())
                            <= 7){
                comparer = r1.getNameOfRenter().compareTo(r2.
                        getNameOfRenter());
            }
            else if (daysBetween(r1.getRentedOn(), r1.getDueBack()) >
                    daysBetween(r2.getRentedOn(),r2.getDueBack())){
                //this means r1 is greater than r2
                comparer = -1;
            }
            else if (daysBetween(r1.getRentedOn(), r1.getDueBack()) <
                    daysBetween(r2.getRentedOn(),r2.getDueBack())){
                comparer = 1;
            }
            return comparer;
        }
    }
}