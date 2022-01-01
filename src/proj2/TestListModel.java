package proj2;

import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.*;

/**********************************************************************
 * Junit testing
 ********************************************************************/

public class TestListModel {

    @Test
    public void testFailedDateEntry() throws ParseException {
        //expects JOPTION pane to show up that the rent date
        // can't be after due date
        ListModel model = new ListModel(); //this already creates a
        // new list with original data in it
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date d1 = df.parse("1/20/2020");
        Date d2 = df.parse("1/20/2000");

        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        g1.setTime(d1);
        g2.setTime(d2);
        Game game = new Game("Ugly boy", g1, g2, null,
                "game1", ConsoleTypes.PlayStation4Pro);
        model.add(game);

        boolean x = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.get(i).getNameOfRenter() == "Ugly boy") {
                x = true;
            }
        }

        assertTrue(x == false);

    }

    @Test
    public void testReturnedScreen() throws ParseException {
        //expects JOPTION pane to show up that the rent date can't be after due
        //date
        ListModel model = new ListModel(); //this already creates a new list
        //with original data in it

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date returned = df.parse("10/20/2008");
        Date d1 = df.parse("10/10/2010");
        Date d2 = df.parse("10/10/2000");


        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar returneddate = new GregorianCalendar();

        g1.setTime(d1);
        g2.setTime(d2);
        returneddate.setTime(returned);

        Console console = new Console("ugly man",g2,g1,null,
                ConsoleTypes.PlayStation4Pro);

        model.add(console);

        console.setActualDateReturned(returneddate);

        ScreenDisplay display = ScreenDisplay.ReturnedItems; //switch to this
        //screen

        model.setDisplay(display);

        boolean x = false;

        for (int i = 0; i < model.getRowCount(); i++){
            String name = model.get(i).getNameOfRenter();
            if (model.get(i).getNameOfRenter().equalsIgnoreCase
                    ("ugly man")){
                x = true;
            }
        }

        assertTrue(x == true);
    }

    @Test
    public void test7daysgamefirst() throws ParseException {
        ListModel model = new ListModel();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date d1 = df.parse("10/12/2001");
        Date d2 = df.parse("10/11/2001");


        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();

        g1.setTime(d1);
        g2.setTime(d2);

        Game game = new Game("Ugly boy", g2, g1, null,
                "game1", ConsoleTypes.PlayStation4Pro);

        model.add(game);

        model.setDisplay(ScreenDisplay.DueWithinWeekGamesFirst);

        int i = 0;
        boolean x = false;
        if (model.get(i).getClass().toString().equalsIgnoreCase
                ("class project2.game")){
            x = true;
        }
        assertTrue(x);
    }
}
