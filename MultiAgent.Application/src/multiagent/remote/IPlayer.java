/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiagent.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;



/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */
public interface IPlayer extends Remote{

    public boolean connect(String name, IStrategy strat) throws RemoteException;
            
    public String getName()throws RemoteException;

    public IStrategy getStrategy() throws RemoteException;
    
    public void setPoints(int points) throws RemoteException;
    
    public int getPoints() throws RemoteException;
    
    public void dispose() throws RemoteException;
    
    public void setName(String name) throws RemoteException;
}
