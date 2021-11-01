/**
 * @Author: Ethan Taylor Behar
 * @CreationDate: Sep 26, 2021
 * @Editors:
 * @EditDate:
 **/
package gamemaker.observer.pattern;

public interface Observable {
	public void registerObserver(Observer observer);
	public void unregisterObserver(Observer observer);
	public void update();
}
