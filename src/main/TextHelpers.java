package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cluedogame.GameOfCluedo;

/**
 * Some helper methods for the TextClient class.
 * @author Sarah Dobie, Chris Read
 *
 */
public class TextHelpers {

	/**
	 * Pauses the game until the user presses the enter key.
	 */
	static void waitToContinue(){
		System.out.println();
		inputString("** Press enter to continue **");
		System.out.println();
	}

	/**
	 * Get integer from user input
	 */
	static int inputNumber(String msg) {
		System.out.print(msg + " ");
		while (true) {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				String v = input.readLine();
				return Integer.parseInt(v);
			} catch (IOException e) {
				System.out.println("Please enter a number.");
			} catch (NumberFormatException e) {
				System.out.println("Please enter a number.");
			}
		}
	}

	/**
	 * Get string from user input
	 */
	static String inputString(String msg) {
		System.out.print(msg + " ");
		while (true) {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				return input.readLine();
			} catch (IOException e) {
				System.out.println("There's been an error. Please try again.");
			}
		}
	}
	
	/**
	 * Returns the given String with the first letter capitalised
	 * @param s The String to be capitalised.
	 * @return The given String with the first letter in uppercase.
	 */
	static String capitalise(String s){
		Character firstLetter = s.charAt(0);
		Character.toUpperCase(firstLetter);
		Character uppercase = (Character)(Character.toUpperCase(firstLetter));
		return s.replaceFirst(firstLetter.toString(), uppercase.toString());
	}
	
	/**
	 * Prints out a list of Strings on one line with quotes around each item,
	 * and a comma and space between each item.
	 * @param list The list to be printed
	 */
	static void printList(List<String> list){
		// print out each character name
		boolean firstTime = true;
		for (String s : list) {
			if (!firstTime) {
				// print a comma if appropriate
				System.out.print(", ");
			}
			firstTime = false;
			System.out.print("\"" + s + "\"");
		}
		System.out.println();
	}
	
	/**
	 * Adds relevant constants to lists of characters, weapons and rooms.
	 */
	static void setup(){
		setupCharacters();
		setupSimpleCharacters();
		setupWeapons();
		setupRooms();
	}

	/**
	 * Adds all character constants to list.
	 */
	static void setupCharacters() {
		TextClient.characters = new ArrayList<String>();
		TextClient.characters.add(GameOfCluedo.SCARLETT);
		TextClient.characters.add(GameOfCluedo.MUSTARD);
		TextClient.characters.add(GameOfCluedo.WHITE);
		TextClient.characters.add(GameOfCluedo.GREEN);
		TextClient.characters.add(GameOfCluedo.PEACOCK);
		TextClient.characters.add(GameOfCluedo.PLUM);
	}

	/**
	 * Adds all simplified character names to list
	 */
	static void setupSimpleCharacters() {
		TextClient.simpleCharacters = new ArrayList<String>();
		TextClient.simpleCharacters.add("Scarlett");
		TextClient.simpleCharacters.add("Mustard");
		TextClient.simpleCharacters.add("White");
		TextClient.simpleCharacters.add("Green");
		TextClient.simpleCharacters.add("Peacock");
		TextClient.simpleCharacters.add("Plum");
	}

	/**
	 * Adds all weapon constants to list.
	 */
	static void setupWeapons() {
		TextClient.weapons = new ArrayList<String>();
		TextClient.weapons.add(GameOfCluedo.CANDLESTICK);
		TextClient.weapons.add(GameOfCluedo.DAGGER);
		TextClient.weapons.add(GameOfCluedo.LEAD_PIPE);
		TextClient.weapons.add(GameOfCluedo.REVOLVER);
		TextClient.weapons.add(GameOfCluedo.ROPE);
		TextClient.weapons.add(GameOfCluedo.SPANNER);
	}

	/**
	 * Adds all room constants to list.
	 */
	static void setupRooms() {
		TextClient.rooms = new ArrayList<String>();
		TextClient.rooms.add(GameOfCluedo.KITCHEN);
		TextClient.rooms.add(GameOfCluedo.BALL_ROOM);
		TextClient.rooms.add(GameOfCluedo.CONSERVATORY);
		TextClient.rooms.add(GameOfCluedo.BILLIARD_ROOM);
		TextClient.rooms.add(GameOfCluedo.LIBRARY);
		TextClient.rooms.add(GameOfCluedo.STUDY);
		TextClient.rooms.add(GameOfCluedo.HALL);
		TextClient.rooms.add(GameOfCluedo.LOUNGE);
		TextClient.rooms.add(GameOfCluedo.DINING_ROOM);
	}
	
	/**
	 * Converts a simplified name, (eg. "Scarlett") into the full name,
	 * (eg. "Miss Scarlett").
	 * @param name The simplified name to convert.
	 * @return The full version of the simplified name.
	 */
	static String simpleToFullName(String name){
		switch(name){
		case GameOfCluedo.SCARLETT:
		case "Scarlett" : case "scarlett" : return GameOfCluedo.SCARLETT;
		case GameOfCluedo.MUSTARD:
		case "Mustard" : case "mustard" : return GameOfCluedo.MUSTARD;
		case GameOfCluedo.WHITE:
		case "White" : case "white" : return GameOfCluedo.WHITE;
		case GameOfCluedo.GREEN:
		case "Reverend Green":
		case "Green" : case "green" : return GameOfCluedo.GREEN;
		case GameOfCluedo.PEACOCK:
		case "Peacock" : case "peacock" : return GameOfCluedo.PEACOCK;
		case GameOfCluedo.PLUM:
		case "Plum" : case "plum" : return GameOfCluedo.PLUM;
		default : return "";
		}
		
	}
	
	/**
	 * Converts a simplified weapon, (eg. "Pipe") into the full name,
	 * (eg. "Lead Pipe").
	 * @param name The simplified weapon name to convert.
	 * @return The full version of the simplified weapon name.
	 */
	static String simpleToFullWeapon(String weapon){
		switch(weapon){
		case GameOfCluedo.CANDLESTICK : case "candlestick" : return GameOfCluedo.CANDLESTICK;
		case GameOfCluedo.DAGGER: case "dagger": return GameOfCluedo.DAGGER;
		case GameOfCluedo.REVOLVER : case "revolver" : return GameOfCluedo.REVOLVER;
		case GameOfCluedo.ROPE : case "rope": return GameOfCluedo.ROPE;
		case GameOfCluedo.SPANNER : case "spanner" : return GameOfCluedo.SPANNER;
		case GameOfCluedo.LEAD_PIPE : case "Pipe" : case "pipe": case "lead pipe" : return GameOfCluedo.LEAD_PIPE;
		default : return "";
		}
	}
	
	/**
	 * Converts a simplified room, (eg. "billiard") into the full name,
	 * (eg. "Billiard Room").
	 * @param name The simplified room name to convert.
	 * @return The full version of the simplified room name.
	 */
	static String simpleToFullRoom(String room){
		switch(room){
		case GameOfCluedo.CONSERVATORY : case "conservatory" :
			return GameOfCluedo.CONSERVATORY;
		case GameOfCluedo.LIBRARY : case "library": return GameOfCluedo.LIBRARY;
		case GameOfCluedo.STUDY : case "study" : return GameOfCluedo.STUDY;
		case GameOfCluedo.HALL: case "hall": return GameOfCluedo.HALL;
		case GameOfCluedo.LOUNGE : case "lounge" : return GameOfCluedo.LOUNGE;
		case GameOfCluedo.KITCHEN : case "kitchen" : return GameOfCluedo.KITCHEN;
		case GameOfCluedo.BILLIARD_ROOM : case "billiard room" :
			case "Billiard": case "billiard" : return GameOfCluedo.BILLIARD_ROOM;
		case GameOfCluedo.DINING_ROOM: case "dining room" : case "Dining":
			case "dining" : return GameOfCluedo.DINING_ROOM;
		case GameOfCluedo.BALL_ROOM: case "ball room" : case "Ball":
			case "ball" : return GameOfCluedo.BALL_ROOM;
		default : return "";
		}
	}
	
	/**
	 * Prompts the player to select a character by typing their simple name.
	 * @return The full name of the character
	 */
	static String selectCharacter(){
		// Display options for the player to select a character
		printList(TextClient.simpleCharacters);
		String character = inputString("Enter the character's name: ");
		character = simpleToFullName(character);
		// check for invalid character
		while (character == ""){
			character = inputString("Invalid input - please try again: ");
			character = simpleToFullName(character);
		}
		return character;
	}
	
	/**
	 * Prompts the player to select a weapon by typing its name.
	 * @return The full name of the weapon
	 */
	static String selectWeapon(){
		// Display options for the player to select a weapon
		printList(TextClient.weapons);
		String weapon = inputString("Enter the weapon: ");
		weapon = simpleToFullWeapon(weapon);
		while (weapon == ""){
			weapon = inputString("Invalid input - please try again: ");
			weapon = simpleToFullWeapon(weapon);
		}
		return weapon;
	}
	
	/**
	 * Prompts the player to select a room by typing its name.
	 * @return The full name of the room
	 */
	static String selectRoom(){
		// Display options for the player to select a room
		printList(TextClient.rooms);
		String room = inputString("Enter the room: ");
		room = simpleToFullRoom(room);
		while (room == ""){
			room = inputString("Invalid input - please try again: ");
			room = simpleToFullRoom(room);
		}
		return room;
	}
	
	/**
	 * Prints an end of game message.
	 */
	static void gameOver(){
		System.out.println("*******************");
		System.out.println("**   GAME OVER   **");
		System.out.println("**               **");
		System.out.println("**   Thanks for  **");
		System.out.println("**    playing!   **");
		System.out.println("**               **");
		System.out.println("*******************");
	}
	
}
