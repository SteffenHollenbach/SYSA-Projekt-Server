/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiagent;

import multiagent.remote.IStrategy;
import multiagent.remote.IAgent;
import java.awt.Color;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiagent.util.AgentUtils;

/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 * 
 *
 */
public class AgentImpl extends UnicastRemoteObject implements IAgent, Serializable {

    private static int CAPACITY = 2;

    private int posx;
    private int posy;
    private int capacity;
    private int load;
    private final String name;
    private Color color;
    private String order;
    private IStrategy strategy;
    private PlayingField playingField;
    private int planedPut;
    private int points;
    private PlayingField rememberField;
    private int[][] customData;
    private IAgent agentArray[]; //andere Agenten des Spielers

    public AgentImpl(String name, IStrategy strategy, PlayingField playingField) throws RemoteException {
        this(name, Color.MAGENTA, 0, 0, CAPACITY, 0, strategy, playingField);
    }

    public AgentImpl(String name, Color color, IStrategy strategy, PlayingField playingField) throws RemoteException {
        this(name, color, 0, 0, CAPACITY, 0, strategy, playingField);
    }

    AgentImpl(String name, Color color, int posx, int posy, int capacity, int load, IStrategy strategy, PlayingField playingField) throws RemoteException {
        super();
        this.posx = posx;
        this.posy = posy;
        this.capacity = capacity;
        this.load = load;
        this.name = name;
        this.color = color;
        this.strategy = strategy;
        this.playingField = playingField;
        rememberField = new PlayingField(playingField.getSize());
        points = 0;
        planedPut = 0;
        customData = new int[8][4];
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPosx() {
        return posx;
    }

    @Override
    public void setPosx(int posx) {
        this.posx = posx;
    }

    @Override
    public int getPosy() {
        return posy;
    }

    @Override
    public void setPosy(int posy) {
        this.posy = posy;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int getLoad() {
        return load;
    }

    @Override
    public void setLoad(int load) {
        this.load = load;
    }

    @Override
    public void go(String direction) {
        this.setOrder(direction);
    }

    public boolean requestField(String direction) {
        return playingField.requestField(this, direction, agentArray);
    }

    @Override
    public void take() {
        setOrder(AgentUtils.TAKE);
    }

    @Override
    public int check() {
        return playingField.getResources(this);
    }

    @Override
    public void put() {
        setPlanedPut(getLoad());
        setOrder(AgentUtils.PUT);
    }

    @Override
    public void put(int value) {
        setPlanedPut(value);
        setOrder(AgentUtils.PUT);
    }

    @Override
    public String getOrder() {
        return order;
    }

    @Override
    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public IStrategy getStrategy() {
        return strategy;
    }

    @Override
    public int getHomeXY() {
        return playingField.getXyhome();
    }

    @Override
    public boolean checkIfOnSpawn() {
    	return checkIfOnSpawn(getPosx(), getPosy());
    }
    
    @Override
    public boolean checkIfOnSpawn(int x, int y) {
        ArrayList<int[]> spawnList = playingField.getSpawnFields();
        int[] currentPos = {x, y};

        for (Iterator iterator = spawnList.iterator(); iterator.hasNext();) {
            int[] is = (int[]) iterator.next();
            if (is[0] == currentPos[0] && is[1] == currentPos[1]) {
                //System.out.println("On Home");
                return true;
            }

        }
        return false;
    }

    @Override
    public int getPlanedPut() {
        return planedPut;
    }

    @Override
    public void setPlanedPut(int planedPut) {
        this.planedPut = planedPut;
    }

    @Override
    public int getPoints() {
        try {
            return playingField.getiPlayerList().get(getName()).getPoints();
        } catch (RemoteException ex) {
            Logger.getLogger(AgentImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }
    
	@Override
    public int getRememberFieldSize() {
    	return rememberField.getSize();
    }

    public void setRememberResources(int resources) {
        rememberField.setRememberResources(this, resources);
    }

    public int getRememberResources(int x, int y) {
        if (x > rememberField.getSize() - 1) {
            x = rememberField.getSize() - 1;
        }
        if (y > rememberField.getSize() - 1) {
            y = rememberField.getSize() - 1;
        }
        return rememberField.getPlayingField()[x][y].getResources();
    }

    public void setRememberResources(int x, int y, int resources) {
        if (x > rememberField.getSize() - 1) {
            x = rememberField.getSize() - 1;
        }
        if (y > rememberField.getSize() - 1) {
            y = rememberField.getSize() - 1;
        }
        rememberField.setRememberResources(x, y, resources);
    }

    @Override
    public void buy() {
        setOrder(AgentUtils.BUY);
    }

    @Override
    public int getTargetAmount() {
        return playingField.getTargetAmount();
    }

    @Override
    public int getAgentsValue() throws RemoteException {
        return playingField.getAgentsValue();
    }

    @Override
    public int getMaxAgents() throws RemoteException {
        return playingField.getMaxAgents();
    }

	@Override
	public boolean hasEnoughToBuy() throws RemoteException {		
		return getPoints() >= getAgentsValue();
	}

	@Override
	public boolean hasMaxAgents() throws RemoteException {		
		return agentArray.length >= getMaxAgents();
	}

	@Override
	public boolean checkSpawnIsPossible() throws RemoteException {
		for (IAgent agent : agentArray) {
            try {
				if (agent.checkIfOnSpawn()) {
				    return false;
				}
			} catch (RemoteException e) {
				return false;
			}          
		} 
		return true;
	}
	
	@Override
	public int getCustomData(int i, int j) throws RemoteException {
		if (i > customData.length - 1){
			i = customData.length - 1;
		}
		if (j > customData[i].length - 1){
			j = customData[i].length - 1;
		}
		return customData[i][j];
	}

	@Override
	public void setCustomData(int i, int j, int data) throws RemoteException {
		if (i > customData.length - 1){
			i = customData.length - 1;
		}
		if (j > customData[i].length - 1){
			j = customData[i].length - 1;
		}
		customData[i][j] = data;		
	}

	@Override
	public boolean buyPossible() throws RemoteException {
		return (hasEnoughToBuy() && checkSpawnIsPossible() && !hasMaxAgents());
	}

	@Override
	public IAgent[] getAgentArray() {
		return agentArray;
	}

	@Override
	public void setAgentArray(IAgent[] agentArray) {
		this.agentArray = agentArray;
	}

	@Override
	public PlayingField getRememberField() throws RemoteException {
		return rememberField;	
	}

	@Override
	public void setRememberField(PlayingField rememberField) {
		this.rememberField = rememberField;
	}
	
	


}
