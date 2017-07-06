/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiagent.remote;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;

import multiagent.PlayingField;

/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */
public interface IAgent extends Remote {

    public String getName() throws RemoteException;

    public int getPosx() throws RemoteException;

    public void setPosx(int posx) throws RemoteException;

    public int getPosy() throws RemoteException;

    public void setPosy(int posy) throws RemoteException;

    public int getCapacity() throws RemoteException;

    public void setCapacity(int capacity) throws RemoteException;

    public int getLoad() throws RemoteException;

    public void setLoad(int load) throws RemoteException;

    public void go(String direction) throws RemoteException;

    public void take() throws RemoteException;

    public int check() throws RemoteException;

    public void put() throws RemoteException;

    public void put(int value) throws RemoteException;

    public String getOrder() throws RemoteException;

    public void setOrder(String order) throws RemoteException;

    public Color getColor() throws RemoteException;

    public void setColor(Color color) throws RemoteException;

    public IStrategy getStrategy() throws RemoteException;

    public boolean requestField(String direction) throws RemoteException;

    public int getHomeXY() throws RemoteException;

    public boolean checkIfOnSpawn() throws RemoteException;
    
    public boolean checkIfOnSpawn(int x, int y) throws RemoteException;

    public int getPlanedPut() throws RemoteException;

    public void setPlanedPut(int planedPut) throws RemoteException;

    public int getPoints() throws RemoteException;

    public void setPoints(int points) throws RemoteException;

    public int getTargetAmount()throws RemoteException;
    
    public void buy()throws RemoteException; 
    
    public int getAgentsValue()throws RemoteException;
    
    public int getMaxAgents()throws RemoteException;
    
    public boolean hasEnoughToBuy() throws RemoteException;
    
    public boolean hasMaxAgents() throws RemoteException;
    
    public boolean checkSpawnIsPossible() throws RemoteException;
    
    public int getCustomData(int i, int j) throws RemoteException;

    public void setCustomData(int i, int j, int data) throws RemoteException;
    
    public boolean buyPossible() throws RemoteException;
    
    public IAgent[] getAgentArray() throws RemoteException;

    public void setAgentArray(IAgent[] agentArray) throws RemoteException;
	
    public int getRememberResources(int x, int y)  throws RemoteException;

    public void setRememberResources(int resources)  throws RemoteException;
    
    public void setRememberResources(int x, int y, int resources)  throws RemoteException;

    public int getRememberFieldSize() throws RemoteException;

    public PlayingField getRememberField()  throws RemoteException;
    
    public void setRememberField(PlayingField rememberField) throws RemoteException;

}
