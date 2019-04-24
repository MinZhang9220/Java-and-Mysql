package controllers;

import datahandling.DiscourseRepository;
import models.Discourse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DiscourseControllerTest {

    DiscourseController discourseController;
    Scanner scanner;

    @BeforeEach
    void setUp() {
        discourseController = new DiscourseController(true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDiscourseFromUserWithNoDiscourses() {
        DiscourseRepository discourseRepository = discourseController.getDiscourseRepository();
        List<Discourse> discourseList = new ArrayList<>();
        discourseRepository.setDiscourses(discourseList);
        Scanner scanner = new Scanner(System.in);
        assertNull(discourseController.getDiscourseFromUser(scanner));
    }

    @Test
    void getDiscourseFromUserWithValidDiscourseId() {
        final InputStream original = System.in;
        String input = "4\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Discourse discourse = discourseController.getDiscourseFromUser(scanner);
        System.setIn(original);
        assertEquals(4, discourse.getId());
        assertEquals("   Sometimes I do feel like I'm a failure. Like there's no hope for me? But even so, I'm not gonna give up. Ever!", discourse.getContent());
    }

    @Test
    void getDiscourseFromUserWithInvalidDiscourseId() {
        final InputStream original = System.in;
        String input = "10\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Discourse discourse = null;
        try {
            discourse = discourseController.getDiscourseFromUser(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(discourse);
    }

    @Test
    void getDiscourseFromUserWithInvalidInput() {
        final InputStream original = System.in;
        String input = "invalidInput\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Discourse discourse = null;
        try {
            discourse = discourseController.getDiscourseFromUser(scanner);
        } catch(NoSuchElementException e){
            //do nothing because this is the expected outcome
        }
        System.setIn(original);
        assertNull(discourse);
    }

    @Test
    void getDiscourseFromUserWithInvalidDiscourseIdThenValidDiscourseId() {
        final InputStream original = System.in;
        String input = "10\n5\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Discourse discourse = discourseController.getDiscourseFromUser(scanner);
        System.setIn(original);
        assertEquals(5, discourse.getId());
        assertEquals("I have to work harder than anyone else to make it! I'll never catch up otherwise! I want to be like you! Like you. The strongest hero.   ", discourse.getContent());
    }

    @Test
    void getDiscourseFromUserWithInvalidInputThenValidDiscourseId() {
        final InputStream original = System.in;
        String input = "invalidInput\n4\n";
        scanner = new Scanner(input);
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Discourse discourse = discourseController.getDiscourseFromUser(scanner);
        System.setIn(original);
        assertEquals(4, discourse.getId());
        assertEquals("   Sometimes I do feel like I'm a failure. Like there's no hope for me? But even so, I'm not gonna give up. Ever!", discourse.getContent());
    }
}