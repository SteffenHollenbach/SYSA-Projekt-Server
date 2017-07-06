package multiagent;

import java.awt.*;
import java.io.Serializable;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiagent.Field;
import multiagent.remote.IAgent;
import multiagent.remote.IPlayer;
import multiagent.util.AgentUtils;

/**
 * 
 * 
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */

public class PlayingField implements Serializable {

    private Field[][] playingField;
    private int size;
    private int tightness;
    private int max;
    private int numberOfPlayer;
    private int xyhome;
    private int agentsValue;
    private HashMap<String, IPlayer> iPlayerList;
    private int targetAmount;
    private ArrayList<IAgent> list;
    private int count, playerCount, maxAgents;
    private ArrayList<int[]> spawnFields;
	private int spawnTemperature;

    public HashMap<String, IPlayer> getiPlayerList() {
        return iPlayerList;
    }

    public void setiPlayerList(HashMap<String, IPlayer> iPlayerList) {
        this.iPlayerList = iPlayerList;
    }

    public int getAgentsValue() {
        return agentsValue;
    }

    public void setAgentsValue(int agentsValue) {
        this.agentsValue = agentsValue;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }


    public PlayingField(int max, int size, int tightness, ArrayList<IAgent> list, int playerCount, int maxAgents) {
        this.max = max;
        this.size = size;
        this.tightness = tightness;
        this.list = list;
        numberOfPlayer = list.size();
        this.playerCount = playerCount;
        this.maxAgents = maxAgents;
        playingField = new Field[size][size];
        xyhome = size / 2;
        spawnFields = new ArrayList<int[]>();
        count = 0;
        spawnTemperature = 0;

    }

