package com.botifier.timewaster.util;

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.botifier.timewaster.entity.player.Player;
import com.botifier.timewaster.util.gui.*;

/**
 * Holds GUI components and renders them
 * @author Botifier
 *
 */
public class GUI {
	/**
	 * Components in this GUI
	 */
	LinkedList<Component> components = new LinkedList<Component>();
	/**
	 * The component that is currently focused
	 */
	Component focused;
	/**
	 * The owner of the GUI
	 */
	Player p;

	/**
	 * GUI constructor
	 * @param p Player The owner
	 */ 
	public GUI(Player p) {
		this.p = p;
	}
	
	/**
	 * Updates The GUI
	 * @param gc GameContainer The Game Container
	 * @param delta int Time since last update
	 */
	public void update(GameContainer gc, int delta) {
		for (int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			if (c == null)
				continue;
			if (c.isEnabled() == false)
				continue;
			c.update(delta);
		}
	}
	
	/**
	 * Renders the GUI
	 * @param gc GameContainer The Game Container
	 * @param g Graphics The graphic renderer
	 */
	public void draw(GameContainer gc, Graphics g) {
		for (int i = components.size()-1; i > -1; i--) {
			Component c = components.get(i);
			if (c.isVisible())
				c.draw(g);
			g.setColor(Color.white);
		}
	}
	
	/**
	 * Unfocuses the focused component
	 */
	public void unfocus() {
		focused = null;
	}
	
	/**
	 * Focus a specific component
	 * @param c Component Target
	 */
	public void focusComponent(Component c) {
		focused = c;
	}
	
	/**
	 * Get the currently focused component
	 * @return Component The focused component
	 */
	public Component getFocused() {
		return focused;
	}
	
	/**
	 * Checks if GUI has a component of a specific type
	 * @param c Class<?> the class of the component type
	 * @return boolean Whether or not this type exists in components
	 */
	public boolean hasComponentType(Class<?> c) {
		for (int i = components.size()-1; i > -1; i--) {
			Component co = components.get(i);
			if (co.getClass() == c)
				return true;
		}
		return false;
	}
	
	/**
	 * Gets all components of a specific type
	 * @param c Class<?> the class of the component type
	 * @return  ArrayList<Component> All components fitting the criteria 
	 */
	public ArrayList<Component> getComponentsOfType(Class<?> c) {
		ArrayList<Component> cl = new ArrayList<Component>();
		for (int i = components.size()-1; i > -1; i--) {
			Component co = components.get(i);
			if (co.getClass() == c)
				cl.add(co);
		}
		return cl;
	}
	
	/**
	 * Gets component at position i
	 * @param i int Position to get from
	 * @return Component Component at position
	 */
	public Component getComponent(int i) {
		if (i >= components.size())
			return null;
		return components.get(i);
	}
	
	/**
	 * Adds a component to the list
	 * @param c Component To add
	 */
	public void addComponent(Component c) {
		components.addFirst(c);
	}
	
	/**
	 * Removes specified component from list
	 * @param c Component To remove
	 */
	public void removeComponent(Component c) {
		components.remove(c);
	}

}
