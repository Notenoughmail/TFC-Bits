package com.notenoughmail.tfcbits.world.noise;

import net.dries007.tfc.world.noise.Noise2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleWeightedNoise2D implements Noise2D {

    private final Noise2D[] noises;
    private final Integer[] weights;

    public SimpleWeightedNoise2D(Noise2D[] noises, Integer[] weights) {
        this.noises = noises;
        List<Noise2D> noiseList = Arrays.stream(noises).toList();
        List<Integer> oldWeightList = Arrays.stream(weights).toList();
        List<Integer> newWeightList = new ArrayList<>(oldWeightList);
        if (oldWeightList.size() < noiseList.size()) {
            for (int i = 0 ; i < noiseList.size() - oldWeightList.size() ; i++) {
                newWeightList.add(0);
            }
        }
        Integer[] actualWeights = new Integer[newWeightList.size()];
        actualWeights = newWeightList.toArray(actualWeights);
        this.weights = actualWeights;
    }

    @Override
    public float noise(float x, float z) {
        List<Float> weightList = new ArrayList<>();
        int total = 0;
        for (Integer weight : weights) {
            total += weight;
        }
        for (Integer i : weights) {
            weightList.add((float) i / total);
        }
        float value = 0.0F;
        for (int i = 0 ; i < noises.length ; i++) {
            value += noises[i].noise(x, z) * weightList.get(i);
        }
        return value;
    }

    public Noise2D changeWeight(int position, int newWeight) {
        if (position > weights.length - 1) {
            throw new IllegalArgumentException("Tried to change the weight in position " + position + " when there are only " + weights.length + " weight(s)");
        }
        weights[position] = newWeight;
        return this;
    }
}