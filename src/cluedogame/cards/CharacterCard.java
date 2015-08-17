package cluedogame.cards;

import java.awt.Image;

/**
 * A card which only represents a Character, eg. "Mrs White",
 * "Professor Plum".
 * @author Sarah Dobie, Chris Read
 *
 */
public class CharacterCard extends Card {

	/**
	 * Constructor for class CharacterCard.
	 * @param name The name of the character represented
	 */
	public CharacterCard(String name, Image image) {
		super(name, image);
	}

	@Override
	public String getName() {
		return name;
	}

}
