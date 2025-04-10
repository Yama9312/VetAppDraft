package com.example.vetappdraft;

public class Node {
    //hold screen?
    Page currentScreen;

    //prior screen
    Node prev;

    //next screen
    Node next;

    //ctor
    Node(Page cScreen) {
        currentScreen = cScreen;
        prev = null;
        next = null;
    }
}
