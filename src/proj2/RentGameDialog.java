package proj2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RentGameDialog extends JDialog implements ActionListener {
    private JTextField txtRentedName;
    private JTextField txtDateRentedOn;
    private JTextField txtDateDueDate;
    private JTextField txtNameOfGame;
    private JComboBox<ConsoleTypes> comBoxConsoleType;

    private JButton okButton;
    private JButton cancelButton;
    private int closeStatus;
    private Game game;
    public static final int OK = 0;
    public static final int CANCEL = 1;

    /*********************************************************
     Instantiate a Custom Dialog as 'modal' and wait for the
     user to provide data and click on a button.

     @param parent reference to the JFrame application
     @param game an instantiated object to be filled with data
     *********************************************************/

    public RentGameDialog(JFrame parent, Game game) {
        // call parent and create a 'modal' dialog
        super(parent, true);
        this.game = game;

        setTitle("Game dialog box");
        closeStatus = CANCEL;
        setSize(400,200);

        // prevent user from closing window
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        txtRentedName = new JTextField("Judy",30);
        txtDateRentedOn = new JTextField(15);
        txtDateDueDate = new JTextField(15);
        txtNameOfGame = new JTextField("Game1", 15);
        comBoxConsoleType = new JComboBox<>(ConsoleTypes.values());

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy"); //format it as per your requirement
        String dateNow = formatter.format(currentDate.getTime());
        currentDate.add(Calendar.DATE, 1);
        String dateTomorrow = formatter.format(currentDate.getTime());

        txtDateRentedOn.setText(dateNow);
        txtDateDueDate.setText(dateTomorrow);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(6,2));

        textPanel.add(new JLabel(""));
        textPanel.add(new JLabel(""));

        textPanel.add(new JLabel("Name of Renter: "));
        textPanel.add(txtRentedName);
        textPanel.add(new JLabel("Date rented on: "));
        textPanel.add(txtDateRentedOn);
        textPanel.add(new JLabel("Due date (est.): "));
        textPanel.add(txtDateDueDate);
        textPanel.add(new JLabel("Name of the Gamed"));
        textPanel.add(txtNameOfGame);
        textPanel.add(new JLabel("ConsoleType"));
        textPanel.add(comBoxConsoleType);

        getContentPane().add(textPanel, BorderLayout.CENTER);

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setVisible (true);
    }

    /**************************************************************
     Respond to either button clicks
     @param e the action event that was just fired
     **************************************************************/
    public void actionPerformed(ActionEvent e) {

        JButton button = (JButton) e.getSource();
        if (button == cancelButton) {
            closeStatus = CANCEL;
        }

        // if OK clicked the fill the object
        if (button == okButton) {
            // save the information in the object
            closeStatus = OK;
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            df.setLenient(false);
            Date d1 = null;
            Date d2 = null;
            try {
                if (txtNameOfGame.getText().isBlank()) {
                    throw new IllegalArgumentException();
                }
                GregorianCalendar rentOnTemp = new GregorianCalendar();
                d1 = df.parse(txtDateRentedOn.getText());
                if(txtDateRentedOn.getText().matches(".*[a-z].*") ||
                        txtDateDueDate.getText().matches(".*[a-z].*")||
                        txtDateDueDate.getText().matches("(.*/}\\!)*?"
                                + "<>,'\"+= @!#$%^&*_-~`\\;:|\\{") ||
                        txtDateRentedOn.getText().matches("(.*/}\\!)*?"
                                + "<>,'\"+= @!#$%^&*_-~`\\;:|\\{")){
                    throw new IllegalArgumentException();
                }
                rentOnTemp.setTime(d1);
                game.setRentedOn(rentOnTemp);

                GregorianCalendar dueDateTemp = new GregorianCalendar();
                d2 = df.parse(txtDateDueDate.getText());
                dueDateTemp.setTime(d2);
                game.setDueBack(dueDateTemp);

                if (txtRentedName.getText().isBlank()) {
                    throw new RuntimeException();
                }

                game.setNameOfRenter(txtRentedName.getText());
                game.setNameGame(txtNameOfGame.getText());

                ConsoleTypes type = ((ConsoleTypes) comBoxConsoleType.getSelectedItem());

                game.setConsole(type);

            } catch (ParseException e1) {
                JOptionPane.showMessageDialog(null, "Invalid Date");
                closeStatus = CANCEL;
            } catch (IllegalArgumentException e1) {
                JOptionPane.showMessageDialog(null, "Error");
                closeStatus = CANCEL;
            } catch (RuntimeException e1) {
                JOptionPane.showMessageDialog(null, "Enter a Name");
                closeStatus = CANCEL;
            }
        }

        dispose();

    }

    /**************************************************************
     Return a String to let the caller know which button
     was clicked

     @return an int representing the option OK or CANCEL
     **************************************************************/
    public int getCloseStatus(){
        return closeStatus;
    }

}


