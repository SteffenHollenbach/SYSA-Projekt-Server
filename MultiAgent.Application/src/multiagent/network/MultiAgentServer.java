/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiagent.network;

import java.net.InetAddress;
import multiagent.remote.IMultiAgentServer;
import multiagent.remote.IPlayer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import multiagent.gui.ServerFrame;
import multiagent.remote.IStrategy;

/**
 *
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */


public class MultiAgentServer extends UnicastRemoteObject implements IMultiAgentServer {
    
    
    private ServerFrame serverFrame;
    private byte[] ipAddr;

   
    String hostname;
            
    
    protected MultiAgentServer() throws RemoteException {
        super();
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
    }

    public MultiAgentServer(ServerFrame aThis) throws RemoteException {
        this();
        serverFrame = aThis;
        InetAddress addr=null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (java.net.UnknownHostException ex) {
            Logger.getLogger(MultiAgentServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Get IP Address
        ipAddr = addr.getAddress();
        // Get hostname
        hostname = addr.getHostName();
    }

    @Override
    public boolean addPlayer(IPlayer name, IStrategy aThis) throws RemoteException {
        return serverFrame.addNewAgent(name,aThis);
    }

    @Override
    public String print() throws RemoteException {
        String test = "test";
        System.out.println(test);
        return test;
    }
    
    @Override
    public byte[] getIpAddr() {
        return ipAddr;
    }
    
    @Override
    public String getHostname() {
        return hostname;
    }
    


   
}
