package stru.stacks;
import stru.exceptionss.EmptyCollectionException;
import stru.helpers.LinearNode;
import stru.interfaces.StackADT;

/**
 * @author Lewis and Chase
 *
 *         Represents a linked implementation of a stack.
 */

public class GenericLinkedStack<T> implements StackADT<T> {
	/** indicates number of elements stored */
	private int count;

	/** pointer to top of stack */
	private LinearNode<T> top;

	/**
	 * Creates an empty stack.
	 */
	public GenericLinkedStack() {
		count = 0;
		top = null;
	}

	/**
	 * Adds the specified element to the top of this stack.
	 * 
	 * @param element element to be pushed on stack
	 */
	@Override
	public void push(T element) {
		LinearNode<T> temp = new LinearNode<T>(element);
		temp.setNext(top);
		top = temp;
		count++;
	}

	/**
	 * Removes the element at the top of this stack and returns a reference to it.
	 * Throws an EmptyCollectionException if the stack is empty.
	 * 
	 * @return T element from top of stack
	 * @throws EmptyCollectionException on pop from empty stack
	 */
	@Override
	public T pop() throws EmptyCollectionException {
		if (isEmpty())
			throw new EmptyCollectionException("Stack");

		T result = top.getElement();
		top = top.getNext();
		count--;

		return result;
	}

	/**
	 * Returns a reference to the element at the top of this stack. The element is
	 * not removed from the stack. Throws an EmptyCollectionException if the stack
	 * is empty.
	 * 
	 * @return T element on top of stack
	 * @throws EmptyCollectionException on peek at empty stack
	 */
	@Override
	public T peek() throws EmptyCollectionException {
		if (isEmpty())
			throw new EmptyCollectionException("Stack");

		return top.getElement();
	}

	/**
	 * Returns true if this stack is empty and false otherwise.
	 * 
	 * @return boolean true if stack is empty
	 */
	@Override
	public boolean isEmpty() {
		return (count == 0);
	}

	/**
	 * Returns the number of elements in this stack.
	 * 
	 * @return int number of elements in this stack
	 */
	@Override
	public int size() {
		return count;
	}

	/**
	 * Returns a string representation of this stack.
	 * 
	 * @return String representation of this stack
	 */
	@Override
	public String toString() {
		String result = "";
		LinearNode<T> current = top;

		while (current != null) {
			result = result + (current.getElement()).toString() + "\n";
			current = current.getNext();
		}

		return result;
	}
}