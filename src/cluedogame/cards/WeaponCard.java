package cluedogame.cards;

import java.awt.Image;

/**
 * A card which only represents a Weapon, eg. "Dagger",
 * "Candlestick".
 * @author Sarah Dobie, Chris Read
 *
 */
public class WeaponCard extends Card {

	/**
	 * Constructor for class WeaponCard.
	 * @param name The name of the weapon represented.
	 */
	public WeaponCard(String name, Image image) {
		super(name, image);
	}

	@Override
	public String getName() {
		return name;
	}

}
