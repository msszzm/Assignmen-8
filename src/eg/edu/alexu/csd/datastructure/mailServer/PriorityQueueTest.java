package eg.edu.alexu.csd.datastructure.mailServer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PriorityQueueTest {

	@Test
	void testInsert() {
		PriorityQueue pq = new PriorityQueue();
		assertTrue(pq.isEmpty());
		pq.insert(34.66, 100);
		assertFalse(pq.isEmpty());
		pq.insert(55, 150);
		assertEquals(34.66,pq.min());
		pq.insert("test", 99);
		pq.insert(777, 102);
		assertEquals("test",pq.min());
		pq.insert('c', 40);
		pq.insert(66, 40);
		pq.insert(77, 40);
		assertEquals('c',pq.min());
		pq.insert(111, 20);
		pq.insert(0, 19);
		pq.insert(545, 40);
		assertEquals(0,pq.min());
		pq.insert(-122, 18);
		pq.insert(555, 40);
		pq.insert(-10, 18);
		assertEquals(-122,pq.min());
		pq.insert("first", 1);
		pq.insert(233, 1);
		pq.insert("lab", 1);
		pq.insert(44, 2);
		pq.insert(54, 1);
		pq.insert("888", 1);
		assertEquals("first",pq.min());
		Assertions.assertThrows(RuntimeException.class,()->pq.insert(1, 0));
		Assertions.assertThrows(RuntimeException.class,()->pq.insert(1, +0));
		Assertions.assertThrows(RuntimeException.class,()->pq.insert(1, -1));
		Assertions.assertThrows(RuntimeException.class,()->pq.insert(1, -20));

	}

	@Test
	void testRemoveMin() {
		PriorityQueue pq = new PriorityQueue();
		pq.insert(110, 110);
		assertFalse(pq.isEmpty());
		assertEquals(110,pq.removeMin());
		assertTrue(pq.isEmpty());
		pq.insert(96, 96);
		pq.insert(95, 95);
		pq.insert(94, 94);
		pq.insert(99, 99);
		pq.insert(98, 98);
		pq.insert(97, 97);
		assertEquals(94,pq.removeMin());
		assertEquals(95,pq.removeMin());
		assertEquals(96,pq.removeMin());
		assertEquals(97,pq.removeMin());
		pq.removeMin();
		assertEquals(99,pq.removeMin());
		Assertions.assertThrows(RuntimeException.class,()->pq.removeMin());
		pq.insert(1, 1);
		pq.removeMin();
		Assertions.assertThrows(RuntimeException.class,()->pq.removeMin());
		
	}

	@Test
	void testMin() {
		PriorityQueue pq = new PriorityQueue();
		Assertions.assertThrows(RuntimeException.class,()->pq.min());
		pq.insert(10, 10);
		assertEquals(10,pq.min());
		pq.insert(9, 9);
		assertEquals(9,pq.min());
		pq.insert(11, 11);
		pq.insert(12, 12);
		assertEquals(9,pq.min());
		pq.insert(92, 9);
		assertEquals(9,pq.min());
		pq.insert(7,7);
		pq.insert(8,8);
		assertEquals(7,pq.min());
		pq.insert(6,6);
		pq.insert(5,5);
		assertEquals(5,pq.min());
		pq.insert(4,4);
		assertEquals(4,pq.min());
		pq.insert(1,1);
		pq.insert(2,2);
		pq.insert(3,3);
		assertEquals(1,pq.min());


	}

	@Test
	void testIsEmpty() {
		PriorityQueue pq = new PriorityQueue();
		assertTrue(pq.isEmpty());
		pq.insert(20, 20);
		assertFalse(pq.isEmpty());
		pq.insert(30, 30);
		pq.insert(10, 10);
		assertFalse(pq.isEmpty());
		pq.removeMin();
		pq.removeMin();
		pq.removeMin();
		assertTrue(pq.isEmpty());
		pq.insert("test", 1);
		assertFalse(pq.isEmpty());
		pq.removeMin();
		assertTrue(pq.isEmpty());
		pq.insert(30, 30);
		pq.insert(70, 70);
		pq.min();
		pq.min();
		assertFalse(pq.isEmpty());
		pq.removeMin();
		pq.removeMin();
		assertTrue(pq.isEmpty());

	}

	@Test
	void testSize() {
		PriorityQueue pq = new PriorityQueue();
		assertEquals(0,pq.size());
		pq.insert(10, 10);
		assertEquals(1,pq.size());
		pq.insert(20, 20);
		assertEquals(2,pq.size());
		pq.insert(1, 1);
		assertEquals(3,pq.size());
		pq.min();
		assertEquals(3,pq.size());
		pq.removeMin();
		assertEquals(2,pq.size());
		pq.removeMin();
		assertEquals(1,pq.size());
		pq.removeMin();
		assertEquals(0,pq.size());
		pq.insert(1, 1);
		pq.insert(2, 1);
		pq.insert(3, 1);
		pq.insert(4, 1);
		pq.insert(5, 1);
		assertEquals(5,pq.size());
	}

}
