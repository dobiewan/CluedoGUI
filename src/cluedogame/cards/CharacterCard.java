package cluedogame.cards;

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
	public CharacterCard(String name) {
		super(name);
	}

	@Override
	public String getName() {
		return name;
	}

}
