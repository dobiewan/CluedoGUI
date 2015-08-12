package cluedogame.cards;

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
	public WeaponCard(String name) {
		super(name);
	}

	@Override
	public String getName() {
		return name;
	}

}
