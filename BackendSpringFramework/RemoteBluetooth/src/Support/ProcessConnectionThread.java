package Support;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
/*import java.awt.event.KeyEvent;*/
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable {

    private StreamConnection mConnection;

    // Constant that indicate command from devices
    private static final String EXIT_CMD = "-1";
	/*
	 * private static final String KEY_RIGHT = "1"; private static final String
	 * KEY_LEFT = "2";
	 */
    private static final String MOUSE_LEFT_CLICK = "3";
    private static final String MOUSE_RIGHT_CLICK = "4";

    public ProcessConnectionThread(StreamConnection connection) {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {
            // prepare to receive data
            InputStream inputStream = mConnection.openInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            System.out.println("waiting for input");

            while (true) {
                String command = in.readLine();
             //   System.out.println(null != command ? command : "Null");
                if (command.equals(EXIT_CMD)) {
                    System.out.println("finish process");
                    break;
                }
                processCommand(command);
            }
        } catch (Exception e) {
        	System.out.println("Bluetooth it's stop ..");
//            e.printStackTrace();
        }
    }

    /**
     * Process the command from client
     *
     * @param command the command code
     * @param commandType for determine type of command(0 for mouse pointer, 1 for left mouse, 2 for right mouse, 3 for chatting mode)
     */
    private void processCommand(String command) {
        try {
            Robot robot = new Robot();
			/*
			 * boolean chattingMode = false; String message;
			 */
			/* chattingMode = command.trim().endsWith("@chattingMode")?true:false; */
        
            
/*            if(chattingMode) {
            	message = command.trim().split("@chattingMode")[0];
            	System.out.println(message);}
            
            //0,1,2 => mouse modes
            else {
                if (command.equals(KEY_RIGHT)) {
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    System.out.println("Right");
                } else if (command.equals(KEY_LEFT)) {
                    robot.keyPress(KeyEvent.VK_LEFT);
                    System.out.println("Left");
                } else */
                	if (command.contains(",")) {
                    //input will come in x,y format if user moves mouse on mousepad
                		
                    float movex = Float.parseFloat(command.split(",")[0]);//extract movement in x direction
                    float movey = Float.parseFloat(command.split(",")[1]);//extract movement in y direction
                    System.out.println("(X,Y) = "+ "("+command+")");
                    Point point = MouseInfo.getPointerInfo().getLocation(); //Get current mouse position
                    float nowx = point.x;
                    float nowy = point.y;
                    robot.mouseMove((int) (nowx + movex), (int) (nowy + movey));//Move mouse pointer to new location
                }
                //if user taps on left button to simulate a left click
                else if (command.contains(MOUSE_LEFT_CLICK)) {
                    //Simulate press and release of mouse button 1(makes sure correct button is pressed based on user's dexterity)
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    System.out.println("Left mouse button command");
                }
                //if user taps on right button to simulate a right click
                else if (command.contains(MOUSE_RIGHT_CLICK)) {
                    //Simulate press and release of mouse button 1(makes sure correct button is pressed based on user's dexterity)
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    System.out.println("Right mouse button command");
                }
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
    }
}