    public PlayingField(int size2) {
        this.size = size2;
        playingField = new Field[size2][size2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
            	playingField[i][j] = new Field(0);
            }
        }

    }

    public void initPlayingField() {
        //i=y, j=x
    	spawnFields.clear();
    	xyhome = size / 2;
        count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i >= xyhome - 1 && i <= xyhome + 1) && (j >= xyhome - 1 && j <= xyhome + 1) && !(i == xyhome && j == xyhome)) {
                    // Spawn

                    spawnFields.add(new int[]{i, j});

                    if ((count < playerCount)) {
                        try {
                            IAgent agent = list.get(count++);
                            agent.setPosx(i);
                            agent.setPosy(j);
                            playingField[i][j] = new Field(agent);
                        } catch (RemoteException ex) {
                            Logger.getLogger(PlayingField.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        playingField[i][j] = new Field(0);
                    }
                } else if (i == xyhome && j == xyhome) {
                    // Mittel
                    playingField[i][j] = new Field(0);
                } else {
                    // Spielfeld
                    int random = (int) Math.round(Math.random() * 100);
                    if (random <= tightness) {
                        playingField[i][j] = new Field((int) Math.round(Math.random() * max));
                    } else {
                        playingField[i][j] = new Field(0);
                    }
                }
            }
        }
    }

    public boolean requestField(IAgent agent, String direction, IAgent agentArray[]) {
        try {
            int x = agent.getPosx();
            int y = agent.getPosy();
            
            switch (direction) {
                case AgentUtils.LEFT: // links
                    if ((x > 0) && ((x - 1 != size / 2) || (y != size /2))){
                        if (playingField[x - 1][y].getAgent() == null) {                        	
                        	if (agent.checkIfOnSpawn(x-1, y)) { //Naechster Zug endet auf Home-Feld
                        		if (agent.checkSpawnIsPossible()){ //Es steht noch kein anderer auf dem Feld
                        			//Alles ok
                        		} else if (!agent.checkIfOnSpawn()){ //Der Agent befindet sich bereits auf einem Home-Feld
                        			//Wenn nicht kein zugang moeglich
                        			return false;
                        		}
                        	}                       	                        	
                            x -= 1;
                        }
                    } else {
                        return false;
                    }
                    break;
                case AgentUtils.TOP: // oben
                    if ((y > 0) && ((y - 1 != size / 2) || (x != size/2))){
                        if (playingField[x][y - 1].getAgent() == null) {
                        	if (agent.checkIfOnSpawn(x, y-1)) { //Naechster Zug endet auf Home-Feld
                        		if (agent.checkSpawnIsPossible()){ //Es steht noch kein anderer auf dem Feld
                        			//Alles ok
                        		} else if (!agent.checkIfOnSpawn()){ //Der Agent befindet sich bereits auf einem Home-Feld
                        			//Wenn nicht kein zugang moeglich
                        			return false;
                        		}
                        	}
                            y -= 1;
                        }

                    } else {
                        return false;
                    }
                    break;
                case AgentUtils.RIGHT: // rechts
                    if ((x < size - 1) && ((x + 1 != size / 2) || (y != size/2))) {
                        if (playingField[x + 1][y].getAgent() == null) {
                        	if (agent.checkIfOnSpawn(x+1, y)) { //Naechster Zug endet auf Home-Feld
                        		if (agent.checkSpawnIsPossible()){ //Es steht noch kein anderer auf dem Feld
                        			//Alles ok
                        		} else if (!agent.checkIfOnSpawn()){ //Der Agent befindet sich bereits auf einem Home-Feld
                        			//Wenn nicht kein zugang moeglich
                        			return false;
                        		}
                        	} 
                            x += 1;
                        }

                    } else {
                        return false;
                    }
                    break;
                case AgentUtils.BOTTOM: // unten
                    if ((y < size - 1) && ((y + 1 != size / 2) || (x != size/2))) {
                        if (playingField[x][y + 1].getAgent() == null) {
                        	if (agent.checkIfOnSpawn(x, y+1)) { //Naechster Zug endet auf Home-Feld
                        		if (agent.checkSpawnIsPossible()){ //Es steht noch kein anderer auf dem Feld
                        			//Alles ok
                        		} else if (!agent.checkIfOnSpawn()){ //Der Agent befindet sich bereits auf einem Home-Feld
                        			//Wenn nicht kein zugang moeglich
                        			return false;
                        		}
                        	}
                            y += 1;
                        }
                    } else {
                        return false;
                    }
                    break;
            }

            return true;
        } catch (RemoteException ex) {
            Logger.getLogger(PlayingField.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }


    public void setOccupancy(int xPos, int yPos, IAgent value) {
        playingField[xPos][yPos].setIAgent(value);
    }

    public void spreadAgent(IAgent agent){
    	try {
	        int xOld = agent.getPosx();
	        int yOld = agent.getPosy();
	    	
	        int x;
	        int y;
	    	
	    	do {
	            x = 0 + (int)(Math.random() * ((getSize()-1 - 0) + 0));
	            y = 0 + (int)(Math.random() * ((getSize()-1 - 0) + 0));
	
	    	} while (playingField[x][y].getAgent() != null);
	    		    	
	        // old Field
	        setOccupancy(xOld, yOld, null);
	        // new Field
	        setOccupancy(x, y, agent);
	        agent.setPosx(x);
	        agent.setPosy(y);
        
    	 } catch (RemoteException ex) {
             Logger.getLogger(PlayingField.class.getName()).log(Level.SEVERE, null, ex);
         }
    	
    }
    
    public void moveAgent(IAgent agent, String direction) {
        try {
        	boolean moved = false;
            int x = agent.getPosx();
            int y = agent.getPosy();
                        
            int xOld = agent.getPosx();
            int yOld = agent.getPosy();

            switch (direction) {
                case AgentUtils.LEFT: // links
                	if ((x > 0) && ((x - 1 != size / 2) || (y != size /2))){
                        if (playingField[x - 1][y].getAgent() == null) {
                        	if (agent.checkIfOnSpawn(x-1, y)) { //N�chster Zug endet auf Home-Feld
                        		if (agent.checkSpawnIsPossible()){ //Es steht noch kein anderer auf dem Feld
                        			//Alles ok
                        		} else if (!agent.checkIfOnSpawn()){ //Der Agent befindet sich bereits auf einem Home-Feld
                        			//Wenn nicht kein zugang m�glich
                        			break;
                        		}
                        	} 
                            x -= 1;
                            moved = true;
                        }
                    }
                    break;
                case AgentUtils.TOP: // oben
                	if ((y > 0) && ((y - 1 != size / 2) || (x != size/2))){
                        if (playingField[x][y - 1].getAgent() == null) {
                        	if (agent.checkIfOnSpawn(x, y-1)) { //N�chster Zug endet auf Home-Feld
                        		if (agent.checkSpawnIsPossible()){ //Es steht noch kein anderer auf dem Feld
                        			//Alles ok
                        		} else if (!agent.checkIfOnSpawn()){ //Der Agent befindet sich bereits auf einem Home-Feld
                        			//Wenn nicht kein zugang m�glich
                        			break;
                        		}
                        	}
                            y -= 1;
                            moved = true;
                        }

                    }
                    break;
                case AgentUtils.RIGHT: // rechts
                	if ((x < size - 1) && ((x + 1 != size / 2) || (y != size/2))) {
                        if (playingField[x + 1][y].getAgent() == null) {
                        	if (agent.checkIfOnSpawn(x+1, y)) { //N�chster Zug endet auf Home-Feld
                        		if (agent.checkSpawnIsPossible()){ //Es steht noch kein anderer auf dem Feld
                        			//Alles ok
                        		} else if (!agent.checkIfOnSpawn()){ //Der Agent befindet sich bereits auf einem Home-Feld
                        			//Wenn nicht kein zugang m�glich
                        			break;
                        		}
                        	} 
                            x += 1;
                            moved = true;
                        }

                    }
                    break;
                case AgentUtils.BOTTOM: // unten
                	if ((y < size - 1) && ((y + 1 != size / 2) || (x != size/2))) {
                        if (playingField[x][y + 1].getAgent() == null) {
                        	if (agent.checkIfOnSpawn(x, y+1)) { //N�chster Zug endet auf Home-Feld
                        		if (agent.checkSpawnIsPossible()){ //Es steht noch kein anderer auf dem Feld
                        			//Alles ok
                        		} else if (!agent.checkIfOnSpawn()){ //Der Agent befindet sich bereits auf einem Home-Feld
                        			//Wenn nicht kein zugang m�glich
                        			break;
                        		}
                        	} 
                            y += 1;
                            moved = true;
                        }

                    }
                    break;
            }
            
            if (moved){
	            // old Field
	            setOccupancy(xOld, yOld, null);
	            // new Field
	            setOccupancy(x, y, agent);
	            agent.setPosx(x);
	            agent.setPosy(y);
            }

        } catch (RemoteException ex) {
            Logger.getLogger(PlayingField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getResources(int xPos, int yPos) {
        return playingField[xPos][yPos].getResources();
    }

    public int getResources(IAgent agent) {
        try {
            return getResources(agent.getPosx(), agent.getPosy());
        } catch (RemoteException ex) {
            Logger.getLogger(PlayingField.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public void setResources(IAgent agent, int value) {
        try {
            int xPos = agent.getPosx();
            int yPos = agent.getPosy();

            if (value > agent.getLoad()) {
                value = agent.getLoad();
            }

            if (agent.checkIfOnSpawn()) {
            	System.out.println("Player" + iPlayerList.size());
                iPlayerList.get(agent.getName()).setPoints(iPlayerList.get(agent.getName()).getPoints() + value);
            } else {
                playingField[xPos][yPos].setResources(this.getResources(agent) + value);
                new SoundClip("bottom_feeder");
            }
            agent.setLoad(agent.getLoad() - value);

        } catch (RemoteException ex) {
            Logger.getLogger(PlayingField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRememberResources(IAgent agent, int value) {
        try {
            int xPos = agent.getPosx();
            int yPos = agent.getPosy();
            
            if (xPos >= 0 && xPos < agent.getRememberFieldSize() && yPos >= 0 && yPos < agent.getRememberFieldSize()) {
                playingField[xPos][yPos].setResources(value);            	
            } else {
            	System.out.println("Fehler in PlayingField.setRememberResources(agent, value): xPos = " + xPos + ", yPos = " + yPos + ", value = " + value); 
            }

        } catch (RemoteException ex) {
            Logger.getLogger(PlayingField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRememberResources(int x, int y, int value) {
        playingField[x][y].setResources(value);
    }

    public void takeResources(IAgent agent) {
        try {
            int xPos = agent.getPosx();
            int yPos = agent.getPosy();

            if ((agent.getCapacity() - agent.getLoad() > 0) && (playingField[xPos][yPos].getResources() > 0)) {

                agent.setLoad(agent.getLoad() + 1);
                playingField[xPos][yPos].setResources(playingField[xPos][yPos].getResources() - 1);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(PlayingField.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Field[][] getPlayingField() {
        return playingField;
    }

    

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    public int getXyhome() {
        return xyhome;
    }

    public void setXyhome(int xyhome) {
        this.xyhome = xyhome;
    }

    public ArrayList<IAgent> getList() {
        return list;
    }

    public void setList(ArrayList<IAgent> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getMaxAgents() {
        return maxAgents;
    }

    public void setMaxAgents(int maxAgents) {
        this.maxAgents = maxAgents;
    }

    public int getSize() {
        return size;
    }

    public int getTightness() {
        return tightness;
    }

    public int getMax() {
        return max;
    }

    public ArrayList<int[]> getSpawnFields() {
        return spawnFields;
    }

    public void setSize(int size) {
        this.size = size;
        playingField = new Field[size][size];
        xyhome = size / 2;
    }

    public void setTightness(int tightness) {
        this.tightness = tightness;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public PlayingField getField() {
        return this;
    }

    public boolean requestField(int x, int y) {
        return playingField[x][y].getAgent() == null;
    }
    
	public int getSpawnTemperature() {
		return spawnTemperature;
	}

	public void setSpawnTemperature(int spawnTemperature) {
		this.spawnTemperature = spawnTemperature;
	}

}
