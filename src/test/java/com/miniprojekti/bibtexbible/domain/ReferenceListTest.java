/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miniprojekti.bibtexbible.domain;

import static com.miniprojekti.misc.Tool.addMissingProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author asjuvone
 */
public class ReferenceListTest {

    private static ReferenceList refList;
    private static Book kirja1;
    private static Book kirja2;

    public ReferenceListTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        refList = new ReferenceList();
        kirja1 = createNewBook("Tekija1", "Titteli1", "1984", "Julkaisija1");
        kirja2 = createNewBook("Tekija2", "Titteli2", "1985", "Julkaisija2");
    }
    
    public Book createNewBook(String author, String title, String year, String publisher) {
        Book b = new Book();
        Set<String> labels = b.getPropertyDescriptions().keySet();
        b.setProperty("author", author);
        b.setProperty("title", title);
        b.setProperty("year", year);
        b.setProperty("publisher", publisher);
        addMissingProperties(b);
        return b;
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddReferenceOneBook() {
        refList.add(kirja1);
        assertTrue(refList.list().contains(kirja1));
    }

    @Test
    public void testAddReferencesSecondAddedBookFound() {
        refList.add(kirja1);
        refList.add(kirja2);
        assertTrue(refList.list().contains(kirja2));
    }

    @Test
    public void testAddSameReferenceMultipleTimes() {
        Book a1 = createNewBook("Mauri Rynnäs", "Teos1", "2015", "UGK");
        Book a2 = createNewBook("Mauri Rynnäs", "Teos1", "2015", "UGK");
        refList.add(a1);
        refList.add(a2);
        refList.add(a2);
        assertEquals(1, refList.list().size());
    }

    @Test
    public void testAddReferencesWithSameID() {
        Book a1 = createNewBook("Mauri Rynnäs", "Teos1", "2015", "UGK");
        Book a2 = createNewBook("Mauri Rynnäs", "Teos2", "2015", "UGK");
        Book a3 = createNewBook("Mauri Rynnäs", "Teos3", "2015", "UGK");
        Book a4 = createNewBook("Mauri Rynnäs", "Teos4", "2015", "UGK");
        refList.add(a1);
        refList.add(a2);
        refList.add(a3);
        refList.add(a4);
        assertEquals(4, refList.list().size());
    }

    @Test
    public void testDeleteReference() {
        refList.add(kirja1);
        refList.delete(kirja1.getID());
        assertFalse(refList.list().contains(kirja1));
    }

    @Test
    public void testDeleteFirstOf2() {
        refList.add(kirja1);
        refList.add(kirja2);
        refList.delete(kirja1.getID());
        assertFalse(refList.list().contains(kirja1));
    }

    @Test
    public void testDeleteLastOf2() {
        refList.add(kirja1);
        refList.add(kirja2);
        refList.delete(kirja2.getID());
        assertFalse(refList.list().contains(kirja2));
    }


    @Test
    public void testDeleteMultipleReferences() {
        refList.add(kirja1);
        refList.add(kirja2);
        refList.delete(kirja1.getID());
        refList.delete(kirja2.getID());
        assertTrue(refList.list().isEmpty());
    }
    
    @Test
    public void clearDeletesAllReferences() {
        refList.add(kirja1);
        refList.add(kirja2);
        refList.clear();
        assertTrue(refList.list().isEmpty());
    }

}
