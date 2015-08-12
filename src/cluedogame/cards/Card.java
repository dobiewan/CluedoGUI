package cluedogame.cards;

/**
 * Represents either a character, weapon, or room card in a
 * game of Cluedo.
 * @author Sarah Dobie, Chris Read
 *
 */
public abstract class Card {
	
	String name; // the item on the card
	
	/**
	 * Constructor for class card.
	 * @param name A String representing the item on
	 * the card
	 */
	public Card(String name){
		this.name = name;
	}
	
	/**
	 * Returns the name of the item on the card,
	 * eg. "Mrs White", "Rope", or "Kitchen".
	 * @return The name of the item on this card.
	 */
	public abstract String getName();
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Card){
			Card c = (Card)o;
			return this.getName().equals(c.getName());
		}
		return false;
	}
	
}