package cluedogame.cards;

/**
 * A card which only represents a Room, eg. "Study",
 * "Dining Room".
 * @author Sarah Dobie, Chris Read
 *
 */
public class RoomCard extends Card {

	/**
	 * Constructor for class RoomCard.
	 * @param name The name of the room represented
	 */
	public RoomCard(String name) {
		super(name);
	}

	@Override
	public String getName() {
		return name;
	}

}
