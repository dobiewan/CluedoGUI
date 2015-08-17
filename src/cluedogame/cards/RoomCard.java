package cluedogame.cards;

import java.awt.Image;

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
	public RoomCard(String name, Image image) {
		super(name, image);
	}

	@Override
	public String getName() {
		return name;
	}

}
