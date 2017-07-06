package multiagent.gui;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import multiagent.Field;
import multiagent.MultiAgent;
import multiagent.PlayingField;

/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */
public class RenderLife extends JPanel {

    static final long serialVersionUID = 1;
    private final int size;
    private int SQ_SIZE = 20;
    private final int GAP = 10;
    private PlayingField playingField;
    private boolean isClosed;

    public boolean isClosed() {
        return isClosed;
    }

    public RenderLife(int size) {
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.size = size;
        
        SQ_SIZE = (gd.getDisplayMode().getHeight() - GAP * 5 - 50) / size;
    	
        JFrame f = new JFrame();
        isClosed=false;
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                isClosed=true;
                f.dispose();
            }
        });
        f.getContentPane().setLayout(new BorderLayout());
        f.setSize(size * SQ_SIZE + GAP * 5, size * SQ_SIZE + GAP * 5);
        f.add(this);
        f.setVisible(true);
    }

    public RenderLife(PlayingField playingField) {
        this(playingField.getSize());
        this.playingField = playingField;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        //fillRect-Parameter: x, y, width, height
        Graphics2D g2 = (Graphics2D) g;
        g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());

        // i ist die Zeile (d.h. y-Koordinate), j die Spalte (d.h. x-Koordinate)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (playingField.getPlayingField()[i][j] != null) {
                    if (playingField.getPlayingField()[i][j].getResources() > 0) {
                        g.setColor(Color.decode("#80f455"));
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                    }
                    if (playingField.getPlayingField()[i][j].getAgent() != null) {
                        try {
                            g.setColor(playingField.getPlayingField()[i][j].getAgent().getColor());
                        } catch (RemoteException ex) {
                            Logger.getLogger(RenderLife.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    g.fillRect(i * SQ_SIZE, j * SQ_SIZE, SQ_SIZE, SQ_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(i * SQ_SIZE, j * SQ_SIZE, SQ_SIZE, SQ_SIZE);

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setFont(new Font("TimesRoman", Font.PLAIN, SQ_SIZE/3));
                    g2.drawString(String.valueOf(playingField.getPlayingField()[i][j].getResources()), (int) ((i + 0.3) * SQ_SIZE), (j + 1) * SQ_SIZE);
                    if (playingField.getPlayingField()[i][j].getAgent() != null) {
                        try {
                            g2.drawString(String.valueOf(playingField.getPlayingField()[i][j].getAgent().getLoad()), (int) ((i + 0.3) * SQ_SIZE), (int) ((j + 0.5) * SQ_SIZE));
                        } catch (RemoteException ex) {
                            Logger.getLogger(RenderLife.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        drawHome(g);

    }

    public void drawHome(Graphics g) {
        int middle = size / 2;
        playingField.getPlayingField()[middle][middle] = new Field(0, Color.BLACK);
        g.setColor(playingField.getPlayingField()[middle][middle].getColor());
        g.fillRect(middle * SQ_SIZE, middle * SQ_SIZE, SQ_SIZE, SQ_SIZE);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, SQ_SIZE/3)); 
        g.drawString(String.valueOf(playingField.getSpawnTemperature()), (int) ((middle + 0.3) * SQ_SIZE), (middle + 1) * SQ_SIZE);
    }

}
