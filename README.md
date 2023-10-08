# The Last of Us
 The last of us game

## Table of Contents
- [Interfaces](#interfaces)
  - [Collectible Interface](#collectible-interface)
- [Characters](#characters)
  - [Character Class](#character-class)
  - [Hero Class](#hero-class)
- [Engine](#engine)
  - [Game Class](#game-class)

---
##Demo


https://github.com/malakabdelbaki/The-Last-Of-Us-game/assets/119429621/031856ac-4412-44c9-8dcd-fb36ed221ff1


## Interfaces

### Collectible Interface

- **Name**: Collectible
- **Package**: model.collectibles
- **Type**: Interface
- **Description**: Interface containing the methods available to objects on the map that can be collected. All Vaccines and Supplies are Collectibles.

#### Methods

1. `void pickUp(Hero h)`: A method that adds the collectible picked by the hero to his corresponding ArrayList.
2. `void use(Hero h)`: A method that removes the used collectible by the hero from the corresponding ArrayList.

---

## Characters

### Character Class

This class should include the following additional methods:

- `void attack()`: This method should handle applying the logic of an attack on the character’s target. While implementing, think of the sub-classes that behave differently, whether they cost an action point or not and what can be extracted generically. Note that characters should only be allowed to attack adjacent cells.

- `void defend(Character c)`: This method is only called whenever a character has been attacked by another character. A defending character causes half his attackDmg to the attacking character. Example: Hero A (having attack damage 30) attacks Zombie 1. Zombie 1 receives 30 damage from the attack, then defends; setting Hero A as their target, and dealing 5 damage to Hero A.

- `void onCharacterDeath()`: This method should be called whenever a character’s currentHp reaches 0. It should handle removing the character from the game and updating the map.

### Hero Class

Note that for any hero moving, attacking, or curing a zombie costs 1 action point. Using the hero’s special action on the other hand costs 1 supply instead. Whenever a medic uses their special on a viable target, the target HP is fully restored.

This class should include the following additional methods:

- `void move(Direction d)`: A method that is called when the current hero wishes to move in a particular direction. If a movement occurs within a turn, both previously and newly visible cells remain visible until the turn the movement occurred in ends.

- `void useSpecial()`: This method should handle a hero wanting to use his special action, given that the conditions are met. While implementing, think of the sub-classes that behave differently, and what can be extracted generically. Refer to the Game description for the different special actions for each hero type.

- `void cure()`: This method should handle using a vaccine to cure a zombie. Heroes can only cure zombies that are in adjacent cells.

---

## Engine

### Game Class

This class should also include the following additional methods:

1. `public static void startGame(Hero h)`: This method is called to handle the initial game setup. It should:
   - Spawn the necessary collectibles (5 Vaccines, 5 Supplies).
   - Spawn 5 traps randomly around the map.
   - Spawn 10 zombies randomly around the map.
   - Add the hero to the controllable heroes pool and remove them from the availableHeroes.
   - Allocate the hero to the bottom left corner of the map.

2. `public static boolean checkWin()`: This method checks the win conditions for the game. Refer to the Game Description document if needed.

3. `public static boolean checkGameOver()`: This method checks the conditions for the game to end. Refer to the Game Description document if needed.

4. `public static void endTurn()`: This method is called when the player decides to end the turn. It should:
   - Allow all zombies to attempt to attack an adjacent hero (if one exists).
   - Reset each hero’s actions, target, and special.
   - Update the map visibility in the game such that only cells adjacent to heroes are visible.
   - Finally, spawn a zombie randomly on the map.

