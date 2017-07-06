package multiagent;

import multiagent.remote.IAgent;
import java.awt.Color;
import java.io.Serializable;
import java.rmi.RemoteException;
/**
 * 
 * @author Marcel_Meinerz (marcel.meinerz@th-bingen.de)
 * @author Steffen_Hollenbach
 * @author Jasmin_Welschbillig
 * 
 * @version 1.0
 */

public class Field implements Serializable{

	private int resources;
	private Color color;
	private IAgent agent;
	
        public Field(IAgent agent) throws RemoteException {
		this(0, agent.getColor(), agent);
	}
        
	public Field(int resources) {
		this(resources, Color.GRAY, null);
	}
	
	public Field(int resources, Color color) {
		this(resources, color, null);
	}
	
	public Field(int resources, Color color, IAgent agent) {
		this.resources = resources;
		this.color = color;
		this.agent = agent;
	}

	public int getResources() {
		return resources;
	}

	public void setResources(int resources) {
		this.resources = resources;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public IAgent getAgent() {
		return agent;
	}

	public void setIAgent(IAgent agent) {
		this.agent = agent;
	}
	
	
	
}
