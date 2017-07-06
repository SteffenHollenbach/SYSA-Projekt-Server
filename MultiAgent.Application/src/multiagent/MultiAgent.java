/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiagent;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import multiagent.gui.ServerFrame;

/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */
public class MultiAgent {
    private static SplashScreen mySplash;
    private static Graphics2D splashGraphics;
    private static Rectangle2D splashTextArea,splashProgressArea;
    private static String[] operation = new String[]{"Lade Altmeister", "Male Agents", "Baue Spielfeld auf", "Setze Resourcen", "Lade Strategien", "Lade Cheats", "Lösche Doku","Schreibe 1.0 in SYSA", "Reiße Weltherrschaft an mich","Reiße Weltherrschaft an mich" };
    public static void main(String[] args) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        splashInit();           // initialize splash overlay drawing parameters
        appInit();              // simulate what an application would do 
                                // before starting
        if (mySplash != null)   // check if we really had a spash screen
            mySplash.close();   // if so we're now done with it
        
        java.awt.EventQueue.invokeLater(() -> {
            ServerFrame frame = new ServerFrame();
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    frame.disposeAll();
                }
            });
            frame.setVisible(true);
            new SoundClip("Loaded");
        });
    }
    private static void splashInit()
    {
        mySplash = SplashScreen.getSplashScreen();
        if (mySplash != null)
        {   // if there are any problems displaying the splash this will be null
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;
            // stake out some area for our status information
            
            splashProgressArea = new Rectangle2D.Double(0, height*.92, width, 12 );
            
            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            Font font = new Font("Dialog", Font.BOLD, 14);
            
            splashGraphics.setFont(font);
            
            // initialize the status info
            splashText("Starting");
            splashProgress(0);
        }
    }
    /**
     * Display text in status area of Splash.  Note: no validation it will fit.
     * @param str - text to be displayed
     */
    public static void splashText(String str)
    {
        if (mySplash != null && mySplash.isVisible())
        {   // important to check here so no other methods need to know if there
            // really is a Splash being displayed
            
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;
            splashGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,0.0f));
            splashTextArea = new Rectangle2D.Double(3., height*.85, width * .45, 20.);
            //splashGraphics.clearRect(0, 0, width, height);
            // erase the last status text
            splashGraphics.setPaint(new Color(0f,0f,0f,0f));
            splashGraphics.fill(splashTextArea);
            splashGraphics.setComposite(AlphaComposite.SrcOver);
            // draw the text
            splashGraphics.setPaint(Color.WHITE);
            splashGraphics.drawString(str, (int)(splashTextArea.getX() + 10),(int)(splashTextArea.getY() + 15));
           
            // make sure it's displayed
            mySplash.update();
        }
    }
    /**
     * Display a (very) basic progress bar
     * @param pct how much of the progress bar to display 0-100
     */
    public static void splashProgress(int pct)
    {
        if (mySplash != null && mySplash.isVisible())
        {

            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            splashGraphics.fill(splashProgressArea);

            // draw an outline
            splashGraphics.setPaint(Color.BLUE);
            splashGraphics.draw(splashProgressArea);

            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();

            int doneWidth = Math.round(pct*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics.setPaint(Color.GREEN);
            splashGraphics.fillRect(x, y+1, doneWidth, hgt-1);

            // make sure it's displayed
            mySplash.update();
        }
    }
    /**
     * just a stub to simulate a long initialization task that updates
     * the text and progress parts of the status in the Splash
     */
    private static void appInit()
    {
        for(int i=1;i<=10;i++)
        {
            int pctDone = i * 10;
            splashText(operation[i-1]);
            splashProgress(pctDone);
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException ex)
            {
                // ignore it
            }
        }
    }
}
