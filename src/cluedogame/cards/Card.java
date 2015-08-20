package cluedogame.cards;

import java.awt.Image;

import cluedogame.GUI.CluedoFrame;

/**
 * Represents either a character, weapon, or room card in a
 * game of Cluedo.
 * @author Sarah Dobie, Chris Read
 *
 */
public abstract class Card {
	
	protected String name; // the item on the card
	protected Image image;
	protected int identifier;
	
	/**
	 * Constructor for class card.
	 * @param name A String representing the item on
	 * the card
	 */
	public Card(String name, Image image, int identifier){
		this.name = name;
		this.image = image.getScaledInstance(60,80, Image.SCALE_FAST);
		this.identifier = identifier;
	}
	
	/**
	 * Returns the name of the item on the card,
	 * eg. "Mrs White", "Rope", or "Kitchen".
	 * @return The name of the item on this card.
	 */
	public abstract String getName();
	
	public Image getImage(){
		return image;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Card){
			Card c = (Card)o;
			return this.getName().equals(c.getName());
		}
		return false;
	}
	
	public int getID(){
		return identifier;
	}
	
}