package com.example.vetappdraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityManager {
    private List<Page> pages;

    public ActivityManager() {
        pages = new ArrayList<>();
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public List<Page> getPages() {
        return pages;
    }

    public void sortPagesByName() {
        pages.sort((page1, page2) -> page1.getName().compareTo(page2.getName()));
    }

    public void swapPages(int index1, int index2) {
        if (index1 < 0 || index1 >= pages.size() || index2 < 0 || index2 >= pages.size()) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        Collections.swap(pages, index1, index2);
    }
}

