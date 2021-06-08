package SpicyRewards.util;

import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class WeightedList<T> {
    private final List<Item> items;
    private int totalWeight;

    public WeightedList() {
        totalWeight = 0;
        items = new ArrayList<>();
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void add(T object, int weight) {
        if(weight > 0) {
            totalWeight += weight;
            items.add(new Item(object, weight));
        }
    }

    public void addAll(ArrayList<T> objects, int weight) {
        totalWeight += (weight * objects.size());
        objects.forEach(o -> items.add(new Item(o, weight)));
    }

    public T getRandom(Random rng) {
        return getRandom(rng, false);
    }

    public T getRandom(Random rng, boolean remove) {
        int r = rng.random(totalWeight);
        int currentWeight = 0;

        Item selected = null;
        for (Item item : items) {
            if ((currentWeight + item.weight) >= r) {
                selected = item;

                break;
            }
            currentWeight += item.weight;
        }

        if (selected != null) {
            if (remove) {
                remove(selected);
            }

            return selected.object;
        } else {
            return null;
        }
    }

    public WeightedList<T> returnSubList(Predicate<T> condition) {
        WeightedList<T> tmp = new WeightedList<>();
        for(Item i : items) {
            if(condition.test(i.object)) {
                tmp.add(i.object, i.weight);
            }
        }
        return tmp;
    }

    public ArrayList<T> getObjects() {
        ArrayList<T> tmp = new ArrayList<>();
        items.forEach(i -> tmp.add(i.object));
        return tmp;
    }

    private void remove(Item item) {
        totalWeight -= item.weight;
        items.remove(item);
    }


    private class Item {
        final int weight;
        final T object;

        private Item(T object, int weight) {
            this.weight = weight;
            this.object = object;
        }
    }
}
