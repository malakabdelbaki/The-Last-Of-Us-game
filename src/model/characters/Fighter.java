package model.characters;

public class Fighter extends Hero {

	public Fighter(String name, int maxHp, int attackDamage, int maxActions) {
		super(name, maxHp, attackDamage, maxActions);
	}
	public String toString() {
		return super.toString() + "\n Type = Fighter";
	}
}
