package com.botifier.timewaster.util.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;

import com.botifier.timewaster.main.MainGame;
import com.botifier.timewaster.util.GUI;

public abstract class InputComponent extends Component implements InputListener {
	boolean input = false;

	public InputComponent(GUI g, Color c, float x, float y, boolean outline) {
		super(g, c, x, y, outline);
		MainGame.mm.getInput().addPrimaryListener(this);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int count) {

	}

	@Override
	public void mouseDragged(int ox, int oy, int nx, int ny) {

	}

	@Override
	public void mouseMoved(int ox, int oy, int nx, int ny) {

	}

	@Override
	public void mousePressed(int button, int x, int y) {

	}

	@Override
	public void mouseReleased(int button, int x, int y) {

	}

	@Override
	public void mouseWheelMoved(int change) {

	}

	@Override
	public void inputEnded() {

	}

	@Override
	public void inputStarted() {

	}

	@Override
	public boolean isAcceptingInput() {
		return input;
	}

	@Override
	public void setInput(Input i) {
		
	}

	@Override
	public void keyPressed(int key, char c) {

	}

	@Override
	public void keyReleased(int key, char c) {

	}

	@Override
	public void controllerButtonPressed(int controller, int button) {

	}

	@Override
	public void controllerButtonReleased(int controller, int button) {

	}

	@Override
	public void controllerDownPressed(int controller) {

	}

	@Override
	public void controllerDownReleased(int controller) {
		
	}

	@Override
	public void controllerLeftPressed(int controller) {
		
	}

	@Override
	public void controllerLeftReleased(int controller) {

	}

	@Override
	public void controllerRightPressed(int controller) {

	}

	@Override
	public void controllerRightReleased(int controller) {

	}

	@Override
	public void controllerUpPressed(int controller) {

	}

	@Override
	public void controllerUpReleased(int controller) {

	}

}
