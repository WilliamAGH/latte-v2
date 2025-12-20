package com.williamcallahan.tui4j.examples.listfancy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Example program for random item generator.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/listfancy/RandomItemGenerator.java
 */
public class RandomItemGenerator {
    private List<String> titles;
    private List<String> descriptions;
    private int titleIndex;
    private int descIndex;
    private final ReentrantLock lock = new ReentrantLock();
    private boolean isShuffled = false;

    public RandomItemGenerator() {
        reset();
    }

    private void reset() {
        titles = new ArrayList<>(Arrays.asList(
                "Artichoke", "Baking Flour", "Bananas", "Barley", "Bean Sprouts", "Bitter Melon",
                "Black Cod", "Blood Orange", "Brown Sugar", "Cashew Apple", "Cashews", "Cat Food",
                "Coconut Milk", "Cucumber", "Curry Paste", "Currywurst", "Dill", "Dragonfruit",
                "Dried Shrimp", "Eggs", "Fish Cake", "Furikake", "Garlic", "Gherkin", "Ginger",
                "Granulated Sugar", "Grapefruit", "Green Onion", "Hazelnuts", "Heavy whipping cream",
                "Honey Dew", "Horseradish", "Jicama", "Kohlrabi", "Leeks", "Lentils", "Licorice Root",
                "Meyer Lemons", "Milk", "Molasses", "Muesli", "Nectarine", "Niagamo Root", "Nopal",
                "Nutella", "Oat Milk", "Oatmeal", "Olives", "Papaya", "Party Gherkin", "Peppers",
                "Persian Lemons", "Pickle", "Pineapple", "Plantains", "Pocky", "Powdered Sugar",
                "Quince", "Radish", "Ramps", "Star Anise", "Sweet Potato", "Tamarind",
                "Unsalted Butter", "Watermelon", "Weißwurst", "Yams", "Yeast", "Yuzu", "Snow Peas"
        ));

        descriptions = new ArrayList<>(Arrays.asList(
                "A little weird", "Bold flavor", "Can’t get enough", "Delectable", "Expensive",
                "Expired", "Exquisite", "Fresh", "Gimme", "In season", "Kind of spicy",
                "Looks fresh", "Looks good to me", "Maybe not", "My favorite", "Oh my", "On sale",
                "Organic", "Questionable", "Really fresh", "Refreshing", "Salty", "Scrumptious",
                "Delectable", "Slightly sweet", "Smells great", "Tasty", "Too ripe", "At last",
                "What?", "Wow", "Yum", "Maybe", "Sure, why not?"
        ));

        if (!isShuffled) {
            Collections.shuffle(titles);
            Collections.shuffle(descriptions);
            isShuffled = true;
        }

        titleIndex = 0;
        descIndex = 0;
    }

    public FancyItem next() {
        if (titles == null || descriptions == null) {
            reset();
        }

        lock.lock();
        try {
            FancyItem item = new FancyItem(titles.get(titleIndex), descriptions.get(descIndex));

            titleIndex = (titleIndex + 1) % titles.size();
            descIndex = (descIndex + 1) % descriptions.size();

            return item;
        } finally {
            lock.unlock();
        }
    }
}