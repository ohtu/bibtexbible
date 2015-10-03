/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miniprojekti.bibtexbible.domain;

import static com.miniprojekti.misc.Tool.replaceScandisForBibTex;
import java.util.HashMap;
import java.util.HashSet;
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
public class ArticleTest {

    Article emptyArticle;
    Article typicalArticle;
    Article fullyDescribedArticle;
    Set<String> labels;

    public ArticleTest() {
    }

    @Before
    public void setUp() {
        emptyArticle = new Article();
        typicalArticle = new Article(
                "Karri Kirjoittaja",
                "Artikkelin Otsikko",
                "1866",
                "Journal of Nature",
                "Volume 58"
        );
        fullyDescribedArticle = new Article();
        for (String label : fullyDescribedArticle.getPropertyDescriptions().keySet()) {
            fullyDescribedArticle.setProperty(label, label + "x");
            // testissä esim. address arvo pitäisi olla "addressx"
        }
        labels = emptyArticle.getPropertyDescriptions().keySet();
    }
    
    /*
    * Allowed labels for article should be the same regardless
    * of constructor used, set properties, etc.
    */
    @Test
    public void testArticleLabelsAreTheSame() {
        Set<String> labels2 = typicalArticle.getPropertyDescriptions().keySet();
        Set<String> labels3 = fullyDescribedArticle.getPropertyDescriptions().keySet();
        assertTrue(labels.size() == labels2.size());
        assertTrue(labels2.size() == labels3.size());
        for (String label : labels) {
            assertTrue(labels2.contains(label));
            assertTrue(labels3.contains(label));
        }
    }
    
    @Test
    public void testConstructorSetsPropertyValuesCorrectly() {
        assertTrue(typicalArticle.getProperty("author").equals("Karri Kirjoittaja"));
        assertTrue(typicalArticle.getProperty("title").equals("Artikkelin Otsikko"));
        assertTrue(typicalArticle.getProperty("year").equals("1866"));
        assertTrue(typicalArticle.getProperty("journal").equals("Journal of Nature"));
        assertTrue(typicalArticle.getProperty("volume").equals("Volume 58"));
        // varmistetaan vielä ettei muita valueita löydy
        int count = 0; // kuinka monta valueta typicalarticleilta löytyy
        for (String label : labels) {
            // varmistetaan samalla ettei emptyarticleilta löydy mitään
            assertTrue(emptyArticle.getProperty(label) == null);
            if (typicalArticle.getProperty(label) != null) count++;
        }
        assertTrue(count==5);
    }
    
    @Test
    public void testSetProperty() {
        for (String label : labels) {
            String value = fullyDescribedArticle.getProperty(label);
            assertTrue(value.equals(label + "x"));
        }
    }
    
    @Test
    public void testSetPropertyWhenOverwriting() {
        fullyDescribedArticle.setProperty("author", "Uolevi");
        assertTrue(fullyDescribedArticle.getProperty("author").equals("Uolevi"));
    }

    @Test
    public void testGetPropertiesReturnsHashMapWithCorrecNumberOfProperties() {
        HashMap<String, String> properties = emptyArticle.getPropertyDescriptions();
        assertEquals(true, properties instanceof HashMap);
        assertEquals(10, properties.size());
    }
    
    @Test
    public void testToString() {
        assertTrue(typicalArticle.toString().contains("Karri"));
        assertTrue(typicalArticle.toString().contains("1866"));
        assertTrue(typicalArticle.toString().contains("Otsikko"));
        assertTrue(typicalArticle.toString().contains("Nature"));
        assertTrue(typicalArticle.toString().contains("Volume 58"));
    }
    
    @Test
    public void testToBibTex() {
        testArticleToBibTex(typicalArticle, 5+2);
        testArticleToBibTex(fullyDescribedArticle, labels.size()+2);
        // test empty article to bibtex?
    }
    
    private void testArticleToBibTex(Article article, int valueCount) {
        String b = article.toBibTex();
        String[] split = b.split("\r\n");
        assertTrue(split.length == valueCount);
        assertTrue(split[0].startsWith("@Article{")); // any ID is ok
        HashSet<String> labelsFound = new HashSet<>();
        for (int i=1; i<split.length-1; i++) {
            String[] rivi = split[i].split("\\=", 2);
            assertTrue(rivi.length == 2);
            String label = rivi[0].trim();
            String expected = article.getProperty(label);
            expected = replaceScandisForBibTex(expected);
            String found = rivi[1].trim();
            found = found.substring(1);
            found = found.substring(0, found.length()-2); // lopusta pois " ja ,
            labelsFound.add(label);
            assertTrue(found.equals(expected));
        }
        assertTrue(labelsFound.size() == valueCount-2);
        assertTrue(b.endsWith("}"));
    }
}