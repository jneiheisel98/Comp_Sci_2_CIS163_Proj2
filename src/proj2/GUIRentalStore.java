package proj2;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.io.*;

public class GUIRentalStore extends JFrame implements ActionListener {
    private JMenuBar menus;

    private JMenu fileMenu;
    private JMenu actionMenu;

    private JMenuItem openSerItem;
    private JMenuItem exitItem;
    private JMenuItem saveSerItem;
    private JMenuItem openTextItem;
    private JMenuItem saveTextItem;
    private JMenuItem rentConsoleItem;
    private JMenuItem rentGameItem;
    private JMenuItem returnItem;

    private JMenuItem currentRentedItemScn;
    private JMenuItem currentReturnedItemScn;
    private JMenuItem withIn7ItemScn;
    private JMenuItem sort30DaysItemScn;
    private JMenuItem sortGameItemScn;
    private JMenuItem everythingScn;
    private JMenuItem lateRentalScn;

    private JPanel panel;

    private ListModel dList;

    private JTable jTable;

    private JScrollPane scrollList;

    public GUIRentalStore(){
        menus = new JMenuBar();
        fileMenu = new JMenu("File");
        actionMenu = new JMenu("Action");
        openSerItem = new JMenuItem("Open File");
        exitItem = new JMenuItem("Exit");
        saveSerItem = new JMenuItem("Save File");
        openTextItem = new JMenuItem("Open Text");
        saveTextItem = new JMenuItem("Save Text");
        rentConsoleItem = new JMenuItem("Rent a Console");
        rentGameItem = new JMenuItem("Rent a Game");
        returnItem = new JMenuItem("Return of Game or Console");

        currentRentedItemScn = new JMenuItem("Current Rental Screen");
        currentReturnedItemScn = new JMenuItem("Returned screen");
        withIn7ItemScn = new JMenuItem("Within 7 Days Screen");
        sortGameItemScn = new JMenuItem("Within 7 Days Games First Screen");
        sort30DaysItemScn = new JMenuItem("Cap all Rentals 14 days late Screen");
        everythingScn = new JMenuItem("Everything Screen");
        lateRentalScn = new JMenuItem("Late Rental Screen");

        fileMenu.add(openSerItem);
        fileMenu.add(saveSerItem);
        fileMenu.addSeparator();
        fileMenu.add(openTextItem);
        fileMenu.add(saveTextItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        fileMenu.addSeparator();
        fileMenu.add(currentRentedItemScn);
        fileMenu.add(currentReturnedItemScn);
        fileMenu.add(withIn7ItemScn);
        fileMenu.add (sortGameItemScn);
        fileMenu.add(sort30DaysItemScn);
        fileMenu.add(everythingScn);
        fileMenu.add(lateRentalScn);
        fileMenu.addSeparator();

        actionMenu.add(rentConsoleItem);
        actionMenu.add(rentGameItem);
        actionMenu.addSeparator();
        actionMenu.add(returnItem);

        menus.add(fileMenu);
        menus.add(actionMenu);

        openSerItem.addActionListener(this);
        saveSerItem.addActionListener(this);
        openTextItem.addActionListener(this);
        saveTextItem.addActionListener(this);
        exitItem.addActionListener(this);
        rentConsoleItem.addActionListener(this);
        rentGameItem.addActionListener(this);
        returnItem.addActionListener(this);

        currentRentedItemScn.addActionListener(this);
        currentReturnedItemScn.addActionListener(this);
        withIn7ItemScn.addActionListener(this);
        sortGameItemScn.addActionListener(this);
        sort30DaysItemScn.addActionListener(this);
        everythingScn.addActionListener(this);
        lateRentalScn.addActionListener(this);

        setJMenuBar(menus);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        dList = new ListModel();
        jTable = new JTable(dList);
        scrollList = new JScrollPane(jTable);
        panel.add(scrollList);
        add(panel);
        scrollList.setPreferredSize(new Dimension(1000,800));

        setVisible(true);
        setSize(1070,950);
    }

    public void actionPerformed(ActionEvent e) {
        Object comp = e.getSource();
        if (lateRentalScn == comp){
            dList.setDisplay(ScreenDisplay.LateRentals);
        }

        if (everythingScn == comp){
            dList.setDisplay(ScreenDisplay.Everything);
        }

        if (currentRentedItemScn == comp)
            dList.setDisplay(ScreenDisplay.CurrentRentalStatus);

        if (currentReturnedItemScn == comp)
            dList.setDisplay(ScreenDisplay.ReturnedItems);

        if (withIn7ItemScn == comp) {
            GregorianCalendar dat = new GregorianCalendar();
            dList.setDisplay(ScreenDisplay.DueWithInWeek);
        }

        if (sortGameItemScn == comp)
            dList.setDisplay(ScreenDisplay.DueWithinWeekGamesFirst);

        if (sort30DaysItemScn == comp)
            dList.setDisplay(ScreenDisplay.Cap14DaysOverdue);

        if (everythingScn == comp)
            dList.setDisplay(ScreenDisplay.Everything);

        if (openSerItem == comp || openTextItem == comp) {
            JFileChooser chooser = new JFileChooser();

            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                if (openSerItem == comp)
                    dList.loadDatabase(filename);
                else if (openTextItem == comp) {
                    try {
                        dList.loadFromText((filename));
                    } catch (FileNotFoundException fileNotFoundException) {
                        JOptionPane.showMessageDialog(null,"Error");
                    } catch (IllegalArgumentException io){
                        JOptionPane.showMessageDialog(null,"Error");
                    }
                }
            }
        }

        if (saveSerItem == comp || saveTextItem == comp) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showSaveDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                if (saveSerItem == e.getSource())
                    dList.saveDatabase(filename);
                else if (saveTextItem == comp)
                    dList.saveAsText(filename);
            }
        }

        if(e.getSource() == exitItem){
            System.exit(1);
        }
        if(e.getSource() == rentConsoleItem){
            Console Console = new Console();
            RentConsoleDialog dialog = new RentConsoleDialog(this, Console);
            if(dialog.getCloseStatus() == RentConsoleDialog.OK){
                dList.add(Console);
            }
        }
        if(e.getSource() == rentGameItem){
            Game gameOnly = new Game();
            RentGameDialog dialog = new RentGameDialog(this, gameOnly);
            if(dialog.getCloseStatus() == RentGameDialog.OK){
                dList.add(gameOnly);
            }
//            else if (dialog.getCloseStatus() == RentGameDialog.CANCEL){
//                System.exit(1);
//        }
        }

        if (returnItem == e.getSource()) {
            try{
                int index = jTable.getSelectedRow();
                if (index != -1) {
                    GregorianCalendar dat = new GregorianCalendar();

                    Rental unit = dList.get(index);
                    if(unit.getActualDateReturned() == null){
                        ReturnedOnDialog dialog = new ReturnedOnDialog(this, unit);

                        JOptionPane.showMessageDialog(null,
                                "  Be sure to thank " + unit.nameOfRenter +
                                        "\n for renting with us. The price is:  " +
                                        unit.getCost(unit.actualDateReturned) +
                                        " dollars");
                        dList.update(index, unit);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Can't return already rented unit");
                    }

                }
            }catch(NullPointerException e1){
                JOptionPane.showMessageDialog(null,"Return Cancelled");
                //do nothing
            }
        }
    }

    public static void main(String[] args) {
        new GUIRentalStore();
    }
}