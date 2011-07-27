package net.bcarlso.critic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Critic {
    private Accumulator<Date> pushAccumulator = new Accumulator<Date>();
    private Accumulator<Date> pullAccumulator = new Accumulator<Date>();

    public int acceptPush(Date date) {
        return pushAccumulator.incrementFor(date);
    }

    public int acceptPull(Date date) {
        return pullAccumulator.incrementFor(date);
    }

    private class Accumulator<T> {
        private Map<T, Integer> counts = new HashMap<T, Integer>();

        public int incrementFor(T key) {
            if(alreadyAccumulating(key))
                increment(key);
            else {
                initializeCountFor(key);
            }

            return currentCountFor(key);
        }

        private void increment(T key) {
            counts.put(key, currentCountFor(key) + 1);
        }

        private boolean alreadyAccumulating(T key) {
            return counts.containsKey(key);
        }

        private void initializeCountFor(T key) {
            int value = 1;
            counts.put(key, value);
        }

        private int currentCountFor(T key) {
            return counts.get(key).intValue();
        }
    }
}
