package eu.thecreator.bytecode.test;

/**
 * Testmain für Codemanipulation
 * 
 * @author andre
 *
 */
public class Main {
	public static void main(String[] args) {
		// Expected: eu.thecreator.bytecode.test.Main@19e0bfd
		System.err.println(new Main());
		// Exprected: toString() changed
		System.err.println(new XT());
		// Funktioniert in Eclipse nur so:
		System.err.println(new XT().test());
	}
}
