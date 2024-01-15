package MlodyPucekIndustries.model.utils;

import MlodyPucekIndustries.model.elements.Animal;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AnimalTree {
    private ArrayList<AnimalNode> roots = new ArrayList<>();
    private HashMap<Animal, AnimalNode> nodes = new HashMap<>();

    public HashMap<Animal, AnimalNode> getNodes() {
        return nodes;
    }

    public ArrayList<AnimalNode> getRoots() {
        return roots;
    }

    private void cleanTreeRec(AnimalNode node){
        if(!node.hasAncestors()){
            if (node.getEnergy() > 0) {
                roots.add(node);
            } else {
                for (AnimalNode child : node.getChildren()) {
                    child.parentDeleted();
                    cleanTreeRec(child);
                }
                nodes.remove(node.getAnimal());
                node = null;
            }
        }
    }

    public void checkIfRootsAreAlive() {
        ArrayList<AnimalNode> rootsToRemove = new ArrayList<>();
        for(AnimalNode root : roots){
            if(root.getEnergy() <= 0){
                cleanTreeRec(root);
                rootsToRemove.add(root);
                removeAnimal(root.getAnimal());
            }
        }
        roots.removeAll(rootsToRemove);
    }

    public void printTree(){
        System.out.println(roots);
        for(AnimalNode root : roots){
            printTreeRec(root, 0, new ArrayList<>());
        }
    }

    private void printTreeRec(AnimalNode node, int depth, ArrayList<AnimalNode> visited){
        if(!visited.contains(node)){
            visited.add(node);
            System.out.println(" ".repeat(depth) + node.getAnimal());
            for(AnimalNode child : node.getChildren()){
                printTreeRec(child, depth + 1, visited);
            }
        }
    }

    public void addAnimal(Animal animal, Animal parent1, Animal parent2){
        boolean hasTwoParents = parent1 != null && parent2 != null;
        byte hasAncestors = 0;
        if (hasTwoParents) {
            hasAncestors = 2;
        }

        AnimalNode node = new AnimalNode(animal, hasAncestors);
        nodes.put(animal, node);

        // todo: Fix
        // todo: test whole thing
        if (hasTwoParents) {
            System.out.println("Parent1: " + parent1);
            System.out.println("Parent2: " + parent2);
            System.out.println(nodes.get(parent1));
            System.out.println(nodes.get(parent2));
            System.out.println(nodes.keySet());
            System.out.println(nodes.values());
            System.out.println(parent1.getEnergy());
            System.out.println(parent2.getEnergy());
            nodes.get(parent1).addChild(node);
            nodes.get(parent2).addChild(node);
        } else {
            roots.add(node);
        }
    }

    private void removeAnimal(Animal animal) {
        nodes.remove(animal);
    }

    private HashSet<AnimalNode> getDescendantCountRec(AnimalNode node){
        HashSet<AnimalNode> descendants = new HashSet<>();
        for (AnimalNode child : node.getChildren()) {
            descendants.add(child);
            descendants.addAll(getDescendantCountRec(child));
        }
        return descendants;
    }

    public int getDescendantCount(Animal animal){
        return getDescendantCountRec(nodes.get(animal)).size();
    }
}
